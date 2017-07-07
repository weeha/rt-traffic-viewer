package app;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by Florian Noack on 07.07.2017.
 */
public class SettingsController implements Initializable{

    @FXML
    private JFXTextField trafficKeyField;
    @FXML
    private JFXTextField routingKeyField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadConfig();
    }

    public void loadConfig(){
        Properties prop = new Properties();
        InputStream in = getClass().getClassLoader().getResourceAsStream(Main.CONFIG);
        try{
            prop.load(in);
            in.close();
            if(prop != null){
                trafficKeyField.setText(prop.getProperty("trafficKey"));
                routingKeyField.setText(prop.getProperty("routingKey"));
            }
        }catch(IOException ie){
            System.err.println("Error while loading config file");
        }catch(NullPointerException ne){
            System.err.println("No config file found for loading API data");
        }
    }

}
