package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.traffic.ProtobufTrafficFlowV5;
import model.traffic.TrafficFlow;
import openlr.PhysicalFormatException;
import openlr.binary.ByteArray;
import openlr.binary.data.RawBinaryData;

/**
 * Created by Florian Noack on 27.05.2017.
 */
public class OpenLRProtoHandler extends OpenLRFileHandler{

    private List<TrafficFlow> flows;

    public OpenLRProtoHandler(File file){
        super(file);
        flows = new ArrayList<TrafficFlow>();
    }

    public List<TrafficFlow> getFlows(){
        return flows;
    }

    @Override
    public void process(){
        FileInputStream input = null;
        TrafficFlow tFlow = null;
        try {
            input = new FileInputStream(file);
            ProtobufTrafficFlowV5.TrafficFlowGroup tGroup =
                    ProtobufTrafficFlowV5.TrafficFlowGroup.parseFrom(input);
            for(ProtobufTrafficFlowV5.TrafficFlow flow : tGroup.getTrafficFlowList()){
                tFlow = new TrafficFlow();
                ByteArray bytes = new ByteArray(flow.getLocation().getOpenlr().toByteArray());
                try {
                    RawBinaryData raw = bDecoder.resolveBinaryData("", bytes);
                    tFlow.setRawData(raw);
                }catch(PhysicalFormatException pe){
                    System.out.println(pe);
                }
                if(tFlow.hasGeoData())
                    this.flows.add(tFlow);
            }
        }catch(IOException ie){
            System.out.println("Something went wrong while processing protocol buffer...");
        }finally{
            try {
                if (file != null)
                    input.close();
            }catch(IOException oe){}
        }

    }
}
