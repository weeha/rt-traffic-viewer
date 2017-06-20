package io;

import app.MainController;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Florian Noack on 05.06.2017.
 */
public class IncidentClient extends TrafficClient{

    public IncidentClient(String url){
        super(url);
    }

    @Override
    public String generateFileString(String url){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(MainController.DATE_FORMAT_NOW);

        String fileName = "";
        fileName += MainController.INCIDENTS_DIR + sdf.format(cal.getTime());
        return fileName + "\\Incidents_" + super.generateFileString(url) + ".xml";
    }
}
