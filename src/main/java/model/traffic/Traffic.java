package model.traffic;

import model.location.FirstLocationReferencePoint;
import model.location.ILocationReferencePoint;
import model.location.LastLocationReferencePoint;
import model.location.LocationReferencePointImpl;
import openlr.binary.data.AbstractLRP;
import openlr.binary.data.RawBinaryData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian Noack on 04.05.2017.
 */
public abstract class Traffic {

    private RawBinaryData rawData;
    private String rawString = "";
    private String creationTime="";
    private String trafficType="";
    private String averageSpeed="";
    private String delayTime="";
    private final String id;
    private FirstLocationReferencePoint firstLRP = null;
    private LastLocationReferencePoint lastLRP = null;
    private List<LocationReferencePointImpl> intermediatePoints;
    private List<LocationReferencePointImpl> lrps;

    public Traffic(String id){
        this.id = id;
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

    public void setRawAsString(String raw){
        rawString = raw;
    }

    public String getRawString(){
        return this.rawString;
    }

    public String getId(){
        return this.id;
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

    @Override
    public String toString(){
        String result = "";
        result += "ID: " + id + "\n";
        result += "RAW-Data: " + rawString + "\n";
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