package com.bkin.logviewer.gui.usage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2017-Feb-06
 * Time: 1:47 PM
 *
 * A simple table to display results from a com.bkin.logviewer.usage query.
 */
class UsageJTable extends JTable
{
	public UsageJTable()
	{

	}

	public void setData(ResultSet set) throws SQLException, NullPointerException
	{
		TableModel model = buildTableModel(set);
		setRowSorter(new TableRowSorter<>(model));
		setModel(model);
	}

	// http://stackoverflow.com/a/34070830/1818526
	private static TableModel buildTableModel(ResultSet resultSet) throws SQLException
	{
		Vector<String> columnNames = new Vector<>();
		Vector<Vector<Object>> dataVector = new Vector<>();

		int columnCount = resultSet.getMetaData().getColumnCount();

		// Column names.
		for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++)
		{
			columnNames.add(resultSet.getMetaData().getColumnName(columnIndex));
		}

		// VersionFolder of the table.
		while (resultSet.next())
		{
			Vector<Object> rowVector = new Vector<>();
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++)
			{
				if (resultSet.getObject(columnIndex) instanceof Integer)
					rowVector.add(resultSet.getInt(columnIndex));
				else
					rowVector.add(resultSet.getString(columnIndex));
			}
			dataVector.add(rowVector);
		}

		// http://stackoverflow.com/questions/6592192/why-does-my-jtable-sort-an-integer-column-incorrectly
		return new DefaultTableModel(dataVector,columnNames)
		{
			@Override
			public Class getColumnClass(int column)
			{
				if (getValueAt(0, column) instanceof Integer)
					return Integer.class;
				else
					return String.class;

			}
		};
	}
}
