package com.s22010695.limitly.activities;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.s22010695.limitly.R;
import com.s22010695.limitly.db_helpers.ZonesTableHandler;

import java.util.List;

public class AddZoneActivity extends AppCompatActivity implements OnMapReadyCallback {

    //declare objects
    private GoogleMap map;
    private List<Address> addressList;
    private EditText searchQuery;
    private ZonesTableHandler locationHelper;

    //declare variables
    private double longitude = 80.7718;
    private double latitude = 7.8731;

    private int radius = 300;
    private String title = "Sri Lanka";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_zone);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //get search query from id
        searchQuery = findViewById(R.id.searchQuery);

        //connect locations table
        locationHelper = new ZonesTableHandler(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        LatLng currentLocation = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions().position(currentLocation).title(title));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 7.0f));
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    //search zone method
    public void searchZone(View view) {
        //convert search query string
        String location = searchQuery.getText().toString();

        //search query validation
        if (location.isEmpty()) {
            //if query is empty toast a error message
            Toast.makeText(this, "Please enter a zone", Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            addressList = new Geocoder(this).getFromLocationName(location, 1);

            //check searched address is there or not
            if (addressList != null) {
                //get latitude and longitude from addressList
                latitude = addressList.get(0).getLatitude();
                longitude = addressList.get(0).getLongitude();
                title = location;

                //set new LatLng, clear current marker, add new marker and zoom new location
                LatLng newLocation = new LatLng(latitude, longitude);
                map.clear();
                map.addMarker(new MarkerOptions().position(newLocation).title(title));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 16.0f));

            } else {
                //if searched location not found toast a error message
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            //if any error toast a error message
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    //add zone method
    public void addLocation(View view) {
        //check user enter new location for prevent save default location
        if (latitude == 7.8731 && longitude == 80.7718 || searchQuery.getText().toString().isEmpty()){
            Toast.makeText(this, "Please search new zone", Toast.LENGTH_SHORT).show();
        } else{
            //save location in db
            boolean res = locationHelper.insertZone(title, latitude, longitude);

            if(res){
                searchQuery.setText("");
                Toast.makeText(this, "Zone Added", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //navigate back to the settings fragment
    public void navBackToSettingsFragment(View view) {
        finish();
    }

}