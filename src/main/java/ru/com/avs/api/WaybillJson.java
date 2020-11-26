package ru.com.avs.api;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
class WaybillJson implements Serializable {

    private int id;
    private int waybill;
    private String comment;
    private String mode;
    private String time;
    private String date;
    private List<WaybillJsonItem> weighings;
    private int departmentId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWaybill() {
        return waybill;
    }

    public void setWaybill(int waybill) {
        this.waybill = waybill;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<WaybillJsonItem> getWeighings() {
        return weighings;
    }

    public void setWeighings(List<WaybillJsonItem> weighings) {
        this.weighings = weighings;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
}
