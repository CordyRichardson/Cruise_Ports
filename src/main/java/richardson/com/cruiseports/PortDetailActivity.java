package richardson.com.cruiseports;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PortDetailActivity extends AppCompatActivity {
    String key = "AIzaSyCnQkUklSHFq5ayjHmuAqh3UIP7-wvCTrY";         // mandatory key to access google api
    final String KEY_STRING = "&key="+key;

    private static Context context;

    ConnectivityManager cm;
    ProgressDialog progressDialog;

    ActionBar actionBar;
    ViewPager pager;

    static List<Fragment> fragments;

    SearchErrorListener errorListener;

    int portNumber;
    final static int PLACE_TYPE_HOTEL = 1;
    final static int PLACE_TYPE_PARKING = 2;

    public PortDetailActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.enableDefaults();
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        checkConnectivity();

        startProgressDialog();

        //Instanitate UI objects
        setContentView(R.layout.activity_port_detail);
        pager = (ViewPager)findViewById(R.id.pager);
        portNumber = getIntent().getIntExtra("port", 0);
        actionBar = getSupportActionBar();
        actionBar.setTitle(PortsList.getInstance().get(portNumber).cityName);

        makeActivityFragmentList();

        if(!PortsList.getInstance().get(portNumber).dataRetrieved){
            new Thread(new Runnable(){
                public void run(){
                    errorListener = new SearchErrorListener();
                    placeIDSearch();
                }
            }).start();

        } else{
            setPagerAdapter();
        }
    }

    public void checkConnectivity(){
        cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() == null || !cm.getActiveNetworkInfo().isConnectedOrConnecting()){
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    public void startProgressDialog(){
        progressDialog = new ProgressDialog(PortDetailActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    public void makeActivityFragmentList() {
        fragments = new ArrayList<>();
        //creates the 3 fragment objects and passes them the ListPorts number
        Bundle fragBundle = new Bundle();
        fragBundle.putInt("portNumber", portNumber);
        fragments.add(new AirportsFragment());
        fragments.add(new ParkingFragment());
        fragments.add(new HotelsFragment());
        for(Fragment fragment : fragments)
            fragment.setArguments(fragBundle);

    }

    public void setPagerAdapter(){
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            final int PAGE_COUNT = 3;
            private String[] tabTitles = new String[]{"Airports", "Parking", "Hotels"};

            @Override
            public Fragment getItem(int pos) {
                return fragments.get(pos);
            }

            @Override
            public int getCount() {
                return PAGE_COUNT;
            }

            @Override
            public CharSequence getPageTitle(int pos) {
                return tabTitles[pos];
            }

        });
        progressDialog.hide();
    }

    public void placeIDSearch(){


        String url = urlStringBuilder(PLACE_TYPE_PARKING);
        JsonObjectRequest parkingSearchRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new IDSearchListener(PLACE_TYPE_PARKING),
                        errorListener);
        PlaceRequestQueue.getInstance(context).getRequestQueue().add(parkingSearchRequest);

        url = urlStringBuilder(PLACE_TYPE_HOTEL);
        JsonObjectRequest hotelSearchRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new IDSearchListener(PLACE_TYPE_HOTEL),
                        errorListener);
        PlaceRequestQueue.getInstance(context).getRequestQueue().add(hotelSearchRequest);

    }

    public void placeDetailSearch(ArrayList<String> iDs, int placeType){

        String placeDetailQuery = "https://maps.googleapis.com/maps/api/place/details/json?" +
                KEY_STRING + "&placeid=";

        for (String id : iDs){
            String url = placeDetailQuery + id;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                    url, null, new PlaceSearchListener(placeType),errorListener);

            PlaceRequestQueue.getInstance(context).getRequestQueue().add(request);
        }
    }

    public String urlStringBuilder(int placeType){
        String url = "";
        final String PARKING_QUERY = "&radius=10000&type=parking";
        final String HOTEL_QUERY = "&radius=5000&type=lodging";

        String location = "&location="+PortsList.getInstance().get(portNumber).latitude + ","
                + PortsList.getInstance().get(portNumber).longitude;

        switch(placeType){
            case PLACE_TYPE_PARKING:
                url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                        + KEY_STRING + location + PARKING_QUERY;
                break;

            case PLACE_TYPE_HOTEL:
                url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                        + KEY_STRING + location + HOTEL_QUERY;
                break;
        }

        return url;
    }


    //search listeners for volley actions
    private class IDSearchListener implements Response.Listener<JSONObject>{
        int placeType;

        public IDSearchListener(int placeType){
            this.placeType = placeType;
        }

        @Override
        public void onResponse (JSONObject response){
            ArrayList<String> iDs = new ArrayList<>();
            try{
                JSONArray results = response.getJSONArray("results");
                if(results.length() == 0)
                    return;

                for (int i = 0; i < results.length(); ++i){
                    JSONObject place = results.getJSONObject(i);
                    String placeId = place.getString("place_id");
                    iDs.add(placeId);
                }
            } catch(JSONException ex){
            }

            placeDetailSearch(iDs, placeType);
        }

    }

    private class PlaceSearchListener implements Response.Listener<JSONObject>{
        int placeType;

        public PlaceSearchListener(int placeType){
            this.placeType = placeType;
        }

        @Override
        public void onResponse(JSONObject response){
            try {
                JSONObject result = response.getJSONObject("result");
                JSONObject geometry = result.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                double latitude = location.getDouble("lat");
                double longitude = location.getDouble("lng");

                String address = result.getString("formatted_address");
                String name = result.getString("name");
                String phoneNumber = result.getString("formatted_phone_number");
                String website = result.getString("website");

                switch (placeType){
                    case PLACE_TYPE_PARKING:
                        Place parkingPlace = new Place();
                        parkingPlace.name = name;
                        parkingPlace.address = address;
                        parkingPlace.latitude = latitude;
                        parkingPlace.longitude = longitude;
                        parkingPlace.phoneNumber = phoneNumber;
                        parkingPlace.website = website;
                        PortsList.getInstance().get(portNumber).parkingList.add(parkingPlace);
                        break;

                    case PLACE_TYPE_HOTEL:
                        Place hotel = new Place();
                        hotel.name = name;
                        hotel.address = address;
                        hotel.latitude = latitude;
                        hotel.longitude = longitude;
                        hotel.phoneNumber = phoneNumber;
                        hotel.website = website;
                        PortsList.getInstance().get(portNumber).hotelList.add(hotel);
                        break;
                }
                PortsList.getInstance().get(portNumber).dataRetrieved = true;

            } catch (JSONException ex){

            }
            setPagerAdapter();
        }
    }

    private class SearchErrorListener implements Response.ErrorListener{
        /* error listener for all volley requests. One object will be created and passed to each request
         */
        @Override
        public void onErrorResponse(VolleyError error){
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}