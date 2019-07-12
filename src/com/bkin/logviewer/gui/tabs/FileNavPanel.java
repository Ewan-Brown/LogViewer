package com.bkin.logviewer.gui.tabs;

import com.bkin.logviewer.files.DisplayableFile;
import com.bkin.logviewer.usage.VersionFolder;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Jul-12
 * Time: 10:38 AM
 *
 * Lists com.bkin.logviewer.files in a scroll pane for a version folder. Sends a file selection notification
 * when a file is selected in the pane. Updates the main text panel to show the selected file contents
 */
class FileNavPanel extends JPanel
{
	private JScrollPane m_scrollPane;
	private JPanel m_navPanel;
	private JPanel m_holderPanel;
	private UUID m_uuid;

	public FileNavPanel(VersionFolder folder, UUID uuid)
	{
		m_uuid = uuid;
		m_navPanel = new JPanel(new GridLayout(0, 1));
		m_holderPanel = new JPanel(new BorderLayout());
		m_scrollPane = new JScrollPane(m_holderPanel);

		styleControls();
		layoutControls(folder);
	}

	private void styleControls()
	{
		m_scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		m_scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	private void layoutControls(VersionFolder folder)
	{
		setLayout(new BorderLayout());

		for (DisplayableFile file : folder.getAllFiles())
		{
			FileButton btn = new FileButton(file, m_uuid);
			if (file.getFileType() == DisplayableFile.FileType.LOGS)
				btn.setSelected();

			m_navPanel.add(btn);
		}

		// This prevents the buttons from expanding vertically in the grid layout
		m_holderPanel.add(m_navPanel, BorderLayout.NORTH);
		m_holderPanel.add(new JPanel(), BorderLayout.CENTER);

		add(m_scrollPane, BorderLayout.CENTER);
	}
}
