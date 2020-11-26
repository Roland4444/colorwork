package ru.com.avs.view;

import java.math.BigDecimal;
import javafx.scene.control.TextField;

public class DecimalField extends TextField {

    public BigDecimal getDecimal() {
        return new BigDecimal(this.getText());
    }

    /**
     * Setting BigDecimal value on TextField.
     *
     * @param value BigDecimal value
     */
    public void setDecimal(BigDecimal value) {
        if (value == null) {
            this.setText("0.0");
        } else {
            this.setText(value.toString());
        }
    }
}
