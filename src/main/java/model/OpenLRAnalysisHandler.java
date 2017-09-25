package model;

import model.traffic.*;

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
    protected final List<File> protoFiles;
    protected AnalysisFactory factory;
    private Date startDate = null;
    private Date endDate = null;

    public OpenLRAnalysisHandler(List<File> files){
        super();
        protoFiles = files;
        analysisList = new ArrayList<AnalysisElemImpl>();
        if(this instanceof IncidentAnalysisHandler)
            factory = new IncidentAnalysisFactory();
        else
            factory = new AnalysisFactory();
    }

    public void setDateInterval(Date d1, Date d2){
        startDate = d1;
        endDate = d2;
    }

    public List<FlowAnalysisElemImpl> getAnalysisList(){
        return factory.getAnalysisList();
    }

    protected boolean isWithinRange(Date testDate) {
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
