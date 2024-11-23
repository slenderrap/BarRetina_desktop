package com.project.Views.UiComponents;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Command {
    private int id;
    private int tableNumber;
    private double totalPrice;
    private CommandStatus.Status status;
    private LocalDateTime date;
    private ArrayList<CommandProduct> products;

    public Command(int id, int tableNumber, double totalPrice, CommandStatus.Status status, LocalDateTime date, ArrayList<CommandProduct> products) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.totalPrice = totalPrice;
        this.status = status;
        this.date = date;
        this.products = products;
    }

    public Command(JSONObject json) {
        this.id = json.getInt("id_comanda");
        this.tableNumber = json.getInt("id_taula");
        try {
            this.totalPrice = json.getDouble("preu_total");
        } catch (JSONException e) {
            this.totalPrice = 0;
        }
        this.status = CommandStatus.valueOf(json.getString("estat"));
        this.date = LocalDateTime.parse(json.getString("data_comanda"));
        this.products = new ArrayList<>();
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTableNumber() { return tableNumber; }
    public void setTableNumber(int tableNumber) { this.tableNumber = tableNumber; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public CommandStatus.Status getStatus() { return status; }
    public void setStatus(CommandStatus.Status status) { this.status = status; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public ArrayList<CommandProduct> getProducts() { return products; }
    public void setProducts(ArrayList<CommandProduct> products) { this.products = products; }

    public void setProducts(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            products.add(new CommandProduct(json));
        }
    }

    public void addProduct(CommandProduct product) {
        products.add(product);
    }

    public void clearProducts() {
        products.clear();
    }
}

