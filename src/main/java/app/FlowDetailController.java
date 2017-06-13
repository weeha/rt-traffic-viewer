package app;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.traffic.TrafficFlow;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Florian Noack on 12.06.2017.
 */
public class FlowDetailController implements Initializable {

    @FXML
    private JFXTextField averageSpeed;
    @FXML
    private JFXTextField travelTime;
    @FXML
    private JFXTextField confidence;
    @FXML
    private JFXTextField trafficCondition;
    @FXML
    private JFXTextField distance;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setFlow(TrafficFlow flow){
        System.out.println(flow);
        if(flow != null){
            averageSpeed.setText(flow.getAverageSpeed());
            travelTime.setText(flow.getTravelTime());
            confidence.setText(flow.getConfidence());
            trafficCondition.setText(flow.getTrafficCondition());
            distance.setText(flow.getDistance());
        }

    }
}
