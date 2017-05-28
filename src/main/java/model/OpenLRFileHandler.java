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

    protected final File file;
    private List<RawBinaryData> data;
    protected final  OpenLRBinaryDecoder bDecoder;

    private final double VERONA_NW_LAT = 45.467219;
    private final double VERONA_NW_LON = 10.969248;
    private final double VERONA_SE_LAT = 45.446207;
    private final double VERONA_SE_LON = 10.995855;
    private final double VERONA_NE_LAT = 45.463246;
    private final double VERONA_NE_LON = 11.006842;
    private final double VERONA_SW_LAT = 45.448255;
    private final double VERONA_SW_LON = 10.968218;

    public OpenLRFileHandler(File file){

        this.file = file;
        this.data = new ArrayList<RawBinaryData>();
        bDecoder = new OpenLRBinaryDecoder();
    }

    public List<RawBinaryData> getLocationData(){
        return data;
    }

    public void process(){
        if(file != null) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                for (String line; (line = reader.readLine()) != null; ) {
                    ByteArray bytes = new ByteArray(line);
                    RawBinaryData raw = bDecoder.resolveBinaryData("", bytes);
                    if(rawWithin(raw)) {
                        data.add(raw);
                    }
                    //System.out.println((bDecoder.resolveBinaryData("", bytes)));
                }
            } catch (FileNotFoundException fe) {
                System.err.println("File not found");
            } catch (IOException ie) {
                System.err.println("IO Error");
            } catch (openlr.PhysicalFormatException pe) {
                System.err.println("Decoding error");
            } finally {
                try {
                    if(reader != null)
                        reader.close();
                } catch (IOException ie) {
                }
            }
        }
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
