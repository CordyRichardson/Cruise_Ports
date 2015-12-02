package richardson.com.cruiseports;

import java.util.ArrayList;

/**
 * Created by cordyrichardson on 10/29/15.
 *
 * Class provides singleton pattern to allow access to single list object to multiple activities
 * and fragments; optimizes performance by avoiding any need to pass the list or a object between
 * activities/fragments
 *
 * Ports are created here with all information that is unlikely to change: port names, city names,
 *  cruise port and airport addresses, etc.
 */

public class PortsList extends ArrayList<Port> {
    private static PortsList listInstance = new PortsList();

    public static PortsList getInstance() {
        return listInstance;
    }

    private PortsList() {
        add(new Port("Cruise Maryland", "Baltimore", "2001 East McComas Street\n" +
                "Baltimore, Maryland 21230", "$15 per day", 39.2649944, -76.5985853,
                "Baltimore International Airport", 39.1753, -76.6683, 11));

        add(new Port("Cruiseport Boston", "Boston", "Tide Street\n" +
                "Boston, Massachusetts 02210", "$16 per day", 42.343685, -71.030506,
                "Logan International Airport", 42.3631, -71.0064, 3.2));

        add(new Port("Port of Charleston", "Charleston", "32 Washington Street\n" +
                "Charleston, South Carolina 29401", "$17 per day", 32.7861554,-79.9247939,
                "Charleston International Airport", 32.8986, -80.0406, 12));

        add(new Port("Port Everglades", "Fort Lauderdale", "1850 Eller Drive\n" +
                "Fort Lauderdale, Florida 33316", "$15 per day", 26.0937112, -80.1246643,
                "Fort Lauderdale - Hollywood International Airport", 26.0725, -80.1528, 5,
                "Miami International Airport", 25.7933, -80.2906, 30));

        add(new Port("Texas Cruise Ship Terminal\nOn Galveston Island", "Galveston",
                "2503 Harborside Drive\n" +
                        "Galveston, Texas 77550", "$10 per day", 29.308143, -94.796417,
                "George Bush Intercontinental Airport", 29.9844, -95.3414, 71,
                "William P. Hobby Airport", 29.6456, -95.2789, 42));

        add (new Port("Aloha Tower Cruise Terminal\nPier 2 Cruise Terminal", "Honolulu",
                "1 Aloha Tower Drive\nHonolulu, HI, 96813", "Parking at terminal not available",
                21.307929, -157.864446, "Honolulu International Airport",
                21.3186, -157.9225, 4.7));

        add(new Port("JaxPort Cruise Terminal", "Jacksonville", "9810 August Drive\n" +
                "+Jacksonville, FL 32226", "$15 per day", 30.4083, -81.5775,
                "Jacksonville International Airport", 30.4942, -81.6878, 11.6));

        add(new Port ("Port of Long Beach","Long Beach", "231 Windsor Way\n" +
                "Long Beach, CA 90802", "$19 per day", 33.7513521, -118.188752,
                "Los Angeles International Airport", 33.9425, -118.4081, 22.2,
                "Long Beach Airport", 33.8178, -118.1517, 11.5));

        add(new Port("Port Miami", "Miami", "1000 Caribbean Way\n " +
                "Miami, FL 33132", "$20 per day", 25.7774265,-80.1782048,
                "Miami International Airport", 25.7933, -80.2906, 8.5,
                "Fort Lauderdale - Hollywood International Airport", 26.0725, -80.1528, 31.6));

        add(new Port("Port of New Orleans", "New Orleans", "1100 Port of New Orleans Place\n" +
                "New Orleans, LA 70130", "$18 per day", 29.9369, -90.0619,
                "Louis Armstrong New Orleans International Airport", 29.9933, -90.2581, 16.6));

        add(new Port("Brooklyn Cruise Terminal", "New York City - Brooklyn", "72 Bowne Street\n" +
                "Brooklyn, NY 11231", "18 dollars per day", 40.6820, -74.0143,
                "John F. Kennedy International Airport", 40.6397, -73.7789, 19.1,
                "LaGuardia Airport", 40.7772, -73.8726, 12.4));

        add(new Port("Manhattan Cruise Terminal", "New York City - Manhattan", "711 12th Avenue\n" +
                "New York, NY 10019", "$40 per day", 40.7680, -73.9966,
                "John F. Kennedy International Airport", 40.6397, -73.7789, 25.3,
                "LaGuardia Airport", 40.7772, -73.8726, 16.2));

        add(new Port("Cruise Norfolk", "Norfolk", "One Waterside Drive\nNorfolk, VA 23510",
                "$15 per day", 36.847385, -76.295669, "Norfolk International Airport",
                36.8947, -76.2011, 9.1));

        add(new Port("Port Canaveral", "Port Canaveral (Orlando)", "9050 Discovery Pl\n" +
                "Port Canaveral, FL 32920", "$16 - $20 per day", 28.405512, -80.632573,
                "Orlando International Airport", 28.4294, -81.3089, 45.1));

        add(new Port("James R. Herman Cruise Terminal", "San Francisco",
                "Pier 35 The Embarcadero\nSan Francisco, CA 94133",
                "Parking at terminal not available", 37.805477, -122.401595,
                "San Francisco International Airport", 37.6189, -122.3750, 15.8,
                "Oakland International Airport", 37.7214, -122.2208, 20.9));

        add(new Port("B Street Cruise Terminal", "San Diego",
                "1140 N Harbor Drive\nSan Diego, CA 92101","Parking at terminal not available",
                32.717673, -117.175058, "San Diego International Airport", 32.733855,
                -117.193733, 2.7));

        add(new Port("Port of San Juan", "San Juan", "Port of San Juan\nSan Juan, Puerto Rico 00916",
                "Parking at terminal not available", 18.462261, -66.109929,
                "Luis Munoz Marin\nInternational Airport", 18.4392, -66.0019, 8.14));

        add(new Port("Port of Seattle", "Seattle", "2001 W Garfield St\n" +
                "Seattle, WA 98119", "$26 per day", 47.632036, -122.380180,
                "Seattle-Tacoma International Airport", 47.4489, -122.3094, 15));

        add(new Port("Tampa Cruise Port", "Tampa", "800 Channelside Dr\n" +
                "Tampa, FL 33602", "$15 per day", 27.9480575, -82.4488813,
                "Tampa International Airport", 27.9756, -82.5333, 9.5));

    }
}
