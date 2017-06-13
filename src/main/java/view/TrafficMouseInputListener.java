package view;

import model.traffic.Traffic;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.input.PanMouseInputListener;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian Noack on 10.06.2017.
 */
public class TrafficMouseInputListener extends PanMouseInputListener{

    private List<Traffic> traffics = null;

    public TrafficMouseInputListener(JXMapViewer viewer){
        super(viewer);
    }

    public void setTraffics(List<Traffic> t){
        traffics = t;
    }

    @Override
    public void mouseClicked(MouseEvent event){
        System.out.println("CLICKED");
        if(traffics != null){
            for(Traffic t : traffics){
                if(t.getShape().contains(event.getPoint())){
                    System.out.println("MATCH");
                    return;
                }else{
                    System.out.println(t.getShape().getBounds());
                }
            }
        }
    }
}
