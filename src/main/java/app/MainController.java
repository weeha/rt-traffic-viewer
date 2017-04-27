package app;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import io.FileLoader;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import model.OpenLRFileHandler;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private SwingNode mapHolder;
    @FXML
    private JFXRippler optionsRippler;
    @FXML
    private StackPane optionsBurger;

    private JFXPopup toolbarPopup;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources){
        this.createAndSetSwingContent(mapHolder);
        this.createOptionsList();
    }

    private void createOptionsList(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/options.fxml"));
        loader.setController(new InputController());
        Region reg = null;
        try {
            reg = loader.load();
        }catch(IOException ie){
            System.out.println("Can not load popup");
        }
        if(reg != null) {
            toolbarPopup = new JFXPopup(reg);
            optionsBurger.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    toolbarPopup.show(optionsBurger, JFXPopup.PopupVPosition.TOP
                            , JFXPopup.PopupHPosition.RIGHT
                            , -12
                            , 15);
                }
            });
        }
    }

    private void createAndSetSwingContent(final SwingNode swingNode) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JXMapViewer mapViewer = new JXMapViewer();

                // Create a TileFactoryInfo for OpenStreetMap
                TileFactoryInfo info = new OSMTileFactoryInfo();
                DefaultTileFactory tileFactory = new DefaultTileFactory(info);
                mapViewer.setTileFactory(tileFactory);

                // Use 8 threads in parallel to load the tiles
                tileFactory.setThreadPoolSize(8);

                // Set a first focus for startup
                GeoPosition tum_campus = new GeoPosition(48.1486, 11.5687);

                mapViewer.setZoom(7);
                mapViewer.setAddressLocation(tum_campus);

                MouseInputListener mia = new PanMouseInputListener(mapViewer);
                mapViewer.addMouseListener(mia);
                mapViewer.addMouseMotionListener(mia);
                mapViewer.addMouseListener(new CenterMapListener(mapViewer));
                mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
                mapViewer.addKeyListener(new PanKeyListener(mapViewer));

                swingNode.setContent(mapViewer);
            }
        });
    }

    private static void showFileChooser(){
        FileLoader loader = new FileLoader();
        loader.startFileChooser();

        OpenLRFileHandler handler = new OpenLRFileHandler(loader.getDataFile());
        handler.process();
    }

    public static final class InputController {
        @FXML
        private JFXListView<?> toolbarPopupList;

        @FXML
        private void submit(){
            switch(toolbarPopupList.getSelectionModel().getSelectedIndex()){
                case 0:
                    showFileChooser();
                    // make popup disappear after selection
                    toolbarPopupList.setExpanded(false);
                    break;
                case 1:
                    Platform.exit();
                    break;
                default:
                    break;
            }
        }
    }
}
