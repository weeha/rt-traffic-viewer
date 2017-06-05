package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import model.location.CoordinateValue;
import openlr.binary.*;
import openlr.binary.data.RawBinaryData;

/**
 * Created by Florian Noack on 25.04.2017.
 */
public class OpenLRFileHandler {

    protected Reader reader = null;
    private List<RawBinaryData> data;
    protected final  OpenLRBinaryDecoder bDecoder;
    private File file;

    private final double VERONA_NW_LAT = 45.467219;
    private final double VERONA_NW_LON = 10.969248;
    private final double VERONA_SE_LAT = 45.446207;
    private final double VERONA_SE_LON = 10.995855;
    private final double VERONA_NE_LAT = 45.463246;
    private final double VERONA_NE_LON = 11.006842;
    private final double VERONA_SW_LAT = 45.448255;
    private final double VERONA_SW_LON = 10.968218;

    public OpenLRFileHandler(){

        this.data = new ArrayList<RawBinaryData>();
        bDecoder = new OpenLRBinaryDecoder();
    }

    public void setData(Object data){
        if(data instanceof File){
            try {
                this.file = file;
                reader = new BufferedReader(new InputStreamReader(new FileInputStream((File) data)));
            }catch(FileNotFoundException fe){
                reader = null;
            }
        }else if(data instanceof String){
            reader = new StringReader((String)data);
        }else{
            System.err.println("Unknown data format to process: " + data.getClass());
        }
    }

    public List<RawBinaryData> getLocationData(){
        return data;
    }

    public void process(){
        if(reader != null) {
            // Nothing to do here
        }
    }

    protected boolean hasFile(){
        if(file != null)
            return file.exists();
        return false;
    }

    protected File getDataFile(){
        if (hasFile())
            return file;
        else
            return null;
    }

    protected boolean rawWithin(RawBinaryData data){
        return true;
        //CoordinateValue val = new CoordinateValue(data.getBinaryFirstLRP().getLon(), data.getBinaryFirstLRP().getLat());
        //return isWithin(val.getLatDeg(), val.getLonDeg());
    }

    protected boolean isWithin(double lat, double lon){
        return lat >= VERONA_SW_LAT &&
                lat <= VERONA_NE_LAT &&
                lon >= VERONA_SW_LON &&
                lon <= VERONA_NE_LON;
    }
}
