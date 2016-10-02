package com.jeddigital.kerkotaxi;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jeddigital.kerkotaxi.Adapters.NearbyVehiclesViewPagerAdapter;
import com.jeddigital.kerkotaxi.AndroidRestClientApi.AndroidRestClientApiMethods;
import com.jeddigital.kerkotaxi.AndroidRestClientApi.Configurations;
import com.jeddigital.kerkotaxi.AnroidRestModels.BookingLatLngPosition;
import com.jeddigital.kerkotaxi.AnroidRestModels.CheckRequestResponse;
import com.jeddigital.kerkotaxi.AnroidRestModels.NearbyVehicle;
import com.jeddigital.kerkotaxi.IOTools.InternalStorageTools;
import com.jeddigital.kerkotaxi.IOTools.StorageConfigurations;
import com.jeddigital.kerkotaxi.Services.CheckRequestService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MenuHyreseActivity extends FragmentActivity implements LocationListener, TouchableWrapper.UpdateMapAfterUserInteraction {

    private Runnable checkRequestInterval = new Runnable(){
        public void run(){
            restApiClientMethods.checkRequestStatus(clientId);

            handler.postDelayed(checkRequestInterval, Configurations.CHECK_REQUEST_STATUS_HOMEPAGE_SERVICE_INTERVAL);
        }
    };

    public static final String clientId = "1";

    public static boolean activeActivity = false;
    Handler handler;
    private GoogleMap map;
    Location client_live_location;
    Geocoder geocoder;
    TextView centerPosTV;
    RelativeLayout overMapLayer;
    RelativeLayout takeMeHereContainer;

    Float cameraDefaultZoom = 15.0F;
    Button kerkoTaxiBTN;

    Marker requestedPositionMarker;
    Marker requestedVehicleMarker = null;
    Marker finishMarker = null;
    Marker startMarker = null;
    Marker clientInMarker = null;
    Polyline clientInPolyline = null;

    DisplayMetrics metrics;
    int scrWidthInPX;
    int scrHeightInPX;
    AndroidRestClientApiMethods restApiClientMethods;

    static HashMap<Integer,Marker> nearbyVehiclesMarkers = new HashMap<Integer, Marker>();
    Dialog requestedVehicleDialog;
    Dialog requestStatusDialog;
    Dialog taxiArrivedDialog;
    RelativeLayout activeBookingDialog;
    LinearLayout nearbyVehiclesDialog;

    ImageView nearbyVehiclesDialogXIcon;

    TextView activeBookingDialog_ArivalTimeTV;
    RelativeLayout activeBookingDialog_CancelBooking;
    SharedPreferences userLoggedInPreferences;
    SharedPreferences.Editor userLoggedInPreferencesEditor;

    int mapPadding; // offset from edges of the map in pixels
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_hyrese);

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
        activeBookingDialog_CancelBooking = (RelativeLayout) activeBookingDialog.findViewById(R.id.cancel_booking_RL);
        requestedVehicleDialog = new Dialog(MenuHyreseActivity.this, R.style.RequestTaxiDialogAnim);
        requestStatusDialog = new Dialog(MenuHyreseActivity.this, R.style.UpAndDownDialogSlideAnim);
        taxiArrivedDialog = new Dialog(MenuHyreseActivity.this, R.style.UpAndDownDialogSlideAnim);

        handler = new Handler();
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        scrWidthInPX = metrics.widthPixels;
        scrHeightInPX = metrics.heightPixels;

        mapPadding = scrWidthInPX/10;

        geocoder = new Geocoder(this, Locale.getDefault());

        if(!isGooglePlayServicesAvailable()) {
            finish();                                                                               // ketu mund ti nxjerrim nje warning qe duhet te update ose instaloje google play services
        }


        MySupportMapFragment supportMapFragment =
                (MySupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, cameraDefaultZoom));
        }

        locationManager.requestLocationUpdates(bestProvider, 10000, 0, this);

        setListenersToUIElements();

    //  showArrivingTaxiDialog();
    //  showRequestStatusDialog();
    //   .updateClientLocation(client_live_location, clientId);
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

        //rest_update_client_location(client_live_location, clientId);
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
       //             centerPosTV.setText(getResources().getString(R.string.searching_place_text));
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
                restApiClientMethods.getNearbyVehicles(map.getCameraPosition().target, clientId);
            }
        });
        activeBookingDialog_CancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restApiClientMethods.cancelRequest(clientId, client_live_location);
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

    public void showNearByVehiclesAction(final List<NearbyVehicle> nearbyVehiclesat, final  LatLng requestedLocation){
        LatLngBounds.Builder nearbyVehiclesBoundsBuilder = new LatLngBounds.Builder();
        nearbyVehiclesMarkers.clear();
        final List<NearbyVehicle> nearbyVehicles = nearbyVehiclesat;


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

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(nearbyVehiclesBoundsBuilder.build(), mapPadding);
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

            nearbyVehiclesViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
                @Override
                public void onPageSelected(int position) {
                    NearbyVehicle positionvehicle = nearbyVehicles.get(position);
                    int id = positionvehicle.getId();
                    nearbyVehiclesMarkers.get(id).showInfoWindow();
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
                restApiClientMethods.cancelRequest(clientId, client_live_location);
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

    private void drawClientInRoute(CheckRequestResponse requestResponse, LatLngBounds.Builder routeBoundsBuilder){

        PolylineOptions clientInPolyLineOptions = new PolylineOptions().width(5).color(Color.GREEN);
        List<BookingLatLngPosition> bookingLatLngPositions = requestResponse.getBooking_vehicle_route();
        for(BookingLatLngPosition bookingLatLngPosition : bookingLatLngPositions){
            if(bookingLatLngPosition.getLat_lng_booking_status() == 4){
                clientInPolyLineOptions.add(new LatLng(bookingLatLngPosition.getLat(), bookingLatLngPosition.getLng()));
                routeBoundsBuilder.include(new LatLng(bookingLatLngPosition.getLat(), bookingLatLngPosition.getLng()));
            }
        }

        if(clientInPolyline != null){
            clientInPolyline.remove();
        }
        clientInPolyline = map.addPolyline(clientInPolyLineOptions);

    }

    private void positionFinishMarker(CheckRequestResponse requestResponse){
        if(finishMarker == null){
            finishMarker = map.addMarker(new MarkerOptions().position(new LatLng(requestResponse.getClient_out_vehicle_lat(), requestResponse.getClient_out_vehicle_lng())));
            finishMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.finish_marker));
            finishMarker.setTitle("FINISH");
            finishMarker.showInfoWindow();
        }
        finishMarker.setPosition(new LatLng(requestResponse.getClient_out_vehicle_lat(), requestResponse.getClient_out_vehicle_lng()));

    }

    private void positionStartMarker(CheckRequestResponse requestResponse){
        if(startMarker == null){
            startMarker = map.addMarker(new MarkerOptions().position(new LatLng(requestResponse.getClient_in_vehicle_lat(), requestResponse.getClient_in_vehicle_lng())));
            startMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.start_marker));
            startMarker.setTitle("START");
            startMarker.showInfoWindow();
        }
        startMarker.setPosition(new LatLng(requestResponse.getClient_in_vehicle_lat(), requestResponse.getClient_in_vehicle_lng()));

    }

    private void positionClientInMarker(CheckRequestResponse requestResponse){
        if(clientInMarker == null){
            clientInMarker = map.addMarker(new MarkerOptions().position(new LatLng(requestResponse.getNearbyVehicle().getLat(), requestResponse.getNearbyVehicle().getLng())));
            clientInMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.client_in));
            clientInMarker.setTitle("Në TAXI");
            clientInMarker.showInfoWindow();
        }
        clientInMarker.setPosition(new LatLng(requestResponse.getNearbyVehicle().getLat(), requestResponse.getNearbyVehicle().getLng()));

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
        Log.e("dev", "handleCheckRequestAction 'homeActivity' bookingStatusId = "+booking_status_id);

        if(booking_status_id == 1){//pending
            kerkoTaxiBTN.setVisibility(View.INVISIBLE);
            takeMeHereContainer.setVisibility(View.INVISIBLE);

            positionRequestedVehicleMarker(requestResponse);
            setRequestedPositionMarker(requestResponse);

            routeBoundsBuilder.include(requestedPositionMarker.getPosition());
            routeBoundsBuilder.include(requestedVehicleMarker.getPosition());
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(routeBoundsBuilder.build(), mapPadding));

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
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(routeBoundsBuilder.build(), mapPadding));

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
            kerkoTaxiBTN.setVisibility(View.INVISIBLE);
            takeMeHereContainer.setVisibility(View.INVISIBLE);
            activeBookingDialog.setVisibility(View.GONE);
            positionRequestedVehicleMarker(requestResponse);
            setRequestedPositionMarker(requestResponse);

            routeBoundsBuilder.include(requestedPositionMarker.getPosition());
            routeBoundsBuilder.include(requestedVehicleMarker.getPosition());
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(routeBoundsBuilder.build(), mapPadding));

            notifyUserForTaxiArrival();

            Toast.makeText(MenuHyreseActivity.this, "Taksija ka mberitur!!", Toast.LENGTH_SHORT).show();
        }else if(booking_status_id == 4){//me klient
            kerkoTaxiBTN.setVisibility(View.INVISIBLE);
            takeMeHereContainer.setVisibility(View.INVISIBLE);
            activeBookingDialog.setVisibility(View.GONE);
            taxiArrivedDialog.cancel();
            removeRequestedVehicleMarker();
            removeRequestedPostionMarker();
            positionClientInMarker(requestResponse);
            positionStartMarker(requestResponse);
            drawClientInRoute(requestResponse, routeBoundsBuilder);

            routeBoundsBuilder.include(startMarker.getPosition());
            routeBoundsBuilder.include(clientInMarker.getPosition());
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(routeBoundsBuilder.build(), mapPadding));
            Toast.makeText(MenuHyreseActivity.this, "Ne taxi!!", Toast.LENGTH_SHORT).show();
        }else if(booking_status_id == 5){//kompletuar
            if(userLoggedInPreferences.getInt(StorageConfigurations.LAST_BOOKING_STATUS_ID_KNOWN, -1) != booking_status_id){//useri nuk eshte notifikuar per kete status
                removeRequestedVehicleMarker();
                removeRequestedPostionMarker();
                removeClientInMarker();
                drawClientInRoute(requestResponse, routeBoundsBuilder);

                positionStartMarker(requestResponse);
                positionFinishMarker(requestResponse);
                routeBoundsBuilder.include(startMarker.getPosition());
                routeBoundsBuilder.include(finishMarker.getPosition());
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(routeBoundsBuilder.build(), mapPadding));
            }

            handler.removeCallbacks(checkRequestInterval);
        }else if(booking_status_id == 6){//Refuzuar Nga Shoferi Ne Pending
            requestedVehicleDialog.cancel();
            removeRequestedPostionMarker();
            removeRequestedVehicleMarker();
            if(userLoggedInPreferences.getInt(StorageConfigurations.LAST_BOOKING_STATUS_ID_KNOWN, -1) != booking_status_id){//useri nuk eshte notifikuar per kete status
                notifyUserForChangedRequestStatus(booking_status_id);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(client_live_location.getLatitude(), client_live_location.getLongitude()),cameraDefaultZoom));
                Toast.makeText(MenuHyreseActivity.this, "Porosia juaj u anullua nga shoferi", Toast.LENGTH_SHORT).show();
            }
            handler.removeCallbacks(checkRequestInterval);
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
        }else if(booking_status_id == 13){//Anulluar nga sistemi per mospranim nga shoferi
            handler.removeCallbacks(checkRequestInterval);
        }else{//no booking status id -> there is no booking for this client
            handler.removeCallbacks(checkRequestInterval);
            return;
        }


        userLoggedInPreferencesEditor.putInt(StorageConfigurations.LAST_BOOKING_STATUS_ID_KNOWN, booking_status_id);
        userLoggedInPreferencesEditor.commit();
    }

    public void cancelRequestAction(int error_code){
        requestedVehicleDialog.cancel();
        removeRequestedPostionMarker();
        removeRequestedVehicleMarker();
        removeClientInMarker();
        activeBookingDialog.setVisibility(View.GONE);
        kerkoTaxiBTN.setVisibility(View.VISIBLE);
        takeMeHereContainer.setVisibility(View.VISIBLE);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(client_live_location.getLatitude(), client_live_location.getLongitude()),cameraDefaultZoom));
        Toast.makeText(MenuHyreseActivity.this, "Porosia u anullua me sukses.", Toast.LENGTH_SHORT).show();
    }

    private void removeRequestedPostionMarker(){
        if(requestedPositionMarker != null){
            requestedPositionMarker.remove();
            requestedPositionMarker = null;
        }
    }
    private void removeRequestedVehicleMarker(){
        if(requestedVehicleMarker != null){
            requestedVehicleMarker.remove();
            requestedVehicleMarker = null;
        }
    }
    private void removeClientInMarker(){
        if(clientInMarker != null){
            clientInMarker.remove();
            clientInMarker = null;
        }
    }
    private void removeStartMarker(){
        if(startMarker != null){
            startMarker.remove();
            startMarker = null;
        }
    }
    private void removeFinishMarker(){
        if(finishMarker != null){
            finishMarker.remove();
            finishMarker = null;
        }
    }
    private void removeClientInRoute(){
        if(clientInPolyline != null){
            clientInPolyline.remove();
            clientInPolyline = null;
        }
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
        TextView statusTittle = (TextView) requestStatusDialog.findViewById(R.id.status_tittle);
        TextView info = (TextView) requestStatusDialog.findViewById(R.id.requested_dialog_info);
        ImageView statusImage = (ImageView) requestStatusDialog.findViewById(R.id.status_image);
        if(booking_status_id == 2){
            statusImage.setImageResource(R.drawable.accepted);
            statusTittle.setText(getResources().getString(R.string.requested_status_dialog_accepted_tittle));
        }else{
            info.setVisibility(View.GONE);
            statusImage.setImageResource(R.drawable.declined);
            statusTittle.setText(getResources().getString(R.string.requested_status_dialog_declined_tittle));
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

    public void notifyUserForTaxiArrival(){
        if(!taxiArrivedDialog.isShowing()){
            taxiArrivedDialog = new Dialog(MenuHyreseActivity.this, R.style.UpAndDownDialogSlideAnim);
            taxiArrivedDialog.setContentView(R.layout.dialog_taxi_arrived);
            taxiArrivedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            taxiArrivedDialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });

            taxiArrivedDialog.show();
            taxiArrivedDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activeActivity = true;
        restApiClientMethods = new AndroidRestClientApiMethods(MenuHyreseActivity.this);
        handler.post(checkRequestInterval);
        Log.e("devLog", "onresume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        activeActivity = false;
        handler.removeCallbacks(checkRequestInterval);
        Intent serviceIntent = new Intent(this, CheckRequestService.class);
        startService(serviceIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onUpdateMapAfterUserInteraction() {
        centerPosTV.setText(get_name_for_location(map.getCameraPosition().target));
    }

    @Override
    public void onUpdateMapOnUserInteraction() {
        centerPosTV.setText(getResources().getString(R.string.searching_place_text));
    }
}