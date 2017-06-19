package app;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import io.*;
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
import model.traffic.Traffic;
import model.traffic.TrafficFlow;
import model.traffic.TrafficIncident;
import view.TrafficViewer;
import view.openStreetMap.SwingWaypoint;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.io.File;
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
    public static final String DATA_DIR = System.getProperty("user.dir") + "\\data\\";
    private final static String FLOWS_API ="https://traffic.tomtom.com/tsq/hdf/ITA-HDF-OPENLR/{0}/content.xml";
    //private final static String FLOWS_API ="http://localhost/test/Flow_OpenLR_20170404_052012.xml";
    //private final static String FLOWS_API_DETAILED ="https://traffic.tomtom.com/tsq/hdf-detailed/ITA-HDF_DETAILED-OPENLR/{0}/content.xml";
    //private final static String FLOWS_API_DETAILED ="http://localhost/test/detailed_all.proto";
    //private static final String FLOWS_API_DETAILED_NFF = "http://localhost/test/detailed_nff.proto";
    //private static final String FLOWS_API_DETAILED_FF = "http://localhost/test/detailed_ff.proto";
    private static final String FLOWS_API_DETAILED_FF = "https://traffic.tomtom.com/tsq/hdf-detailed/ITA-HDF_DETAILED-OPENLR/{0}/content.proto?flowType=ff";
    private static final String FLOWS_API_DETAILED_NFF = "https://traffic.tomtom.com/tsq/hdf-detailed/ITA-HDF_DETAILED-OPENLR/{0}/content.proto?flowType=nff";
    //private final static String INCIDENTS_API ="http://localhost/test/Incidents_OpenLR_20170404_052032.xml";
    private final static String INCIDENTS_API ="https://traffic.tomtom.com/tsq/hdt/ITA-HDT-OPENLR/{0}/content.xml";

    public static StackPane stackPaneHolder;
    private JFXPopup toolbarPopup;
    private static TrafficViewer mapViewer = null;
    public static Image incidentIcon;
    public static Image ffFlowIcon;
    public static Image nffFlowIcon;
    private static Tab incidents = null;
    private static Tab flows = null;
    private Tab settings = null;
    private JFXTabPane tabPane = null;
    private JFXToggleButton detailedFlow;
    private JFXToggleButton storeData;
    private JFXTextField trafficKeyField = null;
    private JFXTextField routingKeyField = null;
    private TrafficClient trafficClient;
    private static Pane flowDetailPane = null;
    private static Pane incidentDetailPane = null;
    private boolean apiSupport = false;
    private static FlowDetailController flowController = null;
    private static IncidentDetailController incidentController = null;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources){
        this.createAndSetSwingContent(mapHolder);
        this.createOptionsList();
        stackPaneHolder = root;
        this.incidentIcon = loadIcon(0);
        this.ffFlowIcon = loadIcon(1);
        this.nffFlowIcon = loadIcon(2);
        try {
            FXMLLoader flowDetailLoader = new FXMLLoader(getClass().getResource("/fxml/flowDetail.fxml"));
            flowDetailPane = (AnchorPane) flowDetailLoader.load();
            flowController = flowDetailLoader.<FlowDetailController>getController();
            FXMLLoader incidentDetailLoader = new FXMLLoader(getClass().getResource("/fxml/incidentDetail.fxml"));
            incidentDetailPane = (AnchorPane) incidentDetailLoader.load();
            incidentController = incidentDetailLoader.<IncidentDetailController>getController();
        }catch(IOException ie){
            ie.printStackTrace();
        }
        this.setSidePanelContent();
        checkForDataDir();
    }

    private void checkForDataDir(){
        File dataFolder = new File(DATA_DIR);
        if(!dataFolder.exists()){
            dataFolder.mkdirs();
        }
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

    private Image loadIcon(int id){
        Image img = null;
        try {
            switch(id){
                case 0:
                    img = ImageIO.read(getClass().getResource("/png/ic_place_black_24dp.png"));
                    break;
                case 1:
                    img = ImageIO.read(getClass().getResource("/png/ic_info_green.png"));
                    break;
                case 2:
                    img = ImageIO.read(getClass().getResource("/png/ic_info_red.png"));
                    break;
                default:
                    System.out.println("Unknown Icon requested...");
            }
        }catch(IOException ie){
            System.out.println("Error loading incidentIcon");
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

    public static void setSidePanelContent(int tab, final Traffic traffic){
        switch(tab){
            case 0:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        incidents.setContent(incidentDetailPane);
                        if(incidentController != null){
                            incidentController.setIncident((TrafficIncident)traffic);
                        }
                    }
                });
                break;
            case 1:
                // flow tab
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        flows.setContent(flowDetailPane);
                        if(flowController != null){
                            flowController.setFlow((TrafficFlow)traffic);
                        }
                    }
                });
                break;
        }
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
                            trafficClient.storeData(storeData.isSelected());
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
                                String url = "";
                                if(detailedFlow.isSelected()) {
                                    String url1 = MessageFormat.format(FLOWS_API_DETAILED_FF, trafficKeyField.getText());
                                    String url2 = MessageFormat.format(FLOWS_API_DETAILED_NFF, trafficKeyField.getText());
                                    trafficClient = new FlowDetailedClient(url1);
                                    trafficClient.storeData(storeData.isSelected());
                                    ((FlowDetailedClient)trafficClient).setSecondFlow(url2);
                                    trafficClient.setCallIntervall(60000);
                                    trafficClient.setMap(mapViewer);
                                    trafficClient.start();
                                }else {
                                    url = MessageFormat.format(FLOWS_API, trafficKeyField.getText());
                                    trafficClient = new FlowClient(url);
                                    trafficClient.setCallIntervall(60000);
                                    trafficClient.setMap(mapViewer);
                                    trafficClient.start();
                                }

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
            settings.setContent(createSettingsPane());
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
                    mapViewer.addWaypoint(new SwingWaypoint(incident, incidentIcon));
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
                    mapViewer.addWaypoint(new SwingWaypoint(flow, incidentIcon));
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
                mapViewer.addWaypoint(new SwingWaypoint(flow, incidentIcon));
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
                    Label flowApiLabel = new Label("Connected to TomTom-API");
                    flowApiLabel.setTextFill(Color.web("#FFFFFF"));
                    flows.setContent(flowApiLabel);
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
            detailedFlow = (JFXToggleButton)settings.lookup("#detailedFlow");
            storeData = (JFXToggleButton)settings.lookup("#storeData");
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
