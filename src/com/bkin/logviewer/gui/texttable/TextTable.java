package com.bkin.logviewer.gui.texttable;

import com.bkin.logviewer.files.Line;
import com.bkin.logviewer.gui.common.filters.Filter;
import com.bkin.logviewer.gui.common.filters.FilterType;
import com.bkin.logviewer.gui.notify.NotificationManager;
import com.bkin.logviewer.gui.utils.ResultsProvider;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Jul-12
 * Time: 11:20 AM
 *
 * Shows all the lines of a log file
 */
public class TextTable extends JTable implements ListSelectionListener, ActionListener, DocumentListener, MouseListener
{
	private TextTableModel m_model;
	private UUID m_uuid;

	private List<Filter> m_filters;
	private List<Line> m_lines;
	private TextCellRenderer textCellRenderer = new TextCellRenderer();
	private CheckCellRenderer checkCellRenderer = new CheckCellRenderer();

	//Persistent
	public TextTable(UUID uuid)
	{
		m_lines = new ArrayList<>();
		m_filters = new ArrayList<>();
		m_uuid = uuid;
	}

	public void setFilters(List<Filter> filters){
		m_filters = filters;
	}

	private void updateModel(List<Line> lines)
	{
		m_model = new TextTableModel(lines);
		setModel(m_model);
		getColumnModel().getColumn(0).setMaxWidth(15);
	}


	// Should only be used to set the data initially or maybe if you want to reset the table.
	public void setData(List<Line> lines)
	{
		m_lines = lines;
		updateModel(lines);
		updateModel(lines);
	}
	// Note that this doesn't set the data. This allows us to keep the original copy for filtering purposes
	@Override
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		if(column == 0){
			return checkCellRenderer;
		}else{
			return textCellRenderer;
		}
	}

	// This will apply all m_filters (line type and text search)
	private void applyFilters()
	{
		updateModel(ResultsProvider.filterList(m_lines, m_filters));
	}

	public int findNext(String searchText, int lastIndex)
	{
		int newIndex = ResultsProvider.findNextStringIndex(m_model.getData(), lastIndex, searchText);

		getSelectionModel().clearSelection();
		getSelectionModel().setSelectionInterval(newIndex, newIndex);
		scrollToCenter(this, newIndex, 0);

		return newIndex;
	}

	public int findPrevious(String searchText, int lastIndex)
	{
		int newIndex = ResultsProvider.findPreviousStringIndex(m_model.getData(), lastIndex, searchText);

		getSelectionModel().clearSelection();
		getSelectionModel().setSelectionInterval(newIndex, newIndex);
		scrollToCenter(this, newIndex, 0);

		return newIndex;
	}

	public int getDataLength()
	{
		return m_model.getData().size();
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		repaint();
		if (e.getValueIsAdjusting())
			return;

        int[] selectedRows = getSelectedRows();
		List<String> lines = new ArrayList<>();
		for (int row : selectedRows)
		{
			if (row >= 0)
			{
				Line line = (Line) getValueAt(row, 1);
				lines.add(line.getText());
			}
		}

		NotificationManager.notifyRowChange(lines, m_uuid);
	}

	public List<Integer> getSelectedIDs()
	{
		List<Integer> ret = new ArrayList<>();
        int[] rows = getSelectedRows();

		for (int row : rows)
		{
			ret.add(m_model.getData().get(row).getID());
		}

		return ret;
	}

	public void setSelectionByID(List<Integer> ids)
	{
		int firstIndex = -1;

		// expensive, but it works
		for (int x = 0; x < getDataLength(); x++)
		{
			if (ids.contains(m_model.getData().get(x).getID()))
			{
				if (firstIndex == -1)
					firstIndex = x;

				getSelectionModel().addSelectionInterval(x, x);
			}
		}

		if (ids.size() > 0)
		{
			scrollToCenter(this, firstIndex, 0);
		}
	}

	// http://www.java2s.com/Tutorial/Java/0240__Swing/ScrollingaCelltotheCenterofaJTableComponent.htm
	private static void scrollToCenter(JTable table, int rowIndex, int vColIndex) {
		if (!(table.getParent() instanceof JViewport)) {
			return;
		}
		JViewport viewport = (JViewport) table.getParent();
		Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);
		Rectangle viewRect = viewport.getViewRect();
		rect.setLocation(rect.x - viewRect.x, rect.y - viewRect.y);

		int centerX = (viewRect.width - rect.width) / 2;
		int centerY = (viewRect.height - rect.height) / 2;
		if (rect.x < centerX) {
			centerX = -centerX;
		}
		if (rect.y < centerY) {
			centerY = -centerY;
		}
		rect.translate(centerX, centerY);
		viewport.scrollRectToVisible(rect);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		applyFilters();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		applyFilters();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		applyFilters();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		applyFilters();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		applyFilters();
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}