package app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.traffic.IncidentAnalysisElemImpl;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

/**
 * Created by Florian Noack on 25.09.2017.
 */
public class IncidentAnalysisDetailController implements Initializable {

    @FXML
    private VBox vbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private Label getHeadLabel(String text){
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 16.0px; -fx-padding: 20px 0px 0px 14px; -fx-text-fill: rgba(0.0, 0.0, 0.0, 0.87);");
        return l;
    }

    private Label getLabel(String text){
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 14.0px; -fx-padding: 10px 0px 0px 18px; -fx-text-fill: rgba(0.0, 0.0, 0.0, 1.0);");
        return l;
    }

    public void setAnalysis(IncidentAnalysisElemImpl selectedItem) {

        vbox.getChildren().add(getHeadLabel("Date: " + selectedItem.getDateString()));
        vbox.getChildren().add(getHeadLabel("OpenLR: " + selectedItem.getRawString()));
        int size = selectedItem.getTraffic().size();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        vbox.getChildren().add(getHeadLabel("Entries: " + selectedItem.getTraffic().size()));
        if(size>0)
            vbox.getChildren().add(getHeadLabel("Start: " + df.format(selectedItem.getTraffic().get(0).getDate()) + "\tEnd: " + df.format(selectedItem.getTraffic().get(size-1).getDate())));

        vbox.getChildren().add(getHeadLabel("Average Speed (km/h):"));
        for(Double e : selectedItem.getAverageSpeedAnalysis().keySet()){
            vbox.getChildren().add(getLabel("- " + e + "    : " + selectedItem.getAverageSpeedAnalysis().get(e) + " occurrences"));
        }
        vbox.getChildren().add(getHeadLabel("Delay Time (seconds):"));
        for(Double e : selectedItem.getDelayTimeAnalysis().keySet()){
            vbox.getChildren().add(getLabel("- " + e + "    : " + selectedItem.getDelayTimeAnalysis().get(e) + " occurrences"));
        }
        vbox.getChildren().add(getHeadLabel("Traffic Type:"));
        for(String e : selectedItem.getTrafficTypeAnalysis().keySet()){
            vbox.getChildren().add(getLabel("- " + e + "    : " + selectedItem.getTrafficTypeAnalysis().get(e) + " occurrences"));
        }
    }

}
