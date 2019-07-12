package com.bkin.logviewer.gui.tabs;

import com.bkin.logviewer.files.DisplayableFile;
import com.bkin.logviewer.gui.notify.FileSelectionListener;
import com.bkin.logviewer.gui.notify.NotificationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Jul-12
 * Time: 3:28 PM
 *
 * This is used as both a visual for selecting a file type and as a notifier
 * for the main text panel to update.
 */
public class FileButton extends JPanel implements ActionListener, FileSelectionListener
{
	private JButton m_button;
	private DisplayableFile m_displayableFile;
	private JPanel m_innerPanel;

	// This is used to make sure we only notify the selected tab
	private UUID m_uuid;

	// Use this if the button is for a standard file type
	public FileButton(DisplayableFile file, UUID uuid)
	{
		m_displayableFile = file;
		DisplayableFile.FileType m_fileType = m_displayableFile.getFileType();
		m_uuid = uuid;

		if (m_fileType.equals(DisplayableFile.FileType.OTHER))
			layoutControls(m_displayableFile.getName());
		else
			layoutControls(m_fileType.getName());

		m_button.addActionListener(this);
		NotificationManager.addFileCategoryListener(this);
	}

	private void layoutControls(String buttonTitle)
	{
		m_innerPanel = new JPanel(new FlowLayout()); // stop the button from expanding

		m_button = new JButton(buttonTitle);
		m_button.setPreferredSize(new Dimension(125, 35));
		m_innerPanel.add(m_button);

		m_innerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

		setLayout(new BorderLayout());
		add(m_innerPanel, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(m_button))
			NotificationManager.notifyCategoryChange(m_displayableFile, m_uuid);
	}

	@Override
	public void fileSelectionChange(DisplayableFile file, UUID uuid)
	{
		if (m_uuid.equals(uuid))
		{
			if (file.equals(m_displayableFile))
				setSelected();
			else
				setUnselected();
		}
	}

	public void setSelected()
	{
		m_innerPanel.setBackground(new Color(175, 173, 180));
		m_button.setFont(new Font("Helvetica", Font.BOLD, 12));
	}

	private void setUnselected()
	{
		m_innerPanel.setBackground(null);
		m_button.setFont(null);
	}
}
