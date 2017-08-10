package io;

import app.MainController;
import model.*;
import model.traffic.TrafficFlow;
import model.traffic.TrafficIncident;
import org.apache.commons.io.FileUtils;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import view.TrafficViewer;
import view.openStreetMap.SwingWaypoint;
import java.io.*;
import java.net.ConnectException;

/**
 * Created by Florian Noack on 05.06.2017.
 */
public class TrafficClient extends HttpClient{

    protected TrafficViewer viewer = null;
    protected boolean store = false;

    public TrafficClient(String url){
        super(url);
        client = HttpClientBuilder.create().build();
    }

    public void storeData(boolean store){
        this.store = store;
    }

    public void run(){
        while(active){
            System.out.println("Connecting to " + URL);
            try {
                response = client.execute(request);
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "ISO-8859-1");
                if(store) {
                    File file = new File(generateFileString(URL));
                    FileUtils.writeStringToFile(file, responseString, "ISO-8859-1");
                    //FileUtils.writeStringToFile(new File(
                    //                generateFileString(URL)),
                    //        responseString);
                }
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
                    if(this instanceof FlowDetailedClient) {
                        viewer.resetFlows();
                        for (TrafficFlow f : ((FlowHandler)handler).getFlows()) {
                                viewer.addTrafficFlow(f);
                            if(f.getRelativeType() != null) {
                                if (f.getRelativeType() == 1)
                                    viewer.addWaypoint(new SwingWaypoint(f, MainController.darkGreenFlowIcon));
                                else if (f.getRelativeType() == 2)
                                    viewer.addWaypoint(new SwingWaypoint(f, MainController.greenFlowIcon));
                                else if (f.getRelativeType() == 3)
                                    viewer.addWaypoint(new SwingWaypoint(f, MainController.yellowFlowIcon));
                                else if (f.getRelativeType() == 4)
                                    viewer.addWaypoint(new SwingWaypoint(f, MainController.orangeFlowIcon));
                                else if (f.getRelativeType() == 5)
                                    viewer.addWaypoint(new SwingWaypoint(f, MainController.redFlowIcon));
                            }else{
                                viewer.addWaypoint(new SwingWaypoint(f, MainController.incidentIcon));
                            }
                        }
                        viewer.showTrafficFlow();
                    }else if(this instanceof FlowClient && !(this instanceof FlowDetailedClient)) {
                        viewer.resetFlows();
                        for (TrafficFlow f : ((FlowHandler)handler).getFlows()) {
                            viewer.addTrafficFlow(f);
                            viewer.addWaypoint(new SwingWaypoint(f, MainController.incidentIcon));
                        }
                        viewer.showTrafficFlow();
                    }else if(this instanceof IncidentClient){
                        viewer.resetIncidents();
                        for (TrafficIncident i : ((OpenLRXMLHandler)handler).getIncidents()) {
                            viewer.addTrafficIncident(i);
                            viewer.addWaypoint(new SwingWaypoint(i, MainController.incidentIcon));
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

    protected String generateFileString(String url){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(MainController.DATE_TIME_FORMAT_NOW);

        return sdf.format(cal.getTime());
    }

    public void setMap(TrafficViewer viewer){
        this.viewer = viewer;
    }
}
