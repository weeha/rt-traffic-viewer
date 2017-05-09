package model.location;

/**
 * Created by Florian Noack on 09.05.2017.
 */

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import openlr.map.GeoCoordinates;

public final class Formatter {
    private static final DecimalFormat ANGLE_FORMAT;
    private static final DecimalFormat LENGTH_FORMAT;
    private static final DecimalFormat COORD_FORMAT;
    private static final DecimalFormat PERCENTAGE_FORMAT;

    static {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        ANGLE_FORMAT = new DecimalFormat("#0.00", dfs);
        LENGTH_FORMAT = new DecimalFormat("#0.00", dfs);
        PERCENTAGE_FORMAT = new DecimalFormat("#0.00%", dfs);
        COORD_FORMAT = new DecimalFormat("#0.000000", dfs);
    }

    private Formatter() {
        throw new UnsupportedOperationException();
    }

    public static String formatAngle(double val) {
        return ANGLE_FORMAT.format(val);
    }

    public static String formatLength(double val) {
        return LENGTH_FORMAT.format(val);
    }

    public static String formatCoord(double val) {
        return COORD_FORMAT.format(val);
    }

    public static String formatCoord(GeoCoordinates coordinate) {
        return String.valueOf(COORD_FORMAT.format(coordinate.getLongitudeDeg())) + ", " + COORD_FORMAT.format(coordinate.getLatitudeDeg());
    }

    public static String formatPercentage(double val) {
        return PERCENTAGE_FORMAT.format(val);
    }
}