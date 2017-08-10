package view.openStreetMap;

import model.location.LocationReferencePointImpl;
import model.traffic.Traffic;
import model.traffic.TrafficFlow;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by Florian Noack on 04.05.2017.
 */
public class TrafficPainter implements Painter<JXMapViewer> {

    protected Traffic traffic;
    protected boolean antiAlias = true;
    protected final Color TRAFFIC_RED = new Color(204,0,0, 128);
    protected final Color TRAFFIC_DARK_GREEN = new Color(46,125,50, 128);
    protected final Color TRAFFIC_LIGHT_GREEN = new Color(100,221,23, 128);
    protected final Color TRAFFIC_YELLOW = new Color(255,235,59, 128);
    protected final Color TRAFFIC_ORANGE = new Color(255,152,0, 128);
    protected final Color UNKNOWN = new Color(117,117,117, 128);
    protected final int WIDTH_FIRST = 4;
    protected final int WIDTH_SECOND = 2;

    public TrafficPainter(Traffic traffic){
        this.traffic = traffic;
    }

    public Traffic getTraffic(){
        return this.traffic;
    }

    protected Color getFlowColor(){
        if(traffic instanceof TrafficFlow){
            if(((TrafficFlow) traffic).getRelativeSpeedValue() != null){
                float rSpeed = ((TrafficFlow) traffic).getRelativeSpeedValue();
                if(rSpeed >= 0.8 && rSpeed <= 1.0)
                    return TRAFFIC_DARK_GREEN;
                else if(rSpeed >= 0.6 && rSpeed < 0.8)
                    return TRAFFIC_LIGHT_GREEN;
                else if(rSpeed >= 0.4 && rSpeed < 0.6)
                    return TRAFFIC_YELLOW;
                else if(rSpeed >= 0.2 && rSpeed < 0.4)
                    return TRAFFIC_ORANGE;
                else if(rSpeed >= 0.0 && rSpeed  < 0.2)
                    return TRAFFIC_RED;
                else
                    return UNKNOWN;

            }
        }
        return UNKNOWN;
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int w, int h){
        Color color = UNKNOWN;
        if(traffic instanceof TrafficFlow){
            if(((TrafficFlow) traffic).getRelativeSpeedValue() != null){
                color = getFlowColor();
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



    /**
     * @param g the graphics object
     * @param map the map
     */

    protected void drawRoute(Graphics2D g, JXMapViewer map){

        int lastX = 0;
        int lastY = 0;
        boolean first = true;

        for(LocationReferencePointImpl point : traffic.getAllLRPs()){
            Point2D pt = map.getTileFactory().geoToPixel(point.getGeoPosition(), map.getZoom());
            if (first){
                first = false;
            }
            else{
                g.drawLine(lastX, lastY, (int) pt.getX(), (int) pt.getY());
            }
            lastX = (int) pt.getX();
            lastY = (int) pt.getY();
        }
    }
}
