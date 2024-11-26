package com.project.Utils;

import com.project.Views.UiComponents.Product;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class UtilsProduct {

    private static UtilsProduct instance;
    public static ArrayList<Product> products = new ArrayList<>();
    public static UtilsWS ws;

    private UtilsProduct() {
        ws = UtilsWS.getSharedInstance();
    }

    public static UtilsProduct getInstance() {
        if (instance == null) {
            instance = new UtilsProduct();
        }
        return instance;
    }

    public Product getProduct(int id) {
        if (!products.isEmpty()) {
            for (Product product : products) {
                if (product.getId() == id) {
                    return product;
                }
            }
        }
        CountDownLatch latch = new CountDownLatch(1);
        ws.setOnMessage((json) -> {
            try {
                JSONObject jsonObject = new JSONObject(json);
                String type = jsonObject.getString("type");
                if (!type.equals("ack")) {
                    return;
                }
                String responseType = jsonObject.getString("responseType");
                if (!responseType.equals("getProducts")) {
                    return;
                }
                JSONArray jsonArray = jsonObject.getJSONArray("products");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject productJson = jsonArray.getJSONObject(i);
                    products.add(new Product(productJson));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });
        JSONObject request = new JSONObject();
        request.put("type", "getProducts");
        ws.safeSend(request.toString());
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }

        return null;
    }
}
