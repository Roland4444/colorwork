package ru.com.avs.model;

public class ThreadModel {

    public static final String CAMERA_THREAD = "camera";
    public static final String SCALE_THREAD = "scale";

    private Thread thread;
    private String type;
    private int scaleId;

    /**
     * Constructor.
     *
     * @param thread  Thread
     * @param type    type of thread
     * @param scaleId Scale.id
     */
    public ThreadModel(Thread thread, String type, int scaleId) {
        this.thread = thread;
        this.type = type;
        this.scaleId = scaleId;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getScaleId() {
        return scaleId;
    }

    public void setScaleId(int scaleId) {
        this.scaleId = scaleId;
    }
}
