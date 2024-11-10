package com.project.Views;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;

import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.application.Platform;

import com.project.Utils.*;

import org.json.JSONArray;


public class TagsControler implements OnSceneVisible {

    private UtilsWS ws;
    private ArrayList<String> tags;

    @FXML
    private VBox tagsList;

    public void onBackButtonClick() {
        UtilsViews.setView("CommandList");
    }

    @Override
    public void onSceneVisible() {
        ws = UtilsWS.getSharedInstance();
        ws.setOnMessage(this::onMessage);
        JSONObject request = new JSONObject();
        request.put("type", "getTags");
        ws.safeSend(request.toString());
    }

    public void onMessage(String message) {
        JSONObject json = new JSONObject(message);
        String type = json.getString("type");
        switch (type) {
            case "ack":
                String responseType = json.getString("responseType");
                if (responseType.equals("getTags")) {
                    Platform.runLater(() -> onGetTagsMessage(json));
                }
                break;
            case "error":
                System.out.println("Error: " + json.getString("message"));
                break;
        }
    }

    public void onGetTagsMessage(JSONObject json) {
        JSONArray tags = json.getJSONArray("tags");
        this.tags = new ArrayList<>();
        tagsList.getChildren().clear();
        for (int i = 0; i < tags.length(); i++) {
            String tag = tags.getString(i);
            this.tags.add(tag);
            Label tagLabel = new Label(tag);
            tagsList.getChildren().add(tagLabel);
        }
    }

}
