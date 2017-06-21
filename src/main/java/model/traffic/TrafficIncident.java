package model.traffic;

/**
 * Created by flori on 04.05.2017.
 */
public class TrafficIncident extends Traffic{

    private int averageNumberOfStops;
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
}
