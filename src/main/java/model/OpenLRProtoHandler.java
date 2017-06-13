package model;

import java.io.*;
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
public class OpenLRProtoHandler extends FlowHandler{


    public OpenLRProtoHandler(){
    }


    @Override
    public void process(){
        InputStream input = null;
        TrafficFlow tFlow;
        try {
            ProtobufTrafficFlowV5.TrafficFlowGroup tGroup = null;
            if(this.hasFile()) {
                input = new FileInputStream(getDataFile());
                tGroup = ProtobufTrafficFlowV5.TrafficFlowGroup.parseFrom(input);
            }else if(!this.hasFile() && getInputData() != null){
                tGroup = ProtobufTrafficFlowV5.TrafficFlowGroup
                        .parseFrom(((String) getInputData()).getBytes("ISO-8859-1"));
            }
            else {
                return;
            }
            for (ProtobufTrafficFlowV5.TrafficFlow flow : tGroup.getTrafficFlowList()) {
                tFlow = new TrafficFlow(flow);
                ByteArray bytes = new ByteArray(flow.getLocation().getOpenlr().toByteArray());
                try {
                    RawBinaryData raw = bDecoder.resolveBinaryData("", bytes);
                    tFlow.setRawData(raw);
                } catch (PhysicalFormatException pe) {
                    System.err.println("OpenLR decoding error");
                    System.out.println(pe);
                }
                if (tFlow.hasGeoData())
                    this.flows.add(tFlow);
            }
        }catch(IOException ie){
            System.out.println("Something went wrong while processing protocol buffer...");
            System.out.println(ie.getStackTrace());
        }finally{
            try {
                if (getDataFile() != null)
                    input.close();
            }catch(IOException oe){}
        }

    }
}
