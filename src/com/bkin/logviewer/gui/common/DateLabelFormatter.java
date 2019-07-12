package com.bkin.logviewer.gui.common;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

// https://www.codejava.net/java-se/swing/how-to-use-jdatepicker-to-display-calendar-component
// https://stackoverflow.com/questions/31277001/jdatepicker-date-formatting

public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_PATTERN);

    @Override
    public Object stringToValue(String text) throws ParseException {
        return DATE_FORMATTER.parseObject(text);
    }

    @Override
    public String valueToString(Object value) {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return DATE_FORMATTER.format(cal.getTime());
        }

        return "";
    }

}