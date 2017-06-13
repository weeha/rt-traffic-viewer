package view.openStreetMap;

/**
 * Created by Florian Noack on 18.04.2017.
 */

import app.MainController;
import model.traffic.Traffic;
import model.traffic.TrafficFlow;
import model.traffic.TrafficIncident;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SwingWaypoint extends DefaultWaypoint {

    private final JButton button;
    private Traffic traffic;

    public SwingWaypoint(GeoPosition coord, Image icon) {

        super(coord);
        if(icon != null) {
            button = new JButton(new ImageIcon(icon));
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
        }
        else
            button = new JButton("");
        button.setSize(24, 24);
        button.setPreferredSize(new Dimension(24, 24));
        button.addMouseListener(new SwingWaypointMouseListener());
        button.setVisible(true);

    }

    public SwingWaypoint(Traffic traffic, Image icon){
        super(traffic.getFirstLRP().getGeoPosition());
        this.traffic = traffic;
        if(icon != null) {
            button = new JButton(new ImageIcon(icon));
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
        }
        else
            button = new JButton("");
        button.setSize(24, 24);
        button.setPreferredSize(new Dimension(24, 24));
        button.addMouseListener(new SwingWaypointMouseListener());
        button.setVisible(true);
    }

    public JButton getButton() {

        return button;

    }

    public Traffic getTraffic(){
        return this.traffic;
    }

    private class SwingWaypointMouseListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {

            if(traffic instanceof TrafficFlow)
                MainController.setSidePanelContent(1, traffic);
            else if(traffic instanceof TrafficIncident)
                MainController.setSidePanelContent(0, traffic);
        }

        public void mousePressed(MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {

        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {

        }

    }

}