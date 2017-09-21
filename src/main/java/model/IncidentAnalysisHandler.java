package model;

import model.traffic.IncidentAnalysis;
import model.traffic.IncidentAnalysisElemImpl;
import model.traffic.TrafficIncident;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Florian Noack on 21.09.2017.
 */
public class IncidentAnalysisHandler extends OpenLRAnalysisHandler{

    private Date startDate;
    private Date endDate;
    private List<TrafficIncident> analysisList;

    public IncidentAnalysisHandler(List<File> files, Date start, Date end){
        super(files);
        startDate = start;
        endDate = end;
        analysisList = new ArrayList<TrafficIncident>();
    }

    protected boolean isWithinRange(Date testDate) {
        return !(testDate.before(startDate) || testDate.after(endDate));
    }

    public void setDateIntervall(Date d1, Date d2){
        startDate = d1;
        endDate = d2;
    }

    @Override
    public void process(){
        OpenLRXMLHandler handler = new OpenLRXMLHandler();
        for(File f : protoFiles){
            handler.setData(f);
            handler.process();
            for(TrafficIncident i : handler.getIncidents()){
                if (isWithinRange(i.getRecordCreationTime()))
                    analysisList.add(i);
            }


        }
    }
}
