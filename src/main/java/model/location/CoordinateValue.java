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

    public CoordinateValue(int longitude, int latitude){
        this.lon = longitude;
        this.lat = latitude;
        this.lonDeg = get32BitRepresentation(longitude);
        this.latDeg = get32BitRepresentation(latitude);
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
        result += "lon: " + lonDeg;
        result += "\nlat: " + latDeg;
        result += "\nlon(raw): " + lon;
        result += "\nlat(raw): " + lat;
        return result;
    }

}