package app;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import io.FileLoader;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import model.OpenLRFileHandler;
import model.OpenLRXMLHandler;
import model.location.CoordinateValue;
import model.traffic.TrafficIncident;
import openlr.binary.data.FirstLRP;
import openlr.binary.data.RawBinaryData;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.*;
import org.jxmapviewer.viewer.*;
import view.DetailDialog;
import view.TrafficViewer;
import view.openStreetMap.SwingWaypoint;
import view.openStreetMap.TrafficPainter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Painter;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class MainController implements Initializable {

    @FXML
    private SwingNode mapHolder;
    @FXML
    private StackPane optionsBurger;
    @FXML
    private StackPane root;

    public static StackPane stackPaneHolder;
    private JFXPopup toolbarPopup;

    private static TrafficViewer mapViewer = null;
    private static Image icon;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources){
        this.createAndSetSwingContent(mapHolder);
        this.createOptionsList();
        stackPaneHolder = root;
        this.icon = loadIcon();
    }

    private void createOptionsList(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/options.fxml"));
        loader.setController(new InputController());
        Region reg = null;
        try {
            reg = loader.load();
        }catch(IOException ie){
            System.out.println("Can not load popup");
        }
        if(reg != null) {
            toolbarPopup = new JFXPopup(reg);
            optionsBurger.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    toolbarPopup.show(optionsBurger, JFXPopup.PopupVPosition.TOP
                            , JFXPopup.PopupHPosition.RIGHT
                            , -12
                            , 15);
                }
            });
        }
    }

    private Image loadIcon(){
        Image img = null;
        try {
            img = ImageIO.read(getClass().getResource("/png/ic_place_black_24dp.png"));
        }catch(IOException ie){
            System.out.println("Error loading icon");
        }
        return img;
    }

    private static void showDiag(){
        System.out.println(stackPaneHolder == null);
        DetailDialog diag = new DetailDialog(stackPaneHolder, null);
        diag.show();
    }

    private void createAndSetSwingContent(final SwingNode swingNode) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mapViewer = new TrafficViewer();
                swingNode.setContent(mapViewer);
                mapViewer.repaint();
            }
        });
    }

    private static void showFileChooser(){
        FileLoader loader = new FileLoader();
        loader.startFileChooser();
        OpenLRXMLHandler handler = new OpenLRXMLHandler(loader.getDataFile());

        Set<SwingWaypoint> waypoints = new HashSet<SwingWaypoint>();
        handler.process();
        List<org.jxmapviewer.painter.Painter<JXMapViewer>> painters = new ArrayList<org.jxmapviewer.painter.Painter<JXMapViewer>>();
        for(TrafficIncident incident : handler.getIncidents()){
            System.out.println(incident);
            painters.add(new TrafficPainter(incident));
        }

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);
        /*
        for(RawBinaryData d: handler.getLocationData()){
            try {
                FirstLRP lrp = d.getBinaryFirstLRP();
                CoordinateValue val = new CoordinateValue(lrp.getLon(), lrp.getLat());
                GeoPosition geoPos = new GeoPosition(val.getLatDeg(), val.getLonDeg());
                //Point2D marker = mapViewer.convertGeoPositionToPoint(geoPos);

                // Create a waypoint painter that takes all the waypoints
                mapViewer.addWaypoint(new SwingWaypoint(geoPos, icon));
            }catch(NullPointerException ne){}
        }
        mapViewer.showWaipoints();
        */
    }

    public static final class InputController {
        @FXML
        private JFXListView<?> toolbarPopupList;

        @FXML
        private void submit(){
            switch(toolbarPopupList.getSelectionModel().getSelectedIndex()){
                case 0:
                    showFileChooser();
                    // make popup disappear after selection
                    toolbarPopupList.setExpanded(false);
                    break;
                case 1:
                    Platform.exit();
                    break;
                default:
                    break;
            }
        }
    }
}
