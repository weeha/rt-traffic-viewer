package io;

import app.MainController;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Florian Noack on 05.06.2017.
 */
public class FlowClient extends TrafficClient{

    public FlowClient(String url){
        super(url);
    }

    @Override
    protected String generateFileString(String url){

        if(this instanceof FlowDetailedClient)
            return super.generateFileString(url);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(MainController.DATE_FORMAT_NOW);

        String fileName = "";
        fileName += MainController.FlOWS_DIR + sdf.format(cal.getTime());
        return fileName + "\\Regular_Flow_" + super.generateFileString(url) + ".xml";
    }
}
