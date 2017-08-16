package view;

import app.MainController;
import model.traffic.Traffic;
import model.traffic.TrafficFlow;
import model.traffic.TrafficIncident;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.*;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.*;
import view.openStreetMap.*;

import javax.swing.event.MouseInputListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Florian Noack on 27.04.2017.
 */
public class TrafficViewer extends JXMapViewer {

    private Set<SwingWaypoint> waypoints;
    private final GeoPosition tum_campus = new GeoPosition(48.1486, 11.5687);
    private List<Traffic> incidents;
    private List<Traffic> flows;
    private CompoundPainter<JXMapViewer> painter;
    private TrafficSelectionPainter selectionPainter;

    public TrafficViewer(){
        super();
        waypoints = new HashSet<SwingWaypoint>();
        incidents = new ArrayList<Traffic>();
        flows = new ArrayList<Traffic>();
        this.setTileFactoryHelper();
        this.setMouseListener();
        this.setZoom(7);
        this.setAddressLocation(tum_campus);
        //this.setSelectionViewer();
    }

    public void resetFlows(){
        flows = new ArrayList<Traffic>();
        removeButtons();
    }

    public void resetIncidents(){
        incidents = new ArrayList<Traffic>();
        removeButtons();
    }

    private void removeButtons(){
        for (SwingWaypoint w : waypoints) {
            this.remove(w.getButton());
        }
        waypoints = new HashSet<SwingWaypoint>();
    }

    public void addWaypoint(SwingWaypoint point){
        waypoints.add(point);
    }

    public void addTrafficIncident(TrafficIncident incident){
        if(incident instanceof TrafficIncident)
            incidents.add(incident);
    }

    public void addTrafficFlow(TrafficFlow flow){
        if(flow instanceof TrafficFlow)
            flows.add(flow);
    }

    public void showTrafficFlow(){
        paintRoutes(flows);
    }

    public void showTrafficIncidents(){
        paintRoutes(incidents);
    }

    private void paintRoutes(List<Traffic> routes){
        WaypointPainter<SwingWaypoint> swingWaypointPainter = new SwingWaypointOverlayPainter();
        swingWaypointPainter.setWaypoints(waypoints);
        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
        for(Traffic traffic : routes){
            painters.add(new TrafficPainter(traffic));
        }

        painter = new CompoundPainter<JXMapViewer>(painters);
        painter.addPainter(swingWaypointPainter);
        this.setOverlayPainter(painter);
        for (SwingWaypoint w : waypoints) {
            this.add(w.getButton());
        }
        this.repaint();
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
        this.repaint();
    }

    public void hideWaypoints(){
        //todo
    }

    private void setSelectionViewer(){
        // Add a selection painter

        SelectionAdapter sa = new SelectionAdapter(this);
        SelectionPainter sp = new SelectionPainter(sa);
        this.addMouseListener(sa);
        this.addMouseMotionListener(sa);
        this.setOverlayPainter(sp);
    }

    private void setMouseListener(){
        MouseInputListener mia = new TrafficMouseInputListener(this);
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

    public void highlightTraffic(Traffic traffic){
        System.out.println(MainController.selectionState.isSelected());
        for (Painter p : painter.getPainters()){
            if(p instanceof TrafficPainter){
                if(((TrafficPainter)p).getTraffic().equals(traffic)){
                    painter.removePainter(p);
                    selectionPainter = new TrafficSelectionPainter(traffic);
                    painter.addPainter(selectionPainter);
                }else{
                    if(MainController.selectionState.isSelected())
                        painter.removePainter(p);
                }
            }
        }
        //painter.removePainter(selectionPainter);
        //selectionPainter = new TrafficSelectionPainter(traffic);
        //painter.addPainter(selectionPainter);

        this.setOverlayPainter(painter);
        this.repaint();
        this.revalidate();
    }

}
