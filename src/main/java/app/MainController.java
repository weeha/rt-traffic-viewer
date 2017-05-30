package app;

import com.jfoenix.controls.*;
import io.FileLoader;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import model.OpenLRFileHandler;
import model.OpenLRProtoHandler;
import model.OpenLRXMLFlowHandler;
import model.OpenLRXMLHandler;
import model.traffic.TrafficFlow;
import model.traffic.TrafficIncident;
import view.TrafficViewer;
import view.openStreetMap.SwingWaypoint;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
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
    @FXML
    private StackPane sideMenu;

    private static final String INCIDENT_TAB = "Incidents";
    private static final String FLOW_TAB = "Traffic Flow";

    public static StackPane stackPaneHolder;
    private JFXPopup toolbarPopup;
    private static TrafficViewer mapViewer = null;
    private static Image icon;
    private Tab incidents = null;
    private Tab flows = null;
    private JFXTabPane tabPane = null;
    private int selectedTab = 1;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources){
        this.createAndSetSwingContent(mapHolder);
        this.createOptionsList();
        stackPaneHolder = root;
        this.icon = loadIcon();
        this.setSidePanelContent();
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

    private void setSidePanelContent(){
        if(sideMenu != null){
            tabPane = new JFXTabPane();
            incidents = new Tab();
            incidents.setText(INCIDENT_TAB);
            /*
            Workaround to get selected Tab.
            Somehow JFoenix-API hasn't implemented this right.
             */
            incidents.setOnSelectionChanged(new EventHandler<javafx.event.Event>() {
                @Override
                public void handle(Event event) {
                    switch(selectedTab){
                        case 0:
                            selectedTab = 1;
                            mapViewer.showTrafficFlow();
                            break;
                        case 1:
                            selectedTab = 0;
                            mapViewer.showTrafficIncidents();
                            break;
                        default:
                            System.out.println("Unknown Tab selected");
                    }
                }
            });

            Label noIncidentDataLabel = new Label("Please load traffic incident data!");
            noIncidentDataLabel.setTextFill(Color.web("#FFFFFF"));
            incidents.setContent(noIncidentDataLabel);
            tabPane.getTabs().add(incidents);
            tabPane.setPrefSize(300, 200);
            flows = new Tab();
            flows.setText(FLOW_TAB);
            Label noFlowDataLabel = new Label("Please load traffic flow data!");
            noFlowDataLabel.setTextFill(Color.web("#FFFFFF"));
            flows.setContent(noFlowDataLabel);
            tabPane.getTabs().add(flows);
            tabPane.setPrefWidth(sideMenu.getWidth());
            sideMenu.getChildren().add(tabPane);
        }
    }

    private static void showFileChooser(){
        FileLoader loader = new FileLoader();
        OpenLRFileHandler handler;
        loader.startFileChooser();
        if(loader.getDataFormat().equals("xml")){
            // now we have to discern between flow and incidents
            if(!loader.isFlowFile()) {
                handler = new OpenLRXMLHandler(loader.getDataFile());
                handler.process();
                mapViewer.resetIncidents();
                for (TrafficIncident incident : ((OpenLRXMLHandler) handler).getIncidents()) {
                    mapViewer.addTrafficIncident(incident);
                    mapViewer.addWaypoint(new SwingWaypoint(incident, icon));
                }
                mapViewer.showTrafficIncidents();
                return;
            }
            else{
                handler = new OpenLRXMLFlowHandler(loader.getDataFile());
                handler.process();
                mapViewer.resetFlows();
                for (TrafficFlow flow : ((OpenLRXMLFlowHandler) handler).getFlows()) {
                    mapViewer.addTrafficFlow(flow);
                }
                mapViewer.showTrafficFlow();
                return;
                }
        }else if(loader.getDataFormat().equals("proto")){
            handler = new OpenLRProtoHandler(loader.getDataFile());
            handler.process();
            mapViewer.resetFlows();
            for(TrafficFlow flow : ((OpenLRProtoHandler)handler).getFlows()){
                mapViewer.addTrafficFlow(flow);
            }
            return;
        }else{
            System.out.println("Unknown data format!");
        }

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
