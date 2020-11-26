package ru.com.avs.scalereader;

import ru.com.avs.model.Scale;

public class ReaderFactory {

    /**
     * Create specify reader.
     * @param scale scale
     * @return specify reader
     * @throws Exception can't create reader
     */
    public static Reader createReader(Scale scale) throws Exception {
        if (scale.getConnectionType().equals("rs232")) {
            switch (scale.getType()) {
                case SCALE_600:
                    return new Scale600RsReader(scale);
                case SCALE_3000_2:
                    return new Scale3002RsReader(scale);
                case SCALE_3000:
                    return new Scale3000RsReader(scale);
                default:
                    throw new Exception("Can't create Reader " + scale.getType() + " " + scale.getConnectionType());
            }
        } else {
            switch (scale.getType()) {
                case SCALE_60:
                    return new Scale60EthReader(scale);
                case SCALE_600:
                    return new Scale600EthReader(scale);
                default:
                    throw new Exception("Can't create Reader " + scale.getType() + " " + scale.getConnectionType());
            }
        }
    }
}
