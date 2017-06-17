package com.example.manish.photoapp.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manish.photoapp.Database.FeedReaderDbHelper;
import com.example.manish.photoapp.Models.LocationTaggedPhoto;
import com.example.manish.photoapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.List;

public class LocationTag extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap _googleMap;
    private Context _caller;
    private GoogleApiClient _googleApiClient;
    private LocationRequest _locationReq;
    private double _lat;
    private double _long;
    private String _photoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_tag);
        if (googleServiceAvailable()) {
            initMap(savedInstanceState);
        }
        _caller = this;
        _photoPath = getIntent().getStringExtra("photoPath");
    }

    private void initMap(Bundle savedInstanceState) {
        MapFragment map = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);
    }

    public boolean googleServiceAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Failed to connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }
    public void closePage(View view) {
        this.finish();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        TextView lat = (TextView)findViewById(R.id.show_lat);
        TextView lon = (TextView)findViewById(R.id.show_long);
        TextView city = (TextView)findViewById(R.id.show_city);
        _googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(_caller, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(_caller, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if(!_googleMap.isMyLocationEnabled()) {
            _googleMap.setMyLocationEnabled(true);
        }
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(myLocation == null){
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_HIGH);
            String provider = locationManager.getBestProvider(criteria, true);
            myLocation = locationManager.getLastKnownLocation(provider);
        }
        if(myLocation != null) {
            _lat = myLocation.getLatitude();
            _long = myLocation.getLongitude();
            LatLng userLocation = new LatLng(_lat, _long);
            _googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14), 1500, null);
            _googleMap.addMarker(new MarkerOptions().position(userLocation)
                                                    .snippet("Lat:" +_lat + "Lng:"+ _long))
                                                    .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            lat.setText("" + _lat);
            lon.setText("" + _long);
            try {
                city.setText(getCity());
            } catch (IOException e) {
               Toast.makeText(getApplicationContext(), "Error getting city",Toast.LENGTH_SHORT).show();
            }
        }
        _googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        //_googleApiClient.connect();
    }
    public void saveEntryToDb(View v) {
        if(_photoPath != null) {
            FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            if(dbHelper.insertEntryByLatLong(new LocationTaggedPhoto(_photoPath, _lat, _long),db)) {
                Toast.makeText(getApplicationContext(), "Successfully Saved Photo",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(_caller, MainActivity.class);
                _caller.startActivity(i);
            }
            else {
                Toast.makeText(getApplicationContext(), "Error saving your photo", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public String getCity() throws IOException {
        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocation(_lat,_long,1);
        return list.get(0).getLocality();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        _locationReq = LocationRequest.create();
        _locationReq.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        _locationReq.setInterval(1000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(_googleApiClient, _locationReq, (com.google.android.gms.location.LocationListener) this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null) {
            Toast.makeText(this, "Location capture failed", Toast.LENGTH_SHORT).show();
        }
        else {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,15);
            _googleMap.animateCamera(update);
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
