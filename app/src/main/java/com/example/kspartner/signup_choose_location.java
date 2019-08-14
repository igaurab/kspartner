package com.example.kspartner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
//Things done in this class are:
//1. Get the current location of the user
//2. Complete the instance of Restaurant model created in Main Activity.
//3. Generate a unique id for the Restaurant
//4. Create a sharedPreference
//      4.1 The sharedPreference contains the id of the Restaurant created and is used for displaying and Adding the contents of the Restaurant
//5. Generate a User Class Based on UID and RID for the newly created Restaurant
public class signup_choose_location extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //permissions
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    //maps
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final float DEFAULT_ZOOM = 15f;
    //widgets
    private EditText mSearchText;
    private Button next;
    //vars
    private String latitude;
    private String longitude;
    private String new_key;
    private String menu_id;
    //Shared Preferences
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_choose_location);
        mSearchText = (EditText) findViewById(R.id.txt_input_search_restaurant);
        next = findViewById(R.id.btn_next_maps);

        //Restaurant Owner User Object
       final Restaurant_owner_info restaurant_owner_info = new Restaurant_owner_info();

//       Shared Preference Initialization:
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Restaurant_Pref", 0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        getLocationPermission();
//          #####################################################################
//            DATABASE WORKS FOR GETTING THE NEXT KEY OF RESTAURANT
//          #####################################################################
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final DatabaseReference restaurant_owner_info_db_reference = FirebaseDatabase.getInstance().getReference("User_Restaurant");

        final String uid = mAuth.getCurrentUser().getUid();
        String emailId = mAuth.getCurrentUser().getEmail();
        restaurant_owner_info.setUid(uid);
        restaurant_owner_info.setEmailId(emailId);
        Log.d("Tags", "onClick: " + uid);

        final Restaurant restaurant = ((Restaurant)getIntent().getSerializableExtra("RESTAURANT_DATA"));
        final String account_created_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Restaurants");
        Query query = databaseReference.orderByKey().limitToLast(1) ;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String prev_key;
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    prev_key = child.getKey();
                    int key_num = Integer.parseInt( String.valueOf( prev_key.charAt(prev_key.length() - 1)));
                    key_num += 1;
                    new_key = "rid"+ key_num;
                    menu_id = "mid" + key_num;
                        assert restaurant != null;
                        restaurant.setR_id(new_key);
                        restaurant.setMenu_id(menu_id);
                        restaurant.setLatitude(latitude);
                        restaurant.setLongitude(longitude);
                        restaurant.setR_created_date(account_created_date);
                        restaurant_owner_info.setRid(new_key);
//                            Shared Preference Editor
                        editor.putString("uid",uid);
                        editor.putString("rid",new_key);
                        editor.putBoolean("session",true);
                        editor.apply();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        });
//##################################################################################################/#
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(String.valueOf(new_key)).setValue(restaurant);
                restaurant_owner_info_db_reference.child(uid).setValue(restaurant_owner_info);
                Intent intent = new Intent(signup_choose_location.this, Add_Description.class);
                startActivity(intent);
            }
        });

    }
    private void init_search() {
        Log.d("Function:", "Init Search entered");

        TextView.OnEditorActionListener searchOnEnter = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                Log.d("Function:", "onEditorAction entered");

                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO
                        || actionId == EditorInfo.IME_ACTION_SEARCH
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    geoLocate();

                } else {

                    Log.d("Function:", "GeoLocate can't be entered");

                }
                return false;
            }
        };

        mSearchText.setOnEditorActionListener(searchOnEnter);
    }

    private void geoLocate() {
        Log.d("Function:","Geolocate entered");
        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(signup_choose_location.this);
        List<Address> list_of_address = new ArrayList<>();
        try {
            list_of_address = geocoder.getFromLocationName(searchString,3);
        }catch (IOException e) {
            Toast.makeText(this,"Oh ho!",Toast.LENGTH_LONG).show();

        }
        if (list_of_address.size() > 0) {
            Address address = list_of_address.get(0);
            Toast.makeText(this,address.toString(),Toast.LENGTH_LONG).show();

            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
        }
    }
    private void getDeviceCurrentLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(signup_choose_location.this, "got your location", Toast.LENGTH_LONG).show();
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM,"me");
                            latitude = String.valueOf(currentLocation.getLatitude());
                            longitude = String.valueOf(currentLocation.getLongitude());

                        } else {
                            Toast.makeText(signup_choose_location.this, "Unable to get Location", Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }

        } catch (SecurityException e) {

        }
    }

    private void moveCamera(LatLng latLng, float zoom,String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

//        Drop a pin
        MarkerOptions marker_options = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(marker_options);
    }

    private void initMap() {
        Toast.makeText(this, "Maps Initialized", Toast.LENGTH_LONG).show();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION, COURSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    initMap();

                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceCurrentLocation();
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            init_search();
        }
    }
}

