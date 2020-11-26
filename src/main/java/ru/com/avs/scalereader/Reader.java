package ru.com.avs.scalereader;

import ru.com.avs.model.Scale;

public abstract class Reader {

    protected Scale scale;

    Reader(Scale scale) {
        this.scale = scale;
    }

    protected abstract byte[] getCommand();

    public abstract void connect() throws Exception;

    public abstract String readWeight() throws Exception;

    public abstract void disconnect() throws Exception;
}
