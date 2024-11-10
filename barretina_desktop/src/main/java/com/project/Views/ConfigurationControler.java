package com.project.Views;

import java.net.URI;
import java.net.URISyntaxException;

import com.project.Utils.ErrorPopup;
import com.project.Utils.UtilsConfig;
import com.project.Utils.UtilsViews;
import com.project.Utils.UtilsWS;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ConfigurationControler {

    @FXML
    private TextField serverUrlTextField;

    @FXML
    private TextField placeTextField;

    public void onSaveButtonClick() {
        URI serverUrl = null;
        String inputUrl = serverUrlTextField.getText();
        if (!inputUrl.matches("^[a-zA-Z][a-zA-Z0-9+.-]*://.*")) {
            ErrorPopup.showError("Invalid server URL", "The server URL must start with a valid scheme", 5, UtilsViews.getStage());
            return;
        }
        try {
            serverUrl = new URI(inputUrl);
        } catch (URISyntaxException e) {
            ErrorPopup.showError("Invalid server URL", "The server URL is not a valid URI", 5, UtilsViews.getStage());
            return;
        }
        UtilsConfig.saveConfig(serverUrl, placeTextField.getText());
        UtilsWS.init(serverUrl.toString());
        UtilsViews.setView("CommandList");
    }
}
