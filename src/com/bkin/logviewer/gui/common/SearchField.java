package com.bkin.logviewer.gui.common;

import com.bkin.logviewer.resources.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains a text box, two navigational arrows and a search icon.<br>
 * Not a filter. Used to find lines containing a search entry string.<br>
 * Allows the user to skip through lines containing the given text.
 */
public class SearchField extends JPanel implements ActionListener
{
	private static final Dimension BUTTON_SIZE = new Dimension(25, 25);

	private List<SearchListener> m_alSearchObjects = new ArrayList<>();

	private JButton m_btnPrevious;
	private JButton m_btnNext;

	private JLabel m_lblFunctionIcon;
	private JTextField m_txtSearch;


	public SearchField()
	{
		styleControls();
		layoutControls();

	}

	private void styleControls()
	{
		Icon m_previousIcon;
		Icon m_nextIcon;
		Icon m_functionIcon;


		m_previousIcon = Images.getIcon(Images.UP_ARROW);
		m_nextIcon = Images.getIcon(Images.DOWN_ARROW);
		m_functionIcon = Images.getIcon(Images.SEARCH);

		m_btnNext = new JButton(m_nextIcon);
		m_btnPrevious = new JButton(m_previousIcon);
		m_lblFunctionIcon = new JLabel(m_functionIcon);

		m_btnNext.setToolTipText("Jump to next occurence");

		m_btnPrevious.setToolTipText("Jump to previous occurence");

		m_btnNext.setMaximumSize(BUTTON_SIZE);
		m_btnNext.setPreferredSize(BUTTON_SIZE);

		m_btnPrevious.setMaximumSize(BUTTON_SIZE);
		m_btnPrevious.setPreferredSize(BUTTON_SIZE);

		m_lblFunctionIcon.setMaximumSize(new Dimension(23, 23));
		m_lblFunctionIcon.setPreferredSize(new Dimension(23, 23));

		m_txtSearch = new JTextField("");
		m_txtSearch.setPreferredSize(new Dimension(150, 23));
		m_txtSearch.setMinimumSize(new Dimension(100, 23));
		m_txtSearch.setToolTipText("Search for lines containing entered text. Use arrows to navigate");

	}


	private void layoutControls()
	{
		setLayout(new BorderLayout(3, 0));

		add(m_lblFunctionIcon, BorderLayout.WEST);

		add(m_txtSearch, BorderLayout.CENTER);
		JPanel navButtonsPanel = new JPanel();
		navButtonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 0));

		navButtonsPanel.add(m_btnPrevious);
		navButtonsPanel.add(m_btnNext);

		add(navButtonsPanel, BorderLayout.EAST);

		m_btnPrevious.addActionListener(this);
		m_btnNext.addActionListener(this);
	}

	public void clear(){
		m_txtSearch.setText("");
	}

	public void addSearchListener(SearchListener method)
	{
		m_alSearchObjects.add(method);
	}

	public interface SearchListener
	{
		void previous(String searchText);
		void next(String searchText);
	}

	private void firePreviousCommands()
	{
		for (SearchListener method : m_alSearchObjects)
		{
			method.previous(m_txtSearch.getText());
		}
	}

	private void fireNextCommands()
	{
		for (SearchListener method : m_alSearchObjects)
		{
			method.next(m_txtSearch.getText());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(m_btnPrevious))
		{
			firePreviousCommands();
		}
		else if (e.getSource().equals(m_btnNext))
		{
			fireNextCommands();
		}
	}


//	public static void main(String arg[])
//	{
//		GUIUtils.setLookAndFeel();
//
//		JFrame frame = new JFrame();
//		frame.setSize(new Dimension(400, 600));
//
//		SearchField field1 = new SearchField(Function.FILTER, Direction.HORIZONTAL);
//		SearchField field2 = new SearchField(Function.SEARCH, Direction.HORIZONTAL);
//		SearchField field3 = new SearchField(Function.FILTER, Direction.VERTICAL);
//		SearchField field4 = new SearchField(Function.SEARCH, Direction.VERTICAL);
//
//		JPanel mainPanel = new JPanel();
//		mainPanel.setLayout(new GridLayout(2, 2));
//		mainPanel.add(field1);
//		mainPanel.add(field2);
//		mainPanel.add(field3);
//		mainPanel.add(field4);
//
//		Container c = frame.getContentPane();
//		c.setLayout(new BorderLayout(5, 5));
//		c.add(mainPanel, BorderLayout.NORTH);
//		c.add(new JPanel(), BorderLayout.CENTER);
//
//		GUIUtils.centerFrameOnScreen(frame);
//
//		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//		frame.setVisible(true);
//	}
}