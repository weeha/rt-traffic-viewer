package model.traffic;

import java.util.Date;
import java.util.List;

/**
 * Created by Florian Noack on 16.08.2017.
 */
public class FlowAnalysis extends TrafficAnalysis{

    private int travelTime;
    private int averageSpeed;
    private double relativeSpeed;
    private String trafficCondition;
    private int confidence;

    public FlowAnalysis(Date date){
        super(date);
    }

    public FlowAnalysis(Date date, int tTime, int aSpeed, double rSpeed, String tCond, int conf){
        super(date);
        travelTime = tTime;
        averageSpeed = aSpeed;
        relativeSpeed = rSpeed;
        trafficCondition = tCond;
        confidence = conf;
    }

    public int getTravelTime(){
        return travelTime;
    }

    public void setTravelTime(int t){
        travelTime = t;
    }

    public int getAverageSpeed(){
        return travelTime;
    }

    public void setAverageSpeed(int s){
        averageSpeed = s;
    }

    public void setRelativeSpeed(double s){
        relativeSpeed = s;
    }

    public double getRelativeSpeed(){
        return relativeSpeed;
    }

    public void setConfidence(int c){
        confidence = c;
    }

    public int getConfidence(){
        return confidence;
    }

    public void setTrafficCondition(String c){
        trafficCondition = c;
    }

    public String getTrafficCondition(){
        return trafficCondition;
    }

    @Override
    public String toString(){
        String result = super.toString();
        return result;
    }

}
