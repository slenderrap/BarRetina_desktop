package com.project.Views.UiComponents;

import javafx.beans.property.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import java.time.format.DateTimeFormatter;

public class CommandUI extends Region {
    private final IntegerProperty commandId = new SimpleIntegerProperty();
    private final IntegerProperty tableNumber = new SimpleIntegerProperty();
    private final DoubleProperty totalPrice = new SimpleDoubleProperty();
    private final ObjectProperty<CommandStatus.Status> status = new SimpleObjectProperty<>();
    
    // UI Components
    private final HBox mainContainer;
    private final Label idLabel;
    private final Label tableLabel;
    private final Label statusLabel;
    private final Label priceLabel;
    private final Label dateLabel;

    public CommandUI(Command command, Runnable onClick) {
        // Initialize properties
        this.commandId.set(command.getId());
        this.tableNumber.set(command.getTableNumber());
        this.totalPrice.set(command.getTotalPrice());
        this.status.set(command.getStatus());

        // Initialize UI Components
        idLabel = new Label("Comanda #" + commandId.get());
        idLabel.getStyleClass().add("idLabel");
        
        tableLabel = new Label("Taula " + tableNumber.get());
        tableLabel.getStyleClass().add("tableLabel");
        
        statusLabel = new Label(CommandStatus.getStatus(status.get()));
        statusLabel.getStyleClass().addAll("statusLabel", status.get().toString());
        
        priceLabel = new Label(String.format("%.2f€", totalPrice.get()));
        priceLabel.getStyleClass().add("priceLabel");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        dateLabel = new Label(command.getDate().format(formatter));
        dateLabel.getStyleClass().add("dateLabel");

        mainContainer = new HBox(idLabel, tableLabel, statusLabel, dateLabel, priceLabel);
        mainContainer.setSpacing(10);
        mainContainer.setAlignment(Pos.CENTER_LEFT);
        mainContainer.getStyleClass().add("commandContainer");
        //Add event listener to the main container
        mainContainer.setOnMouseClicked(event -> onClick.run());
        
        getChildren().add(mainContainer);
        
        setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
    }

    @Override
    protected void layoutChildren() {
        mainContainer.resizeRelocate(
            getInsets().getLeft(),
            getInsets().getTop(),
            getWidth() - getInsets().getLeft() - getInsets().getRight(),
            getHeight() - getInsets().getTop() - getInsets().getBottom()
        );
    }

    // Getters and setters for properties
    public int getCommandId() { return commandId.get(); }
    public IntegerProperty commandIdProperty() { return commandId; }
    public void setCommandId(int commandId) { this.commandId.set(commandId); }

    public int getTableNumber() { return tableNumber.get(); }
    public IntegerProperty tableNumberProperty() { return tableNumber; }
    public void setTableNumber(int tableNumber) { this.tableNumber.set(tableNumber); }

    public double getTotalPrice() { return totalPrice.get(); }
    public DoubleProperty totalPriceProperty() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice.set(totalPrice); }

    public CommandStatus.Status getStatus() { return status.get(); }
    public ObjectProperty<CommandStatus.Status> statusProperty() { return status; }
    public void setStatus(CommandStatus.Status status) { this.status.set(status); }
}
