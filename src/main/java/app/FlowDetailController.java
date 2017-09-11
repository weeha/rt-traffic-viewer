package app;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.traffic.TrafficFlow;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Florian Noack on 12.06.2017.
 */
public class FlowDetailController implements Initializable {

    @FXML
    private JFXTextField identifier;
    @FXML
    private JFXTextField averageSpeed;
    @FXML
    private JFXTextField travelTime;
    @FXML
    private JFXTextField confidence;
    @FXML
    private JFXTextField relativeSpeed;
    @FXML
    private JFXTextField trafficCondition;
    @FXML
    private JFXTextField distance;
    @FXML
    private JFXTextField openLR;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setFlow(TrafficFlow flow){
        if(flow != null){
            identifier.setText(flow.getIdentifier());
            averageSpeed.setText(flow.getAverageSpeed());
            travelTime.setText(flow.getTravelTime());
            confidence.setText(flow.getConfidence());
            relativeSpeed.setText(flow.getRelativeSpeed());
            trafficCondition.setText(flow.getTrafficCondition());
            distance.setText(flow.getDistance());
            openLR.setText(flow.getRawString());
        }

    }
}
