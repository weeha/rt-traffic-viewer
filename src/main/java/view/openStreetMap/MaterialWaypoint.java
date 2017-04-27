package view.openStreetMap;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * Created by Florian Noack on 27.04.2017.
 */
public class MaterialWaypoint extends DefaultWaypoint{

    private final Button button;
    private final GeoPosition coord;

    public MaterialWaypoint(final GeoPosition coord){

        super(coord);
        this.coord = coord;
        button = new Button();
        button.setPrefSize(24, 24);
        button.setMaxSize(24, 24);
        button.setOnAction(new CoordEventHandler());

        button.setVisible(true);

    }


    public Button getButton() {

        return button;

    }



    private class CoordEventHandler implements EventHandler {

        @Override
        public void handle(Event event) {
            System.out.println(coord);
        }
    }
}
