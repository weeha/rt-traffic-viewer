package model.traffic;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fnoack on 21.09.2017.
 */
public class IncidentAnalysisElemImpl extends AnalysisElemImpl{

    private String rawString;

    public IncidentAnalysisElemImpl(TrafficIncident i){
        rawString = i.getRawString();
        this.setFirstLRP(i.getFirstLRP());
        this.setIntermediatePoints(i.getIntermediates());
        this.setLastLRP(i.getLastLRP());
    }
    public boolean matches(String rawBytes){
        return this.rawString.equals(rawBytes);
    }

    public Map<Double,Integer> getDelayTimeAnalysis(){
        Map<Double,Integer> count = new HashMap<Double,Integer>();
        for(TrafficAnalysis i : getTraffic()){
            Double value = ((IncidentAnalysis)i).getDelayTime();
            if(!count.containsKey(value)){
                count.put(value, 1);
            }else{
                count.put(value, count.get(value) + 1);
            }

        }
        return count;
    }

    public Map<Double,Integer> getAverageSpeedAnalysis(){
        Map<Double,Integer> count = new HashMap<Double,Integer>();
        for(TrafficAnalysis i : getTraffic()){
            Double value = ((IncidentAnalysis)i).getAverageSpeed();
            if(!count.containsKey(value)){
                count.put(value, 1);
            }else{
                count.put(value, count.get(value) + 1);
            }

        }
        return count;
    }

    public Map<String,Integer> getTrafficTypeAnalysis(){
        Map<String,Integer> count = new HashMap<String,Integer>();
        for(TrafficAnalysis i : getTraffic()){
            String value = ((IncidentAnalysis)i).getTrafficType();
            if(!count.containsKey(value)){
                count.put(value, 1);
            }else{
                count.put(value, count.get(value) + 1);
            }

        }
        return count;
    }
}
