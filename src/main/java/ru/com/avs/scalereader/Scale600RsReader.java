package ru.com.avs.scalereader;

import ru.com.avs.model.Scale;

public class Scale600RsReader extends RsReader {

    public Scale600RsReader(Scale scale) {
        super(scale);
    }

    protected String parseWeight(String weight) {
        if (weight != null) {
            String[] results = weight.split(" ");
            if (results.length >= 4) {
                String hx = results[3] + results[2];
                weight = String.valueOf((Integer.parseInt(hx, 16)) * 0.1);
            }
        }
        return weight;
    }
}
