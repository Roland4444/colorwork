package ru.com.avs.scalereader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import ru.com.avs.model.Scale;

public class Scale600EthReader extends EthReader {

    public Scale600EthReader(Scale scale) {
        super(scale);
    }

    @Override
    public String readWeight() throws IOException {
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        out.write(this.scale.getEthCmdBytes());
        byte[] data = new byte[20];
        int c = in.read(data);
        int decimal6 = (int) data[6] & 0xff;
        int decimal7 = (int) data[7] & 0xff;

        String hex6 = Integer.toHexString(decimal6);
        String hex7 = Integer.toHexString(decimal7);
        String weightHex = hex7 + hex6;
        float weight = (float) Integer.parseInt(weightHex, 16) / 10;
        return String.valueOf(weight);
    }
}
