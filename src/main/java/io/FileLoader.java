package io;

import com.jfoenix.controls.JFXPopup;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;


/**
 * Created by Florian Noack on 25.04.2017.
 */
public class FileLoader {

    private final FileChooser fileChooser;
    private Desktop desktop = Desktop.getDesktop();
    private File dataFile = null;

    public FileLoader(){
        fileChooser = new FileChooser();
    }

    public File getDataFile(){
        return this.dataFile;
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
