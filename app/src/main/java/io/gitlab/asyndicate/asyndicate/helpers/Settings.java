package io.gitlab.asyndicate.asyndicate.helpers;

import android.content.Context;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.Scanner;


public class Settings {

    private Context mContext;

    private static Settings mInstance;
    private RequestQueue requestQueue;

    private Settings(Context context) {
        mContext = context;
    }

    private JSONObject getSession() {
        try {
            String me = "";
            FileInputStream in = new FileInputStream(mContext.getFilesDir() + "/me.json");
            Scanner s = new Scanner(in);
            while (s.hasNext()) {
                me += s.nextLine();
            }
            return new JSONObject(me);
        } catch (Exception e) {
            return null;
        }
    }

    public static Settings getInstance() {
        return mInstance;
    }

    public static Settings getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Settings(context);
        }
        return mInstance;
    }

    public static String getString(String key) {
        try {
            return Settings.getInstance().getSession().getString(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getString(String key, String placeholder) {
        try {
            return Settings.getInstance().getSession().getString(key);
        } catch (Exception ignored) {
            return placeholder;
        }
    }

    public static JSONObject getJSONObject(String key) {
        try {
            return Settings.getInstance().getSession().getJSONObject(key);
        } catch (Exception e) {
            return null;
        }
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public Context getContext() {
        return mContext;
    }
}
