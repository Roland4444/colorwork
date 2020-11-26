package ru.com.avs.model;

import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
@Table(name = "weighings")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weighing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "brutto")
    private BigDecimal brutto;

    @Column(name = "netto")
    private BigDecimal netto;

    @JsonProperty("tare")
    private BigDecimal tare;

    @Column(name = "clogging")
    private BigDecimal clogging;

    @Column(name = "trash")
    private BigDecimal trash;

    @JsonProperty("metalId")
    @Column(name = "metal_id")
    private int metal;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "waybill_id")
    @JsonBackReference
    private Waybill waybill;

    @Transient
    @JsonProperty("photoPreview")
    private String photoPreview;

    @Transient
    @JsonProperty("photoFull")
    private String photoFull;

    @Column(name = "deleted")
    private boolean deleted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getBrutto() {
        return brutto;
    }

    public void setBrutto(BigDecimal brutto) {
        this.brutto = brutto;
    }

    /**
     * Transient netto.
     *
     * @return BigDecimal netto
     */
    @JsonIgnore
    public BigDecimal getNetto() {
        this.brutto.subtract(this.tare);
        BigDecimal sub = brutto.subtract(tare).subtract(trash);
        BigDecimal percentage = clogging
                .divide(new BigDecimal("100.00"))
                .multiply(sub);
        netto = sub.subtract(percentage)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        return netto;
    }

    public void setNetto(BigDecimal netto) {
        this.netto = netto;
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

    @JsonIgnore
    public int getMetal() {
        return metal;
    }

    public void setMetal(Integer metal) {
        this.metal = metal;
    }

    @JsonIgnore
    public Waybill getWaybill() {
        return waybill;
    }

    public void setWaybill(Waybill waybill) {
        this.waybill = waybill;
    }

    public String getPhotoPreview() {
        return photoPreview;
    }

    public void setPhotoPreview(String photoPreview) {
        this.photoPreview = photoPreview;
    }

    public String getPhotoFull() {
        return photoFull;
    }

    public void setPhotoFull(String photoFull) {
        this.photoFull = photoFull;
    }

    public BigDecimal getTare() {
        return tare;
    }

    public void setTare(BigDecimal tare) {
        this.tare = tare;
    }

    @JsonIgnore
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
