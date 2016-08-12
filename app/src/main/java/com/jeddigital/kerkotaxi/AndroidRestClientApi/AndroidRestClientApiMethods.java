package com.jeddigital.kerkotaxi.AndroidRestClientApi;

import android.content.Context;
import android.location.Location;
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
import com.jeddigital.kerkotaxi.AnroidRestModels.CheckRequestResponse;
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
                            }else{
                                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("qqq", "ErrorResponseOnLocationChanged: " + error.getMessage());
                if (error instanceof NoConnectionError) {
                    Log.e("qqq", "NoConnectionError: " + error.getMessage());
                } else if (error instanceof TimeoutError) {
                    Log.e("qqq", "TimeoutError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    Log.e("qqq", "AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    Log.e("qqq", "ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    Log.e("qqq", "NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    Log.e("qqq", "ParseError: " + error.getMessage());
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

                Log.e("qqq send", String.valueOf(requestedLocation.latitude));
                Log.e("qqq send ", String.valueOf(requestedLocation.longitude));
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


    public static void cancelRequest(final String client_id, final Location client_location) {
        final Double client_lat = client_location.getLatitude();
        final Double client_lng = client_location.getLongitude();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configurations.CANCEL_REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject responseJSONObject;
                        try {
                            responseJSONObject = new JSONObject(response);

                            int error_code = responseJSONObject.getInt("error_code");
                            String error_code_desc = responseJSONObject.getString("error_code_desc");

                            ((MenuHyreseActivity)context).cancelRequestAction(error_code);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("qqq", "ErrorResponseOnLocationChanged: " + error.getMessage());
                if (error instanceof NoConnectionError) {
                    Log.e("qqq", "NoConnectionError: " + error.getMessage());
                } else if (error instanceof TimeoutError) {
                } else if (error instanceof AuthFailureError) {
                    Log.e("qqq", "AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    Log.e("qqq", "ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    Log.e("qqq", "NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    Log.e("qqq", "ParseError: " + error.getMessage());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                //params.put("getNearbyTaxis", getNearbyTaxis);
                params.put("client_id", client_id);
                params.put("client_lat", ""+client_lat);
                params.put("client_lng", ""+client_lng);

                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    public static void requestTaxi(final int vehicle_id, final String client_id, final LatLng requested_location, final LatLng actual_location) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configurations.REQUEST_TAXI_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject responseJSONObject;
                        try {
                            responseJSONObject = new JSONObject(response);

                            int error_code = responseJSONObject.getInt("error_code");
                            String error_code_desc = responseJSONObject.getString("error_code_desc");

                            ((MenuHyreseActivity)context).requestTaxiActionResponse(vehicle_id, error_code);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("qqq", "ErrorResponseOnLocationChanged: " + error.getMessage());
                if (error instanceof NoConnectionError) {
                    Log.e("qqq", "NoConnectionError: " + error.getMessage());
                } else if (error instanceof TimeoutError) {
                } else if (error instanceof AuthFailureError) {
                    Log.e("qqq", "AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    Log.e("qqq", "ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    Log.e("qqq", "NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    Log.e("qqq", "ParseError: " + error.getMessage());
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
                params.put("client_actual_lat", ""+actual_location.latitude);
                params.put("client_actual_lng", ""+actual_location.longitude);

                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }


    public static void updateClientLocation(final Location location, final String client_id) {
        final String latitude = String.valueOf(location.getLatitude()).toString().trim();
        final String longitude = String.valueOf(location.getLongitude()).toString().trim();

        StringRequest postRequest = new StringRequest(Request.Method.POST, Configurations.UPDATE_KLIENT_LOCATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int error_code = jsonResponse.getInt("error_code");
                            String error_code_desc = jsonResponse.getString("error_code_desc");
                            if (error_code == 0) {
                                Log.e("qqq", "koordinatat u derguan me sukses");
                            } else {
                                Log.e("qqq", error_code_desc);
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
                params.put("client_lat", latitude);
                params.put("client_lng", longitude);

                Log.e("qqq_liveLocation_params", latitude);
                Log.e("qqq_liveLocation_params", longitude);

                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(postRequest);
    }

    public static void checkRequestStatus(final String client_id) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Configurations.CHECK_REQUEST_STATUS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int error_code = jsonResponse.getInt("error_code");
                            String error_code_desc = jsonResponse.getString("error_code_desc");
                            //int booking_status_id  = jsonResponse.getInt("booking_status_id");

                            CheckRequestResponse requestResponse = gson.fromJson(jsonResponse.getString("booking"), new TypeToken<CheckRequestResponse>(){}.getType());

                            ((MenuHyreseActivity)context).handleCheckRequestAction(requestResponse);

                            if (error_code == 0) {
                                Log.e("qqq", "requesti u mor me sukses");
                            } else {
                                Log.e("qqq", error_code_desc);
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

                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(postRequest);
    }


}
