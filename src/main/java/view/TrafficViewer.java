package view;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;
import javax.swing.event.MouseInputListener;

/**
 * Created by Florian Noack on 27.04.2017.
 */
public class TrafficViewer extends JXMapViewer {

    public TrafficViewer(){
        super();
        this.setTileFactoryHelper();
        this.setMouseListener();
    }

    private void setMouseListener(){
        MouseInputListener mia = new PanMouseInputListener(this);
        this.addMouseListener(mia);
        this.addMouseMotionListener(mia);
        this.addMouseListener(new CenterMapListener(this));
        this.addMouseWheelListener(new ZoomMouseWheelListenerCenter(this));
        this.addKeyListener(new PanKeyListener(this));
    }

    private void setTileFactoryHelper(){
        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        this.setTileFactory(tileFactory);

        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);
    }

}
