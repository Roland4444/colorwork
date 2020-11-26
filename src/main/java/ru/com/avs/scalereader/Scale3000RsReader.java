package ru.com.avs.scalereader;

import static ru.com.avs.util.UserUtils.removeSpaces;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.DatatypeConverter;
import ru.com.avs.model.Scale;

public class Scale3000RsReader extends RsReader {

    public Scale3000RsReader(Scale scale) {
        super(scale);
    }

    protected String parseWeight(String weight) {
        if (weight != null) {
            String hex = removeSpaces(weight);
            Pattern pattern = Pattern.compile("7777(.*?)6B670D0A");
            Matcher matcher = pattern.matcher(hex);
            if (matcher.find()) {
                byte[] bytes = DatatypeConverter.parseHexBinary(matcher.group(1));
                weight = new BigDecimal(new String(bytes, StandardCharsets.UTF_8)).toString();
            }
        }
        return weight;
    }
}
