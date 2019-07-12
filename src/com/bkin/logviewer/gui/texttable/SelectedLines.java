package com.bkin.logviewer.gui.texttable;

import com.bkin.logviewer.resources.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Aug-24
 * Time: 3:31 PM
 *
 * Class to allow formatting of selected lines. Allows us to send lines from the table to the textarea
 * and format them according to the textarea classes controls.
 */
class SelectedLines
{
	private List<String> m_lines;
	public Setting SEPARATE_LINES = new Setting(Setting.State.ON);

	public SelectedLines()
	{
		m_lines = new ArrayList<>();
	}


	public String getFormattedLines()
	{
		StringBuilder b = new StringBuilder();
		for (String line : m_lines)
		{
			b.append(line).append("\n");

			if (SEPARATE_LINES.isOn())
			{
				b.append("================================================================================================================\n");
			}
		}

		return b.toString();
	}

	public List<String> getLines()
	{
		return m_lines;
	}

	public void setLines(List<String> lines)
	{
		m_lines = lines;
	}
}
