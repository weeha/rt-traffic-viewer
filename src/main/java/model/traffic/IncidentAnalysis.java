package model.traffic;

import java.util.Date;

/**
 * Created by fnoack on 16.08.2017.
 */
public class IncidentAnalysis extends TrafficAnalysis{

    private Double delayTime = null;
    private Double averageSpeed = null;
    private String trafficType = null;

    public IncidentAnalysis(Date date){
        super(date);
    }

    public void setAverageSpeed(double speed){
        averageSpeed = speed;
    }

    public double getAverageSpeed(){
        return averageSpeed;
    }

    public void setDelayTime(double time){
        delayTime = time;
    }

    public double getDelayTime(){
        return delayTime;
    }

    public void setTrafficType(String type){
        trafficType = type;
    }

    public String getTrafficType(){
        return trafficType == null ? "N/A" : trafficType;
    }
}
