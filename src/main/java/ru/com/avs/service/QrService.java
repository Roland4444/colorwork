package ru.com.avs.service;

import javafx.scene.image.WritableImage;

public interface QrService {
    WritableImage generate(String content);
}
