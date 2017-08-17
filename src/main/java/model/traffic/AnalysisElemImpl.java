package model.traffic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Florian Noack on 17.08.2017.
 */
public abstract class AnalysisElemImpl extends Traffic implements AnalysisElem {

    private List<TrafficAnalysis> trafficSituation;

    public AnalysisElemImpl(){
        super();
        trafficSituation = new ArrayList<TrafficAnalysis>();
    }

    @Override
    public List<TrafficAnalysis> getTraffic() {
        return trafficSituation;
    }

    @Override
    public List<TrafficAnalysis> getTraffic(Date start, Date end){
        List result = new ArrayList();
        for(TrafficAnalysis t : trafficSituation){
            if(t.getDate().after(start) && t.getDate().before(end))
                result.add(t);
        }
        return result;
    }

    public void addTrafficAnalysis(TrafficAnalysis analysis){
        trafficSituation.add(analysis);
    }
}
