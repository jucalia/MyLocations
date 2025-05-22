package com.example.julia_mylocations;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SecondActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;
    public LocationManager lm;
    public Criteria criteria;
    String provider;
    private Marker marcadorAtual = null;

    private final LatLng[] locations = {
            new LatLng(-13.255112, -43.405225), // Cidade atual
            new LatLng(-20.754891, -42.879283), // Viçosa
            new LatLng(-20.7512, -42.8730)  // DPI/UFV
    };
    private final LatLng vicLocation = locations[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();

        PackageManager packageManager = getPackageManager();
        boolean hasGPS = getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);

        if(hasGPS){
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            Log.i("LOCATION", "Usando GPS");
        }else{
            Log.i("LOCATION", "Usando WI-FI ou dados");
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        }

    }

    @Override
    protected void onStart(){
        super.onStart();
        provider = lm.getBestProvider(criteria,true);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        lm.removeUpdates(this); //remove os updates da localização
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int locationIndex = getIntent().getIntExtra("location_id", 0);
        moveToLocation(locationIndex);
    }

    public void foca_mapa(View v) {
        int tag = Integer.parseInt(v.getTag().toString()) - 1;
        moveToLocation(tag);
    }

    private void moveToLocation(int index) {
        LatLng latLng = locations[index];
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marcador " + index));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    public void marca_localizacao(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissão de localização não concedida.", Toast.LENGTH_SHORT).show();
            return;
        }

        Location location = lm.getLastKnownLocation(provider);
        if (location != null) {
            LatLng atual = new LatLng(location.getLatitude(), location.getLongitude());

            if (marcadorAtual != null) {
                marcadorAtual.remove();
            }

            marcadorAtual = mMap.addMarker(new MarkerOptions()
                    .position(atual)
                    .title("Minha localização atual")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(atual, 17));

            float[] results = new float[1];
            Location.distanceBetween(atual.latitude, atual.longitude, vicLocation.latitude, vicLocation.longitude, results);

            Toast.makeText(this, "Distância até Viçosa: " + results[0] + " metros", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Localização atual indisponível", Toast.LENGTH_SHORT).show();
        }
    }
}
