package io;

import model.RouteXMLHandler;
import model.location.ILocationReferencePoint;
import model.traffic.Traffic;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jxmapviewer.viewer.GeoPosition;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * Created by Florian Noack on 05.06.2017.
 */
public class RoutingClient extends HttpClient{

    private String key = null;
    private final Traffic traffic;

    private static final String ROUTING_API = "https://api.tomtom.com/routing/1/calculateRoute/{0},{1}:{2},{3}?key={4}";

    public RoutingClient(Traffic traffic){
        this.traffic = traffic;
        client = HttpClientBuilder.create().build();
    }

    public void setKey(String key){
        this.key = key;
    }

    @Override
    public void run(){
        if(key != null){
            for(int i = 0; i < traffic.getAllLRPs().size();){
                ILocationReferencePoint p1 = traffic.getAllLRPs().get(i);
                ILocationReferencePoint p2 = traffic.getAllLRPs().get(i+1);
                String url = MessageFormat.format(ROUTING_API,
                        p1.getLatidude(),
                        p1.getLongitude(),
                        p2.getLatidude(),
                        p2.getLongitude(),
                        key);
                System.out.println(url);
                request = new HttpGet(url);
                try{
                    response = client.execute(request);
                    HttpEntity entity = response.getEntity();
                    String responseString = EntityUtils.toString(entity, "UTF-8");
                    RouteXMLHandler handler = new RouteXMLHandler(responseString);
                    handler.process();
                    for(GeoPosition g : handler.getCoordinates())
                        System.out.println(g);
                }catch(IOException ie){}
            }
        }else{
            throw new IllegalArgumentException("Missing Routing Key!");
        }
    }

}
