package com.project.Views.UiComponents;

import java.util.HashMap;
import java.util.Map;

public class CommandStatus {
    public static Map<Status, String> statusMap = new HashMap<>() {
        {
            put(Status.PENDING, "Sense pagar");
            put(Status.PAYING, "Pagant");
            put(Status.PAID, "Pagada");
        }
    };

    public enum Status {
        PENDING,
        PAYING,
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
                case "sense_pagar": return Status.PENDING;
                case "efectuant_pagament": return Status.PAYING;
                case "pagat": return Status.PAID;
            }
            // If the status is not found in the table column Names, throw an exception
            throw new IllegalArgumentException("No se encontró el estado: " + status);
        }
    }
}