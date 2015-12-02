package richardson.com.cruiseports;

import java.util.ArrayList;

/**
 * Traditional setters/getters pattern is sacrificed in favor of direct field access in order to
 * optimize performance; per recommendation from google
 *
 * source: http://developer.android.com/training/articles/perf-tips.html
 */

public class Port {
    String name, cityName, address;
    double latitude, longitude;

    boolean dataRetrieved;

    String parkingCost;
    Airport airport1 = null, airport2 = null;

    ArrayList<Place> parkingList;
    ArrayList<Place> hotelList;

    public Port(String name, String cityName, String address, String parkingCost, double latitude,
                double longitude, String airportName, double airportLatitude, double airportLongitude,
                double distanceToPort1){
        this.name = name;
        this.cityName = cityName;
        this.address = address;
        this.parkingCost = parkingCost;
        this.latitude = latitude;
        this.longitude = longitude;

        dataRetrieved = false;

        addAirport1(airportName, airportLatitude, airportLongitude, distanceToPort1);

        parkingList = new ArrayList<>();
        hotelList = new ArrayList<>();
    }

    public Port(String name, String cityName, String address, String parkingCost, double latitude,
                double longitude, String airportName1, double airportLatitude1,
                double airportLongitude1, double distanceToPort1,
                String airportName2, double airportLatitude2,
                double airportLongitude2, double distanceToPort2){
        this.name = name;
        this.cityName = cityName;
        this.address = address;
        this.parkingCost = parkingCost;
        this.latitude = latitude;
        this.longitude = longitude;

        addAirport1(airportName1, airportLatitude1, airportLongitude1, distanceToPort1);
        addAirport2(airportName2, airportLatitude2, airportLongitude2, distanceToPort2);

        parkingList = new ArrayList<>();
        hotelList = new ArrayList<>();

    }

    public void addAirport1(String airportName, double latitude, double longitude,
                            double distanceToPort){
            airport1 = new Airport(airportName, latitude, longitude, distanceToPort);
    }

    public void addAirport2(String airportName, double latitude, double longitude,
                            double distanceToPort){
        airport2 = new Airport(airportName, latitude, longitude, distanceToPort);
    }

    public class Airport{
        public String airportName;
        public double latitude, longitude, distanceToPort;

        public Airport(String airportName, double latitude, double longitude, double distanceToPort){
            this.airportName = airportName;
            this.latitude = latitude;
            this.longitude = longitude;
            this.distanceToPort = distanceToPort;

        }
    } // end airport class
}
