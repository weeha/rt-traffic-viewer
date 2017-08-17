package model;

import model.traffic.AnalysisFactory;
import model.traffic.AnalysisElemImpl;
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

    public OpenLRAnalysisHandler(List<File> files){
        super();
        protoFiles = files;
        analysisList = new ArrayList<AnalysisElemImpl>();
        factory = new AnalysisFactory();
    }

    public void setProtoFiles(List<File> files){

    }

    @Override
    public void process(){
        InputStream input = null;
        ProtobufTrafficFlowV5.TrafficFlowGroup tGroup = null;

        for(File f : protoFiles){
            try {
                input = new FileInputStream(f);
                tGroup = ProtobufTrafficFlowV5.TrafficFlowGroup.parseFrom(input);
                System.out.println(new Date((long)tGroup.getMetaInformation().getCreateTimeUTCSeconds()*1000));
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
