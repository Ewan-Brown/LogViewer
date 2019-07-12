package com.bkin.logviewer.gui.texttable;

import com.bkin.logviewer.files.Line;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Jul-12
 * Time: 11:43 AM
 *
 * This cell will be an entire row in the log table to represent a single line in the log.
 */

class TextCellRenderer extends DefaultTableCellRenderer
{
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component c = super.getTableCellRendererComponent(table,
				value, isSelected, hasFocus, row, column);
		Line l = (Line)value;
		if (isSelected)
		{
			this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
			c.setBackground(Color.WHITE);
		}
		else{
			c.setBackground(l.getType().getColour());
		}
		c.setForeground(Color.BLACK);

		return this;
	}
	public void setValue(Object value){
		setText(((Line)value).getText());
	}

}