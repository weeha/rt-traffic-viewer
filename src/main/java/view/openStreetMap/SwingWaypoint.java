package view.openStreetMap;

/**
 * Created by Florian Noack on 18.04.2017.
 */

import app.DetailController;
import app.MainController;
import javafx.application.Platform;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import view.DetailDialog;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class SwingWaypoint extends DefaultWaypoint {

    private final JButton button;
    private final GeoPosition coord;


    public SwingWaypoint(GeoPosition coord, ImageIcon icon) {

        super(coord);
        this.coord = coord;
        if(icon != null)
            button = new JButton(icon);
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

    private class SwingWaypointMouseListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {

            System.out.println(coord);
            if(MainController.stackPaneHolder != null){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        DetailDialog diag = new DetailDialog(MainController.stackPaneHolder, coord);
                        diag.show();
                    }
                });

            }
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