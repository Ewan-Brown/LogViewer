package com.bkin.logviewer.gui.texttable;

import com.bkin.logviewer.resources.Images;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

class CheckCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component c = super.getTableCellRendererComponent(table,
                value, isSelected, hasFocus, row, column);
        return c;
    }

    public void setValue(Object value){
        if(((Boolean)value)) {
            setIcon(Images.getIcon(Images.PLUS));
        }
        else{
            setIcon(null);
        }
    }

}
