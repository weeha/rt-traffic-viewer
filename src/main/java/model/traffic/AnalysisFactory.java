package model.traffic;

import openlr.PhysicalFormatException;
import openlr.binary.ByteArray;
import openlr.binary.OpenLRBinaryDecoder;
import openlr.binary.data.RawBinaryData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Florian Noack on 17.08.2017.
 */
public class AnalysisFactory {

    private final List<FlowAnalysisElemImpl> analysisList;
    private final OpenLRBinaryDecoder bDecoder;

    public AnalysisFactory(){
        analysisList = new ArrayList<FlowAnalysisElemImpl>();
        bDecoder = new OpenLRBinaryDecoder();
    }

    public void addTrafficFlowGroup(ProtobufTrafficFlowV5.TrafficFlowGroup tGroup){
        Date d = new Date((long)tGroup.getMetaInformation().getCreateTimeUTCSeconds()*1000);
        for (ProtobufTrafficFlowV5.TrafficFlow flow : tGroup.getTrafficFlowList()) {
            try {
                ByteArray bytes = new ByteArray(flow.getLocation().getOpenlr().toByteArray());
                RawBinaryData raw = bDecoder.resolveBinaryData("", bytes);
                int index = getUpdateIndex(flow.getLocation().getOpenlr());
                FlowAnalysis fa = new FlowAnalysis(d);
                fa.setAverageSpeed(flow.getSpeed().getAverageSpeedKmph());
                fa.setConfidence(flow.getSpeed().getConfidence());
                fa.setRelativeSpeed(flow.getSpeed().getRelativeSpeed());
                fa.setTrafficCondition(flow.getSpeed().getTrafficCondition().toString());
                fa.setTravelTime(flow.getSpeed().getTravelTimeSeconds());
                if(index != -1){
                    analysisList.get(index).addTrafficAnalysis(fa);
                }else{
                    FlowAnalysisElemImpl fAnalysis = new FlowAnalysisElemImpl(flow);
                    fAnalysis.setRawData(raw);
                    fAnalysis.addTrafficAnalysis(fa);
                    analysisList.add(fAnalysis);
                }
            } catch (PhysicalFormatException pe) {
                System.err.println("OpenLR decoding error");
                System.out.println(pe);
            }
        }
        System.out.println(analysisList.size());
    }

    private int getUpdateIndex(com.google.protobuf.ByteString raw){
        for(FlowAnalysisElemImpl a : analysisList){
            if(a.matches(raw)){
                return analysisList.indexOf(a);
            }
        }
        return -1;
    }



}
