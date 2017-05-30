package io;

import com.jfoenix.controls.JFXPopup;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;

import javax.xml.stream.*;
import java.awt.Desktop;
import java.io.*;


/**
 * Created by Florian Noack on 25.04.2017.
 */
public class FileLoader {

    private final FileChooser fileChooser;
    private Desktop desktop = Desktop.getDesktop();
    private File dataFile = null;
    private final String FLOW_IDENTIFIER = "elaboratedData";

    public FileLoader(){
        fileChooser = new FileChooser();
    }

    public File getDataFile(){
        return this.dataFile;
    }

    public String getDataFormat(){
        if(dataFile != null)
            return FilenameUtils.getExtension(dataFile.getAbsolutePath());
        else
            return "";
    }

    public boolean isFlowFile(){
        if(getDataFormat().equals("xml")){
            BufferedReader reader = null;
            XMLStreamReader xml = null;
            try{
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)));
                XMLInputFactory factory;
                xml = XMLInputFactory.newInstance().createXMLStreamReader(reader);
                while(xml.hasNext()){
                    if (xml.next() == XMLStreamConstants.START_ELEMENT
                            && FLOW_IDENTIFIER.equals(xml.getLocalName())) {
                        return true;
                    }
                }
            }catch(FileNotFoundException fe){}
            catch (XMLStreamException xe){}
            finally{
                try {
                    xml.close();
                }catch(XMLStreamException e){}
            }
        }
        return false;
    }

    public void startFileChooser(){
        fileChooser.setTitle("Open Resource File");
        dataFile = fileChooser.showOpenDialog(new JFXPopup());
    }

    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            System.err.println("Couldn't load File");
        }
    }

}
