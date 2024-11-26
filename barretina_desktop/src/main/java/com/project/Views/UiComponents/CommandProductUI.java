package com.project.Views.UiComponents;

import javafx.beans.property.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.Optional;

import org.json.JSONObject;

import com.project.Utils.ImageUtils;
import com.project.Utils.UtilsWS;
import com.project.Views.UiComponents.ProductStatus.Status;

public class CommandProductUI extends Region {
    private final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private final ObjectProperty<ProductStatus.Status> status = new SimpleObjectProperty<>();
    private final IntegerProperty quantityPaid = new SimpleIntegerProperty();

    // UI Components
    private VBox mainContainer;
    private ComboBox<String> statusComboBox;
    private Runnable onStatusChange;
    private CommandProduct commandProduct;
    private int commandId;

    public CommandProductUI(CommandProduct commandProduct, int commandId, Runnable onStatusChange) {
        this.onStatusChange = onStatusChange;
        this.commandProduct = commandProduct;
        this.commandId = commandId;
        buildLayout();
    }

    public void buildLayout() {
        //Sync properties
        this.product.set(commandProduct.getProduct());
        this.quantity.set(commandProduct.getQuantity());
        this.status.set(commandProduct.getStatus());
        this.quantityPaid.set(commandProduct.getQuantityPaid());

        mainContainer = new VBox();
        this.getChildren().clear();

        if (status.get().equals(ProductStatus.Status.READY) && quantityPaid.get() != 0 && quantityPaid.get() != quantity.get()) {
            mainContainer.getChildren().add(createProductContainer(quantity.get()-quantityPaid.get(), true));
            mainContainer.getChildren().add(createProductContainer(quantityPaid.get(), false));
        } else {
            mainContainer.getChildren().add(createProductContainer(quantity.get(), true));
        }

        mainContainer.setSpacing(10);
        mainContainer.setAlignment(Pos.CENTER_LEFT);
        mainContainer.getStyleClass().add("commandProductMainContainer");
        
        getChildren().add(mainContainer);
        
        setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        requestLayout();
    }


