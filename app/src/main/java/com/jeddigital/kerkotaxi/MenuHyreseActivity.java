package com.jeddigital.kerkotaxi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import java.util.HashMap;
import java.util.Map;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.jeddigital.kerkotaxi.Models.Taxi;
import java.util.List;


public class MenuHyreseActivity extends FragmentActivity implements LocationListener {

    private GoogleMap Harta;
    Location client_live_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isGooglePlayServicesAvailable()) {
            finish();                                                                               // ketu mund ti nxjerrim nje warning qe duhet te update ose instaloje google play services
        }

        setContentView(R.layout.activity_menu_hyrese);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Harta.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        client_live_location = locationManager.getLastKnownLocation(bestProvider);
        if (client_live_location != null) {
            onLocationChanged(client_live_location);
        }

        locationManager.requestLocationUpdates(bestProvider, 10000, 0, (LocationListener) this);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        client_live_location = location;
        double client_latitude = client_live_location.getLatitude();
        double client_longitude = client_live_location.getLongitude();
        LatLng latLng = new LatLng(client_latitude, client_longitude);
        Harta.moveCamera(CameraUpdateFactory.newLatLngZoom((latLng), 14.0F));
        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        send_client_location();
    }

    private void send_client_location(){
        SharedPreferences Preferencat_Klient = getSharedPreferences(Configurations.SHARED_PREF_CLIENT, Context.MODE_PRIVATE);

        final String client_location= "clientlocation";
        final String client_id = Preferencat_Klient.getString(Configurations.ClIENT_ID_PREF, "");
        final String client_live_latitude  = String.valueOf(client_live_location.getLatitude()).toString().trim();
        final String client_live_longitude = String.valueOf(client_live_location.getLongitude()).toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configurations.WEB_SERVICE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase(String.valueOf(Configurations.RESPONSE_SUCCESS))) {
                            Log.d("qqq", "Po dergohen te dhenat e vendndodhjes se klientit");
                        } else {
                            Log.d("qqq", " erroriiiiiiiiiiiiiiiiiiiiiii *********:" + response);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("qqq", "ErrorResponseOnLocationChanged: " + error.getMessage());
                if(error instanceof NoConnectionError) {
                    Log.d("qqq", "NoConnectionError: " + error.getMessage());
                }
                else if( error instanceof TimeoutError) {

                }
                else if (error instanceof AuthFailureError) {
                    Log.d("qqq", "AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    Log.d("qqq", "ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    Log.d("qqq", "NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    Log.d("qqq", "ParseError: " + error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("clientlocation", client_location);
                params.put("client_id", client_id);
                params.put("lat", client_live_latitude);
                params.put("lng", client_live_longitude);

                Log.d("qqq", client_live_latitude);
                Log.d("qqq", client_live_longitude);
       /*       SharedPreferences Preferencat_Klient = getSharedPreferences(Configurations.SHARED_PREF_CLIENT, Context.MODE_PRIVATE);
                SharedPreferences.Editor set_Prefs = Preferencat_Klient.edit();
                set_Prefs.putString(Configurations.CLIENT_LATITUDE_PREF, client_live_latitude);
                set_Prefs.putString(Configurations.CLIENT_LONGITUDE_PREF, client_live_longitude);
                set_Prefs.commit();
        */
                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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




    private List<Taxi> getNearbyTaxis (){
            return null;
    }
}
