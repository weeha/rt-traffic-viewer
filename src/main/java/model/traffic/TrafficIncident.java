package model.traffic;

import javax.swing.text.DateFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
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
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ssZ");
        try {
            Instant instant = Instant.parse(recordCreationTime);
            this.recordCreationTime = Date.from(instant);
        }catch(Exception pe){
            pe.printStackTrace();
        }
    }

    public Date getRecordCreationTime(){
        return this.recordCreationTime;
    }

    public static void main(String [] args){
        TrafficIncident i= new TrafficIncident(null);
        //i.setRecordCreationTime("2017-04-04T04:20:00Z");
        Instant instant = Instant.parse("2017-04-04T04:20:00Z");
        System.out.println(Date.from(instant));
    }
}
