package io;

import app.MainController;
import model.FlowHandler;
import model.OpenLRFileHandler;
import model.OpenLRProtoHandler;
import model.traffic.TrafficFlow;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import view.openStreetMap.SwingWaypoint;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Florian Noack on 10.06.2017.
 */
public class FlowDetailedClientDeprecated extends TrafficClient{

    private String [] urls;
    private final RequestConfig conf;

    public FlowDetailedClientDeprecated(String url){
        super(url);
        urls = new String[2];
        urls[0] = url;
        client = HttpClientBuilder.create().build();
        conf = RequestConfig.custom()
                .setCircularRedirectsAllowed(false)
                .setConnectionRequestTimeout(4000)
                .setConnectTimeout(4000)
                .setMaxRedirects(0)
                .setRedirectsEnabled(false)
                .setSocketTimeout(4000)
                .build();
    }

    public void setSecondFlow(String url){
        urls[1] = url;
    }

    @Override
    public void run(){
        while(active){
            if(viewer != null)
                viewer.resetFlows();
            try {
                boolean ff = true;
                for (String u : urls) {
                    System.out.println("Connecting to " + u);
                    request = new HttpGet(u);
                    request.setConfig(conf);
                    response = client.execute(request);
                    HttpEntity entity = response.getEntity();
                    String responseString = EntityUtils.toString(entity, "ISO-8859-1");
                    if(store)
                        FileUtils.writeStringToFile(new File(
                                generateFileString(u)),
                                responseString);
                    OpenLRFileHandler handler = null;
                    handler = new OpenLRProtoHandler();
                    handler.setData(responseString);
                    handler.process();
                    if(viewer != null) {
                        for (TrafficFlow f : ((FlowHandler)handler).getFlows()) {
                            if(ff) {
                                f.setTrafficCondition("FREE_FLOW");
                                viewer.addWaypoint(new SwingWaypoint(f, MainController.ffFlowIcon));
                            }
                            else {
                                f.setTrafficCondition("NON_FREE_FLOW");
                                viewer.addWaypoint(new SwingWaypoint(f, MainController.nffFlowIcon));
                            }
                            viewer.addTrafficFlow(f);
                        }
                    }
                    ff = false;
                }
                if(viewer != null)
                    viewer.showTrafficFlow();
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

    @Override
    public String generateFileString(String url){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(MainController.DATE_FORMAT_NOW);

        String fileName = "";
        fileName += MainController.FlOWS_DIR + sdf.format(cal.getTime()) + "\\";
        fileName += "Detailed_Flow_";
        if(url.contains("flowType=ff"))
            fileName += "flowType_ff_";
        if(url.contains("flowType=nff"))
            fileName += "flowType_nff_";
        return fileName + super.generateFileString(url) + ".proto";
    }
}
