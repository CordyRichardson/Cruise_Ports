package richardson.com.cruiseports;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class PlaceRequestQueue{
    private static PlaceRequestQueue instance;
    private RequestQueue requestQueue;
    private static Context context;

    private PlaceRequestQueue(Context context){
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(context);
        }

        return requestQueue;
    }

    public static synchronized PlaceRequestQueue getInstance(Context context){
        if(instance == null){
            instance = new PlaceRequestQueue(context);
        }
        return instance;
    }

}
