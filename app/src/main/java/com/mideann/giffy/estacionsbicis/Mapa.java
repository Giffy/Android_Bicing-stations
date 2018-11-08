package com.mideann.giffy.estacionsbicis;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

/*
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />  <!-- Permiso localizacion por GPS -->
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />  <!-- Permiso localizacion por Network -->
*/
public class Mapa extends AppCompatActivity implements LocationListener {

    private ArrayList<Estacio> llistaEstacions;
    private MyApp app;
    private MapView map;
    private IMapController mapController;
    private LocationManager locationManager;
    private Location location, center, test;
    private double longitud, latitud;
    private boolean tracking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        this.tracking = false;
        this.app = (MyApp) this.getApplication();
        this.llistaEstacions = app.getEstacions();

        // añadimos la libreria osmdroid en build.gradle app
        // implementation 'org.osmdroid:osmdroid-android:6.0.2'
        org.osmdroid.config.Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        this.map = this.findViewById(R.id.id_map);
        this.map.setBuiltInZoomControls(true);                    // incluim controls per fer zoom
        this.map.setMultiTouchControls(true);
        this.mapController = this.map.getController();
        this.mapController.setZoom(18d);                          // definim Zoom inicial del mapa
        this.mapController.setCenter(new GeoPoint(41.391349d, 2.18061d));

        this.center = createLocation (2.18061d, 41.391349d );

        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,   // GPS location provider
                    Manifest.permission.ACCESS_COARSE_LOCATION   // Network location provider
            }, 1);
        } else {
            this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 01, 0f, this);
            this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 01, 0f, this);
            accessGPS();
        }
    }


    @SuppressLint("MissingPermission")
    private void accessGPS() {

        //   Colocamos un indicador de donde nos encontramos -  un hombrecito o una flecha
        GpsMyLocationProvider myLoc = new GpsMyLocationProvider ( this );
        MyLocationNewOverlay myLocOver = new MyLocationNewOverlay ( myLoc , this.map );

        this.map.getOverlays().clear();
        this.map.getOverlays().add(myLocOver);

        for (int i = 0; i< llistaEstacions.size(); i++){

            //this.test = createLocation(llistaEstacions.get(i).getLongitude(), llistaEstacions.get(i).getLatitude());
            this.test = llistaEstacions.get(i).getLocation();

            if (distance( this.center, this.test)) {

                //   Colocamos un point of interest
                Marker marker = new Marker(this.map);

//                marker.setPosition( new GeoPoint( llistaEstacions.get(i).getLatitude() , llistaEstacions.get(i).getLongitude()));
                marker.setPosition( new GeoPoint( llistaEstacions.get(i).getLocation()));

                if (llistaEstacions.get(i).getType().equals("BIKE-ELECTRIC")) {
                    marker.setIcon(this.getResources().getDrawable(R.drawable.marker_bike_electric)); // Utilizamos una imagen de R para bicis electricas
                    marker.setTitle("Bicing Electric");
                } else {
                    marker.setIcon(this.getResources().getDrawable(R.drawable.marker_bike)); // Utilizamos una imagen de R para bicis normales
                    marker.setTitle("Bicing");
                }
                if (!llistaEstacions.get(i).getStatus().equals("OPN")) {
                    marker.setIcon(this.getResources().getDrawable(R.drawable.marker_out_of_service)); // Utilizamos una imagen de R para bicis normales
                    marker.setTitle("Bicing fora de servei");
                }

                marker.setSubDescription(llistaEstacions.get(i).getAddress());
                String description = llistaEstacions.get(i).getAddress() + "<br>Bicis disponibles: " + llistaEstacions.get(i).getBikes()
                        + "<br>Llocs lliures: " + llistaEstacions.get(i).getSlots();
                marker.setSubDescription(description);

                // marker.setIcon(this.getResources().getDrawable(R.drawable.noria));  // Añadimos una imagen personalizada
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);  // indica donde está la punta de la flecha o localizacion de la imagen
                this.map.getOverlays().add(marker);
            }
        }
    }

    // Comprobacio de l'activació de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode==1 && grantResults.length>1 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
            this.accessGPS();
        }
    }

    private Location createLocation (double longitude , double latitude){
        this.location = new Location("service Provider");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    private boolean distance (Location center, Location test){
        float distanceInMeters = center.distanceTo(test);
        boolean isWithin10km = distanceInMeters < 1000;
        return isWithin10km;
    }

    @Override
    protected void onPause(){
        super.onPause();
        this.map.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        this.map.onResume();
    }

    public void centrarEnPosicio(View v){
        this.mapController.animateTo(new GeoPoint(this.latitud, this.longitud));
        accessGPS();
    }

    public void accioTracking(View v){
        if (tracking) {
            tracking = false;
        } else {
            tracking = true;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.center = location;
        if ( this.tracking ){
            // this.mapController.animateTo(new GeoPoint(location.getLatitude(), location.getLongitude()));
            this.mapController.animateTo(new GeoPoint( location ));
            accessGPS();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
