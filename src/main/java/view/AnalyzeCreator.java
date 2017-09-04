package view;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Created by Florian Noack on 04.09.2017.
 */
public class AnalyzeCreator extends Pane{

    private VBox box;

    public AnalyzeCreator(){
        box = new VBox();
        createPane();
    }

    private void createPane(){
        box.getChildren().add(getDatePicker());
        box.getChildren().add(createLabel("Start Time"));
        box.getChildren().add(getTimePicker("#3f51b5"));
        box.getChildren().add(createLabel("End Time"));
        box.getChildren().add(getTimePicker("#3f51b5"));
    }

    private JFXTimePicker getTimePicker(String color){
        JFXTimePicker timePicker = new JFXTimePicker();
        timePicker.setDefaultColor(Color.valueOf(color));
        return timePicker;
    }

    private JFXDatePicker getDatePicker(){
        JFXDatePicker datePicker = new JFXDatePicker();
        return datePicker;
    }

    private Label createLabel(String text){
        Label l = new Label();
        l.setText(text);
        l.setTextFill(Color.valueOf("#FFFFFF"));
        return l;
    }
}
