package com.jeddigital.kerkotaxi.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.LatLng;
import com.jeddigital.kerkotaxi.AndroidRestClientApi.AndroidRestClientApiMethods;
import com.jeddigital.kerkotaxi.AndroidRestClientApi.Configurations;
import com.jeddigital.kerkotaxi.AnroidRestModels.NearbyVehicle;
import com.jeddigital.kerkotaxi.IOTools.InternalStorageTools;
import com.jeddigital.kerkotaxi.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Theodhori on 7/29/2016.
 */
public class NearbyVehiclesViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        List<NearbyVehicle> nearbyVehicles;
        Context context;
        LatLng requestedLocation;
        static AndroidRestClientApiMethods restApiMethods;

        public NearbyVehiclesViewPagerAdapter(List<NearbyVehicle> nearbyVehicles, Context context, LatLng requestedLocation) {
            this.context = context;
            this.nearbyVehicles = nearbyVehicles;
            this.requestedLocation = requestedLocation;
            restApiMethods = new AndroidRestClientApiMethods(context);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.dialog_listview_item_nearby_vehicle, container, false);
            ImageView driverFoto = (ImageView)view.findViewById(R.id.driver_foto);
            TextView emerMbiemer = (TextView) view.findViewById(R.id.name);
            TextView tipiMakines = (TextView) view.findViewById(R.id.car_type);
            TextView arrivalTime = (TextView) view.findViewById(R.id.arrival);
            Button takeMeBTN = (Button) view.findViewById(R.id.take_me_btn);

            takeMeBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restApiMethods.requestTaxi(nearbyVehicles.get(position).getId(),String.valueOf(1),requestedLocation);
                }
            });

            InternalStorageTools.getAndShowPhoto(context, driverFoto, nearbyVehicles.get(position).getDriver().getPhoto_url());
            emerMbiemer.setText(nearbyVehicles.get(position).getDriver().getFirst_name() + " " + nearbyVehicles.get(position).getDriver().getLast_name());
            tipiMakines.setText(nearbyVehicles.get(position).getCar_model());
            arrivalTime.setText(nearbyVehicles.get(position).getDistance_params().getTime_readable());

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return nearbyVehicles.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }

}