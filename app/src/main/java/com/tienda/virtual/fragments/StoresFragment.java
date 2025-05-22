package com.tienda.virtual.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tienda.virtual.R;

import java.util.Locale;

public class StoresFragment extends Fragment implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST = 1001;

    private GoogleMap map;
    private FusedLocationProviderClient fusedClient;
    private LinearLayout llStoreList;

    // Nombres y coordenadas de tiendas de ejemplo
    private final String[] storeNames = {
            "Gamer Central", "Pro Gear Store", "Elite Gaming Hub"
    };
    private final LatLng[] storeCoords = {
            new LatLng(4.7110, -74.0721),     // Bogotá
            new LatLng(4.7125, -74.0700),
            new LatLng(4.7090, -74.0740)
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup parent, Bundle st) {
        View root = inf.inflate(R.layout.fragment_stores, parent, false);

        fusedClient  = LocationServices.getFusedLocationProviderClient(requireContext());
        llStoreList  = root.findViewById(R.id.llStoreList);

        // Inicializar el mapa
        SupportMapFragment mapFrag = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFrag.getMapAsync(this);

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permiso
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
            return;
        }
        setupMapAndList();
    }

    private void setupMapAndList() {
        map.setMyLocationEnabled(true);
        fusedClient.getLastLocation().addOnSuccessListener(loc -> {
            if (loc == null) {
                Toast.makeText(getContext(),
                        "No se pudo obtener ubicación. Mostrando ubicaciones por defecto.",
                        Toast.LENGTH_SHORT).show();
                displayStores(4.7110, -74.0721); // Bogotá por defecto
            } else {
                double lat = loc.getLatitude();
                double lng = loc.getLongitude();
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 14f));
                displayStores(lat, lng);
            }
        });
    }

    private void displayStores(double userLat, double userLng) {
        llStoreList.removeAllViews();
        for (int i = 0; i < storeNames.length; i++) {
            LatLng coord = storeCoords[i];
            // Añadir marcador al mapa
            map.addMarker(new MarkerOptions()
                    .position(coord)
                    .title(storeNames[i]));

            // Calcular distancia
            float[] result = new float[1];
            Location.distanceBetween(userLat, userLng,
                    coord.latitude, coord.longitude, result);
            float km = result[0] / 1000f;

            // Crear y agregar texto en la lista
            TextView tv = new TextView(requireContext());
            tv.setText(String.format(Locale.getDefault(),
                    "%s — %.1f km", storeNames[i], km));
            tv.setTextColor(getResources().getColor(android.R.color.white));
            tv.setPadding(0, 8, 0, 8);
            llStoreList.addView(tv);
        }
    }

    @Override
    public void onRequestPermissionsResult(int req, @NonNull String[] perms,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(req, perms, grantResults);
        if (req == LOCATION_PERMISSION_REQUEST
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            map.getUiSettings().setMyLocationButtonEnabled(true);
            setupMapAndList();
        } else {
            Toast.makeText(getContext(),
                    "Permiso de ubicación denegado.", Toast.LENGTH_SHORT).show();
            // Mostrar lista estática sin mapa
            map.clear();
            llStoreList.removeAllViews();
            for (String name : storeNames) {
                TextView tv = new TextView(requireContext());
                tv.setText(name);
                tv.setTextColor(getResources().getColor(android.R.color.white));
                tv.setPadding(0, 8, 0, 8);
                llStoreList.addView(tv);
            }
        }
    }
}