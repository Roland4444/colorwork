package ru.com.avs.thread;

import ru.com.avs.util.EmbeddedJetty;

public class HttpServerThread extends Thread {

    @Override
    public void run() {
        EmbeddedJetty embeddedJetty = new EmbeddedJetty();
        try {
            embeddedJetty.startJetty(8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
