package model.traffic;

import javax.swing.text.DateFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Florian Noack on 04.05.2017.
 */
public class TrafficIncident extends Traffic{

    private int averageNumberOfStops;
    private Date recordCreationTime;
    private final String ID;

    public TrafficIncident(String id){

        this.ID = id;
    }

    public String getId(){
        return ID;
    }

    public int getAverageNumberOfStops(){
        return this.averageNumberOfStops;
    }

    public void setAverageNumberOfStops(int stops){
        averageNumberOfStops = stops;
    }

    @Override
    public String toString(){
        String result = "";
        result += "ID: " + ID + "\n";
        result += "RAW-Data: " + getRawString() + "\n";
        return result + super.toString();
    }

    public void setRecordCreationTime(String recordCreationTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ssZ");
        try {
            this.recordCreationTime = simpleDateFormat.parse(recordCreationTime);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
    }

    public Date getRecordCreationTime(){
        return this.recordCreationTime;
    }
}
