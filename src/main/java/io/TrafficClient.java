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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Florian Noack on 05.06.2017.
 */
public class TrafficClient extends HttpClient{

    protected TrafficViewer viewer = null;
    protected boolean store = false;
    public static final String DATE_FORMAT_NOW = "yyyy_MM_dd_HH_mm_ss";

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
                if(store)
                    FileUtils.writeStringToFile(new File(
                            generateFileString(URL)),
                            responseString);
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

        String fileName = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);

        Pattern p = Pattern.compile("[^/]*$");
        Matcher m = p.matcher(url);
        if(m.find()){
            fileName = m.group(0);
            fileName.replace("?", "_");
            fileName += sdf.format(cal.getTime());
            if(fileName.contains(".proto")){
                fileName.replace(".proto", "_");
                fileName += ".proto";
            }
            else if(fileName.contains(".xml")){
                fileName.replace(".xml", "_");
                fileName += ".xml";
            }
        }

        return MainController.DATA_DIR + fileName;
    }

    public void setMap(TrafficViewer viewer){
        this.viewer = viewer;
    }
}
