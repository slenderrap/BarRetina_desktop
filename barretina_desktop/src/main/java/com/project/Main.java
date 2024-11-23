package com.project;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import com.project.Utils.UtilsConfig;
import com.project.Utils.UtilsViews;
import com.project.Utils.UtilsWS;

public class Main extends Application {
    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage stage) throws Exception {
        UtilsViews.addView(Main.class, "DetailCommand", "/fxml/detail_command.fxml", "/css/detail_command.css");
        UtilsViews.addView(Main.class, "Configuration", "/fxml/configuration.fxml", "/css/configuration.css");
        UtilsViews.addView(Main.class, "CommandList", "/fxml/command_list.fxml", "/css/command_list.css");
        UtilsViews.addView(Main.class, "Products", "/fxml/products.fxml", "/css/products.css");
        UtilsViews.addView(Main.class, "Tags", "/fxml/tags.fxml", "/css/tags.css");
        if (!UtilsConfig.configExists()) {
            UtilsViews.setView("Configuration");
        } else {
            UtilsWS.init(UtilsConfig.getConfig().getServerUrl().toString());
            UtilsViews.setView("CommandList");
        }
        
        Scene scene = new Scene(UtilsViews.parentContainer);
        stage.setScene(scene);
        stage.setTitle("BarRetina");
        stage.show();
    }
}
