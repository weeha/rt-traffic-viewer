package model.traffic;

import java.util.Date;

/**
 * Created by Florian Noack on 16.08.2017.
 */
public class TrafficAnalysis{

    private Date date;

    public TrafficAnalysis(Date date){
        this.date = date;

    }

    public Date getDate(){
        return this.date;
    }

    @Override
    public String toString(){
        return "Date:\t" + date + "\n";
    }

}
