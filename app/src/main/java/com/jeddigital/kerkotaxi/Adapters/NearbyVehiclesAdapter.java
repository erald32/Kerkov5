package com.jeddigital.kerkotaxi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeddigital.kerkotaxi.AnroidRestModels.NearbyVehicle;
import com.jeddigital.kerkotaxi.IOTools.InternalStorageTools;
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
        vi = inflater.inflate(R.layout.dialog_listview_item_nearby_vehicle, null);

        ImageView driverFoto = (ImageView)vi.findViewById(R.id.driver_foto);
        TextView emerMbiemer = (TextView) vi.findViewById(R.id.name);
        TextView tipiMakines = (TextView) vi.findViewById(R.id.car_type);
        TextView arrivalTime = (TextView) vi.findViewById(R.id.arrival);

        InternalStorageTools.getAndShowPhoto(context, driverFoto, nearbyVehicles.get(position).getDriver().getPhoto_url());
        emerMbiemer.setText(nearbyVehicles.get(position).getDriver().getFirst_name() + " " + nearbyVehicles.get(position).getDriver().getLast_name());
        tipiMakines.setText(nearbyVehicles.get(position).getCar_model());
        arrivalTime.setText(nearbyVehicles.get(position).getDistance_params().getTime_readable());


        return vi;
    }
}
