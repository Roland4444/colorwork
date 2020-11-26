package ru.com.avs.scalereader;

import java.net.Socket;
import ru.com.avs.model.Scale;

abstract class EthReader extends Reader {

    protected Socket socket;

    EthReader(Scale scale) {
        super(scale);
    }

    @Override
    protected byte[] getCommand() {
        return this.scale.getEthCmdBytes();
    }

    @Override
    public void connect() throws Exception {
        socket = new Socket(this.scale.getIp(), Integer.parseInt(this.scale.getEthPort()));
    }

    @Override
    public void disconnect() throws Exception {
        socket.close();
    }
}
