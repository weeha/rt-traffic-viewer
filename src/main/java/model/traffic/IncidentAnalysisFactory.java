package model.traffic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian Noack on 21.09.2017.
 */
public class IncidentAnalysisFactory extends AnalysisFactory{

    private final List<IncidentAnalysisElemImpl> analysisList;

    public IncidentAnalysisFactory(){
        analysisList = new ArrayList<IncidentAnalysisElemImpl>();
    }

    public void addIncidents(List<TrafficIncident> incidents){
        for(TrafficIncident i : incidents){
           IncidentAnalysis ia = new IncidentAnalysis(i.getRecordCreationTime());
           try {
               ia.setAverageSpeed((i.getAverageSpeed().equals("")) ? null : Double.parseDouble(i.getAverageSpeed()));
               ia.setDelayTime(i.getDelayTime().equals("") ? null : Double.parseDouble(i.getDelayTime()));
               ia.setTrafficType(i.getTrafficType());
               int index = getUpdateIndex(i.getRawString());
               if (index != -1) {
                   analysisList.get(index).addTrafficAnalysis(ia);
               } else {
                   IncidentAnalysisElemImpl iAnalysis = new IncidentAnalysisElemImpl(i);
                   iAnalysis.addTrafficAnalysis(ia);
                   iAnalysis.setRawAsString(i.getRawString());
                   analysisList.add(iAnalysis);
               }
           }catch(NullPointerException ne){

           }
           }
    }

    public List<IncidentAnalysisElemImpl> getIncidentAnalysisList(){
        return analysisList;
    }

    private int getUpdateIndex(String raw){
        for(IncidentAnalysisElemImpl a : analysisList){
            if(a.matches(raw)){
                System.out.println(a.getTraffic().size());
                return analysisList.indexOf(a);
            }
        }
        return -1;
    }
}
