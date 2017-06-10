package io;

import model.*;
import model.traffic.TrafficFlow;
import model.traffic.TrafficIncident;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import view.TrafficViewer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;

/**
 * Created by Florian Noack on 05.06.2017.
 */
public class TrafficClient extends HttpClient{

    private TrafficViewer viewer = null;

    public TrafficClient(String url){
        super(url);
            client = HttpClientBuilder.create().build();
    }

    public void run(){
        while(active){
            System.out.println("Connecting to " + URL);
            try {
                response = client.execute(request);
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "ISO-8859-1");
                OpenLRFileHandler handler = null;
                if(this instanceof FlowClient) {
                    if(URL.endsWith(".xml"))
                        handler = new OpenLRXMLFlowHandler();
                    else if(URL.endsWith(".proto"))
                        handler = new OpenLRProtoHandler();
                }
                else
                    handler = new OpenLRXMLHandler();
                handler.setData(responseString);
                handler.process();
                if(viewer != null){
                    if(this instanceof FlowClient) {
                        viewer.resetFlows();
                        for (TrafficFlow f : ((FlowHandler)handler).getFlows()) {
                            viewer.addTrafficFlow(f);
                        }
                        viewer.showTrafficFlow();
                    }else if(this instanceof IncidentClient){
                        viewer.resetIncidents();
                        for (TrafficIncident i : ((OpenLRXMLHandler)handler).getIncidents()) {
                            viewer.addTrafficIncident(i);
                        }
                        viewer.showTrafficIncidents();
                    }
                }

            }catch(ClientProtocolException ce){

            }catch(ConnectException cpe){

            }catch(IOException ie){
                ie.printStackTrace();
            }
            finally {
                try {
                    Thread.sleep(callIntervall);
                }catch(InterruptedException ie){}
            }
        }
    }

    public void storeTraffic(){
        if(response != null){
            //TODO
            /*
            try {
                InputStream data = response.getEntity().getContent();
                OutputStream output = new FileOutputStream("C:\\Users\\flori\\Documents\\Development\\openlr\\Decompile\\p.proto");
                try {
                    ByteStreams.copy(data, output);
                } finally {
                    Closeables.closeQuietly(output);
                }

            }catch(IOException ie){

            }
            */
        }
    }

    public void setMap(TrafficViewer viewer){
        this.viewer = viewer;
    }
}
