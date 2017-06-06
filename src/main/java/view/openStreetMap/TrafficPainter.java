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

    private Traffic traffic;
    private boolean antiAlias = true;
    private final Color HEAVY_TRAFFIC = new Color(204,0,0, 128);
    private final Color FREE_TRAFFIC = new Color(100,221,23, 128);
    private final Color UNKNOWN = new Color(117,117,117, 128);

    public TrafficPainter(Traffic traffic){
        this.traffic = traffic;
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int w, int h){
        Color color = UNKNOWN;

        g = (Graphics2D) g.create();
        // convert from viewport to world bitmap
        Rectangle rect = map.getViewportBounds();
        g.translate(-rect.x, -rect.y);
        if (antiAlias)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // do the drawing
        g.setColor(color);
        g.setStroke(new BasicStroke(4));
        drawRoute(g, map);
        // do the drawing again

        g.setColor(color);
        g.setStroke(new BasicStroke(2));
        drawRoute(g, map);
        g.dispose();

    }



    /**
     * @param g the graphics object
     * @param map the map
     */

    private void drawRoute(Graphics2D g, JXMapViewer map){

        int lastX = 0;
        int lastY = 0;
        boolean first = true;
        //Point2D pt = map.getTileFactory().geoToPixel(traffic.getFirstLRP().getGeoPosition(), map.getZoom());
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
