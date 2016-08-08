package com.jeddigital.kerkotaxi;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jeddigital.kerkotaxi.Adapters.NearbyVehiclesViewPagerAdapter;
import com.jeddigital.kerkotaxi.AndroidRestClientApi.AndroidRestClientApiMethods;
import com.jeddigital.kerkotaxi.AndroidRestClientApi.Configurations;
import com.jeddigital.kerkotaxi.AnroidRestModels.NearbyVehicle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MenuHyreseActivity extends FragmentActivity implements LocationListener {

    private GoogleMap map;
    Location client_live_location;
    Geocoder geocoder;
    TextView centerPosTV;
    RelativeLayout overMapLayer;
    RelativeLayout takeMeHereContainer;

    Button kerkoTaxoBTN;

    DisplayMetrics metrics;
    int scrWidthInPX;
    int scrHeightInPX;
    AndroidRestClientApiMethods restApiClientMethods;

    static HashMap<Integer,Marker> nearbyVehiclesMarkers = new HashMap<Integer, Marker>();
    Dialog nearbyVehiclesDialog;
    Dialog requestedVehicleDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_hyrese);

        restApiClientMethods = new AndroidRestClientApiMethods(MenuHyreseActivity.this);

        centerPosTV = (TextView) findViewById(R.id.center_position_tv);
        overMapLayer = (RelativeLayout)findViewById(R.id.overMapLayer);
        kerkoTaxoBTN = (Button) findViewById(R.id.kerko_taxi_btn);
        takeMeHereContainer = (RelativeLayout) findViewById(R.id.take_me_here_container);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        scrWidthInPX = metrics.widthPixels;
        scrHeightInPX = metrics.heightPixels;



        geocoder = new Geocoder(this, Locale.getDefault());


        if (!isGooglePlayServicesAvailable()) {
            finish();                                                                               // ketu mund ti nxjerrim nje warning qe duhet te update ose instaloje google play services
        }


        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = supportMapFragment.getMap();
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
        map.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        client_live_location = locationManager.getLastKnownLocation(bestProvider);
        if (client_live_location != null) {
            onLocationChanged(client_live_location);
            LatLng currentLocation = new LatLng(client_live_location.getLatitude(),client_live_location.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15.0F));
        }

        locationManager.requestLocationUpdates(bestProvider, 10000, 0, this);

        setListenersToUIElements();


        showArrivingTaxiDialog();
    //  rest_update_client_location(client_live_location, "1");
    //  get_name_for_location(new LatLng(41.325935, 19.818081));
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

        //rest_update_client_location(client_live_location, "1");
        Log.d("Location changed","yes");
    }

    private void rest_update_client_location(Location location, final String client_id) {
        final String client_live_latitude = String.valueOf(location.getLatitude()).toString().trim();
        final String client_live_longitude = String.valueOf(location.getLongitude()).toString().trim();

        StringRequest postRequest = new StringRequest(Request.Method.POST, Configurations.UPDATE_KLIENT_LOCATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int error_code = jsonResponse.getInt("error_code");
                            String error_code_desc = jsonResponse.getString("error_code_desc");
                            if (error_code == 0) {
                                Log.d("qqq", "koordinatat u derguan me sukses");
                            } else {
                                Log.d("qqq", error_code_desc);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("client_id", client_id);
                params.put("client_lat", client_live_latitude);
                params.put("client_lng", client_live_longitude);

                Log.d("qqq_liveLocation_params", client_live_latitude);
                Log.d("qqq_liveLocation_params", client_live_longitude);

                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);
    }

    private void rest_cancel_request(final int booking_id, final int vehicle_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configurations.CANCEL_REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject responseJSONObject;
                        try {
                            responseJSONObject = new JSONObject(response);

                            int error_code = responseJSONObject.getInt("error_code");
                            String error_code_desc = responseJSONObject.getString("error_code_desc");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("qqq", "ErrorResponseOnLocationChanged: " + error.getMessage());
                if (error instanceof NoConnectionError) {
                    Log.d("qqq", "NoConnectionError: " + error.getMessage());
                } else if (error instanceof TimeoutError) {
                } else if (error instanceof AuthFailureError) {
                    Log.d("qqq", "AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    Log.d("qqq", "ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    Log.d("qqq", "NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    Log.d("qqq", "ParseError: " + error.getMessage());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                //params.put("getNearbyTaxis", getNearbyTaxis);
                params.put("booking_id", ""+booking_id);
                params.put("vehicle_id", ""+vehicle_id);

                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private String get_name_for_location(LatLng location) {
        String addressName = "";
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);

            if(addresses.size() > 0){
                for(int i = 0; i<=addresses.get(0).getMaxAddressLineIndex();i++){
                    addressName += addresses.get(0).getAddressLine(i)+", ";
                }
                addressName = addressName.substring(0,addressName.length()-2);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressName;
    }

    private void setListenersToUIElements(){
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                centerPosTV.setText(get_name_for_location(cameraPosition.target));
            }
        });
        overMapLayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
                    centerPosTV.setText(getResources().getString(R.string.searching_place_text));
                }
                return false;
            }
        });

        kerkoTaxoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kerkoTaxoBTN.setVisibility(View.INVISIBLE);
                takeMeHereContainer.setVisibility(View.INVISIBLE);
                Marker requestedPositionMarker = map.addMarker(new MarkerOptions().position(map.getCameraPosition().target));
                requestedPositionMarker.setTitle("Lorem");
                requestedPositionMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.client_pin));
                restApiClientMethods.getNearbyVehicles(map.getCameraPosition().target, "1");

            }
        });
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

    private void highlightDotWithId(HashMap<Integer, TextView> dots, int id){
        for(Map.Entry<Integer, TextView> entry : dots.entrySet()) {
            Integer key = entry.getKey();
            TextView textView = entry.getValue();
            textView.setTextColor(getResources().getColor(R.color.viewpager_pasive_dot));
        }
        dots.get(id).setTextColor(getResources().getColor(R.color.viewpager_active_dot));
    }

    private void highlightMarkerWithId(HashMap<Integer,Marker> markers, int id){
        for(Map.Entry<Integer, Marker> entry : markers.entrySet()) {
            Integer key = entry.getKey();
            Marker marker = entry.getValue();
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_pin));
        }
        markers.get(id).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.highlighted_taxi_pin));
        markers.get(id).showInfoWindow();
    }


    public void showNearByVehiclesAction(final List<NearbyVehicle> nearbyVehicles, final  LatLng requestedLocation){
        LatLngBounds.Builder nearbyVehiclesBoundsBuilder = new LatLngBounds.Builder();
        nearbyVehiclesMarkers.clear();


        if(nearbyVehicles.size() > 0){
            for(int i = 0; i< nearbyVehicles.size();i++){
                LatLng nearbyVehicleLatLng = new LatLng(nearbyVehicles.get(i).getLat(),nearbyVehicles.get(i).getLng());
                Marker nearbyVehicleMarker = map.addMarker( new MarkerOptions().position(nearbyVehicleLatLng));
                nearbyVehicleMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_pin));
                nearbyVehicleMarker.setTitle("Lorem ipsum");
                nearbyVehiclesMarkers.put(nearbyVehicles.get(i).getId(), nearbyVehicleMarker);
                nearbyVehiclesBoundsBuilder.include(nearbyVehicleLatLng);
            }
            nearbyVehiclesBoundsBuilder.include(requestedLocation);

            map.setPadding(0,0,0,scrHeightInPX/2);
            int padding = scrWidthInPX/10; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(nearbyVehiclesBoundsBuilder.build(), padding);
            map.animateCamera(cu);

            nearbyVehiclesDialog = new Dialog(MenuHyreseActivity.this, R.style.UpAndDownDialogSlideAnim);
            nearbyVehiclesDialog.setContentView(R.layout.dialog_nearby_vehicles);
            nearbyVehiclesDialog.getWindow().getAttributes().height = scrHeightInPX/2;
            nearbyVehiclesDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
            nearbyVehiclesDialog.getWindow().getAttributes().flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            nearbyVehiclesDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    map.setPadding(0,0,0,0);
                    map.clear();
                    map.animateCamera(CameraUpdateFactory.newLatLng(requestedLocation));
                    kerkoTaxoBTN.setVisibility(View.VISIBLE);
                    takeMeHereContainer.setVisibility(View.VISIBLE);
                }
            });

            nearbyVehiclesDialog.show();
            //------------------------------------

            NearbyVehiclesViewPagerAdapter nearbyVehiclesViewPagerAdapter = new NearbyVehiclesViewPagerAdapter(nearbyVehicles, MenuHyreseActivity.this, requestedLocation);
            ViewPager nearbyVehiclesViewpager = (ViewPager) nearbyVehiclesDialog.findViewById(R.id.view_pager);
            LinearLayout dotsLayout = (LinearLayout) nearbyVehiclesDialog.findViewById(R.id.dots_layout);
            final HashMap<Integer, TextView> dots = new HashMap<Integer, TextView>();
            for(int i =0; i<nearbyVehicles.size();i++){
                TextView dot = new TextView(MenuHyreseActivity.this);
                dot.setText(Html.fromHtml("&#8226;"));
                dot.setTextSize(35);
                dotsLayout.addView(dot);
                dots.put(nearbyVehicles.get(i).getId(), dot);
            }
                highlightDotWithId(dots, nearbyVehicles.get(0).getId());
                highlightMarkerWithId(nearbyVehiclesMarkers, nearbyVehicles.get(0).getId());

            nearbyVehiclesViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
                @Override
                public void onPageSelected(int position) {
                    nearbyVehiclesMarkers.get(nearbyVehicles.get(position).getId()).showInfoWindow();
                    highlightDotWithId(dots, nearbyVehicles.get(position).getId());
                    highlightMarkerWithId(nearbyVehiclesMarkers, nearbyVehicles.get(position).getId());
                }
                @Override
                public void onPageScrollStateChanged(int state) {}
            });
            nearbyVehiclesViewpager.setAdapter(nearbyVehiclesViewPagerAdapter);

        }else{
            kerkoTaxoBTN.setVisibility(View.VISIBLE);
            takeMeHereContainer.setVisibility(View.VISIBLE);
            map.clear();
            Toast.makeText(MenuHyreseActivity.this, "Nuk u gjet asnjë makine e disponueshme për momentin", Toast.LENGTH_LONG).show();
        }
    }

    public void requestTaxiAction(int vehicle_id, int error_code){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                kerkoTaxoBTN.setVisibility(View.INVISIBLE);
                takeMeHereContainer.setVisibility(View.INVISIBLE);
            }
        },500);
        nearbyVehiclesDialog.cancel();
        requestedVehicleDialog = new Dialog(MenuHyreseActivity.this, R.style.RequestTaxiDialogAnim);
        requestedVehicleDialog.setContentView(R.layout.dialog_requested_vehicle);
        requestedVehicleDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestedVehicleDialog.setCancelable(false);
        requestedVehicleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                map.setPadding(0,0,0,0);
                map.clear();
            }
        });

        requestedVehicleDialog.show();

        ImageView rotating_car = (ImageView) requestedVehicleDialog.findViewById(R.id.rotating_car);
        Animation infinite_rotate = AnimationUtils.loadAnimation(MenuHyreseActivity.this, R.anim.full_rotation);
        infinite_rotate.setRepeatMode(Animation.RESTART);
        infinite_rotate.setRepeatCount(Animation.INFINITE);
        rotating_car.startAnimation(infinite_rotate);

        Toast.makeText(this, "error code response: ", Toast.LENGTH_SHORT).show();
        nearbyVehiclesMarkers.get(vehicle_id).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.client_pin));
    }


    public void showRequestStatusDialog(){
        Dialog requestStatusDialog = new Dialog(MenuHyreseActivity.this, R.style.UpAndDownDialogSlideAnim);
        requestStatusDialog.setContentView(R.layout.dialog_request_status);
        requestStatusDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestStatusDialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });

        ImageView statusImage = (ImageView) requestStatusDialog.findViewById(R.id.status_image);
        statusImage.setImageResource(R.drawable.accepted);
        requestStatusDialog.show();
        requestStatusDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }


    public void showArrivingTaxiDialog(){
        Dialog arrivingTaxiDialog = new Dialog(MenuHyreseActivity.this, R.style.UpAndDownDialogSlideAnim);
        arrivingTaxiDialog.setContentView(R.layout.dialog_taxi_arriving);
        arrivingTaxiDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        arrivingTaxiDialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });

        arrivingTaxiDialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        arrivingTaxiDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        arrivingTaxiDialog.getWindow().getAttributes().flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;



        arrivingTaxiDialog.show();

    }
}