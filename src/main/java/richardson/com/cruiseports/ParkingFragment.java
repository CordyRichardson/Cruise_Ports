package richardson.com.cruiseports;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ParkingFragment extends Fragment implements OnMapReadyCallback {
    int portNumber;

    ListView parkingListView;

    SupportMapFragment mapFragment;

    HashMap<String, Integer> mHashMap;

    public ParkingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        portNumber = getArguments().getInt("portNumber");
        mHashMap = new HashMap<>();
        mapFragment = (SupportMapFragment)getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.parking_map);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_parking, container, false);
        if(mapFragment == null){
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.parking_map, mapFragment)
                    .commit();
        }

        TextView portName = (TextView)view.findViewById(R.id.port_name);
        portName.setText(PortsList.getInstance().get(portNumber).name);
        TextView portAddress = (TextView)view.findViewById(R.id.port_address);
        portAddress.setText(PortsList.getInstance().get(portNumber).address);

        TextView portParking = (TextView)view.findViewById(R.id.port_parking_amount);
        String portParkingText = getString(R.string.port_parking) + PortsList.getInstance()
                .get(portNumber).parkingCost;
        portParking.setText(portParkingText);

        if(PortsList.getInstance().get(portNumber).parkingList.size() != 0){
            TextView otherOptions = (TextView)view.findViewById(R.id.other_options);
            otherOptions.setText(getString(R.string.other_parking));

            parkingListView = (ListView)view.findViewById(R.id.parking_listView);
            parkingListView.setAdapter(new PlaceListAdapter(getActivity().getApplicationContext(),
                    PortsList.getInstance().get(portNumber).parkingList));
        }

        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng portCoordinates = new LatLng(PortsList.getInstance().get(portNumber).latitude,
                PortsList.getInstance().get(portNumber).longitude);

        LatLngBounds.Builder boundsBuilder;


        boundsBuilder = new LatLngBounds.Builder().include(portCoordinates);

        map.setMyLocationEnabled(true);

        Marker portMarker = map.addMarker(new MarkerOptions()
                            .title(PortsList.getInstance().get(portNumber).name)
                            .position(portCoordinates)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.cruiseship))
        );
        portMarker.showInfoWindow();
        mHashMap.put(portMarker.getId(), 0);

        if(PortsList.getInstance().get(portNumber).parkingList.size() == 0){
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(portCoordinates, 12));
        } else {
            int placeNumber = 1;
            for (Place place : PortsList.getInstance().get(portNumber).parkingList) {
                String assetName = "number_" + placeNumber + ".png";
                LatLng latLng = new LatLng(place.latitude, place.longitude);
                Marker marker = map.addMarker(new MarkerOptions()
                        .title(place.name)
                        .position(new LatLng(place.latitude, place.longitude))
                        .icon(BitmapDescriptorFactory.fromAsset(assetName)));

                boundsBuilder.include(latLng);
                mHashMap.put(marker.getId(), placeNumber);
                ++placeNumber;
            }

            map.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 5));
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                int itemSelected = mHashMap.get(marker.getId());
                if(itemSelected > 0)
                    parkingListView.smoothScrollToPosition(itemSelected-1);
                return true;
            }

        });
    }
}
