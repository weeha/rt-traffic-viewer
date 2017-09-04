package model;

import model.traffic.AnalysisFactory;
import model.traffic.AnalysisElemImpl;
import model.traffic.FlowAnalysisElemImpl;
import model.traffic.ProtobufTrafficFlowV5;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fnoack on 17.08.2017.
 */
public class OpenLRAnalysisHandler extends OpenLRFileHandler{

    private final List<AnalysisElemImpl> analysisList;
    private final List<File> protoFiles;
    private AnalysisFactory factory;
    private Date startDate = null;
    private Date endDate = null;

    public OpenLRAnalysisHandler(List<File> files){
        super();
        protoFiles = files;
        analysisList = new ArrayList<AnalysisElemImpl>();
        factory = new AnalysisFactory();
    }

    public void setDateIntervall(Date d1, Date d2){
        startDate = d1;
        endDate = d2;
    }

    public void setProtoFiles(List<File> files){

    }

    public List<FlowAnalysisElemImpl> getAnalysisList(){
        return factory.getAnalysisList();
    }

    private boolean isWithinRange(Date testDate) {
        return !(testDate.before(startDate) || testDate.after(endDate));
    }

    @Override
    public void process(){
        InputStream input = null;
        ProtobufTrafficFlowV5.TrafficFlowGroup tGroup = null;

        for(File f : protoFiles){
            try {
                input = new FileInputStream(f);
                tGroup = ProtobufTrafficFlowV5.TrafficFlowGroup.parseFrom(input);
                Date trafficDate = (new Date((long)tGroup.getMetaInformation().getCreateTimeUTCSeconds()*1000));
                if(startDate != null && endDate != null){
                    if(!isWithinRange(trafficDate)) {
                        try {
                            if(input != null)
                                input.close();
                        }catch(IOException oe){
                            oe.printStackTrace();
                        }
                        continue;
                    }
                }
                System.out.println(trafficDate);
                factory.addTrafficFlowGroup(tGroup);
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                try {
                    if(input != null)
                        input.close();
                }catch(IOException oe){
                    oe.printStackTrace();
                }
            }
        }
        System.out.println("END");
    }
}
