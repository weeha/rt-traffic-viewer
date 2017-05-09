package model.location;

/**
 * Created by Florian Noack on 18.04.2017.
 */
public class CoordinateValue {
    private static final double ROUNDING_FACTOR = 0.5;
    private final double lonDeg;
    private final double latDeg;
    private final int lon;
    private final int lat;

    public CoordinateValue(int latitude, int longitude){
        this.lon = longitude;
        this.lat = latitude;
        this.lonDeg = get32BitRepresentation(longitude);
        this.latDeg = get32BitRepresentation(latitude);
    }

    public CoordinateValue(int latitude, int longitude, int prevLatitude, int prevLongitude){
        //double lon = prevLon + (double)lrp.getLon() / 100000.0;
        //double lat = prevLat + (double)lrp.getLat() / 100000.0;
        this.lat = latitude;
        this.lon = longitude;
        this.lonDeg = get32BitRepresentation(prevLongitude) + (double)longitude / 100000.0;
        this.latDeg = get32BitRepresentation(prevLatitude) + (double)latitude / 100000.0;
    }

    private double colculateCoord(double prevValue, int newValue){
        return 0.0;
    }

    public double getLonDeg(){
        return lonDeg;
    }

    public double getLatDeg() {
        return latDeg;
    }

    public static double get32BitRepresentation(int val) {
        int sgn = (int)Math.signum(val);
        double retVal = ((double)val - (double)sgn * 0.5) * 2.1457672119038307E-5;
        return retVal;
    }

    @Override
    public String toString(){
        String result = "";
        result += "lat: " + latDeg;
        result += "\nlon: " + lonDeg;
        result += "\nlon(raw): " + lon;
        result += "\nlat(raw): " + lat;
        return result;
    }
}