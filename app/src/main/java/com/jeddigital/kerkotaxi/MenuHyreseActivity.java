package com.jeddigital.kerkotaxi;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jeddigital.kerkotaxi.Adapters.NearbyVehiclesViewPagerAdapter;
import com.jeddigital.kerkotaxi.AndroidRestClientApi.AndroidRestClientApiMethods;
import com.jeddigital.kerkotaxi.AnroidRestModels.CheckRequestResponse;
import com.jeddigital.kerkotaxi.AnroidRestModels.NearbyVehicle;
import com.jeddigital.kerkotaxi.IOTools.InternalStorageTools;
import com.jeddigital.kerkotaxi.IOTools.StorageConfigurations;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MenuHyreseActivity extends FragmentActivity implements LocationListener {

    private Runnable checkRequestInterval = new Runnable(){
        public void run(){
            restApiClientMethods.checkRequestStatus("1");

            handler.postDelayed(checkRequestInterval, 3000);
        }
    };

    Handler handler;
    private GoogleMap map;
    Location client_live_location;
    Geocoder geocoder;
    TextView centerPosTV;
    RelativeLayout overMapLayer;
    RelativeLayout takeMeHereContainer;

    Button kerkoTaxiBTN;

    Marker requestedPositionMarker;
    Marker requestedVehicleMarker = null;

    DisplayMetrics metrics;
    int scrWidthInPX;
    int scrHeightInPX;
    AndroidRestClientApiMethods restApiClientMethods;

    static HashMap<Integer,Marker> nearbyVehiclesMarkers = new HashMap<Integer, Marker>();
    Dialog requestedVehicleDialog;
    Dialog requestStatusDialog;
    RelativeLayout activeBookingDialog;
    LinearLayout nearbyVehiclesDialog;

    ImageView nearbyVehiclesDialogXIcon;

    TextView activeBookingDialog_ArivalTimeTV;
    SharedPreferences userLoggedInPreferences;
    SharedPreferences.Editor userLoggedInPreferencesEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_hyrese);

        restApiClientMethods = new AndroidRestClientApiMethods(MenuHyreseActivity.this);
        userLoggedInPreferences = getSharedPreferences(StorageConfigurations.USER_LOGGED_IN_SHARED_PREFS_KEY, MODE_PRIVATE);
        userLoggedInPreferencesEditor = userLoggedInPreferences.edit();

        centerPosTV = (TextView) findViewById(R.id.center_position_tv);
        overMapLayer = (RelativeLayout)findViewById(R.id.overMapLayer);
        kerkoTaxiBTN = (Button) findViewById(R.id.kerko_taxi_btn);
        takeMeHereContainer = (RelativeLayout) findViewById(R.id.take_me_here_container);

        activeBookingDialog = (RelativeLayout) findViewById(R.id.dialog_taxi_arriving);
        nearbyVehiclesDialog = (LinearLayout) findViewById(R.id.dialog_nearby_vehicles);
        nearbyVehiclesDialogXIcon = (ImageView) findViewById(R.id.dialog_nearby_vehicles_x_icon);

        activeBookingDialog_ArivalTimeTV = (TextView) activeBookingDialog.findViewById(R.id.arrival_time);

        requestedVehicleDialog = new Dialog(MenuHyreseActivity.this, R.style.RequestTaxiDialogAnim);
        requestStatusDialog = new Dialog(MenuHyreseActivity.this, R.style.UpAndDownDialogSlideAnim);

        handler = new Handler();
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        scrWidthInPX = metrics.widthPixels;
        scrHeightInPX = metrics.heightPixels;

        geocoder = new Geocoder(this, Locale.getDefault());

        if(!isGooglePlayServicesAvailable()) {
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

        handler.post(checkRequestInterval);
    //  showArrivingTaxiDialog();
    //  showRequestStatusDialog();
    //  restApiClientMethods.updateClientLocation(client_live_location, "1");
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
   /*     map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                centerPosTV.setText(get_name_for_location(cameraPosition.target));
            }
        });*/
        overMapLayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
                    centerPosTV.setText(getResources().getString(R.string.searching_place_text));
                }
                return false;
            }
        });

        kerkoTaxiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kerkoTaxiBTN.setVisibility(View.INVISIBLE);
                takeMeHereContainer.setVisibility(View.INVISIBLE);
                requestedPositionMarker = map.addMarker(new MarkerOptions().position(map.getCameraPosition().target));
                requestedPositionMarker.setTitle("Requested location");
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
            nearbyVehiclesDialog.setVisibility(View.VISIBLE);
            nearbyVehiclesDialogXIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    map.clear();
                    map.animateCamera(CameraUpdateFactory.newLatLng(requestedLocation));
                    kerkoTaxiBTN.setVisibility(View.VISIBLE);
                    takeMeHereContainer.setVisibility(View.VISIBLE);
                    nearbyVehiclesDialog.setVisibility(View.GONE);
                }
            });
            for(int i = 0; i< nearbyVehicles.size();i++){
                LatLng nearbyVehicleLatLng = new LatLng(nearbyVehicles.get(i).getLat(),nearbyVehicles.get(i).getLng());
                Marker nearbyVehicleMarker = map.addMarker( new MarkerOptions().position(nearbyVehicleLatLng));
                nearbyVehicleMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_pin));
                nearbyVehicleMarker.setTitle("Lorem ipsum");
                nearbyVehiclesMarkers.put(nearbyVehicles.get(i).getId(), nearbyVehicleMarker);
                nearbyVehiclesBoundsBuilder.include(nearbyVehicleLatLng);
            }
            nearbyVehiclesBoundsBuilder.include(requestedLocation);

            int padding = scrWidthInPX/10; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(nearbyVehiclesBoundsBuilder.build(), padding);
            map.animateCamera(cu);

            //------------------------------------

            NearbyVehiclesViewPagerAdapter nearbyVehiclesViewPagerAdapter = new NearbyVehiclesViewPagerAdapter(nearbyVehicles, MenuHyreseActivity.this, requestedLocation, new LatLng(client_live_location.getLatitude(), client_live_location.getLongitude()));
            ViewPager nearbyVehiclesViewpager = (ViewPager) nearbyVehiclesDialog.findViewById(R.id.view_pager);
            nearbyVehiclesViewpager.setClipToPadding(false);
            int pagerPadding = (int)(scrWidthInPX * 0.1);
            int pagerMargin = (int)(scrWidthInPX * 0.1);
            nearbyVehiclesViewpager.setPadding(pagerPadding, 0, pagerPadding, 0);

            LinearLayout dotsLayout = (LinearLayout) nearbyVehiclesDialog.findViewById(R.id.dots_layout);
            dotsLayout.removeAllViews();
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
            kerkoTaxiBTN.setVisibility(View.VISIBLE);
            takeMeHereContainer.setVisibility(View.VISIBLE);
            map.clear();
            Toast.makeText(MenuHyreseActivity.this, "Nuk u gjet asnjë makine e disponueshme për momentin", Toast.LENGTH_LONG).show();
        }
    }

    public void requestTaxiActionStarted(NearbyVehicle nearbyVehicle){
       handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                kerkoTaxiBTN.setVisibility(View.INVISIBLE);
                takeMeHereContainer.setVisibility(View.INVISIBLE);
            }
        },500);
        nearbyVehiclesDialog.setVisibility(View.GONE);
        requestedVehicleDialog.setContentView(R.layout.dialog_requested_vehicle);
        requestedVehicleDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestedVehicleDialog.setCancelable(false);
        requestedVehicleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                for (Map.Entry<Integer,Marker> entry : nearbyVehiclesMarkers.entrySet()) {
                    Marker nearbyVehicleMarker = entry.getValue();
                    nearbyVehicleMarker.remove();
                }
            }
        });

        ImageView rotating_car = (ImageView) requestedVehicleDialog.findViewById(R.id.rotating_car);
        Animation infinite_rotate = AnimationUtils.loadAnimation(MenuHyreseActivity.this, R.anim.full_rotation);
        infinite_rotate.setRepeatMode(Animation.RESTART);
        infinite_rotate.setRepeatCount(Animation.INFINITE);
        rotating_car.startAnimation(infinite_rotate);

        ImageView driverPic = (ImageView) requestedVehicleDialog.findViewById(R.id.driver_pic);
        TextView  driverName = (TextView) requestedVehicleDialog.findViewById(R.id.driver_name);
        Button declineBookingBtn = (Button) requestedVehicleDialog.findViewById(R.id.decline_booking);
        declineBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restApiClientMethods.cancelRequest("1", client_live_location);
                kerkoTaxiBTN.setVisibility(View.VISIBLE);
                takeMeHereContainer.setVisibility(View.VISIBLE);
            }
        });

        InternalStorageTools.getAndShowPhoto(MenuHyreseActivity.this, driverPic, nearbyVehicle.getDriver().getPhoto_url());
        driverName.setText(nearbyVehicle.getDriver().getFirst_name() + " " + nearbyVehicle.getDriver().getLast_name());
        requestedVehicleDialog.show();

    }

    public void requestTaxiActionResponse(int vehicle_id, int error_code){
        if(error_code == 0){
            handler.post(checkRequestInterval);

        }else{//
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestedVehicleDialog.dismiss();
                    Toast.makeText(MenuHyreseActivity.this, "Dicka shkoi keq, porosia juaj nuk u krijua", Toast.LENGTH_SHORT).show();
                }
            },1000);
        }
        Toast.makeText(this, "error code response: "+error_code, Toast.LENGTH_SHORT).show();
    }

    private void positionRequestedVehicleMarker(CheckRequestResponse requestResponse){
        if(requestedVehicleMarker == null){
            requestedVehicleMarker = map.addMarker(new MarkerOptions().position(new LatLng(requestResponse.getNearbyVehicle().getLat(), requestResponse.getNearbyVehicle().getLng())));
            requestedVehicleMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.highlighted_taxi_pin));
            requestedVehicleMarker.setTitle("TAXI-ja në ardhje");
            requestedVehicleMarker.showInfoWindow();
        }
        requestedVehicleMarker.setPosition(new LatLng(requestResponse.getNearbyVehicle().getLat(), requestResponse.getNearbyVehicle().getLng()));
    }

    private void setRequestedPositionMarker(CheckRequestResponse requestResponse){
        if(requestedPositionMarker == null){
            requestedPositionMarker = map.addMarker(new MarkerOptions().position(new LatLng(requestResponse.getClient_requested_lat(), requestResponse.getClient_requested_lng())));
            requestedPositionMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.client_pin));
            requestedPositionMarker.setTitle("pozicioni i kerkuar");
            requestedPositionMarker.showInfoWindow();
        }
        requestedPositionMarker.setPosition(new LatLng(requestResponse.getClient_requested_lat(), requestResponse.getClient_requested_client_lng()));

    }

    public void handleCheckRequestAction(CheckRequestResponse requestResponse){
        int booking_status_id = requestResponse.getStatus_id();
        LatLngBounds.Builder routeBoundsBuilder = new LatLngBounds.Builder();

        if(booking_status_id == 1){//pending
            kerkoTaxiBTN.setVisibility(View.INVISIBLE);
            takeMeHereContainer.setVisibility(View.INVISIBLE);

            positionRequestedVehicleMarker(requestResponse);
            setRequestedPositionMarker(requestResponse);

            routeBoundsBuilder.include(requestedPositionMarker.getPosition());
            routeBoundsBuilder.include(requestedVehicleMarker.getPosition());

            if(!requestedVehicleDialog.isShowing()){
                requestTaxiActionStarted(requestResponse.getNearbyVehicle());
            }

        }else if(booking_status_id == 2){//duke mare klientin
            kerkoTaxiBTN.setVisibility(View.INVISIBLE);
            takeMeHereContainer.setVisibility(View.INVISIBLE);

            requestedVehicleDialog.cancel();
            positionRequestedVehicleMarker(requestResponse);
            setRequestedPositionMarker(requestResponse);

            routeBoundsBuilder.include(requestedPositionMarker.getPosition());
            routeBoundsBuilder.include(requestedVehicleMarker.getPosition());

            if(userLoggedInPreferences.getInt(StorageConfigurations.LAST_BOOKING_STATUS_ID_KNOWN, -1) != booking_status_id){//useri nuk eshte notifikuar per kete status
                notifyUserForChangedRequestStatus(booking_status_id);
            }else{
                requestStatusDialog.cancel();
                if(activeBookingDialog.getVisibility() != View.VISIBLE){//bookings dialogTable hasn't been shown, so we make first initialisations
                    activeBookingDialog.setVisibility(View.VISIBLE);
                }
                activeBookingDialog_ArivalTimeTV.setText(requestResponse.getNearbyVehicle().getDistance_params().getTime_readable());
            }
        }else if(booking_status_id == 3){//duke pritur klientin
            positionRequestedVehicleMarker(requestResponse);
            setRequestedPositionMarker(requestResponse);

            routeBoundsBuilder.include(requestedPositionMarker.getPosition());
            routeBoundsBuilder.include(requestedVehicleMarker.getPosition());

            Toast.makeText(MenuHyreseActivity.this, "Taksija ka mberitur!!", Toast.LENGTH_SHORT).show();
        }else if(booking_status_id == 4){//me klient

        }else if(booking_status_id == 5){//kompletuar
            handler.removeCallbacks(checkRequestInterval);
        }else if(booking_status_id == 6){//Refuzuar Nga Shoferi Ne Pending
            requestedVehicleDialog.cancel();
            if(userLoggedInPreferences.getInt(StorageConfigurations.LAST_BOOKING_STATUS_ID_KNOWN, -1) != booking_status_id){//useri nuk eshte notifikuar per kete status
                notifyUserForChangedRequestStatus(booking_status_id);
            }
            handler.removeCallbacks(checkRequestInterval);
            Toast.makeText(MenuHyreseActivity.this, "Porosia juaj u anullua nga shoferi", Toast.LENGTH_SHORT).show();
        }else if(booking_status_id == 7){//Refuzuar Nga Klienti Ne Pending
            handler.removeCallbacks(checkRequestInterval);
        }else if(booking_status_id == 8){//Refuzuar Nga Shoferi Duke Marre Klientin
            if(userLoggedInPreferences.getInt(StorageConfigurations.LAST_BOOKING_STATUS_ID_KNOWN, -1) != booking_status_id){//useri nuk eshte notifikuar per kete status
                notifyUserForChangedRequestStatus(booking_status_id);
            }
            handler.removeCallbacks(checkRequestInterval);
        }else if(booking_status_id == 9){//Refuzuar Nga Klienti Duke Marre Klientin
            handler.removeCallbacks(checkRequestInterval);
        }else if(booking_status_id == 10){//Refuzuar Nga Shoferi Duke Pritur Klientin
            if(userLoggedInPreferences.getInt(StorageConfigurations.LAST_BOOKING_STATUS_ID_KNOWN, -1) != booking_status_id){//useri nuk eshte notifikuar per kete status
                notifyUserForChangedRequestStatus(booking_status_id);
            }
            handler.removeCallbacks(checkRequestInterval);
        }else if(booking_status_id == 11){//Refuzuar Nga Klienti Duke Pritur Klientin
            if(userLoggedInPreferences.getInt(StorageConfigurations.LAST_BOOKING_STATUS_ID_KNOWN, -1) != booking_status_id){//useri nuk eshte notifikuar per kete status
                notifyUserForChangedRequestStatus(booking_status_id);
            }
            handler.removeCallbacks(checkRequestInterval);
        }else if(booking_status_id == 12){//Refuzuar Nga Shoferi Me Klient
            handler.removeCallbacks(checkRequestInterval);
        }else{//no booking status id -> there is no booking for this client
            handler.removeCallbacks(checkRequestInterval);
            return;
        }

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(routeBoundsBuilder.build(), 50));

        userLoggedInPreferencesEditor.putInt(StorageConfigurations.LAST_BOOKING_STATUS_ID_KNOWN, booking_status_id);
        userLoggedInPreferencesEditor.commit();
    }

    public void cancelRequestAction(int error_code){
        requestedVehicleDialog.cancel();
        Toast.makeText(MenuHyreseActivity.this, "Porosia u anullua me sukses.", Toast.LENGTH_SHORT).show();
    }

    public void notifyUserForChangedRequestStatus(final int booking_status_id){

        requestStatusDialog = new Dialog(MenuHyreseActivity.this, R.style.UpAndDownDialogSlideAnim);
        requestStatusDialog.setContentView(R.layout.dialog_request_status);
        requestStatusDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestStatusDialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(booking_status_id == 2){

                }else{
                    kerkoTaxiBTN.setVisibility(View.VISIBLE);
                    takeMeHereContainer.setVisibility(View.VISIBLE);
                }
            }
        });
        ImageView statusImage = (ImageView) requestStatusDialog.findViewById(R.id.status_image);
        if(booking_status_id == 2){
            statusImage.setImageResource(R.drawable.accepted);
        }else{
            statusImage.setImageResource(R.drawable.declined);
        }
        requestStatusDialog.show();
        requestStatusDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                requestStatusDialog.cancel();
            }
        },5000);
    }



    @Override
    protected void onStop() {
        super.onStop();

        handler.removeCallbacks(checkRequestInterval);
    }
}