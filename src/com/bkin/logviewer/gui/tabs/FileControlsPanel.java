package com.bkin.logviewer.gui.tabs;

import com.bkin.logviewer.files.DisplayableFile;
import com.bkin.logviewer.files.Line;
import com.bkin.logviewer.gui.common.SearchField;
import com.bkin.logviewer.gui.common.filters.*;
import com.bkin.logviewer.gui.notify.FileSelectionListener;
import com.bkin.logviewer.gui.notify.NotificationManager;
import com.bkin.logviewer.gui.texttable.TextTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Jul-12
 * Time: 10:53 AM
 *
 * Has several controls that will affect the display of the text contents of a file.
 * Will need to use some method to access the data table in FileTabPanel
 */
public class FileControlsPanel extends JPanel implements ActionListener, FileSelectionListener
{

	private SearchField m_searchField;
	private FilterText m_filterText;
	private FilterDate m_filterDate;

	private List<Filter> filterList = new ArrayList<>();

	private List<LineTypeFilter> m_alFilterObjects = new ArrayList<>();

	private UUID m_uuid;

	public void passTable(TextTable a){
		for(Filter f : filterList){
			f.addTableListeners(a);
		}
	}

	public FileControlsPanel(FileTextPanel textPanel)
	{
		// link the control panel and the text panel by uuid
		m_uuid = textPanel.getUUID();
		m_searchField = new SearchField();
		m_searchField.addSearchListener(textPanel);

		m_filterDate = new FilterDate();
		m_filterText = new FilterText();

		filterList.add(m_filterText);
		filterList.add(m_filterDate);

		resetControls();
		layoutControls();


		NotificationManager.addFileCategoryListener(this);
	}

	public List<Filter> getFilters(){
		return filterList;
	}

	private void layoutControls()
	{
		setLayout(new BorderLayout());

		JPanel westPanel = new JPanel();
		JPanel textBoxPanel = new JPanel();
		JPanel separationPanel = new JPanel();
		separationPanel.setSize(15,5);

		westPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		westPanel.add(textBoxPanel);
		westPanel.add(separationPanel);
		westPanel.add(m_filterDate);
		westPanel.setBorder(BorderFactory.createLineBorder(Color.black));


		textBoxPanel.setLayout(new BoxLayout(textBoxPanel, BoxLayout.Y_AXIS));
		textBoxPanel.add(m_searchField); //Add spacer between these - new emtpy jpanel)
		textBoxPanel.add(m_filterText);

		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        FilterType ft = new FilterType();
        eastPanel.add(ft);
        filterList.add(ft);
		eastPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		add(westPanel, BorderLayout.WEST);
		add(eastPanel, BorderLayout.EAST);
	}

//	setLayout(new BorderLayout());
//
//		JPanel westPanel = new JPanel();
//
//		westPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
//		westPanel.add(m_searchField);
//
//		for(Filter f : filterList){
//			westPanel.add(f);
//		}
//
//		add(westPanel, BorderLayout.WEST);
//
//		JPanel eastPanel = new JPanel();
//		eastPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
//        FilterType ft = new FilterType();
//        eastPanel.add(ft);
//        filterList.add(ft);
//
//		add(eastPanel, BorderLayout.CENTER);

	private void resetControls()
	{

		m_searchField.clear();

		for(Filter f : filterList){
			f.clear();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		fireFilterCommands();
	}

	@Override
	public void fileSelectionChange(DisplayableFile file, UUID uuid)
	{
		// This stops us from reseting the controls when another tab is selecting a file in the navigation panel.
		if (m_uuid.equals(uuid))
			resetControls();
	}

	public interface LineTypeFilter
	{
		void filter(List<Line.LineType> lineTypes);
	}

	public void addFilterListener(LineTypeFilter method)
	{
		m_alFilterObjects.add(method);
	}

	public void fireFilterCommands()
	{
		List<Line.LineType> lineTypes = new ArrayList<>();
//		if (m_chkDebug.isSelected()) lineTypes.add(Line.LineType.DEBUG);
//		if (m_chkError.isSelected()) lineTypes.add(Line.LineType.ERROR);
//		if (m_chkWarning.isSelected()) lineTypes.add(Line.LineType.WARNING);
//		if (m_chkInfo.isSelected()) lineTypes.add(Line.LineType.INFO);
//		if (m_chkMisc.isSelected()) lineTypes.add(Line.LineType.MISC);

		for (LineTypeFilter method : m_alFilterObjects)
		{
			method.filter(lineTypes);
		}
	}

}
