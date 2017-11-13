package com.example.handlers.parkingslots;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    RequestQueue requestQueue;
    private String URL = "http://192.168.43.175:4000/getparkings";
    private String LINK = "http://192.168.43.175:4000/statusPay/notifications";
    Double lat, lon;
    private boolean mLocationPermissionGranted;
    private ArrayList<Parking> parkingArray;
    ProgressDialog pDialog;
    boolean showNotification = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        getLocationPermission();
        new LongOperation().execute();
    }

    private class LongOperation extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            while (true) {
                if (!showNotification) {
                    try {
                        Thread.sleep(8000);
                        checkForInformation();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else
                    return null;
            }
        }

        @Override
        protected void onPostExecute(Void result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void checkForInformation() {

        Log.e(TAG, "checkForInformation: ");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status == "true") {
                        showNotification = true;
                        showDialogForPayment();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void showDialogForPayment() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Pay Bill Rs 100")
                .setMessage("pay your parking bill digitally !")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            updateUI();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        if (mLocationPermissionGranted)
            updateUI();
    }

    private void updateUI() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Fetching location please wait");
        pDialog.show();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Get user location
        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                //Got the location!
                lat = location.getLatitude();
                lon = location.getLongitude();
//                LatLng sydney = new LatLng(lat, lon);
//                float zoomLevel = 16.0f; //This goes up to 21
//                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
                Log.e(TAG, "gotLocation: " + location.getLatitude() + "  " + location.getLongitude());
                requestQueue = Volley.newRequestQueue(MapsActivity.this);
                getParkingLocations();
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);

        // Sync mapFragment
        mapFragment.getMapAsync(this);
    }

    private void getParkingLocations() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("true")) {
                                parkingArray = new ArrayList<>();
                                JSONArray jsonArray = jsonObject.getJSONArray("message");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    String lat = object.getString("lat");
                                    String lon = object.getString("lng");
                                    String total = object.getString("total");
                                    String four_avail = object.getString("four_avail");
                                    String three_avail = object.getString("three_avail");
                                    String two_avail = object.getString("two_avail");
                                    Parking parking = new Parking(id,
                                            name, lat, lon, total, four_avail,
                                            three_avail, two_avail);
                                    parkingArray.add(parking);
                                }
                                pDialog.dismiss();
                                setParkingLocations();
                            }
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(MapsActivity.this, "Error", Toast.LENGTH_SHORT);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lat", lat.toString());
                params.put("lng", lon.toString());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void setParkingLocations() {
        for (Parking parking : parkingArray) {
            Double lat = Double.parseDouble(parking.getLat());
            Double lon = Double.parseDouble(parking.getLon());
            String snippet = "Total parking available " + parking.getTotal() + "\n" + "Four Wheelers : " + parking.getFour_avail() + " Two Wheelers : " + parking.getTwo_avail();
            Log.e(TAG, "setParkingLocations: " + lat + " " + lon);
            String name = parking.getName();
            createMarker(lat, lon, name, snippet);
        }
        float zoomLevel = 12.0f;
        LatLng location = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title("My Location")
                .snippet("Current location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                int position = (int) (marker.getTag());
//                Log.e(TAG, "onMarkerClick: " + position );
//                //Using position get Value from arraylist
//                return false;
//            }
//        });
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);

    }


    protected Marker createMarker(double latitude, double longitude, String title, String snippet) {

        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet));
    }
}
