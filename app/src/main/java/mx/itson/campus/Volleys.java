package mx.itson.campus;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Volleys {
    private static Volleys mVolleyS = null;

    private RequestQueue mRequestQueue;

    private Volleys(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static Volleys getInstance(Context context) {
        if (mVolleyS == null) {
            mVolleyS = new Volleys(context);
        }
        return mVolleyS;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }




}
