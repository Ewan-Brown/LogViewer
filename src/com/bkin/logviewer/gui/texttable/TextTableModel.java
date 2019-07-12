package com.bkin.logviewer.gui.texttable;

import com.bkin.logviewer.files.Line;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Jul-12
 * Time: 11:41 AM
 *
 * Will hold all the cell data for the log table. Should be a single column and a row for each log entry
 */
class TextTableModel extends AbstractTableModel
{
	private List<Line> m_data;

	TextTableModel(List<Line> logLines)
	{
		m_data = logLines;
	}

	@Override
	public int getRowCount()
	{
		return m_data.size();
	}

	@Override
	public int getColumnCount()
	{
		return 2;
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		if(columnIndex == 0){
			return "Multiline";
		}else{
			return "Log Lines";
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		if(columnIndex == 1){
			return m_data.get(rowIndex);
		}else{
			return m_data.get(rowIndex).isMultiLine();
		}
	}

	public List<Line> getData()
	{
		return m_data;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		//m_data.get(rowIndex).setText((String)aValue);
	}
}
