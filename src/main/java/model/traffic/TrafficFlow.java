package model.traffic;

/**
 * Created by Florian Noack on 27.05.2017.
 */
public class TrafficFlow extends Traffic{

    private boolean roadClosure;
    private ProtobufTrafficFlowV5.TrafficFlow pFLow;

    public TrafficFlow(){
        super();
    }

    public TrafficFlow(ProtobufTrafficFlowV5.TrafficFlow pFLow){
        this.pFLow = pFLow;
    }

    public void setRoadClosed(boolean closed){
        roadClosure = closed;
    }

    public boolean isRoadCosed(){
        return roadClosure;
    }
}
