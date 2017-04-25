package app;

import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private SwingNode mapHolder;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources){
        this.createAndSetSwingContent(mapHolder);
    }

    private void createAndSetSwingContent(final SwingNode swingNode) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JXMapViewer mapViewer = new JXMapViewer();

                // Create a TileFactoryInfo for OpenStreetMap
                TileFactoryInfo info = new OSMTileFactoryInfo();
                DefaultTileFactory tileFactory = new DefaultTileFactory(info);
                mapViewer.setTileFactory(tileFactory);

                // Use 8 threads in parallel to load the tiles
                tileFactory.setThreadPoolSize(8);

                // Set a first focus for startup
                GeoPosition tum_campus = new GeoPosition(48.1486, 11.5687);

                mapViewer.setZoom(7);
                mapViewer.setAddressLocation(tum_campus);

                MouseInputListener mia = new PanMouseInputListener(mapViewer);
                mapViewer.addMouseListener(mia);
                mapViewer.addMouseMotionListener(mia);
                mapViewer.addMouseListener(new CenterMapListener(mapViewer));
                mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
                mapViewer.addKeyListener(new PanKeyListener(mapViewer));

                swingNode.setContent(mapViewer);
            }
        });
    }
}
