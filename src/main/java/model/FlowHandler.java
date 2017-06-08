package model;

import model.traffic.TrafficFlow;

import javax.xml.stream.XMLInputFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian Noack on 6/8/2017.
 */
public class FlowHandler extends OpenLRFileHandler{

    protected List<TrafficFlow> flows;

    public FlowHandler(){
        flows = new ArrayList<TrafficFlow>();
    }

    public List<TrafficFlow> getFlows(){
        return flows;
    }
}
