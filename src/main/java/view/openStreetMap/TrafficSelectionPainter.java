package view.openStreetMap;

import model.location.LocationReferencePointImpl;
import model.traffic.Traffic;
import model.traffic.TrafficFlow;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by Florian Noack on 04.07.2017.
 */
public class TrafficSelectionPainter extends TrafficPainter{

    protected final int WIDTH_FIRST = 6;
    protected final int WIDTH_SECOND = 3;

    public TrafficSelectionPainter(Traffic traffic){
        super(traffic);
        traffic.calculateRoute();
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int w, int h){
        Color color = UNKNOWN;
        if(traffic instanceof TrafficFlow){
            if(traffic instanceof TrafficFlow){
                if(((TrafficFlow) traffic).getRelativeSpeedValue() != null){
                    color = getFlowColor();
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
        g.setStroke(new BasicStroke(WIDTH_FIRST));
        drawRoute(g, map);
        // do the drawing again

        g.setColor(color);
        g.setStroke(new BasicStroke(WIDTH_SECOND));
        drawRoute(g, map);
        g.dispose();
    }

    @Override
    protected void drawRoute(Graphics2D g, JXMapViewer map){

        int lastX = 0;
        int lastY = 0;
        boolean first = true;
        if(traffic.getRoutingInformation() != null) {
            for (GeoPosition pos : traffic.getRoutingInformation()) {
                Point2D pt = map.getTileFactory().geoToPixel(pos, map.getZoom());
                if (first) {
                    first = false;
                } else {
                    g.drawLine(lastX, lastY, (int) pt.getX(), (int) pt.getY());
                }
                lastX = (int) pt.getX();
                lastY = (int) pt.getY();
            }
        }else{
            super.drawRoute(g, map);
        }
    }
}
