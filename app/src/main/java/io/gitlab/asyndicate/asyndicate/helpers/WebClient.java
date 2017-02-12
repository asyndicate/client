package io.gitlab.asyndicate.asyndicate.helpers;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.gitlab.asyndicate.asyndicate.R;


public class WebClient {

    private String URL;
    private static Context mContext;

    public WebClient(Context context) {
        URL = context.getResources().getString(R.string.server_url);
        mContext = context;
    }

    public void get(String url, HashMap params, final AsyncHttpResponseHandler handler) {

        if (params == null) {
            params = new HashMap<String, String>();
        }

        try {
//            url += "?client=" + URLEncoder.encode(Settings.getClient(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String api_token = PreferenceManager.getDefaultSharedPreferences(mContext).getString("api_token", null);

        Log.d("WebClient", "Binding api_key: " + api_token);

//        Log.d("WebClient", params.toString());

        Log.d("WebClient", "Sending HTTP GET " + URL + url);

        final HashMap finalParams = params;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handler.onSuccess(0, response);
                        handler.onFinish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String response = null;
                NetworkResponse net = error.networkResponse;
                if (net != null) {
                    response = new String(net.data);
                }
                handler.onFailure(0, response, error);
                handler.onFinish();
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-type", "application/json");
                headers.put("Authorization", String.format("Bearer %s", api_token));
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject body = new JSONObject(finalParams);
                Log.d("WebClient", "-----------------");
                Log.d("WebClient", body.toString());
                Log.d("WebClient", "-----------------");
                return body.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public RetryPolicy getRetryPolicy() {
                return new RetryPolicy() {
                    @Override
                    public int getCurrentTimeout() {
                        return 20 * 1000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 0;
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError {
                        throw error;
                    }
                };
            }
        };

        Settings.getInstance().getRequestQueue().add(stringRequest);
        handler.onStart();
    }

    public void post(String url, HashMap params, final AsyncHttpResponseHandler handler) {

        if (params == null) {
            params = new HashMap<>();
        }

        try {
//            url += "?client=" + URLEncoder.encode(Settings.getClient(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String api_token = PreferenceManager.getDefaultSharedPreferences(mContext).getString("api_token", null);

        Log.d("WebClient", "Sending HTTP POST " + URL + url);

        final HashMap finalParams = params;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handler.onSuccess(0, response);
                        handler.onFinish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String response = null;
                NetworkResponse net = error.networkResponse;

                if (net != null) {
                    response = new String(net.data);
                }

                handler.onFailure(0, response, error);
                handler.onFinish();
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
//                headers.put("Content-type", "application/json");
                headers.put("Authorization", String.format("Bearer %s", api_token));
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject body = new JSONObject(finalParams);
                return body.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public RetryPolicy getRetryPolicy() {
                return new RetryPolicy() {
                    @Override
                    public int getCurrentTimeout() {
                        return 20 * 1000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 0;
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError {
                        throw error;
                    }
                };
            }
        };

        Settings.getInstance().getRequestQueue().add(stringRequest);
        handler.onStart();
    }
}