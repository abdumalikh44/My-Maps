package com.example.googlemaps.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.googlemaps.Model;
import com.example.googlemaps.R;
import com.example.googlemaps.databinding.FragmentHomeBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    FragmentHomeBinding binding;
    SupportMapFragment mapFragment;
    private GoogleMap nMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker marker;
    private MarkerOptions markerOptions;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding = FragmentHomeBinding.inflate(inflater, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("post");

        mapInitialize();

         return binding.getRoot();
    }

    private void mapInitialize() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(16);
        locationRequest.setFastestInterval(3000);

        binding.searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH
                        || i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    gotToSearchLocation();
                }

                return false;
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

    }

    private void gotToSearchLocation() {
        String searchLocation = binding.searchEdt.getText().toString();

        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchLocation, 1);
        }catch (IOException e){
            e.printStackTrace();
        }

        if(list.size()>0){
            Address address = list.get(0);
            String location = address.getAdminArea();
            double latitude = address.getLatitude();
            double longitude = address.getLongitude();
            gotoLatlng(latitude, longitude, 17f);
            /*if(marker != null){
                marker.remove();
            }
            //Untuk membuat penanda lokasi
            markerOptions = new MarkerOptions();
            markerOptions.title(location);
            markerOptions.draggable(true);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            markerOptions.position(new LatLng(latitude, longitude));
            marker  = nMap.addMarker(markerOptions);*/
            /*TestOut*/


        }
    }

    private void gotoLatlng(double latitude, double longitude, float v) {
        LatLng latLng = new LatLng(latitude, longitude);
        /*binding.Latitude.setText(String.valueOf(latLng.latitude));
        binding.Longitude.setText(String.valueOf(latLng.longitude));*/
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 17f);
        nMap.animateCamera(update);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        nMap = googleMap;

        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    Model model = dataSnapshot.getValue(Model.class);
                                    LatLng latLng = new LatLng(model.getLatitude(), model.getLongitude());
                                    markerOptions = new MarkerOptions();
                                    markerOptions.position(latLng)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                    marker = nMap.addMarker(markerOptions);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        nMap.setMyLocationEnabled(true);
                        fusedLocationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                nMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(getContext(), "Permission"+
                                permissionDeniedResponse.getPermissionName() + ""+ " was denied!", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

        /*if(nMap != null){
            nMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener(){

                @Override
                public void onMarkerDrag(@NonNull Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(@NonNull Marker marker) {
                    Geocoder geocoder = new Geocoder(getContext());
                    List<Address> list = null;
                    try{
                        LatLng markerPosition = marker.getPosition();
                        list = geocoder.getFromLocation(markerPosition.latitude, markerPosition.longitude, 1);
                        binding.Latitude.setText(String.valueOf(markerPosition.latitude));
                        binding.Longitude.setText(String.valueOf(markerPosition.longitude));
                        Address address = list.get(0);
                        marker.setTitle(address.getAdminArea());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onMarkerDragStart(@NonNull Marker marker) {

                }
            });

        }*/

    }
}