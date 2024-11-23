package com.project.Views.UiComponents;

import java.util.HashMap;
import java.util.Map;

public class ProductStatus {
    public static Map<Status, String> statusMap = new HashMap<>() {
        {
            put(Status.PENDING, "Demanat");
            put(Status.PREPARING, "Preparant");
            put(Status.READY, "Llest");
            put(Status.PAID, "Pagat");
        }
    };

    public enum Status {
        PENDING,
        PREPARING,
        READY,
        PAID
    }

    public static String getStatus(Status status) {
        return statusMap.get(status);
    }

    public static Status valueOf(String status) {
        try {
            // Try to get the status from the enum
            return Status.valueOf(status);
        } catch (IllegalArgumentException e) {
            // If the status is not found in the enum, try to get it from the map
            for (Map.Entry<Status, String> entry : statusMap.entrySet()) {
                if (entry.getValue().equals(status)) {
                    return entry.getKey();
                }
            }
            // If the status is not found in the map, try to get from the table column Names
            switch (status) {
                case "demanat": return Status.PENDING;
                case "preparacio": return Status.PREPARING;
                case "llest": return Status.READY;
                case "pagat": return Status.PAID;
            }
            throw new IllegalArgumentException("No se encontró el estado: " + status);
        }
    }
} 