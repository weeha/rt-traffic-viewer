package model.traffic;

import java.util.Date;

/**
 * Created by fnoack on 16.08.2017.
 */
public class IncidentAnalysis extends TrafficAnalysis{

    private int delayTime;
    private int averageSpeed;
    private String trafficType = null;

    public IncidentAnalysis(Date date){
        super(date);
    }

    public void setAverageSpeed(int speed){
        averageSpeed = speed;
    }

    public int getAverageSpeed(){
        return averageSpeed;
    }

    public void setDelayTime(int time){
        delayTime = time;
    }

    public void setTrafficType(String type){
        trafficType = type;
    }

    public String getTrafficType(){
        return trafficType == null ? "N/A" : trafficType;
    }
}
