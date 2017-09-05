package view.Analysis;

import javafx.scene.chart.*;
import model.traffic.TrafficAnalysis;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Florian Noack on 05.09.2017.
 */
public abstract class AnalysisChart<X,Y> extends LineChart{

    protected XYChart.Series series;
    protected final SimpleDateFormat ft =
            new SimpleDateFormat("hh:mm");

    public AnalysisChart() {
        super(new CategoryAxis(), new NumberAxis());
        this.getXAxis().setLabel("Time [hh:mm]");
        series = new XYChart.Series();
    }

    public void setYAxisLabel(String label){
        this.getYAxis().setLabel(label);
    }

    public abstract void setTrafficAnalysis(List<TrafficAnalysis> aList);
}
