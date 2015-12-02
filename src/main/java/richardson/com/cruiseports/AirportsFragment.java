package richardson.com.cruiseports;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class AirportsFragment extends Fragment implements OnMapReadyCallback {
    Port port;
    SupportMapFragment mapFragment;

    public AirportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        int portNumber = getArguments().getInt("portNumber");
        port = PortsList.getInstance().get(portNumber);
        mapFragment = (SupportMapFragment)getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.airport_map);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_airports, container, false);

        if(mapFragment == null){
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.airport_map, mapFragment)
                    .commit();
        }

        mapFragment.getMapAsync(this);

        String airportDistanceString = getString(R.string.airport_distance_text);

        String airportDistanceText = port.airport1.distanceToPort + " " +  airportDistanceString;

        TextView airportName = (TextView)view.findViewById(R.id.airport1_name);
        airportName.setText(port.airport1.airportName);
        TextView airportDistance = (TextView)view.findViewById(R.id.airport1_distance);
        airportDistance.setText(airportDistanceText);

        if(port.airport2 != null){
            TextView airportName2 = (TextView)view.findViewById(R.id.airport2_name);
            airportName2.setText(port.airport2.airportName);
            TextView airportDistance2 = (TextView)view.findViewById(R.id.airport2_distance);

            String airportDistanceText2 = port.airport2.distanceToPort + " " + airportDistanceString;
            airportDistance2.setText(airportDistanceText2);
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map){
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        map.clear();

        LatLng portCoordinates = new LatLng(port.latitude, port.longitude);
        LatLng airportCoordinates = new LatLng(port.airport1.latitude,
                port.airport1.longitude);

        map.setMyLocationEnabled(true);

        map.addMarker(new MarkerOptions()
                        .title(port.name)
                        .position(portCoordinates)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.cruiseship))
        );

        map.addMarker(new MarkerOptions()
                        .title(port.airport1.airportName)
                        .position(airportCoordinates)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.airport))

        );

        boundsBuilder.include(portCoordinates)
                .include(airportCoordinates);

        map.addPolyline(new PolylineOptions()
                        .add(portCoordinates, airportCoordinates)
                        .color(R.color.firstPrimaryBlue));

        if(port.airport2 != null){
            LatLng airportCoordinates2 = new LatLng(port.airport2.latitude,port.airport2.longitude);
            map.addMarker(new MarkerOptions()
                    .title(port.airport2.airportName)
                    .position(airportCoordinates2)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.airport))
            );

            boundsBuilder.include(airportCoordinates2);


            map.addPolyline(new PolylineOptions()
                .add(portCoordinates, airportCoordinates2)
                .color(R.color.firstPrimaryBlue));
        }

        map.addPolyline(new PolylineOptions());
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 75));


    }

}
