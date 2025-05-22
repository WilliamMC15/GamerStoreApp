package com.tienda.virtual.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.tienda.virtual.R;
import com.tienda.virtual.utils.PreferenceManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Servicio que gestiona:
 * - Música de fondo (looping)
 * - Efectos de sonido (SoundPool)
 */
public class MediaPlayerService extends Service {
    public static final String ACTION_START_MUSIC   = "com.tienda.virtual.START_MUSIC";
    public static final String ACTION_STOP_MUSIC    = "com.tienda.virtual.STOP_MUSIC";
    public static final String ACTION_PLAY_EFFECT   = "com.tienda.virtual.PLAY_EFFECT";
    public static final String EXTRA_EFFECT_RES_ID  = "extra_effect_res_id";

    private MediaPlayer bgPlayer;
    private SoundPool soundPool;
    private Map<Integer, Integer> soundMap;
    private PreferenceManager pm;

    @Override
    public void onCreate() {
        super.onCreate();
        pm = new PreferenceManager(this);

        // 1) Preparar MediaPlayer para música de fondo
        bgPlayer = MediaPlayer.create(this, R.raw.digital_serenity);
        bgPlayer.setLooping(true);
        float vol = pm.getSoundVolume();
        bgPlayer.setVolume(vol, vol);

        // 2) Preparar SoundPool para efectos
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(attrs)
                .build();

        soundMap = new HashMap<>();
        soundMap.put(R.raw.game_on,
                soundPool.load(this, R.raw.game_on, 1));
        soundMap.put(R.raw.serene_waves,
                soundPool.load(this, R.raw.serene_waves, 1));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) return START_STICKY;
        String action = intent.getAction();

        if (ACTION_START_MUSIC.equals(action)) {
            if (pm.isMusicEnabled() && !bgPlayer.isPlaying()) {
                bgPlayer.start();
            }
        } else if (ACTION_STOP_MUSIC.equals(action)) {
            if (bgPlayer.isPlaying()) {
                bgPlayer.pause();
            }
        } else if (ACTION_PLAY_EFFECT.equals(action)) {
            int resId = intent.getIntExtra(EXTRA_EFFECT_RES_ID, -1);
            Integer soundId = soundMap.get(resId);
            if (soundId != null && pm.isMusicEnabled()) {
                float volume = pm.getSoundVolume();
                soundPool.play(soundId, volume, volume, 1, 0, 1f);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bgPlayer != null) {
            bgPlayer.stop();
            bgPlayer.release();
        }
        soundPool.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /** Helper para lanzar efectos desde cualquier Activity/Fragment */
    public static void playEffect(Context ctx, int effectResId) {
        Intent intent = new Intent(ctx, MediaPlayerService.class);
        intent.setAction(ACTION_PLAY_EFFECT);
        intent.putExtra(EXTRA_EFFECT_RES_ID, effectResId);
        ctx.startService(intent);
    }
}