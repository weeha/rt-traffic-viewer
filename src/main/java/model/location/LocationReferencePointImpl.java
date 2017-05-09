package model.location;

import openlr.binary.data.AbstractLRP;
import openlr.binary.impl.LocationReferenceBinaryImpl;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * Created by Florian Noack on 04.05.2017.
 */
public class LocationReferencePointImpl implements ILocationReferencePoint{

    private final AbstractLRP lrp;
    private final CoordinateValue coord;


    public LocationReferencePointImpl(AbstractLRP lrp){
        this.lrp = lrp;
        coord = new CoordinateValue(lrp.getLat(), lrp.getLon());
    }

    public LocationReferencePointImpl(AbstractLRP lrp, AbstractLRP prevLRP){
        this.lrp = lrp;
        coord = new CoordinateValue(lrp.getLat(), lrp.getLon(), prevLRP.getLat(), prevLRP.getLon());
    }

    @Override
    public GeoPosition getGeoPosition() {

        return new GeoPosition(coord.getLatDeg(), coord.getLonDeg());
    }

    @Override
    public AbstractLRP getLRP(){
        return this.lrp;
    }

    @Override
    public double getLongitude() {
        return coord.getLonDeg();
    }

    @Override
    public double getLatidude() {
        return coord.getLatDeg();
    }

    @Override
    public String toString(){
        return coord.toString();
    }
}
