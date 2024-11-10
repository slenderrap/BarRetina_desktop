package com.project.Views;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;

import com.project.Utils.OnSceneVisible;
import com.project.Utils.UtilsViews;
import com.project.Utils.UtilsWS;
import com.project.Views.UiComponents.Product;
import com.project.Views.UiComponents.ProductUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import org.json.JSONObject;
import org.json.JSONArray;

public class ProductsControler implements OnSceneVisible {

    private UtilsWS ws;
    private ArrayList<Product> products;

    @FXML
    private VBox productsList;

    public void onBackButtonClick() {
        UtilsViews.setView("CommandList");
    }

    @Override
    public void onSceneVisible() {
        ws = UtilsWS.getSharedInstance();
        ws.setOnMessage(this::onMessage);
        JSONObject request = new JSONObject();
        request.put("type", "getProducts");
        ws.safeSend(request.toString());
    }

    public void onMessage(String message) {
        JSONObject json = new JSONObject(message);
        String type = json.getString("type");
        switch (type) {
            case "ack":
                String responseType = json.getString("responseType");
                if (responseType.equals("getProducts")) {
                    Platform.runLater(() -> onGetProductsMessage(json));
                }
                break;
            case "error":
                System.out.println("Error: " + json.getString("message"));
                break;
        }
    }

    public void onGetProductsMessage(JSONObject json) {
        JSONArray products = json.getJSONArray("products");
        this.products = new ArrayList<>();
        productsList.getChildren().clear();
        for (int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            Product p = new Product(product);
            this.products.add(p);
            ProductUI ui = new ProductUI(p);
            productsList.getChildren().add(ui);
        }
    }
}