    private HBox createProductContainer(int quantity, boolean combobox) {
        // Initialize UI Components
        Label nameLabel = new Label(product.get().getName());
        nameLabel.getStyleClass().add("nameLabel");

        Label quantityLabel = new Label("x" + quantity);
        quantityLabel.getStyleClass().add("quantityLabel");

        // Create status ComboBox
        Label statusLabel = new Label(ProductStatus.getStatus(ProductStatus.Status.PAID));
        if (combobox) {
            statusComboBox = new ComboBox<>(FXCollections.observableArrayList(
                ProductStatus.statusMap.values()
            ));
            statusComboBox.setValue(ProductStatus.getStatus(status.get()));
            statusComboBox.getStyleClass().add("statusComboBox");
            statusComboBox.setOnAction(e -> {
                String selectedStatus = statusComboBox.getValue();
                commandProduct.setStatus(ProductStatus.valueOf(selectedStatus));
                setStatus(commandProduct.getStatus());
                buildLayout();
                if (onStatusChange != null) {
                    onStatusChange.run();
                }
            });
        }

        Button payButton = new Button("Pagar");
        payButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                PayDialog();
            }
        });

        ImageView imageView = new ImageView(ImageUtils.getImageFromBase64(product.get().getImageBase64()));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        imageView.getStyleClass().add("imageView");

        double totalPrice = product.get().getPrice() * quantity;
        Label priceLabel = new Label(String.format("%.2f€", totalPrice));
        priceLabel.getStyleClass().add("priceLabel");
        HBox productContainer;
        boolean nothingPaid = quantityPaid.get() == 0 && status.get().equals(ProductStatus.Status.READY);
        if (combobox) {
            productContainer = new HBox(imageView, nameLabel, quantityLabel, statusComboBox, priceLabel);
            if (nothingPaid) {
                productContainer.getChildren().add(payButton);
            }
        }
        else {
            productContainer = new HBox(imageView, nameLabel, quantityLabel, statusLabel, priceLabel, payButton);
        }
        productContainer.setSpacing(10);
        productContainer.setAlignment(Pos.CENTER_LEFT);
        productContainer.getStyleClass().add("commandProductContainer");

        return productContainer;
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

    public void PayDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType cancelButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.LEFT);
        ButtonType payAmountButtonType = new ButtonType("Pagar Quantitat", ButtonBar.ButtonData.RIGHT);
        ButtonType payAllButtonType = new ButtonType("Pagar tot", ButtonBar.ButtonData.RIGHT);
        
        alert.getButtonTypes().setAll(cancelButtonType, payAmountButtonType, payAllButtonType);
        alert.setTitle("Pay " + product.get().getName());
        alert.setHeaderText("¿Cuántas unidades desea pagar?");

        TextField amountTextField = new TextField();
        alert.getDialogPane().setContent(amountTextField);
        
        Button payAmountButton = (Button) alert.getDialogPane().lookupButton(payAmountButtonType);
        payAmountButton.addEventFilter(ActionEvent.ACTION, e -> {
            System.out.println("Pay Amount");
            //validate amount
            int amount = 0;
            try {
                amount = Integer.parseInt(amountTextField.getText());
            } catch (NumberFormatException ex) {
                e.consume();
                System.out.println("not a number");
                return;
            }
            if (amount <= 0 || amount > quantity.get() - quantityPaid.get()) {
                e.consume();
                System.out.println("too much");
                return;
            }
        });

        alert.showAndWait().ifPresent(response -> {
            if (response == payAmountButtonType) {
                System.out.println("Pay Amount response");
                int amount = Integer.parseInt(amountTextField.getText());

                System.out.println("amount: " + amount);
                System.out.println("quntity paid: " + commandProduct.getQuantityPaid());
                System.out.println("quntity: " + commandProduct.getQuantity());

                commandProduct.setQuantityPaid(commandProduct.getQuantityPaid()+amount);

                System.out.println("quntity paid: " + commandProduct.getQuantityPaid());
                System.out.println("quntity: " + commandProduct.getQuantity());
                if (commandProduct.getQuantityPaid() == commandProduct.getQuantity()) {
                    commandProduct.setStatus(ProductStatus.Status.PAID);
                }
                JSONObject json = new JSONObject();
                json.put("type", "payAmount");
                json.put("commandId", commandId);
                json.put("productId", commandProduct.getProduct().getId());
                json.put("amount", amount);
                UtilsWS.getSharedInstance().safeSend(json.toString());
                buildLayout();
            }
            else if (response == payAllButtonType) {
                JSONObject json = new JSONObject();
                json.put("type", "payAmount");
                json.put("commandId", commandId);
                json.put("productId", commandProduct.getProduct().getId());
                json.put("amount", commandProduct.getQuantity() - commandProduct.getQuantityPaid());
                commandProduct.setQuantityPaid(commandProduct.getQuantity());
                commandProduct.setStatus(ProductStatus.Status.PAID);
                buildLayout();
                UtilsWS.getSharedInstance().safeSend(json.toString());
            }
        });
    }

    // Getters and setters for properties
    public Product getProduct() { return product.get(); }
    public ObjectProperty<Product> productProperty() { return product; }
    public void setProduct(Product product) { this.product.set(product); }

    public int getQuantity() { return quantity.get(); }
    public IntegerProperty quantityProperty() { return quantity; }
    public void setQuantity(int quantity) { this.quantity.set(quantity); }

    public ProductStatus.Status getStatus() { return status.get(); }
    public ObjectProperty<ProductStatus.Status> statusProperty() { return status; }
    public void setStatus(ProductStatus.Status status) { 
        this.status.set(status);
        statusComboBox.setValue(ProductStatus.getStatus(status));
    }

    public int getQuantityPaid() { return quantityPaid.get(); }
    public IntegerProperty quantityPaidProperty() { return quantityPaid; }
    public void setQuantityPaid(int quantityPaid) { this.quantityPaid.set(quantityPaid); }
}
