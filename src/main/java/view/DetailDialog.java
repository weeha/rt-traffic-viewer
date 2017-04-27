package view;

import app.DetailController;
import com.jfoenix.controls.JFXDialog;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jxmapviewer.viewer.GeoPosition;

import javax.xml.soap.Detail;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

/**
 * Created by Florian Noack on 27.04.2017.
 */
public class DetailDialog{

    private Region reg;
    private JFXDialog diag;
    private final StackPane pane;
    private final GeoPosition coords;
    private final DetailController controller;

    public DetailDialog(StackPane pane, GeoPosition coords){
        this.pane = pane;
        this.coords = coords;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detailDialog.fxml"));
        reg = null;
        try {
            reg = loader.load();
        }catch(IOException ie){
            System.out.println("Can not load popup");
        }
        controller = loader.<DetailController>getController();
        controller.setCoords(coords);
    }

    public void show(){
        diag = new JFXDialog(pane, reg, JFXDialog.DialogTransition.CENTER);
        diag.show();
    }


}
