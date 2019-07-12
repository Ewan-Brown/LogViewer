package com.bkin.logviewer.gui.tabs;

import com.bkin.logviewer.usage.VersionFolder;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Jul-12
 * Time: 10:14 AM
 *
 * Pulls all the controls for version file together into a single panel.
 * This pannel will exist for each version folder as well as the "extra" com.bkin.logviewer.files like com.bkin.logviewer.usage.csv
 */
public class FileTabPanel extends JPanel
{
	private FileTextPanel m_textPanel;
	private FileControlsPanel m_controlsPanel; // todo custom panel class to control the textPanel - adam
	private FileNavPanel m_fileNavPanel;

	// This is the main UUID for a single version tab. This will be used in the text, control, and nav panel to determine
	// whether we should update these controls when the notification manager sends the request.


	public FileTabPanel(VersionFolder versionFolder)
	{
		UUID m_uuid = UUID.randomUUID();

		m_textPanel = new FileTextPanel(versionFolder.getAllFiles().get(0), m_uuid);

		// Search and line type filter handling
		m_controlsPanel = new FileControlsPanel(m_textPanel);

		m_controlsPanel.addFilterListener(m_textPanel);
		m_controlsPanel.fireFilterCommands();

		m_controlsPanel.passTable(m_textPanel.getTextTable());

		m_textPanel.setTableFilters(m_controlsPanel.getFilters());

		m_fileNavPanel = new FileNavPanel(versionFolder, m_uuid);

		layoutControls();
	}

	private void layoutControls()
	{
		setLayout(new BorderLayout());
		add(m_fileNavPanel, BorderLayout.WEST);
		add(m_textPanel, BorderLayout.CENTER);
		add(m_controlsPanel, BorderLayout.NORTH);
	}
}
