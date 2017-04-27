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
import model.location.CoordinateValue;
import openlr.binary.data.FirstLRP;
import openlr.binary.data.RawBinaryData;
import org.jxmapviewer.viewer.*;
import view.DetailDialog;
import view.TrafficViewer;
import view.openStreetMap.SwingWaypoint;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URL;
import java.util.*;

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

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources){
        this.createAndSetSwingContent(mapHolder);
        this.createOptionsList();
        stackPaneHolder = root;
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
            }
        });
    }

    private static void showFileChooser(){
        FileLoader loader = new FileLoader();
        loader.startFileChooser();

        OpenLRFileHandler handler = new OpenLRFileHandler(loader.getDataFile());
        Set<SwingWaypoint> waypoints = new HashSet<SwingWaypoint>();
        handler.process();

        for(RawBinaryData d: handler.getLocationData()){
            try {
                FirstLRP lrp = d.getBinaryFirstLRP();
                CoordinateValue val = new CoordinateValue(lrp.getLon(), lrp.getLat());
                GeoPosition geoPos = new GeoPosition(val.getLatDeg(), val.getLonDeg());
                //Point2D marker = mapViewer.convertGeoPositionToPoint(geoPos);

                // Create a waypoint painter that takes all the waypoints
                mapViewer.addWaypoint(new SwingWaypoint(geoPos, null));
            }catch(NullPointerException ne){}
        }
        mapViewer.showWaipoints();

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
