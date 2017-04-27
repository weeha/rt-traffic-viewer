package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
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
    private Label content;

    private GeoPosition coords = null;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        headline.setText("OpenLR-Data");
        closeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                JFXDialog holder = (JFXDialog)layout.getParent().getParent();
                holder.close();
            }
        });
    }

    public void setCoords(GeoPosition coords){
        if(coords != null)
            content.setText(coords.toString());
    }
}
