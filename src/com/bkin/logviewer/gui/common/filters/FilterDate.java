package com.bkin.logviewer.gui.common.filters;

import com.bkin.logviewer.files.Line;
import com.bkin.logviewer.gui.common.DateLabelFormatter;
import com.bkin.logviewer.gui.texttable.TextTable;
import com.bkin.logviewer.resources.Images;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

/**
 *Contains two Date picker components and a 'function toggle' button that enables or disabled this filter completely. Also has a little calendar icon to the side<br>
 *Filters by a minimum and maximum date(by year month and day), only allowing lines which are dated between (exclusive) the min and max date.
 * <p></p>
 * <b>Rules:</b>
 * <li>If both dates are set to the same day then only that day is allowed.</li>
 * <li>If only a max date is chosen then everything before that date (exclusive) is allowed.</li>
 * <li>If only a min date is chosen then everything after thast date (exclusive) is allowed.</li>
 * <li>If no dates are chosen, or both are set to cleared then all dates are allowed.</li>
 * <li>If a line has no parsed date then it is shown regardless of above settings.</li>
 */
public class FilterDate extends Filter {

    //Two different formats, one is used for the datepicker component (only goes to days) and the other is used for line-dates which go to seconds.
    private static final SimpleDateFormat DATE_FORMAT_DAYS = new SimpleDateFormat("yyyy-MM-dd"); // HH:mm:ss
    public static final SimpleDateFormat DATE_FORMAT_SECONDS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private JCheckBox m_btnToggleDate;
    private JLabel m_lblFunctionIcon;
    private JDatePickerImpl m_dateMinPicker;
    private JDatePickerImpl m_dateMaxPicker;

    public FilterDate(){
        super();
    }

    public void styleControls()
    {
        Icon m_functionIcon = Images.getIcon(Images.CALENDAR);
        m_lblFunctionIcon = new JLabel(m_functionIcon);

        m_btnToggleDate = new JCheckBox("Filter date?");
        m_btnToggleDate.setMaximumSize(new Dimension(100,25));
        m_btnToggleDate.setPreferredSize(new Dimension(100,25));

        m_lblFunctionIcon.setMaximumSize(new Dimension(23, 23));
        m_lblFunctionIcon.setPreferredSize(new Dimension(23, 23));

        m_dateMinPicker = createDatePicker();
        m_dateMaxPicker = createDatePicker();
    }

    private JDatePickerImpl createDatePicker(){
        UtilDateModel model = new UtilDateModel();
        Properties prop = new Properties();
        prop.put("text.today", "Today");
        prop.put("text.month", "Month");
        prop.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, prop);
        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    public void layoutControls()
    {
        setLayout(new BorderLayout(3, 0));
        add(m_lblFunctionIcon, BorderLayout.WEST);
        JPanel datePanel = new JPanel();
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new FlowLayout());
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
        datePanel.add(m_dateMinPicker);
        datePanel.add(m_dateMaxPicker);
        m_dateMinPicker.getJFormattedTextField().setToolTipText("Minimum date for filter. Exclusive");
        m_dateMaxPicker.getJFormattedTextField().setToolTipText("Maximum date for filter. Exclusive");

        m_btnToggleDate.setToolTipText("Toggle date filter");

        outerPanel.add(datePanel);
        outerPanel.add(m_btnToggleDate);

        add(outerPanel);

    }

    public void clear(){
        m_dateMinPicker.getJFormattedTextField().setText("");
        m_dateMaxPicker.getJFormattedTextField().setText("");
    }
    public void addTableListeners(TextTable a){
        m_btnToggleDate.addActionListener(a);
        m_dateMinPicker.addActionListener(a);
        m_dateMaxPicker.addActionListener(a);
    }

    private static Date getDate(JDatePickerImpl datePicker){
        String string = datePicker.getJFormattedTextField().getText();
        return parseDate(string);
    }

    private static Date parseDate(String s){
        Date d = null;
        try {
            d = (s.isEmpty()) ? new Date(0) : DATE_FORMAT_DAYS.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    private Date getMinDate(){
        return getDate(m_dateMinPicker);
    }

    private Date getMaxDate(){
        return getDate(m_dateMaxPicker);
    }

    @Override
    public boolean isAllowed(Line l) {
        boolean isAllowed = false;
        if (m_btnToggleDate.isSelected()) {
            Date lineDate = l.getParsedDate();
            Date minDate = getMinDate();
            Date maxDate = getMaxDate();
                if (lineDate == null || (minDate.getTime() == 0 && maxDate.getTime() == 0)) {
                    isAllowed = true;
                } else {
                    Calendar cal = new GregorianCalendar();
                    cal.setTime(lineDate);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    lineDate = cal.getTime();
                    if (minDate.getTime() == maxDate.getTime() && minDate.getTime() == lineDate.getTime()) {
                        isAllowed = true;
                    } else if (minDate.getTime() == 0 && maxDate.after(lineDate)) {
                        isAllowed = true;
                    } else if (minDate.before(lineDate) && maxDate.getTime() == 0) {
                        isAllowed = true;
                    } else if (minDate.before(lineDate) && maxDate.after(lineDate)) {
                        isAllowed = true;
                    }

                }
        } else {
            isAllowed = true;
        }
        return isAllowed;
    }

}
