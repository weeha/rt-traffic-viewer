package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import openlr.binary.*;
import openlr.binary.data.RawBinaryData;

/**
 * Created by Florian Noack on 25.04.2017.
 */
public class OpenLRFileHandler {

    private final File file;
    private List<RawBinaryData> data;
    private final  OpenLRBinaryDecoder bDecoder;

    public OpenLRFileHandler(File file){

        this.file = file;
        this.data = new ArrayList<RawBinaryData>();
        bDecoder = new OpenLRBinaryDecoder();
    }

    public List<RawBinaryData> getLocationData(){
        return data;
    }

    public void process(){
        BufferedReader reader = null;
        try {
             reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            for (String line; (line = reader.readLine()) != null;) {
                ByteArray bytes = new ByteArray(line);
                data.add(bDecoder.resolveBinaryData("", bytes));
                System.out.println((bDecoder.resolveBinaryData("", bytes)));
            }
        }catch(FileNotFoundException fe){
            System.err.println("File not found");
        }catch(IOException ie){
            System.err.println("IO Error");
        }catch(openlr.PhysicalFormatException pe){
            System.err.println("Decoding error");
        }
        finally{
            try {
                reader.close();
            }catch(IOException ie){}
        }
    }
}
