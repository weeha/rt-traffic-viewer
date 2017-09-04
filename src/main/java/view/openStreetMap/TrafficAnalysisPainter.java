package view.openStreetMap;

import model.traffic.Traffic;

import java.awt.*;

/**
 * Created by Florian Noack on 04.09.2017.
 */
public class TrafficAnalysisPainter extends TrafficSelectionPainter{

    public TrafficAnalysisPainter(Traffic traffic){
        super(traffic);
        this.color = new Color(0,150,136, 128);
    }
}
