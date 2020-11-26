package ru.com.avs.thread;

import static ru.com.avs.util.UserUtils.waiting;

import javafx.scene.control.Label;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.com.avs.model.Scale;
import ru.com.avs.scalereader.Reader;
import ru.com.avs.scalereader.ReaderFactory;
import ru.com.avs.util.Utils;

@Component("ScaleThread")
@Scope("prototype")
public class ScaleThread extends Thread {

    String weight = "0.0";
    private Label lblScaleWeight;
    private boolean isActive;
    private final Scale scale;

    /**
     * Constructor.
     *
     * @param lblScaleWeight Label
     * @param scale       Scale
     */
    public ScaleThread(Label lblScaleWeight, Scale scale) {
        this.lblScaleWeight = lblScaleWeight;
        this.scale = scale;
        this.isActive = true;
    }

    @Override
    public void run() {
        boolean isConnected = false;
        Reader weightReader = null;
        try {
            weightReader = ReaderFactory.createReader(this.scale);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (!isConnected && isActive && weightReader != null) {
            try {
                weightReader.connect();
                isConnected = true;
                while (isActive) {
                    weight = weightReader.readWeight();
                    Utils.onFxThread(lblScaleWeight, weight);
                    waiting(1000);
                }
                weightReader.disconnect();
            } catch (Exception e) {
                waiting(1000);
                continue;
            }
            waiting(1000);
        }
    }

    /**
     * Getting weight.
     */
    public String getWeight() {
        return weight;
    }

    /**
     * Stopping thread.
     */
    public void stopThread() {
        this.isActive = false;
        do {
            if (!this.isAlive()) {
                this.interrupt();
            }
        } while (this.isAlive());
    }
}
