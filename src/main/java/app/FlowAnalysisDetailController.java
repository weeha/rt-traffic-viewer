package app;

import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Florian Noack on 04.09.2017.
 */
public class FlowAnalysisDetailController implements Initializable {

    @FXML
    private JFXTabPane analysisTabs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tab travelTime = new Tab("Travel Time");
        Tab relativeSpeed = new Tab("Relative Speed");
        setTab(travelTime);
        setTab(relativeSpeed);
    }

    private void setTab(Tab t){
        analysisTabs.getTabs().add(t);
    }
}
