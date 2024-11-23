package com.project.Views.UiComponents;

import javafx.beans.property.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;

import com.project.Utils.ImageUtils;

public class CommandProductUI extends Region {
    private final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private final ObjectProperty<ProductStatus.Status> status = new SimpleObjectProperty<>();
    private final IntegerProperty quantityPaid = new SimpleIntegerProperty();

    // UI Components
    private VBox mainContainer;
    private ComboBox<String> statusComboBox;

    public CommandProductUI(CommandProduct commandProduct, Runnable onStatusChange) {
        // Initialize properties
        this.product.set(commandProduct.getProduct());
        this.quantity.set(commandProduct.getQuantity());
        this.status.set(commandProduct.getStatus());
        this.quantityPaid.set(commandProduct.getQuantityPaid());

        buildLayout(commandProduct, onStatusChange);

    }

    private void buildLayout(CommandProduct commandProduct, Runnable onStatusChange) {
        mainContainer = new VBox();
        this.getChildren().clear();

        if (status.get().equals(ProductStatus.Status.READY) && quantityPaid.get() != 0 && quantityPaid.get() != quantity.get()) {
            mainContainer.getChildren().add(createProductContainer(commandProduct, onStatusChange, quantity.get()-quantityPaid.get(), true));
            mainContainer.getChildren().add(createProductContainer(commandProduct, onStatusChange, quantityPaid.get(), false));
        } else {
            mainContainer.getChildren().add(createProductContainer(commandProduct, onStatusChange, quantity.get(), true));
        }

        mainContainer.setSpacing(10);
        mainContainer.setAlignment(Pos.CENTER_LEFT);
        mainContainer.getStyleClass().add("commandProductMainContainer");
        
        getChildren().add(mainContainer);
        
        setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        requestLayout();
    }


    private HBox createProductContainer(CommandProduct commandProduct, Runnable onStatusChange, int quantity, boolean combobox) {
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
                buildLayout(commandProduct, onStatusChange);
                if (onStatusChange != null) {
                    onStatusChange.run();
                }
            });
        }

        ImageView imageView = new ImageView(ImageUtils.getImageFromBase64(product.get().getImageBase64()));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        imageView.getStyleClass().add("imageView");

        double totalPrice = product.get().getPrice() * quantity;
        Label priceLabel = new Label(String.format("%.2f€", totalPrice));
        priceLabel.getStyleClass().add("priceLabel");
        HBox productContainer;
        if (combobox) {
            productContainer = new HBox(imageView, nameLabel, quantityLabel, statusComboBox, priceLabel);
        }
        else
            productContainer = new HBox(imageView, nameLabel, quantityLabel, statusLabel, priceLabel);
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
