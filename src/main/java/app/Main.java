package app;


import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main extends Application {

    public static final String CONFIG = "config.properties";

    public void loadConfig(){
        Properties prop = new Properties();
        InputStream in = getClass().getClassLoader().getResourceAsStream(CONFIG);
        try{
            prop.load(in);
            in.close();
            if(prop != null){
                MainController.FLOWS_API_DETAILED = prop.getProperty("FLOWS_API_DETAILED");
                MainController.FLOWS_API = prop.getProperty("FLOWS_API");
                MainController.FLOWS_API_DETAILED_NFF = prop.getProperty("FLOWS_API_DETAILED_NFF");
                MainController.FLOWS_API_DETAILED_FF = prop.getProperty("FLOWS_API_DETAILED_FF");
                MainController.INCIDENTS_API = prop.getProperty("INCIDENTS_API");
            }
        }catch(IOException ie){
            System.err.println("Error while loading config file");
        }catch(NullPointerException ne){
            System.err.println("No config file found for loading API data");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        loadConfig();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainFrame.fxml"));
        primaryStage.setTitle("RT Traffic Viewer");
        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(Main.class.getResource("/css/jfoenix-fonts.css").toExternalForm(),
                Main.class.getResource("/css/jfoenix-design.css").toExternalForm(),
                Main.class.getResource("/css/trffic-viewer-design.css").toExternalForm());
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
