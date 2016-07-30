package com.jeddigital.kerkotaxi.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeddigital.kerkotaxi.AnroidRestModels.NearbyVehicle;
import com.jeddigital.kerkotaxi.IOTools.InternalStorageTools;
import com.jeddigital.kerkotaxi.R;

import java.util.List;

/**
 * Created by Theodhori on 7/29/2016.
 */
public class NearbyVehiclesViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        List<NearbyVehicle> nearbyVehicles;
        Context c;

        public NearbyVehiclesViewPagerAdapter(List<NearbyVehicle> nearbyVehicles, Context c) {
            this.c = c;
            this.nearbyVehicles = nearbyVehicles;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.dialog_listview_item_nearby_vehicle, container, false);
            ImageView driverFoto = (ImageView)view.findViewById(R.id.driver_foto);
            TextView emerMbiemer = (TextView) view.findViewById(R.id.name);
            TextView tipiMakines = (TextView) view.findViewById(R.id.car_type);
            TextView arrivalTime = (TextView) view.findViewById(R.id.arrival);

            InternalStorageTools.getAndShowPhoto(c, driverFoto, nearbyVehicles.get(position).getDriver().getPhoto_url());
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