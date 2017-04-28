package view;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.*;
import org.jxmapviewer.viewer.*;
import view.openStreetMap.SwingWaypoint;
import view.openStreetMap.SwingWaypointOverlayPainter;
import javax.swing.event.MouseInputListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Florian Noack on 27.04.2017.
 */
public class TrafficViewer extends JXMapViewer {

    private Set<SwingWaypoint> waypoints;
    private final GeoPosition tum_campus = new GeoPosition(48.1486, 11.5687);

    public TrafficViewer(){
        super();
        waypoints = new HashSet<SwingWaypoint>();
        this.setTileFactoryHelper();
        this.setMouseListener();
        this.setZoom(7);
        this.setAddressLocation(tum_campus);
    }

    public void addWaypoint(SwingWaypoint point){
        waypoints.add(point);
    }

    public void showWaipoints(){
        // Set the overlay painter
        WaypointPainter<SwingWaypoint> swingWaypointPainter = new SwingWaypointOverlayPainter();
        swingWaypointPainter.setWaypoints(waypoints);
        this.setOverlayPainter(swingWaypointPainter);
        // Add the Buttons to the view
        for (SwingWaypoint w : waypoints) {
            this.add(w.getButton());
        }
    }

    public void hideWaypoints(){
        //todo
    }

    private void setMouseListener(){
        MouseInputListener mia = new PanMouseInputListener(this);
        this.addMouseListener(mia);
        this.addMouseMotionListener(mia);
        this.addMouseListener(new CenterMapListener(this));
        this.addMouseWheelListener(new ZoomMouseWheelListenerCursor(this));
        this.addKeyListener(new PanKeyListener(this));
    }

    private void setTileFactoryHelper(){
        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        this.setTileFactory(tileFactory);

        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);
    }

}
