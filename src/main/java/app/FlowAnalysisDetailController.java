package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.traffic.FlowAnalysis;
import model.traffic.FlowAnalysisElemImpl;
import view.Analysis.FlowAnalysisChart;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Florian Noack on 04.09.2017.
 */
public class FlowAnalysisDetailController implements Initializable {

    @FXML
    private JFXTabPane analysisTabs;

    private Tab travelTime;
    private Tab averageSpeed;
    private Tab relativeSpeed;
    private Tab export;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        travelTime = new Tab("Travel Time");
        averageSpeed = new Tab("Average Speed");
        relativeSpeed = new Tab("Relative Speed");
        export = new Tab("Export");
        JFXButton exportButton = new JFXButton("Export");
        exportButton.setStyle("-fx-background-color: #00e5ff;");
        exportButton.setTextFill(Color.WHITE);
        export.setContent(exportButton);
        setTab(travelTime);
        setTab(averageSpeed);
        setTab(relativeSpeed);
        setTab(export);
    }

    private void setTab(Tab t){
        analysisTabs.getTabs().add(t);
    }

    public void setAnalysis(FlowAnalysisElemImpl analysis){
        final FlowAnalysisChart travelTimeChart = new FlowAnalysisChart();
        travelTimeChart.setYAxisLabel("Travel Time [seconds]");
        travelTimeChart.setTrafficAnalysis(analysis.getTraffic());
        travelTime.setContent(travelTimeChart);

        final FlowAnalysisChart averageSpeedChart = new FlowAnalysisChart();
        averageSpeedChart.setYAxisLabel("Average Speed [km/h]");
        averageSpeedChart.setTrafficAnalysis(analysis.getTraffic());
        averageSpeed.setContent(averageSpeedChart);

        final FlowAnalysisChart relativeSpeedChart = new FlowAnalysisChart();
        relativeSpeedChart.setYAxisLabel("Relative Speed");
        relativeSpeedChart.setTrafficAnalysis(analysis.getTraffic());
        relativeSpeed.setContent(relativeSpeedChart);
    }
}
