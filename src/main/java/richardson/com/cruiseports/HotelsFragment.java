package richardson.com.cruiseports;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class HotelsFragment extends Fragment implements OnMapReadyCallback {
    ListView hotelListView;
    HashMap<String, Integer> mHashMap;
    SupportMapFragment mapFragment;
    int portNumber;

    public HotelsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        portNumber = getArguments().getInt("portNumber");
        mapFragment = (SupportMapFragment)getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.hotels_map);
        mHashMap = new HashMap<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_hotels, container, false);

        if(mapFragment == null){
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.hotels_map, mapFragment)
                    .commit();
        }


        //add UI items
        TextView portName = (TextView)view.findViewById(R.id.port_name);
            portName.setText(PortsList.getInstance().get(portNumber).name);
        TextView portAddress = (TextView)view.findViewById(R.id.port_address);
            portAddress.setText(PortsList.getInstance().get(portNumber).address);

        hotelListView = (ListView)view.findViewById(R.id.hotels_listView);
            hotelListView.setAdapter(new PlaceListAdapter(getActivity().getApplicationContext(),
                PortsList.getInstance().get(portNumber).hotelList));
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng portCoordinates = new LatLng(PortsList.getInstance().get(portNumber).latitude,
                PortsList.getInstance().get(portNumber).longitude);

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder().include(portCoordinates);

        map.setMyLocationEnabled(true);

        Marker portMarker = map.addMarker(new MarkerOptions()
                        .title(PortsList.getInstance().get(portNumber).name)
                        .position(portCoordinates)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.cruiseship))
        );

        portMarker.showInfoWindow();
        mHashMap.put(portMarker.getId(), 0);

        int placeNumber = 1;
        for (Place place : PortsList.getInstance().get(portNumber).hotelList) {
            String assetName = "number_"+placeNumber+".png";
            LatLng latLng = new LatLng(place.latitude, place.longitude);

            Marker marker = map.addMarker(new MarkerOptions()
                            .title(place.name)
                            .position(new LatLng(place.latitude, place.longitude))
                            .icon(BitmapDescriptorFactory.fromAsset(assetName))
            );
            mHashMap.put(marker.getId(), placeNumber);
            boundsBuilder.include(latLng);
            ++placeNumber;
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                    marker.showInfoWindow();
                    int itemSelected = mHashMap.get(marker.getId());
                    if(itemSelected > 0)
                        hotelListView.smoothScrollToPosition(itemSelected-1);
                return true;
            }
        });

       map.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 5));
    }
}


