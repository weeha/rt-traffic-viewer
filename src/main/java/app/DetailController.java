package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import model.traffic.TrafficIncident;
import org.jxmapviewer.viewer.GeoPosition;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Florian Noack on 27.04.2017.
 */
public class DetailController implements Initializable {

    @FXML
    private JFXButton closeButton;
    @FXML
    private JFXDialogLayout layout;
    @FXML
    private Label headline;
    @FXML
    private Label id;
    @FXML
    private JFXTextField raw;
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        headline.setText("Traffic Incident");
        closeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                JFXDialog holder = (JFXDialog)layout.getParent().getParent();
                holder.close();
            }
        });
    }

    public void setIncident(TrafficIncident incident){
        if(incident != null){
			System.out.println(incident);
            id.setText(incident.getId());
            raw.setText(incident.getRawString());
            creationTime.setText(incident.getCreationTime());
            trafficType.setText(incident.getTrafficType());
            averageSpeed.setText(incident.getAverageSpeed());
            delayTime.setText(incident.getDelayTime());
            distance.setText(incident.getDistance());
        }
    }
}
