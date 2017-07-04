package view.openStreetMap;

import model.traffic.Traffic;

/**
 * Created by Florian Noack on 04.07.2017.
 */
public class TrafficSelectionPainter extends TrafficPainter{

    protected final int WIDTH_FIRST = 15;
    protected final int WIDTH_SECOND = 10;

    public TrafficSelectionPainter(Traffic traffic){
        super(traffic);
    }
}
