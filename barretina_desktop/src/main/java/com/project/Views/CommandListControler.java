package com.project.Views;

import org.json.JSONArray;
import org.json.JSONObject;

import com.project.Utils.OnSceneVisible;
import com.project.Utils.UtilsViews;
import com.project.Utils.UtilsWS;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import java.util.ArrayList;

import com.project.Views.UiComponents.Command;
import com.project.Views.UiComponents.CommandUI;

class GetCommandsThread implements Runnable {
    private UtilsWS ws;
    private int interval;

    public GetCommandsThread(UtilsWS ws, int interval) {
        this.ws = ws;
        this.interval = interval;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            JSONObject request = new JSONObject();
            request.put("type", "getCommands");
            ws.safeSend(request.toString());
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

public class CommandListControler implements OnSceneVisible {
    
    private UtilsWS ws;
    @FXML
    private VBox commandsList;
    private ArrayList<Command> commands;
    private Thread getCommandsThread;

    public void onSceneVisible() {
        ws = UtilsWS.getSharedInstance();
        ws.setOnMessage(this::onMessage);
        getCommandsThread = new Thread(new GetCommandsThread(ws, 1000));
        getCommandsThread.start();
    }

    public void onProductsButtonClick() {
        UtilsViews.setView("Products");
        getCommandsThread.interrupt();
    }

    public void onTagsButtonClick() {
        UtilsViews.setView("Tags");
        getCommandsThread.interrupt();
    }

    public void onMessage(String message) {
        JSONObject json = new JSONObject(message);
        String type = json.getString("type");
        switch (type) {
            case "ack":
                String responseType = json.getString("responseType");
                if (responseType.equals("getCommands")) {
                    Platform.runLater(() -> onGetCommandsMessage(json));
                }
                break;
            case "error":
                System.out.println("Error: " + json.getString("message"));
                break;
        }
    }

    public void onGetCommandsMessage(JSONObject json) {
        JSONArray commandsArray = json.getJSONArray("commands");
        commands = new ArrayList<>();
        commandsList.getChildren().clear();
        // Fill the list with commands
        for (int i = 0; i < commandsArray.length(); i++) {
            JSONObject commandJson = commandsArray.getJSONObject(i);
            Command cmd = new Command(commandJson);
            commands.add(cmd);
        }
        //  Sort the commands by date
        commands.sort((c1, c2) -> -c1.getDate().compareTo(c2.getDate()));
        // Fill the UI list with commands
        for (Command cmd : commands) {
            CommandUI cmdUI = new CommandUI(cmd, () -> onCommandClick(cmd));
            commandsList.getChildren().add(cmdUI);
        }
    }

    public void onCommandClick(Command command) {
        DetailedCommandControler detailedCommandControler = (DetailedCommandControler) UtilsViews.getController("DetailCommand");
        detailedCommandControler.setCommand(command);
        UtilsViews.setView("DetailCommand");
        getCommandsThread.interrupt();
    }
}
