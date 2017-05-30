package model.traffic;

/**
 * Created by Florian Noack on 27.05.2017.
 */
public class TrafficFlow extends Traffic{

    private boolean roadClosure;
    private String travelTime = null;
    private String relativeSpeed = null;
    private String trafficCondition = null;
    private ProtobufTrafficFlowV5.TrafficFlow pFLow;

    public TrafficFlow(){
        super();
    }

    public TrafficFlow(ProtobufTrafficFlowV5.TrafficFlow pFLow){
        this.pFLow = pFLow;
    }

    public void setTravelTime(String travelTime){
        this.travelTime = travelTime;
    }

    public String getTravelTime(){
        return travelTime;
    }

    public void setRelativeSpeed(String relSpeed){
        relativeSpeed = relSpeed;
    }

    public String getRelativeSpeed(){
        return relativeSpeed;
    }

    public void setTrafficCondition(String cond){
        trafficCondition = cond;
    }

    public String getTrafficCondition(){
        return trafficCondition;
    }

    public void setRoadClosed(boolean closed){
        roadClosure = closed;
    }

    public boolean isRoadCosed(){
        return roadClosure;
    }
}
