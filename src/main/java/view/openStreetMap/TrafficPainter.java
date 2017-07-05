package view.openStreetMap;

import io.RoutingClient;
import io.TrafficClient;
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
    protected final Color HEAVY_TRAFFIC = new Color(204,0,0, 128);
    protected final Color FREE_TRAFFIC = new Color(100,221,23, 128);
    protected final Color UNKNOWN = new Color(117,117,117, 128);
    protected final int WIDTH_FIRST = 4;
    protected final int WIDTH_SECOND = 2;

    public TrafficPainter(Traffic traffic){
        this.traffic = traffic;
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
