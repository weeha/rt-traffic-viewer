package view.Analysis;

import com.jfoenix.controls.JFXTabPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Created by Florian Noack on 04.09.2017.
 */
public class AnalysisHolder extends StackPane {

    protected JFXTabPane chartPane;

    public AnalysisHolder(){
        chartPane = new JFXTabPane();
        this.getChildren().add(chartPane);
    }

    protected void addTab(Tab tab){
        this.chartPane.getTabs().add(tab);
    }
}
