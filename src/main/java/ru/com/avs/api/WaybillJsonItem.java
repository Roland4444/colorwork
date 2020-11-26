package ru.com.avs.api;

import java.io.Serializable;
import java.math.BigDecimal;

class WaybillJsonItem implements Serializable {

    private BigDecimal tare;
    private int metalId;
    private BigDecimal clogging;
    private BigDecimal trash;
    private BigDecimal brutto;
    private String photoFull;
    private String photoPreview;

    public BigDecimal getTare() {
        return tare;
    }

    public void setTare(BigDecimal tare) {
        this.tare = tare;
    }

    public int getMetalId() {
        return metalId;
    }

    public void setMetalId(int metalId) {
        this.metalId = metalId;
    }

    public BigDecimal getClogging() {
        return clogging;
    }

    public void setClogging(BigDecimal clogging) {
        this.clogging = clogging;
    }

    public BigDecimal getTrash() {
        return trash;
    }

    public void setTrash(BigDecimal trash) {
        this.trash = trash;
    }

    public BigDecimal getBrutto() {
        return brutto;
    }

    public void setBrutto(BigDecimal brutto) {
        this.brutto = brutto;
    }

    public String getPhotoFull() {
        return photoFull;
    }

    public void setPhotoFull(String photoFull) {
        this.photoFull = photoFull;
    }

    public String getPhotoPreview() {
        return photoPreview;
    }

    public void setPhotoPreview(String photoPreview) {
        this.photoPreview = photoPreview;
    }
}
