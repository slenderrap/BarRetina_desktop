package com.project.Utils;

import javafx.scene.image.Image;

import java.util.Base64;
import java.io.ByteArrayInputStream;

public class ImageUtils {
    public static Image getImageFromBase64(String base64) {
        return new Image(new ByteArrayInputStream(Base64.getDecoder().decode(base64)));
    }
}
