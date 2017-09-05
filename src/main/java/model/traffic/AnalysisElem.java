package model.traffic;

import java.util.Date;
import java.util.List;

/**
 * Created by fnoack on 17.08.2017.
 */
public interface AnalysisElem {

    List<TrafficAnalysis> getTraffic();
    List<TrafficAnalysis> getTraffic(Date start, Date end);
}
