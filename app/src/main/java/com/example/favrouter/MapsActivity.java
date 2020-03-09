package com.example.favrouter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.example.favrouter.backendRecycler.DataProvider;
import com.example.favrouter.database.DatabaseOper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, LocationListener {

    private GoogleMap mMap;
    private Toolbar toolbar_maps;
    private String marcador;
    private LocationManager locationManager;
    private String provider;
    private LatLng latLng;
    private int position = -1;
    private double lat;
    private double longi;
    private List<Address> addressList;
    private Context context;
    private DatabaseOper databaseOper;
    private DataProvider dataProvider;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        databaseOper = new DatabaseOper(this);
        getData();


    }

    @SuppressLint("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
            LatLngBounds BRASIL = new LatLngBounds(new LatLng(-15.584168, -47.825927), new LatLng(-15.584168, -47.825927));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BRASIL.getCenter(),5));
        locationManager.requestLocationUpdates(provider, 500, 1, this);

        mMap.setOnMapLongClickListener(this);

    }

    @Override
    public void onLocationChanged(Location location) {
        if(position != -1){
            latLng = new LatLng(lat,longi);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
            mMap.addMarker(new MarkerOptions().position(latLng).title(MainActivity.dataProviderList.get(position).getLocation()));
        }else{
            if (latLng == null){
                latLng = new LatLng(location.getLatitude(),location.getLongitude());
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
            mMap.addMarker(new MarkerOptions().position(latLng).title("Sua localização"));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapLongClick(LatLng point) {
        addressList = new ArrayList<>();
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        marcador = new Date().toString();

        try {
            addressList = geocoder.getFromLocation(point.latitude,point.longitude,1);
            marcador = addressList.get(0).getAddressLine(0);
        }catch (Exception e){
            e.printStackTrace();
        }

        mMap.addMarker(new MarkerOptions().position(point).title(marcador).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        dataProvider = new DataProvider();
        dataProvider.setLatitude(point.latitude);
        dataProvider.setLongitude(point.longitude);
        dataProvider.setLocation(marcador);

        databaseOper.createData(dataProvider);

    }

    public void getData(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            lat = bundle.getDouble("latitude");
            longi = bundle.getDouble("longitude");
            position = bundle.getInt("position");
        }
    }
}
