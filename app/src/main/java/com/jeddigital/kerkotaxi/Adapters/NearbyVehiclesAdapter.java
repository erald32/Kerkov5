package com.jeddigital.kerkotaxi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jeddigital.kerkotaxi.AnroidRestModels.NearbyVehicle;
import com.jeddigital.kerkotaxi.R;

import java.util.List;

/**
 * Created by Theodhori on 7/21/2016.
 */
public class NearbyVehiclesAdapter extends BaseAdapter{
    List<NearbyVehicle> nearbyVehicles;
    LayoutInflater inflater;
    Context context;

    public NearbyVehiclesAdapter(List<NearbyVehicle> nearbyVehicles, Context context){
        this.nearbyVehicles = nearbyVehicles;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return nearbyVehicles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi;
        vi = inflater.inflate(R.layout.listview_item_nearby_vehicle, null);


        return vi;
    }
}
