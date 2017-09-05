package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.OpenLRAnalysisHandler;
import model.traffic.FlowAnalysisElemImpl;
import model.traffic.Traffic;
import model.traffic.TrafficAnalysis;
import view.Analysis.FlowAnalysisHolder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Florian Noack on 04.09.2017.
 */
public class AnalyseController implements Initializable {

    @FXML
    VBox box;
    @FXML
    JFXListView<Traffic> trafficList;
    @FXML
    JFXButton selectionButton;
    @FXML
    JFXButton analyzeButton;

    private final String MAIN_COLOR = "#00e5ff";
    private String analysePath = "";

    private JFXDatePicker datePicker = null;
    private JFXTimePicker startTime = null;
    private JFXTimePicker endTime = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println(analysePath);
        datePicker = createDatePicker();
        startTime = createTimePicker(MAIN_COLOR);
        endTime = createTimePicker(MAIN_COLOR);

        box.getChildren().add(0, datePicker);
        //box.getChildren().add(1, createLabel("Start Time:"));
        box.getChildren().add(1, startTime);
        //box.getChildren().add(3, createLabel("Start Time:"));
        box.getChildren().add(2, endTime);
        trafficList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Traffic>() {
            @Override
            public void changed(ObservableValue<? extends Traffic> observable, Traffic oldValue, Traffic newValue) {
                MainController.highlightTraffic(newValue);
            }
        });

        selectionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LocalDateTime dTime1  = LocalDateTime.of(datePicker.getValue(), startTime.getValue());
                LocalDateTime dTime2  = LocalDateTime.of(datePicker.getValue(), endTime.getValue());
                Date startDate = Date.from(dTime1.atZone(ZoneId.systemDefault()).toInstant());
                Date endDate = Date.from(dTime2.atZone(ZoneId.systemDefault()).toInstant());
                final File folder = new File(analysePath);
                OpenLRAnalysisHandler handler = new OpenLRAnalysisHandler(getFilesFromFolder(folder));
                handler.setDateIntervall(startDate, endDate);
                handler.process();
                System.out.println(handler.getAnalysisList().size());
                trafficList.getItems().setAll(handler.getAnalysisList());
                System.out.println(trafficList.getItems().size());
            }
        });
        analyzeButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                FXMLLoader loader;
                try {
                    loader = new FXMLLoader(getClass().getResource("/fxml/flowAnalysisDetail.fxml"));
                    Parent root = (Parent)loader.load();

                            Stage stage = new Stage();
                    FlowAnalysisDetailController controller = (FlowAnalysisDetailController)loader.getController();
                    controller.setAnalysis((FlowAnalysisElemImpl)trafficList.getSelectionModel().getSelectedItem());
                    stage.setTitle("Analysis");
                    stage.setScene(new Scene(root, 640, 860));
                    stage.show();
                }catch(IOException ie){
                    ie.printStackTrace();
                }
            }
        });
    }

    public void setAnalysisPath(String path){
        analysePath = MainController.DATA_DIR + path;
    }

    private JFXTimePicker createTimePicker(String color){
        JFXTimePicker timePicker = new JFXTimePicker();
        timePicker.setValue(LocalTime.now());
        timePicker.setDefaultColor(Color.valueOf(color));
        return timePicker;
    }

    private JFXDatePicker createDatePicker(){
        JFXDatePicker datePicker = new JFXDatePicker();
        datePicker.setValue(LocalDate.now());
        return datePicker;
    }

    private Label createLabel(String text){
        Label l = new Label();
        l.setText(text);
        l.setTextFill(Color.valueOf("#FFFFFF"));
        return l;
    }

    public static List<File> getFilesFromFolder(final File folder) {
        List<File> files = new ArrayList<File>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                for(File f : getFilesFromFolder(fileEntry)){
                    files.add(f);
                }
            } else {
                files.add(fileEntry);
            }
        }
        return files;
    }
}
