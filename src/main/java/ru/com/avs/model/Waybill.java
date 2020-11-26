package ru.com.avs.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "waybills")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Waybill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "waybill")
    private int waybill;

    @Column(name = "department_id")
    private int departmentId;

    @Column(name = "date_create")
    private LocalDate dateCreate;

    @Column(name = "time_create")
    private LocalTime timeCreate;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "upload")
    private boolean upload;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "waybill")
    @JsonManagedReference
    private List<Weighing> weighings;

    @Column(name = "mode")
    private String mode;

    @Column(name = "complete")
    private boolean complete;

    @Column(name = "scale_id")
    private int scaleId;

    @OneToMany(mappedBy = "waybill", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonProperty("tares")
    private List<TempTare> tempTares;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @JsonProperty("date")
    @JsonSerialize(using = JsonDateSerializer.class)
    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    @JsonProperty("time")
    @JsonSerialize(using = JsonTimeSerializer.class)
    public LocalTime getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(LocalTime timeCreate) {
        this.timeCreate = timeCreate;
    }

    @JsonIgnore
    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    public List<Weighing> getWeighings() {
        weighings.removeIf(e -> (e.isDeleted()));
        return weighings;
    }

    public void setWeighings(List<Weighing> weighings) {
        this.weighings = weighings;
    }

    public int getWaybill() {
        return waybill;
    }

    public void setWaybill(int waybill) {
        this.waybill = waybill;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @JsonIgnore
    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public int getScaleId() {
        return scaleId;
    }

    public void setScaleId(int scaleId) {
        this.scaleId = scaleId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<TempTare> getTempTares() {
        return tempTares;
    }

    public void setTempTares(List<TempTare> tempTares) {
        this.tempTares = tempTares;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Waybill waybill1 = (Waybill) o;
        return waybill == waybill1.waybill &&
                Objects.equals(dateCreate, waybill1.dateCreate) &&
                Objects.equals(comment, waybill1.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(waybill, dateCreate, comment);
    }

    /**
     * Concat old entity with new entity.
     *
     * @param newWaybill new entity
     */
    public void concat(Waybill newWaybill) {
        this.comment = newWaybill.getComment();
        this.dateCreate = newWaybill.getDateCreate();
        this.waybill = newWaybill.getWaybill();
    }
}
