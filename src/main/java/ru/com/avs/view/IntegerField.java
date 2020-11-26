package ru.com.avs.view;

import javafx.scene.control.TextField;

public class IntegerField extends TextField {

    /**
     * Getting Integer value from TextField.
     *
     * @return Integer value
     */
    public Integer getInteger() {
        if (this.getText().length() == 0) {
            return 0;
        } else {
            return new Integer(this.getText());
        }
    }

    /**
     * Setting Integer value on TextField.
     *
     * @param value Integer value
     */
    public void setInteger(Integer value) {
        if (value == null) {
            this.setText("0");
        } else {
            this.setText(value.toString());
        }
    }
}
