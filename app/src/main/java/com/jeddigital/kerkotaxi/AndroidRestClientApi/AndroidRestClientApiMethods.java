package com.jeddigital.kerkotaxi.AndroidRestClientApi;

import android.content.Context;
import android.util.Log;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jeddigital.kerkotaxi.AnroidRestModels.NearbyVehicle;
import com.jeddigital.kerkotaxi.GSON.BooleanTypeAdapter;
import com.jeddigital.kerkotaxi.MenuHyreseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by theodhori on 7/31/2016.
 */
public class AndroidRestClientApiMethods {
    static Gson gson;
    static Context context;

    public AndroidRestClientApiMethods(Context context){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(boolean.class, new BooleanTypeAdapter());
        gson = builder.create();
        this.context = context;
    }

    public static void getNearbyVehicles(final LatLng requestedLocation, final String client_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configurations.GET_NEARBY_TAXIS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject responseJSONObject;
                        try {
                            responseJSONObject = new JSONObject(response);

                            int error_code = responseJSONObject.getInt("error_code");
                            String error_code_desc = responseJSONObject.getString("error_code_desc");

                            if(error_code == 0){
                                JSONArray nearbyVehiclesJSONArray = responseJSONObject.getJSONArray("nearby_vehicles");
                                List<NearbyVehicle> nearbyVehicles = gson.fromJson(responseJSONObject.getString("nearby_vehicles"), new TypeToken<List<NearbyVehicle>>(){}.getType());

                                ((MenuHyreseActivity)context).showNearByVehiclesAction(nearbyVehicles, requestedLocation);
                            }

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
                params.put("client_id", client_id);
                params.put("client_lat", String.valueOf(requestedLocation.latitude));
                params.put("client_lng", String.valueOf(requestedLocation.longitude));

                Log.d("qqq send", String.valueOf(requestedLocation.latitude));
                Log.d("qqq send ", String.valueOf(requestedLocation.longitude));
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    public static void requestTaxi(final int vehicle_id, final String client_id, final LatLng requested_location) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configurations.REQUEST_TAXI_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject responseJSONObject;
                        try {
                            responseJSONObject = new JSONObject(response);

                            int error_code = responseJSONObject.getInt("error_code");
                            String error_code_desc = responseJSONObject.getString("error_code_desc");

                            ((MenuHyreseActivity)context).requestTaxiAction(vehicle_id);
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
                params.put("client_id", client_id);
                params.put("vehicle_id", ""+vehicle_id);
                params.put("requested_lat", ""+requested_location.latitude);
                params.put("requested_lng", ""+requested_location.longitude);

                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
