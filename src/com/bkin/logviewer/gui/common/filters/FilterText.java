package com.bkin.logviewer.gui.common.filters;

import com.bkin.logviewer.files.Line;
import com.bkin.logviewer.gui.texttable.TextTable;
import com.bkin.logviewer.resources.Images;

import javax.swing.*;
import java.awt.*;

import static com.bkin.logviewer.gui.utils.ResultsProvider.containsIgnoreCase;

/**
 * Contains a single box for text, with a filter icon beside it.
 * Filters text based on an input string. Only lines containing the input string (case ignored) are allowed. <br>
 * if the text box is empty then all lines are allowed. This will also search through extra lines of text if this line has them.
 */
public class FilterText extends Filter{

    private JLabel m_lblFunctionIcon;
    private JTextField m_txtSearch;

    public FilterText(){
        super();
    }

    public void styleControls()
    {
        Icon m_functionIcon;
        m_functionIcon = Images.getIcon(Images.FILTER);
        m_lblFunctionIcon = new JLabel(m_functionIcon);
        m_lblFunctionIcon.setMaximumSize(new Dimension(23, 23));
        m_lblFunctionIcon.setPreferredSize(new Dimension(23, 23));
        m_txtSearch = new JTextField("");
        m_txtSearch.setPreferredSize(new Dimension(150, 23));
        m_txtSearch.setMinimumSize(new Dimension(100, 23));

        m_txtSearch.setToolTipText("Filter by text");

    }

    public void layoutControls()
    {
        setLayout(new BorderLayout(3, 0));
        add(m_lblFunctionIcon, BorderLayout.WEST);
        add(m_txtSearch, BorderLayout.CENTER);
    }

    public void clear(){
        m_txtSearch.setText("");
    }
    public void addTableListeners(TextTable a){
        m_txtSearch.getDocument().addDocumentListener(a);
    }

    @Override
    public boolean isAllowed(Line l) {
        boolean isAllowed = false;
        if (containsIgnoreCase(l.getText(), getText())){
            isAllowed = true;
        }
        return isAllowed;
    }

    private String getText()
    {
        return m_txtSearch.getText();
    }

}
