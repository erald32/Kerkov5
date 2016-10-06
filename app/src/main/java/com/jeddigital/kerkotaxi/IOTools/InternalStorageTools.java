package com.jeddigital.kerkotaxi.IOTools;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.jeddigital.kerkotaxi.CustomUI.RoundImage;

/**
 * Created by theodhori on 7/28/2016.
 */
public class InternalStorageTools {

    public static void getAndShowPhoto(final Context c, final ImageView imageview, String photo_url) {
        ImageRequest imageRequest = new ImageRequest(photo_url
                , new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap responseBitmap) {
                RoundImage roundImage = new RoundImage(responseBitmap);
                imageview.setImageDrawable(roundImage);
            }
        },0,0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Log.d("dev", "NoConnectionError: " + error.getMessage());
                } else if (error instanceof TimeoutError) {
                    Log.d("dev", "AuthFailureError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    Log.d("dev", "AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    Log.d("dev", "ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    Log.d("dev", "NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    Log.d("dev", "ParseError: " + error.getMessage());
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(c);
        requestQueue.add(imageRequest);
    }
}
