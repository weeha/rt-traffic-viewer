package app;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import io.FileLoader;
import io.FlowClient;
import io.IncidentClient;
import io.TrafficClient;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.event.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
import java.text.MessageFormat;
import java.util.*;

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
    private static final String SETTINGS = "Settings";
    private final static String ROUTING_API = "https://api.tomtom.com/routing/1/calculateRoute/41.848994,12.609140:41.852834,12.598690?key=XEPi5PqA9rSiJ6ZYYZKJ68Us1exG4YKH";
    //private final static String FLOWS_API ="https://traffic.tomtom.com/tsq/hdf/ITA-HDF-OPENLR/bd200f72-3871-42bf-a65b-3e792386e702/content.xml";
    private final static String FLOWS_API ="https://traffic.tomtom.com/tsq/hdf/ITA-HDF-OPENLR/{0}/content.xml";
    private final static String FLOWS_API_DETAILD ="https://traffic.tomtom.com/tsq/hdf-detailed/ITA-HDF_DETAILED-OPENLR/{0}/content.proto";
    private final static String INCIDENTS_API ="http://localhost/test/Incidents_OpenLR_20170404_052032.xml";

    public static StackPane stackPaneHolder;
    private JFXPopup toolbarPopup;
    private static TrafficViewer mapViewer = null;
    private static Image icon;
    private Tab incidents = null;
    private Tab flows = null;
    private Tab settings = null;
    private JFXTabPane tabPane = null;
    private JFXToggleButton liveButton;
    private JFXTextField trafficKeyField = null;
    private JFXTextField routingKeyField = null;
    private TrafficClient trafficClient;
    private boolean apiSupport = false;

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

            incidents.setOnSelectionChanged(new EventHandler<javafx.event.Event>() {
                @Override
                public void handle(Event event) {
                    if(incidents.isSelected()){
                        if(apiSupport){
                            trafficClient = new IncidentClient(INCIDENTS_API);
                            trafficClient.setCallIntervall(60000);
                            trafficClient.setMap(mapViewer);
                            trafficClient.start();
                        }else {
                            mapViewer.showTrafficIncidents();
                        }
                    }else{
                        if(trafficClient != null){
                            trafficClient.stop();
                        }
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
            flows.setOnSelectionChanged(new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    if(flows.isSelected()){
                        if(apiSupport){
                            if(trafficKeyField != null) {
                                String url = MessageFormat.format(FLOWS_API, trafficKeyField.getText());
                                trafficClient = new FlowClient(url);
                                trafficClient.setCallIntervall(60000);
                                trafficClient.setMap(mapViewer);
                                trafficClient.start();
                            }else{
                                //TODO
                            }
                        }else {
                            mapViewer.showTrafficFlow();
                        }
                    }else{
                        if(trafficClient != null){
                            trafficClient.stop();
                        }
                    }
                }
            });

            Label noFlowDataLabel = new Label("Please load traffic flow data!");
            noFlowDataLabel.setTextFill(Color.web("#FFFFFF"));
            flows.setContent(noFlowDataLabel);
            tabPane.getTabs().add(flows);
            settings = new Tab();
            settings.setText(SETTINGS);
            settings.setOnSelectionChanged(new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    //TODO
                }
            });
            //liveButton = createLiveButton();
            settings.setContent(createSettingsPane());
            //trafficKeyField = createTextField("Traffic Key", true);
            tabPane.getTabs().add(settings);
            tabPane.setPrefWidth(sideMenu.getWidth());
            sideMenu.getChildren().add(tabPane);
        }
    }

    private JFXTextField createTextField(String desc, boolean validation){
        final JFXTextField field = new JFXTextField();
        field.setPromptText(desc);
        if(validation){
            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage(desc + " required!");
            field.getValidators().add(validator);
            field.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        field.validate();
                    }
                }
            });
        }
        return field;
    }

    private static void showFileChooser(){
        FileLoader loader = new FileLoader();
        OpenLRFileHandler handler;
        loader.startFileChooser();
        if(loader.getDataFormat().equals("xml")){
            // now we have to discern between flow and incidents
            if(!loader.isFlowFile()) {
                handler = new OpenLRXMLHandler();
                handler.setData(loader.getDataFile());
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
                handler = new OpenLRXMLFlowHandler();
                handler.setData(loader.getDataFile());
                handler.process();
                mapViewer.resetFlows();
                for (TrafficFlow flow : ((OpenLRXMLFlowHandler) handler).getFlows()) {
                    mapViewer.addTrafficFlow(flow);
                }
                mapViewer.showTrafficFlow();
                return;
                }
        }else if(loader.getDataFormat().equals("proto")){
            handler = new OpenLRProtoHandler();
            handler.setData(loader.getDataFile());
            handler.process();
            mapViewer.resetFlows();
            for(TrafficFlow flow : ((OpenLRProtoHandler)handler).getFlows()){
                mapViewer.addTrafficFlow(flow);
            }
            mapViewer.showTrafficFlow();
            return;
        }else{
            System.out.println("Unknown data format!");
        }

    }

    private void createLiveButton(final JFXToggleButton button){
        //final JFXToggleButton button = new JFXToggleButton();
        button.setText("Live Mode");
        button.setTextFill(Color.web("#FFFFFF"));
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(button.isSelected()  ){
                    Label incidentApiLabel = new Label("Connected to TomTom-API");
                    incidentApiLabel.setTextFill(Color.web("#FFFFFF"));
                    incidents.setContent(incidentApiLabel);
                    Label flowtApiLabel = new Label("Connected to TomTom-API");
                    flowtApiLabel.setTextFill(Color.web("#FFFFFF"));
                    flows.setContent(flowtApiLabel);
                    apiSupport = true;
                }else{
                    trafficClient.stop();
                    Label noFlowDataLabel = new Label("Please load traffic flow data!");
                    noFlowDataLabel.setTextFill(Color.web("#FFFFFF"));
                    flows.setContent(noFlowDataLabel);
                    Label noIncidentDataLabel = new Label("Please load traffic incident data!");
                    noIncidentDataLabel.setTextFill(Color.web("#FFFFFF"));
                    incidents.setContent(noIncidentDataLabel);
                    apiSupport = false;
                }

            }
        });
    }

    private Pane createSettingsPane(){
        Pane settings = null;
        try {
            settings = (AnchorPane)FXMLLoader.load(getClass().getResource("/fxml/settings.fxml"));
            JFXToggleButton liveButton = (JFXToggleButton)settings.lookup("#liveMode");
            createLiveButton(liveButton);
            trafficKeyField = (JFXTextField)settings.lookup("#trafficKeyField");
            routingKeyField = (JFXTextField)settings.lookup("#routingKeyField");
        }catch(IOException ie){
            System.err.println("Can not load settings pane");
        }
        return settings;
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
