package com.tiwari.aditi.eguzzle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(MapActivity.this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
            init();
        }
    }

    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }

    public static double HaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371;
        double latDistance = toRad(lat2 - lat1);
        double lonDistance = toRad(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return (distance);
    }

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 10f;


    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private String city;


    private double[] bangalore_lats = {12.927007, 12.956866, 12.8102007, 12.873319,
            13.0447739, 12.9733411, 12.9660461, 12.9363644, 12.9567894, 12.9756585,
            12.9735088, 13.0191352, 12.932401, 12.9756585, 12.9170164, 12.987552,
            12.9689921, 12.9864394, 13.0018169, 12.975557, 12.9731232, 12.9324134,
            12.9113094, 12.9092577, 13.0209018, 12.9225031};
    private double[] bangalore_lons = {77.578633, 77.581161, 77.6622891, 77.616946,
            77.7355685, 77.728132, 77.5989649, 77.6077652, 77.5885525, 77.641261,
            77.7294329, 77.6412291, 77.563981, 77.641261, 77.5984137, 77.6208192,
            7.6361515, 77.7357274, 77.6234658, 77.598291, 77.6032993, 77.6038723,
            77.676223, 77.651705, 77.5983191, 77.584627};
    private String[] bangalore_pts =  {"Zon Motors", "Maruthi Electronics",
            "Mahindra Reva Solar Station", "Volta Automotive", "SemaConnect",
            "Fast Chargning Mahindra Electric", "Mahindra Electric Fast Charger",
            "Mahindra Electric Fast Charger", "Viraja E-bikes", "Ather Grid",
            "Lithium DC_FC", "Anandhi Power", "Supreme Ridz", "Ather Grid",
            "Go GreenBOV", "Ather Grid", "Ather Grid", "Ather Grid", "Ather Grid",
            "Ather Grid", "Ather The Only Place", "Ather Grid", "Ather Grid",
            "Ather Grid", "Ather Grid", "Ather Grid"};


    private double[] delhi_lats = {28.5503609, 28.578503, 28.578503, 28.647665, 28.660506,
            28.670424, 28.5836863, 28.5022134, 28.6275183, 28.538623, 28.630996,
            28.647908, 28.6193011, 28.5006296, 28.6289636, 28.669565, 28.5003352,
            28.631966, 28.6917027, 28.672591, 28.4535404, 28.6868613, 28.690257,
            28.4180814, 28.647037, 28.675825, 28.4735596, 28.4522262, 28.441221,
            28.670434, 28.6160868, 28.6838843, 28.7029761};
    private double[] delhi_lons = {77.2139161, 77.1834761, 77.1834761, 77.196636, 77.223487,
            77.057227, 77.0801998, 77.2850002, 77.1111214, 77.266545, 77.281494,
            77.196681, 77.0674563, 77.084374, 77.3026459, 77.30848, 77.1679283,
            77.304524, 77.2990554, 77.0830233, 77.0623282, 77.0924392, 77.269441,
            77.0506896, 77.0530629, 77.290481, 77.0180872, 77.0563429, 77.0394841,
            77.078501, 77.35484, 76.9318827, 77.1414601};
    private String[] delhi_pts =  {"EV Plugin Charge", "Fortum Charger", "Standard Electric",
            "Baaz Headgar", "Jai Lakshmi Traders", "Wholesale E-Rickshaw", "Dada Dev",
            "Saini Hero Electric", "Incredible Electric Vehicles", "Mahindra E Rickshaw",
            "PN Electricals", "Karan Enterprises", "EVSpecialists.in", "SmartE Hub",
            "Z&D Z Advertising", "Standard Electric And Gerenral Ind", "OK Play",
            "Z&D Z Advertising", "OK Play Showroom", "Lotus Auto", "Ion Ohms", "NICE-DC",
            "Abhi Moters", "Honeywell", "Shri Subhash E Rickshaw", "Bhartiya E Rickshaw",
            "Rath E Rickshaw", "SBD Green Energy", "NN4 Energy", "Ravi E Rickshaw",
            "A1 Motors", "Saarthi E Rickshaw", "Biot Motors"};


    private double[] hyd_lats = {17.4251626, 17.453905, 17.424499, 17.4876539, 16.975521,
            17.4456982, 17.341353, 17.4244876, 17.3980569, 17.4255808, 17.496944, 17.4980389,
            17.3627782, 17.4233418, 17.4448978, 17.5951434, 17.376469, 17.4740801};
    private double[] hyd_lons = {78.4388706, 78.417768, 78.4509927, 78.4135795, 78.729289,
            78.3488064, 78.804283, 78.379201, 78.5548082, 78.4144172, 78.371794, 78.3777485,
            78.4352417, 78.4589431, 78.4654886, 78.0843647, 79.021602, 78.5629887};
    private String[] hyd_pts =  {"Amplify Mobility", "Unique Battery Charging", "Sattar Auto",
            "GPS Tracking Solution", "Sri Sai Electrician", "Gayam Motor Works",
            "DN Reddy Electrical", "HPCL Chrging Station", "Fortum Mahindra Electric",
            "Fortum Mahindra Electric", "PGCIL Charging Station", "Fortum Mahindra Electric",
            "Fortum Mahindra Electric", "Fortum Mahindra Electric", "Fortum Mahindra Electric",
            "Electric Bike", "Kamal Electrical", "Aniraj Motors"};


    private double[] chennai_lats = {13.053278, 12.9757007, 13.0846989, 13.038582, 12.9609729,
            12.969888, 13.0486391, 12.916836, 12.787861, 13.061943, 13.0102552};
    private double[] chennai_lons = {80.238158, 80.1569581, 80.2142043, 80.213402, 80.1376462,
            80.1885132, 80.2072507, 80.102252,80.029013, 80.2716148, 80.1396864};
    private String[] chennai_pts = {"Romai Electric Schooter", "Ayudh Bhavan Factory",
            "Falcon-i", "Genesis Forte Tech", "Sifa Battery", "Vinayaga Traders",
            "Viston Greentech", "Tambaram Hero Electric Bikes", "Power Active",
            "KGN Car AC", "Shiv Electric Wheels"};


    private double[] mumbai_lats = {19.079898, 19.094981, 19.139308, 19.196104, 19.179674,
            19.118226, 19.0176147, 19.0963532, 19.0743356, 19.0852915};
    private double[] mumbai_lons = {72.840278, 72.9262379, 72.855178, 72.833859, 72.844109,
            72.8683713, 72.8561644, 73.0030217, 72.9869988, 72.8872842};
    private String[] mumbai_pts = {"Plugin Electrics", "Tata Power Charging Point",
            "Deepak Electrics", "Rock Ultimate Electronic Toy Shop", "EVteQ",
            "Reliance Electric Charging Station", "Al Majeed Trading", "Magenta EV-1",
            "Magenta EV-2", "EV Charging Station"};

    private double[] kol_lats = {22.6324972, 22.625509, 22.537166, 22.8633982, 22.5817103,
            22.5670271, 22.8052577, 22.6461286};
    private double[] kol_lons = {88.4161281, 88.41320, 88.3700895, 88.3540637, 88.3416626,
            88.2662117, 88.3207227, 88.3508478};
    private String[] kol_pts = {"DB Enterprise", "Bhoothnaath Motors", "Zakir & Sons",
            "WBSETCL CHANDANNAGAR", "Platform 18", "Charging Point", "Eevee Engineering",
            "Bajragbali Toto Dealers"};


    private double[] pune_lats = {18.542232, 18.5394154, 18.46092, 18.7591127, 18.498417,
            18.4726725, 18.6958518, 18.5920995, 18.5110463, 18.4614341, 18.541435, 18.5672934,
            18.499698, 18.573301, 18.7595127, 18.7596404, 18.4762607, 18.6086587, 18.6433876};
    private double[] pune_lons = {73.9353745, 73.9346419, 73.88826, 73.3708389, 73.958601,
            73.9784573, 74.139045, 73.9064926, 73.6843495, 73.8583056, 73.777999, 73.7718616,
            73.949568, 73.907199, 73.4274442, 73.4276756, 74.2681368, 73.8223795, 73.7531565};
    private String[] pune_pts = {"Tunwal E-Vehicle", "Okinawa Electric Schooter",
            "Hira Distributers", "Khandala Motor Garage", "Tunwal E-Vehicle", "Siddhant Enterprises",
            "Exide Batteries", "Mechatronic Trading", "Deshmukh Motors", "Aarna Motors",
            "Vidyut Terra Motors", "PowerUp EV", "Pragati Autoline", "KT eMotors",
            "Chargegrid Magenta EV Station", "EV Charging Station", "AVAN Motors",
            "AVAN Motors", "Yash Batteries"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        city = bundle.getString("cityname");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getLocationPermission();
    }

    private void init() {
        Log.d(TAG, "init: initializing");

        geoLocate();

        Button btnMap = (Button) findViewById(R.id.btn10k);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geoLocate10();
            }
        });
    }

    private void geoLocate() {

        Geocoder geocoder = new Geocoder(MapActivity.this);

        double[] curr_lats = {};
        double[] curr_lons = {};
        String[] curr_pts = {};
        double city_lat = 0;
        double city_lon = 0;

        if(city.equals("bangalore")) {

            Log.d(TAG, "geoLocate: Geolocating " + city);

            curr_lats = bangalore_lats;
            curr_lons = bangalore_lons;
            curr_pts = bangalore_pts;
            city_lat = 12.9716;
            city_lon = 77.5946;

        }else if(city.equals("delhi")){
            Log.d(TAG, "geoLocate: Geolocating " + city);

            curr_lats = delhi_lats;
            curr_lons = delhi_lons;
            curr_pts = delhi_pts;
            city_lat = 28.7041;
            city_lon = 77.1025;
        }else if(city.equals("hyderabad")){
            Log.d(TAG, "geoLocate: Geolocating " + city);

            curr_lats = hyd_lats;
            curr_lons = hyd_lons;
            curr_pts = hyd_pts;
            city_lat = 17.3850;
            city_lon = 78.4867;
        }else if(city.equals("chennai")){
            Log.d(TAG, "geoLocate: Geolocating " + city);

            curr_lats = chennai_lats;
            curr_lons = chennai_lons;
            curr_pts = chennai_pts;
            city_lat = 13.0827;
            city_lon = 80.2707;
        }else if(city.equals("mumbai")){
            Log.d(TAG, "geoLocate: Geolocating " + city);

            curr_lats = mumbai_lats;
            curr_lons = mumbai_lons;
            curr_pts = mumbai_pts;
            city_lat = 19.0760;
            city_lon = 72.8777;
        }else if(city.equals("kolkata")){
            Log.d(TAG, "geoLocate: Geolocating " + city);

            curr_lats = kol_lats;
            curr_lons = kol_lons;
            curr_pts = kol_pts;
            city_lat = 22.5726;
            city_lon = 88.3639;
        }else if(city.equals("pune")){
            Log.d(TAG, "geoLocate: Geolocating " + city);

            curr_lats = pune_lats;
            curr_lons = pune_lons;
            curr_pts = pune_pts;
            city_lat = 18.5204;
            city_lon = 73.8567;
        }


        moveCamera(new LatLng(city_lat,city_lon), DEFAULT_ZOOM);

        for (int i = 0; i < curr_pts.length; i++) {
            List<Address> list = new ArrayList<>();

            try {
                list = geocoder.getFromLocation(curr_lats[i], curr_lons[i], 1);
            } catch (IOException e) {
                Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
            }

            if (list.size() > 0) {
                Address address = list.get(0);
                Log.d(TAG, "geoLocate: found a location: " + address.toString());
                //moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM);

                MarkerOptions options = new MarkerOptions()
                        .position(new LatLng(address.getLatitude(),
                                address.getLongitude()))
                        .title(curr_pts[i]);
                mMap.addMarker(options);
            } else {
                Log.d(TAG, "geoLocate: No results found");
            }
        }


    }

    private void geoLocate10() {

        mMap.clear();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (mLocationPermissionsGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onComplete: current location is null");
                Toast.makeText(MapActivity.this, "unable to get location",
                        Toast.LENGTH_SHORT).show();
                return;
            }else{

                Log.d(TAG, "getDeviceLocation: getting the device location");

                try{
                    if(mLocationPermissionsGranted){
                        Task location = mFusedLocationProviderClient.getLastLocation();
                        location.addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){
                                    Log.d(TAG, "onComplete: found location");
                                    Location currentLocation = (Location) task.getResult();

                                    moveCamera(new LatLng(currentLocation.getLatitude(),
                                                    currentLocation.getLongitude()),
                                            11f);


                                    Geocoder geocoder = new Geocoder(MapActivity.this);

                                    double[] curr_lats = {};
                                    double[] curr_lons = {};
                                    String[] curr_pts = {};

                                    int within_10 = 0;

                                    if (city.equals("bangalore")) {

                                        Log.d(TAG, "geoLocate: Geolocating " + city);

                                        curr_lats = bangalore_lats;
                                        curr_lons = bangalore_lons;
                                        curr_pts = bangalore_pts;
                                    } else if (city.equals("delhi")) {
                                        Log.d(TAG, "geoLocate: Geolocating " + city);

                                        curr_lats = delhi_lats;
                                        curr_lons = delhi_lons;
                                        curr_pts = delhi_pts;
                                    }else if(city.equals("hyderabad")){
                                        Log.d(TAG, "geoLocate: Geolocating " + city);

                                        curr_lats = hyd_lats;
                                        curr_lons = hyd_lons;
                                        curr_pts = hyd_pts;
                                    }else if(city.equals("chennai")){
                                        Log.d(TAG, "geoLocate: Geolocating " + city);

                                        curr_lats = chennai_lats;
                                        curr_lons = chennai_lons;
                                        curr_pts = chennai_pts;
                                    }else if(city.equals("mumbai")){
                                        Log.d(TAG, "geoLocate: Geolocating " + city);

                                        curr_lats = mumbai_lats;
                                        curr_lons = mumbai_lons;
                                        curr_pts = mumbai_pts;
                                    }else if(city.equals("kolkata")){
                                        Log.d(TAG, "geoLocate: Geolocating " + city);

                                        curr_lats = kol_lats;
                                        curr_lons = kol_lons;
                                        curr_pts = kol_pts;
                                    }else if(city.equals("pune")){
                                        Log.d(TAG, "geoLocate: Geolocating " + city);

                                        curr_lats = pune_lats;
                                        curr_lons = pune_lons;
                                        curr_pts = pune_pts;
                                    }

                                    Log.d(TAG, "geoLocate: Geolocating " + city + "within 10 K");

                                    for (int i = 0; i < curr_pts.length; i++) {
                                        List<Address> list = new ArrayList<>();

                                        double curr_distance = HaversineDistance(curr_lats[i], curr_lons[i],
                                                currentLocation.getLatitude(), currentLocation.getLongitude());

                                        if (curr_distance <= 10.0) {

                                            within_10++;
                                            Log.d(TAG, "geoLocate: Haver Distance " + curr_distance);

                                            try {
                                                list = geocoder.getFromLocation(curr_lats[i], curr_lons[i], 1);
                                            } catch (IOException e) {
                                                Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
                                            }

                                            if (list.size() > 0) {
                                                Address address = list.get(0);
                                                Log.d(TAG, "geoLocate: found a location: " + address.toString());

                                                MarkerOptions options = new MarkerOptions()
                                                        .position(new LatLng(address.getLatitude(),
                                                                address.getLongitude()))
                                                        .title(curr_pts[i]);
                                                mMap.addMarker(options);
                                            } else {
                                                Log.d(TAG, "geoLocate: No results found");
                                            }
                                        }

                                    }

                                    if (within_10 == 0) {
                                        Log.d(TAG, "geoLocate: No results found within 10 K");
                                        Toast.makeText(MapActivity.this,
                                                "No results found within 10 KM of your location",
                                                Toast.LENGTH_LONG).show();
                                    }



                                }else{
                                    Log.d(TAG, "onComplete: current location is null");
                                    Toast.makeText(MapActivity.this, "unable to get location",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }catch (SecurityException e) {
                    Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
                }

            }

        }




    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the device location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionsGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, "unable to get location",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }

    }

    private void moveCamera(LatLng latlng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to : lat: " + latlng.latitude + ", lng: " + latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }


    private void initMap(){
        Log.d(TAG, "initMap: initialize map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i =0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: Permission Granted");
                    mLocationPermissionsGranted = true;
                    //initialize map

                    initMap();
                }
            }
        }
    }
}
