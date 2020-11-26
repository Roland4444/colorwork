package ru.com.avs.scalereader;

import static ru.com.avs.util.UserUtils.toByte;

import java.math.BigDecimal;

import jssc.SerialPort;
import ru.com.avs.model.Scale;

abstract class RsReader extends Reader {

    protected SerialPort serialPort;

    RsReader(Scale scale) {
        super(scale);
        serialPort = new SerialPort(this.scale.getPort());
    }

    @Override
    protected byte[] getCommand() {
        return toByte(this.scale.getType().getCommand());
    }

    @Override
    public void connect() throws Exception {
        if (serialPort.openPort()) {
            serialPort.setParams(scale.getType().getSpeed(), 8, 1, 0);
        }
    }

    @Override
    public String readWeight() throws Exception {
        serialPort.writeByte(getCommand()[0]);
        String weight = serialPort.readHexString();
        weight = parseWeight(weight);

        if (weight != null) {
            return new BigDecimal(weight)
                    .setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toString();
        } else {
            return "0.0";
        }
    }

    @Override
    public void disconnect() throws Exception {
        serialPort.closePort();
    }

    protected abstract String parseWeight(String weight);
}
