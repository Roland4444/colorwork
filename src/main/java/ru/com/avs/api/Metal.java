package ru.com.avs.api;

import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
class Metal {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MetalsData {

        @JsonProperty("data")
        private List<Metal> metalList;

        @JsonProperty("status")
        private String status;

        public List<Metal> getMetalList() {
            return metalList;
        }

        public void setMetalList(List<Metal> metalList) {
            this.metalList = metalList;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
