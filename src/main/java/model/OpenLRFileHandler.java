package model;

import java.io.*;

/**
 * Created by flori on 25.04.2017.
 */
public class OpenLRFileHandler {

    private final File file;

    public OpenLRFileHandler(File file){
        this.file = file;
    }

    public void process(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            for (String line; (line = reader.readLine()) != null;) {
                System.out.println(line);
            }
        }catch(FileNotFoundException fe){
            System.err.println("File not found");
        }catch(IOException ie){
            System.err.println("IO Error");
        }
    }
}
