package ru.com.avs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "SCALES")
public class Scale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "port")
    private String port;

    @Column(name = "type")
    @Enumerated(EnumType.ORDINAL)
    private ScaleType type;

    @Column(name = "camera")
    private String camera;

    @OneToOne
    @JoinColumn(name = "mode_id")
    private Mode mode;

    @Column(name = "connection_type ")
    private String connectionType;

    @Column(name = "ip")
    private String ip;

    @Column(name = "eth_port ")
    private String ethPort;

    @Column(name = "eth_cmd")
    private String ethCmd;

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

    @JsonIgnore
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public ScaleType getType() {
        return type;
    }

    public void setType(ScaleType type) {
        this.type = type;
    }

    @JsonIgnore
    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    @JsonIgnore
    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getEthPort() {
        return ethPort;
    }

    public void setEthPort(String ethPort) {
        this.ethPort = ethPort;
    }

    public String getEthCmd() {
        return ethCmd;
    }

    /**
     * Get command as bytes array.
     * @return byte[]
     */
    @JsonIgnore
    public byte[] getEthCmdBytes() {
        String[] bytes = this.getEthCmd().split(" ");
        byte[] cmd = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            cmd[i] = (byte) Integer.parseInt(bytes[i], 16);
        }
        return cmd;
    }

    public void setEthCmd(String ethCmd) {
        this.ethCmd = ethCmd;
    }
}
