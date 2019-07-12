package com.bkin.logviewer.gui.tabs;

import com.bkin.logviewer.files.DisplayableFile;
import com.bkin.logviewer.files.Line;
import com.bkin.logviewer.gui.common.filters.Filter;
import com.bkin.logviewer.gui.common.SearchField;
import com.bkin.logviewer.gui.notify.FileSelectionListener;
import com.bkin.logviewer.gui.notify.NotificationManager;
import com.bkin.logviewer.gui.notify.RowSelectionListener;
import com.bkin.logviewer.gui.texttable.SelectedRowPanel;
import com.bkin.logviewer.gui.texttable.TextTable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Jul-12
 * Time: 10:55 AM
 *
 * Will show all the text contents of a file in a JTable inside a scrollpane.
 * The contents of this panel will change depending on file selected and controls activated.
 */
public class FileTextPanel extends JPanel implements FileSelectionListener, SearchField.SearchListener, FileControlsPanel.LineTypeFilter, RowSelectionListener
{
	private DisplayableFile m_currentFile;
	private UUID m_uuid;
	private TextTable m_table;

	private String m_lastSearch = "";
	private int m_lastIndex = 0;

	private List<Integer> m_alSelectionCache;

	public FileTextPanel(DisplayableFile file, UUID uuid){
		m_uuid = uuid;
		m_currentFile = file;
		m_table = new TextTable(m_uuid);
		m_alSelectionCache = new ArrayList<>();

		NotificationManager.addFileCategoryListener(this);
		NotificationManager.addRowSelectionListener(this);
		layoutControls();
		setFocusable(true);
	}

	public void setTableFilters(List<Filter> filters){
		m_table.setFilters(filters);
	}

	public TextTable getTextTable(){
		return m_table;
	}

	private void layoutControls()
	{
		setLayout(new BorderLayout());
		removeAll();

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		m_table.setRowSelectionAllowed(true);
		m_table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		m_table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		m_table.getTableHeader().setReorderingAllowed(false);

		if (m_currentFile != null)
		{
			// By default, search for all line types
			List<Line.LineType> lineTypes = new ArrayList<>();
			lineTypes.addAll(Arrays.asList(Line.LineType.values()));

			// Apply the default filters before there's any data and make sure we don't bother processing it yet (saves time)

			m_table.setData(m_currentFile.getLines());
			m_table.setRowSelectionAllowed(true);
			m_table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			m_table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

			JScrollPane pane = new JScrollPane(m_table);
			pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

			JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, pane, new SelectedRowPanel(m_uuid));
			splitPane.setResizeWeight(0.8);
			mainPanel.add(splitPane, BorderLayout.CENTER);
		}
		else
		{
			mainPanel.add(new JLabel("No logs found"), BorderLayout.CENTER);
		}

		add(mainPanel, BorderLayout.CENTER);
	}

	@Override
	public void fileSelectionChange(DisplayableFile file, UUID uuid)
	{
		// Don't fire the event if we aren't inside the tab containing this panel
		if (! uuid.equals(m_uuid))
			return;

		m_currentFile = file;
		layoutControls();

		invalidate();
		validate();
		repaint();
	}


	public UUID getUUID()
	{
		return m_uuid;
	}

	@Override
	public void previous(String searchText)
	{
		// use last index if not the first search
		if (searchText.equals(m_lastSearch))
		{
			m_lastIndex = m_table.findPrevious(m_lastSearch, m_lastIndex);
		}
		else // start a new search from the bottom
		{
			m_lastSearch = searchText;
			m_lastIndex = m_table.getDataLength();

			m_lastIndex = m_table.findPrevious(m_lastSearch, m_lastIndex);
		}

		if (m_lastIndex == -1)
		{
			m_lastIndex = m_table.getDataLength();
		}
	}

	@Override
	public void next(String searchText)
	{
		// use last index if not the first search
		if (searchText.equals(m_lastSearch))
		{
			m_lastIndex = m_table.findNext(m_lastSearch, m_lastIndex);
		}
		else // start a new search from the top
		{
			m_lastSearch = searchText;
			m_lastIndex = -1;

			m_lastIndex = m_table.findNext(m_lastSearch, m_lastIndex);
		}
	}

	// Line type checkboxes trigger this
	@Override
	public void filter(List<Line.LineType> lineTypes)
	{
		m_alSelectionCache = m_table.getSelectedIDs();
		m_table.setSelectionByID(m_alSelectionCache);
	}

	@Override
	public void rowSelectionChange(List<String> lines, UUID uuid) {
		this.repaint();
	}
}