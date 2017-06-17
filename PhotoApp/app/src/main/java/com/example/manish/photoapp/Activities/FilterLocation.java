package com.example.manish.photoapp.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manish.photoapp.Contracts.IDataRecord;
import com.example.manish.photoapp.Database.FeedReaderDbHelper;
import com.example.manish.photoapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FilterLocation extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap _googleMap;
    private double _lat;
    private double _long;
    private TextView _userRange;
    private final int EARTH_RADIUS = 6371;
    private Circle _mapCircle;
    private Location _location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_location);
        _userRange = (TextView)findViewById(R.id.range_display);
        initMap(savedInstanceState);

    }
    private void initMap(Bundle savedInstanceState) {
        MapFragment map = (MapFragment) getFragmentManager().findFragmentById(R.id.filter_location_map);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);
        _userRange.setText("10");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        _googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if(!_googleMap.isMyLocationEnabled()) {
            _googleMap.setMyLocationEnabled(true);
        }
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        _location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(_location == null){
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_HIGH);
            String provider = locationManager.getBestProvider(criteria, true);
            _location = locationManager.getLastKnownLocation(provider);
        }
        if(_location != null) {
            _lat = _location.getLatitude();
            _long = _location.getLongitude();
            LatLng userLocation = new LatLng(_lat, _long);
            _googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14), 1500, null);
            _googleMap.addMarker(new MarkerOptions().position(userLocation)
                    .snippet("Lat:" +_lat + "Lng:"+ _long))
                    .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            drawRange(userLocation);
        }
    }
    public void increment(View v) {
        int prevValue = Integer.parseInt(_userRange.getText().toString());
        int newValue = prevValue + 20;
        _userRange.setText(newValue + "");
        LatLng userLocation = new LatLng(_lat, _long);
        if(_mapCircle != null) {
            _mapCircle.remove();
        }
        drawRange(userLocation);
    }
    public void decrement(View v) {
        int prevValue = Integer.parseInt(_userRange.getText().toString());
        int newValue = prevValue - 20;
        if(newValue <= 0) {
            newValue = prevValue;
        }
        else {
            _userRange.setText(newValue + "");
            LatLng userLocation = new LatLng(_lat, _long);
            if(_mapCircle != null) {
                _mapCircle.remove();
            }
            drawRange(userLocation);
        }
    }
    public void closeIntent(View v) {
        this.finish();
    }
    private void drawRange(LatLng center) {
        int radius = Integer.parseInt(_userRange.getText().toString());
        int pixels = convertMetersToPixels(_lat,_long,radius);
        CircleOptions options = new CircleOptions().center(center).radius(pixels).fillColor(0x33FF0000).strokeColor(Color.BLUE).strokeWidth(3);
        _mapCircle = _googleMap.addCircle(options);
    }
    private int convertMetersToPixels(double lat, double lng, double radiusInMeters) {
        double dx = (2*Math.PI/360) * EARTH_RADIUS * Math.cos(_lat);
        double dy = (2*Math.PI/360) * EARTH_RADIUS * Math.cos(_lat);
        _lat = _lat + (dy/EARTH_RADIUS) * (180/Math.PI);
        _long = _long + (dx/EARTH_RADIUS) * (180/Math.PI) / Math.cos(_lat * Math.PI/180);
        double lat1 = radiusInMeters / EARTH_RADIUS;
        double lng1 = radiusInMeters / (EARTH_RADIUS * Math.cos((Math.PI * lat / 180)));

        double lat2 = lat + lat1 * 180 / Math.PI;
        double lng2 = lng + lng1 * 180 / Math.PI;

        Point p1 = _googleMap.getProjection().toScreenLocation(new LatLng(lat, lng));
        Point p2 = _googleMap.getProjection().toScreenLocation(new LatLng(lat2, lng2));

        return Math.abs(p1.x - p2.x);
    }
    public void saveEntryToDB(View v) {
        String query = "SELECT * FROM " + IDataRecord.TABLE_NAME + " WHERE " + IDataRecord.COLUMN_NAME_SUBTITLE3 + " IS NOT NULL ";
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("locationFilterQuery",query);
        startActivity(i);
    }
}
