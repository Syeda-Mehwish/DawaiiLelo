package com.example.dawaiilello.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.dawaiilello.DekiveryActivity;
import com.example.dawaiilello.PostActivity;
import com.example.dawaiilello.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddressActivity extends AppCompatActivity {
  public static   double Latitude=0.00;
 public static    double Longitude =0.00;
    String address;
    String time="min";
    String fragment;
   public  static String token;
   public static String token1;

    FusedLocationProviderClient mFusedLocationClient;


    TextView latitudeTextView, longitTextView;
    int PERMISSION_ID = 44;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        latitudeTextView = findViewById(R.id.latTextView);
        longitTextView = findViewById(R.id.lonTextView);
        fragment=getIntent().getStringExtra("intent");
        token = getIntent().getStringExtra("token");
        token1=getIntent().getStringExtra("token1");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        getLastLocation();


    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitudeTextView.setText(location.getLatitude() + "");
                            longitTextView.setText(location.getLongitude() + "");
                            Latitude=location.getLatitude();
                            Longitude=location.getLongitude();
                            try {
                                setAddress(Latitude,Longitude);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                });

            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
           Latitude=mLastLocation.getLatitude();
           Longitude=mLastLocation.getLongitude();
            try {
                setAddress(Latitude,Longitude);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private void setAddress(Double latitude, Double longitude) throws IOException {
        Geocoder geocoder;

        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        if(addresses.size() > 0) {
            Log.d("max", " " + addresses.get(0).getMaxAddressLineIndex());
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            addresses.get(0).getAdminArea();
            Toast.makeText(AddressActivity.this, address+" "+city+" "+state+" "+country+" "+postalCode+" "+knownName, Toast.LENGTH_SHORT).show();
            if (!fragment.equals("Prescription")) {


                FirebaseFirestore.getInstance().collection("Riders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            String keyword = documentSnapshot.get("AssignedArea").toString();
                            Boolean found = address.contains(keyword);
                            if (found) {
                                String id = documentSnapshot.get("Rider_id").toString();
                                Map<String, Object> userOrder = new HashMap<>();
                                userOrder.put("Order ID", fragment);
                                FirebaseFirestore.getInstance().collection("Riders").document(id).collection("orders").document(fragment).set(userOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }

                        }

                    }
                });
            }
            Location pharmacy = new Location("point B");
            Location user =new Location("point A");
            user.setLatitude(Latitude);
            user.setLongitude(Longitude);
            pharmacy.setLatitude(25.36431069300296);
            pharmacy.setLongitude(68.35629558371635);
            float x = user.distanceTo(pharmacy);
            //distance.distanceBetween(25.370332,68.340832,25.36431069300296,68.35629558371635,results);
            //float x = results[0];
            int kilometers = (int) (x/1000);
            kilometers=kilometers*3;
            if (kilometers<60){

            }else if(kilometers>60){
                kilometers=kilometers/60;
                time="hours";
            }
            Toast.makeText(AddressActivity.this, String.valueOf(kilometers*3)+"  "+String.valueOf(x), Toast.LENGTH_SHORT).show();
            if (!fragment.equals("Prescription")) {
                Map<String, Object> userOrder = new HashMap<>();
                userOrder.put("Address", address);
                userOrder.put("Estimated Time", String.valueOf(kilometers + " " + time));
                userOrder.put("Latitude", Latitude);
                userOrder.put("Longitude", Longitude);
                FirebaseFirestore.getInstance().collection("ORDERS").document(fragment).update(userOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }else{
                Map<String, Object> data = new HashMap<>();
                 data.put("Address",address);
                  data.put("Latitude",(double)Latitude);
                  data.put("Longitude",(double)Longitude);
                  data.put("Estimated Time",String.valueOf(kilometers+" "+time));
                FirebaseFirestore.getInstance().collection("prescription").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AddressActivity.this, "Image is uploaded", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
            Intent i = new Intent(AddressActivity.this, DekiveryActivity.class);
            i.putExtra("order_id",fragment);
            i.putExtra("time",String.valueOf(kilometers+" "+time));
            i.putExtra("address","live");
            startActivity(i);
        }







             }


    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }
}