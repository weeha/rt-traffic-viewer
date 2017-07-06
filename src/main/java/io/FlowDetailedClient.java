package io;

import app.MainController;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ga69hon on 7/6/2017.
 */
public class FlowDetailedClient extends FlowClient{


    public FlowDetailedClient(String url){
        super(url);
    }

    @Override
    protected String generateFileString(String url){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(MainController.DATE_FORMAT_NOW);

        String fileName = "";
        fileName += MainController.FlOWS_DIR + sdf.format(cal.getTime());
        return fileName + "\\Detailed_Flow_" + super.generateFileString(url) + ".proto";
    }
}
