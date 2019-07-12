package com.bkin.logviewer.gui.texttable;

import com.bkin.logviewer.gui.notify.NotificationManager;
import com.bkin.logviewer.gui.notify.RowSelectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Jul-14
 * Time: 11:57 AM
 *
 * This will receive a bunch of text from the selected row and show it properly in a text area.
 * It's too awkward to show the full text in the log table.
 */
public class SelectedRowPanel extends JPanel implements RowSelectionListener, ActionListener
{
	private UUID m_uuid;
	private JTextArea m_textArea;
	private JScrollPane m_scrollPane;
	private JCheckBox m_chkSeparator;
	private SelectedLines m_lines;

	public SelectedRowPanel(UUID uuid)
	{
		m_uuid = uuid;
		m_textArea = new JTextArea("");
		m_textArea.setRows(6);

		m_scrollPane = new JScrollPane(m_textArea);
		m_scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		m_scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		m_lines = new SelectedLines();
		m_lines.SEPARATE_LINES.enable();

		m_chkSeparator = new JCheckBox("Separate lines");
		m_chkSeparator.addActionListener(this);
		m_chkSeparator.setSelected(m_lines.SEPARATE_LINES.isOn());
		NotificationManager.addRowSelectionListener(this);
		layoutControls();
	}

	private void layoutControls()
	{
		setLayout(new BorderLayout());
		add(m_chkSeparator, BorderLayout.NORTH);
		add(m_scrollPane, BorderLayout.CENTER);
	}

	public UUID getUUID()
	{
		return m_uuid;
	}

	private void setText()
	{
		m_textArea.setText(m_lines.getFormattedLines());
		m_textArea.setCaretPosition(0);
	}

	@Override
	public void rowSelectionChange(List<String> lines, UUID uuid)
	{
		if (uuid.equals(m_uuid))
		{
			m_lines.setLines(lines);
			setText();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(m_chkSeparator))
		{
			if (m_chkSeparator.isSelected())
				m_lines.SEPARATE_LINES.enable();
			else
				m_lines.SEPARATE_LINES.disable();

			setText();
		}
	}
}
