package app;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.traffic.TrafficIncident;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Florian Noack on 13.06.2017.
 */
public class IncidentDetailController implements Initializable {

    @FXML
    private JFXTextField creationTime;
    @FXML
    private JFXTextField trafficType;
    @FXML
    private JFXTextField averageSpeed;
    @FXML
    private JFXTextField delayTime;
    @FXML
    private JFXTextField distance;
    @FXML
    private JFXTextField openLR;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setIncident(TrafficIncident incident){
        if(incident != null){
            creationTime.setText(incident.getCreationTime());
            trafficType.setText(incident.getTrafficType());
            averageSpeed.setText(incident.getAverageSpeed());
            delayTime.setText(incident.getDelayTime());
            distance.setText(incident.getDistance());
            openLR.setText(incident.getRawString());
        }
    }
}
