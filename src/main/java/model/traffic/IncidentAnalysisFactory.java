package model.traffic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian Noack on 21.09.2017.
 */
public class IncidentAnalysisFactory {

    private final List<IncidentAnalysisElemImpl> analysisList;

    public IncidentAnalysisFactory(){
        analysisList = new ArrayList<IncidentAnalysisElemImpl>();
    }

    public void addIncidents(List<TrafficIncident> incidents){
        for(TrafficIncident i : incidents){
           IncidentAnalysis ia = new IncidentAnalysis(i.getRecordCreationTime());
        }
    }
}
