package view.openStreetMap;

import model.traffic.Traffic;
import model.traffic.TrafficFlow;
import org.jxmapviewer.JXMapViewer;

import java.awt.*;

/**
 * Created by Florian Noack on 04.07.2017.
 */
public class TrafficSelectionPainter extends TrafficPainter{

    protected final int WIDTH_FIRST = 15;
    protected final int WIDTH_SECOND = 10;

    public TrafficSelectionPainter(Traffic traffic){
        super(traffic);
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int w, int h){
        Color color = UNKNOWN;
        if(traffic instanceof TrafficFlow){
            if(((TrafficFlow) traffic).getTrafficCondition() != null){
                if(((TrafficFlow) traffic).getTrafficCondition().equals("FREE_FLOW")){
                    color = FREE_TRAFFIC;
                }
                else{
                    color = HEAVY_TRAFFIC;
                }
            }
        }


        g = (Graphics2D) g.create();
        // convert from viewport to world bitmap
        Rectangle rect = map.getViewportBounds();
        traffic.setShape(rect);
        g.translate(-rect.x, -rect.y);
        if (antiAlias)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // do the drawing
        g.setColor(color);
        g.setStroke(new BasicStroke(15));
        drawRoute(g, map);
        // do the drawing again

        g.setColor(color);
        g.setStroke(new BasicStroke(10));
        drawRoute(g, map);
        g.dispose();
    }
}
