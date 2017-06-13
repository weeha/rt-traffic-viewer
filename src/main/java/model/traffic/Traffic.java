package model.traffic;

import model.location.FirstLocationReferencePoint;
import model.location.ILocationReferencePoint;
import model.location.LastLocationReferencePoint;
import model.location.LocationReferencePointImpl;
import openlr.binary.data.AbstractLRP;
import openlr.binary.data.FirstLRP;
import openlr.binary.data.IntermediateLRP;
import openlr.binary.data.RawBinaryData;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian Noack on 04.05.2017.
 */
public abstract class Traffic {

    private RawBinaryData rawData;
    private String creationTime="";
    private String trafficType="";
    protected String averageSpeed="";
    private String delayTime="";
    private Shape shape;
    private FirstLocationReferencePoint firstLRP = null;
    private LastLocationReferencePoint lastLRP = null;
    private List<LocationReferencePointImpl> intermediatePoints;
    private List<LocationReferencePointImpl> lrps;

    public Traffic(){
        intermediatePoints = new ArrayList<LocationReferencePointImpl>();
        lrps = new ArrayList<LocationReferencePointImpl>();
    }

    public void setRawData(RawBinaryData data){
        this.rawData = data;
        firstLRP = new FirstLocationReferencePoint(data.getBinaryFirstLRP());
        lrps.add(firstLRP);
        ILocationReferencePoint prevLRP = firstLRP;
        if(data.getBinaryIntermediates() != null) {
            for (AbstractLRP lrp : data.getBinaryIntermediates()) {
                LocationReferencePointImpl point = new LocationReferencePointImpl(lrp, prevLRP);
                intermediatePoints.add(point);
                lrps.add(point);
                prevLRP = point;
            }
        }
        // LastLRP = prevLRP - BinaryLastLRP
        lastLRP = new LastLocationReferencePoint(data.getBinaryLastLRP(), prevLRP);
        lrps.add(lastLRP);
    }

    public void setCreationTime(String creationTime){
        this.creationTime = creationTime;
    }

    public String getCreationTime(){
        return this.creationTime;
    }

    public void setTrafficType(String trafficType){
        this.trafficType = trafficType;
    }

    public String getTrafficType(){
        return this.trafficType;
    }

    public void setDelayTime (String delayTime){
        this.delayTime = delayTime;
    }

    public String getDelayTime(){
        return this.delayTime;
    }

    public void setAverageSpeed(String averageSpeed){
        this.averageSpeed = averageSpeed;
    }

    public String getAverageSpeed(){
        return this.averageSpeed;
    }

    public RawBinaryData getRawData(){
        return this.rawData;
    }

    public FirstLocationReferencePoint getFirstLRP(){
        return this.firstLRP;
    }

    public LastLocationReferencePoint getLastLRP(){
        return this.lastLRP;
    }

    public List<LocationReferencePointImpl> getIntermediates(){
        return this.intermediatePoints;
    }

    public List<LocationReferencePointImpl> getAllLRPs(){
        return this.lrps;
    }

    public String getDistance(){
        int dnp;
        double lowerBound;
        double upperBound;
        DecimalFormat formatter = new DecimalFormat("#0.00");
        dnp = ((FirstLRP)firstLRP.getLRP()).getAttrib3().getDnp();
        lowerBound = getLowerBoundDistance(dnp);
        upperBound = getUpperBoundDistance(dnp);
        for(LocationReferencePointImpl intermediate : intermediatePoints) {
            dnp = ((IntermediateLRP) intermediate.getLRP()).getAttrib3().getDnp();
            lowerBound += getLowerBoundDistance(dnp);
            upperBound += getUpperBoundDistance(dnp);
        }
        return "[" + formatter.format(lowerBound) + "m - " + formatter.format(upperBound) + "m]";
    }

    private double getLowerBoundDistance(int dnpValue){
        if (dnpValue >= 0 && dnpValue <= 255) {
            return ((double)((float)dnpValue * 58.6f));
        }
        return 0.0;
    }

    private double getUpperBoundDistance(int dnpValue){
        if (dnpValue >= 0 && dnpValue <= 255) {
            return ((double)((float)(dnpValue + 1) * 58.6f));
        }
        return 0.0;
    }

    public boolean hasGeoData(){
        return rawData != null;
    }

    public Shape getShape(){
        return shape;
    }

    public void setShape(Shape s){
        shape = shape;
    }

    @Override
    public String toString(){
        String result = "";
        result += "TrafficType:" + trafficType +"\n";
        result += "Average Speed:" + averageSpeed +"\n";
        result += "Delay Time:" + delayTime +"\n";
        result += "First LRP: \n" + firstLRP + "\n";
        for(LocationReferencePointImpl point : intermediatePoints)
            result += "Intermediate LRP: \n" + point+ "\n";
        result += "Last LRP: \n" + lastLRP + "\n";
        return result;
    }
}
