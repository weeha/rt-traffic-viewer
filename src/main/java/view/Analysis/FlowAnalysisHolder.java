package view.Analysis;

import javafx.scene.control.Tab;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Florian Noack on 04.09.2017.
 */
public class FlowAnalysisHolder extends AnalysisHolder{

    public FlowAnalysisHolder(){
        super();

    }

    private void setTabs(){
        Tab travelTime = new Tab("Travel Time");
        travelTime.setContent(new Rectangle(200,200, Color.LIGHTSTEELBLUE));

        addTab(travelTime);
    }
}
