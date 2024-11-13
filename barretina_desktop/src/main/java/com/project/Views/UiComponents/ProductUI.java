package com.project.Views.UiComponents;

import javafx.beans.property.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import java.util.List;
import javafx.geometry.Pos;

import com.project.Utils.ImageUtils;
public class ProductUI extends Region {
    private final IntegerProperty productId = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final ListProperty<String> tags = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final StringProperty description = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();

    // UI Components
    private final HBox mainContainer;
    private final Label nameLabel;
    private final VBox tagsList;
    private final Label descriptionLabel;
    private final Label priceLabel;
    private final ImageView imageView;

    public ProductUI(Product product) {
        //Initialize properties
        this.productId.set(product.getId());
        this.name.set(product.getName());
        this.tags.setAll(product.getTags());
        this.description.set(product.getDescription());
        this.price.set(product.getPrice());

        //Initialize UI Components
        nameLabel = new Label(name.get());
        nameLabel.getStyleClass().add("nameLabel");
        descriptionLabel = new Label(description.get());
        descriptionLabel.getStyleClass().add("descriptionLabel");
        priceLabel = new Label(price.get() + "€");
        priceLabel.getStyleClass().add("priceLabel");
        tagsList = new VBox();
        tagsList.getStyleClass().add("tagsList");
        for (String tag : tags) {
            Label tagLabel = new Label(tag);
            tagLabel.getStyleClass().add("tag");
            tagsList.getChildren().add(tagLabel);
        }
        tagsList.setAlignment(Pos.CENTER_LEFT);
        imageView = new ImageView(ImageUtils.getImageFromBase64(product.getImageBase64()));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        imageView.getStyleClass().add("imageView");

        mainContainer = new HBox(imageView, nameLabel, tagsList, descriptionLabel, priceLabel);
        mainContainer.setSpacing(10);
        mainContainer.setAlignment(Pos.CENTER_LEFT);
        mainContainer.getStyleClass().add("productContainer");
        getChildren().add(mainContainer);

        setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
    }

    @Override
    protected void layoutChildren() {
        // Layout the container within this region
        mainContainer.resizeRelocate(
            getInsets().getLeft(),
            getInsets().getTop(),
            getWidth() - getInsets().getLeft() - getInsets().getRight(),
            getHeight() - getInsets().getTop() - getInsets().getBottom()
        );
    }

    public int getProductId() {
        return productId.get();
    }

    public IntegerProperty productIdProperty() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId.set(productId);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public List<String> getTags() {
        return tags.get();
    }

    public ListProperty<String> tagsProperty() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags.setAll(tags);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }
}
