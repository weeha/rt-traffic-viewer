package model.traffic;

/**
 * Created by flori on 04.05.2017.
 */
public class TrafficIncident extends Traffic{

    private int averageNumberOfStops;
    private final String ID;
    private String rawString = "";

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

    public void setRawAsString(String raw){
        rawString = raw;
    }

    public String getRawString(){
        return this.rawString;
    }

    @Override
    public String toString(){
        String result = "";
        result += "ID: " + ID + "\n";
        result += "RAW-Data: " + rawString + "\n";
        return result + super.toString();
    }
}
