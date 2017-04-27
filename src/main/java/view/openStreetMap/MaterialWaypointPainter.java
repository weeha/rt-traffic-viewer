package view.openStreetMap;

import javafx.scene.control.*;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by Florian Noack on 27.04.2017.
 */
public class MaterialWaypointPainter extends WaypointPainter<MaterialWaypoint> {

    @Override
    protected void doPaint(Graphics2D g, JXMapViewer jxMapViewer, int width, int height) {
        for (MaterialWaypoint waypoint : getWaypoints()) {
            Point2D point = jxMapViewer.getTileFactory().geoToPixel(
                    waypoint.getPosition(), jxMapViewer.getZoom());
            Rectangle rectangle = jxMapViewer.getViewportBounds();
            int buttonX = (int)(point.getX() - rectangle.getX());
            int buttonY = (int)(point.getY() - rectangle.getY());
            javafx.scene.control.Button button = waypoint.getButton();
            button.setLayoutX(buttonX - button.getWidth() / 2);
            button.setLayoutY(buttonY - button.getHeight() / 2);
        }
    }
}
