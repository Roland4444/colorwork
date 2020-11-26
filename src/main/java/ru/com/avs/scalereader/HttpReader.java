package ru.com.avs.scalereader;

import ru.com.avs.model.Scale;

abstract class HttpReader extends Reader {

    HttpReader(Scale scale) {
        super(scale);
    }

    @Override
    protected byte[] getCommand() {
        return null;
    }

    @Override
    public void connect() throws Exception {
    }

    @Override
    public void disconnect() throws Exception {
    }
}
