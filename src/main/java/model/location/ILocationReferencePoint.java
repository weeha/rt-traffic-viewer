package model.location;

import openlr.binary.data.AbstractLRP;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * Created by flori on 04.05.2017.
 */
public interface ILocationReferencePoint {

    GeoPosition getGeoPosition();
    double getLongitude();
    double getLatidude();
    AbstractLRP getLRP();

}
