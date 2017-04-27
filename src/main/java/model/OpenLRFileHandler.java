package model;

import java.io.*;

/**
 * Created by Florian Noack on 25.04.2017.
 */
public class OpenLRFileHandler {

    private final File file;

    public OpenLRFileHandler(File file){
        this.file = file;
    }

    public void process(){
        BufferedReader reader = null;
        try {
             reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            for (String line; (line = reader.readLine()) != null;) {
                System.out.println(line);
            }
        }catch(FileNotFoundException fe){
            System.err.println("File not found");
        }catch(IOException ie){
            System.err.println("IO Error");
        }finally{
            try {
                reader.close();
            }catch(IOException ie){}
        }
    }
}
