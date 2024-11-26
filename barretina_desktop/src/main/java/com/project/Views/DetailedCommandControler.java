package com.project.Views;

import org.json.JSONArray;
import org.json.JSONObject;

import com.project.Utils.OnSceneVisible;
import com.project.Utils.UtilsViews;
import com.project.Utils.UtilsWS;
import com.project.Views.UiComponents.Command;
import com.project.Views.UiComponents.CommandProduct;
import com.project.Views.UiComponents.CommandProductUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class DetailedCommandControler implements OnSceneVisible {
    
    private UtilsWS ws;
    private Command command;
    private int commandId;
    @FXML
    private VBox productList;

    public void setCommand(Command command) {
        this.command = command;
        this.commandId = command.getId();
    }

    @Override
    public void onSceneVisible() {
        command.clearProducts();
        ws = UtilsWS.getSharedInstance();
        ws.setOnMessage(this::onMessage);
        JSONObject json = new JSONObject();
        json.put("type", "getCommand");
        json.put("commandId", commandId);
        ws.safeSend(json.toString());
    }

    public void onMessage(String message) {
        JSONObject json = new JSONObject(message);
        String type = json.getString("type");
        switch (type) {
            case "ack":
                String responseType = json.getString("responseType");
                if (responseType.equals("getCommand")) {
                    Platform.runLater(() -> onGetCommandMessage(json));
                }
                break;
            case "error":
                System.out.println("Error: " + json.getString("message"));
                break;
        }
    }

    public void back() {
        UtilsViews.setView("CommandList");
    }

    public void onGetCommandMessage(JSONObject json) {
        JSONArray jsonArray = json.getJSONArray("products");
        command.clearProducts();
        productList.getChildren().clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject productJson = jsonArray.getJSONObject(i);
            System.out.println(productJson.toString());
            CommandProduct product = new CommandProduct(productJson);
            command.addProduct(product);
            productList.getChildren().add(new CommandProductUI(product, () -> {
                //no trigger
            }));
        }
    }

}
