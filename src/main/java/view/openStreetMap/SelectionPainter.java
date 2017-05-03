package view.openStreetMap;

import java.awt.*;

/**
 * Created by Florian Noack on 02.05.2017.
 */
public class SelectionPainter implements org.jxmapviewer.painter.Painter<Object>{

    private Color fillColor = new Color(128, 192, 255, 128);
    private Color frameColor = new Color(0, 0, 255, 128);
    private SelectionAdapter adapter;

    public SelectionPainter(SelectionAdapter adapter){
        this.adapter = adapter;
    }


    public void paint(Graphics2D g, Object t, int width, int height){
        Rectangle rc = adapter.getRectangle();

        if (rc != null){
            g.setColor(frameColor);
            g.draw(rc);
            g.setColor(fillColor);
            g.fill(rc);
        }
    }
}