package com.bkin.logviewer.gui.usage;

import com.bkin.logviewer.usage.KSTStats;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import static com.bkin.logviewer.usage.KSTStats.*;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2017-Jan-17
 * Time: 3:30 PM
 */
public class KSTDialog extends JDialog implements ActionListener
{
	private UsageJTable m_table;
	private KSTStats m_stats;

	private JRadioButton m_yearMonth = new JRadioButton("year/month");
	private JRadioButton m_yearMonthExam = new JRadioButton("year/month/exam");
	private JRadioButton m_yearMonthExamModel = new JRadioButton("year/month/exam/model");
	private JRadioButton m_yearMonthExamModelProtocol = new JRadioButton("year/month/exam/model/protocol");

	private JButton m_btnExecute = new JButton(">");
	private JTextField m_txtSQL = new JTextField("");

	public KSTDialog(KSTStats usageStats)
	{
		ButtonGroup bg = new ButtonGroup();
		bg.add(m_yearMonth);
		bg.add(m_yearMonthExam);
		bg.add(m_yearMonthExamModel);
		bg.add(m_yearMonthExamModelProtocol);

		m_stats = usageStats;
		layoutControls();

		m_btnExecute.addActionListener(this);
		m_yearMonth.addActionListener(this);
		m_yearMonthExam.addActionListener(this);
		m_yearMonthExamModel.addActionListener(this);
		m_yearMonthExamModelProtocol.addActionListener(this);
	}

	private void layoutControls()
	{
		setLayout(new BorderLayout());

		// Setup the table
		m_table = new UsageJTable();

		try
		{
			m_table.setData(m_stats.getByYearMonthExam());
		}
		catch (NullPointerException | SQLException e)
		{
			JOptionPane.showMessageDialog(this, "Exception getting initial data \n\n" + e.getMessage(), "Exception occurred", JOptionPane.ERROR_MESSAGE);
			m_table.setModel(new DefaultTableModel());
		}

		m_table.setCellSelectionEnabled(true);
		m_table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		m_table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		m_table.getTableHeader().setReorderingAllowed(false);

		// Scroll pane for the table
		JScrollPane pane = new JScrollPane(m_table);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// Text and execute panel
		JPanel textPanel = new JPanel(new BorderLayout(5, 0));
		textPanel.add(m_txtSQL, BorderLayout.CENTER);
		textPanel.add(m_btnExecute, BorderLayout.EAST);
		textPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// Radio panel
		JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		radioPanel.add(m_yearMonth);
		radioPanel.add(m_yearMonthExam);
		radioPanel.add(m_yearMonthExamModel);
		radioPanel.add(m_yearMonthExamModelProtocol);

		// NORTH panel
		JPanel northPanel = new JPanel(new BorderLayout(0, 0));
		northPanel.add(textPanel, BorderLayout.CENTER);
		northPanel.add(radioPanel, BorderLayout.SOUTH);

		// Table panel
		JPanel tablePanel = new JPanel(new BorderLayout(0, 0));
		tablePanel.add(pane);
		tablePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		add(northPanel, BorderLayout.NORTH);
		add(tablePanel, BorderLayout.CENTER);
		setSize(600, 600);
		setTitle("BKIN KST com.bkin.logviewer.usage parser");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		try
		{


			if (e.getSource().equals(m_btnExecute))
			{
				m_table.setData(m_stats.customSelect(m_txtSQL.getText()));
			}
			else if (e.getSource().equals(m_yearMonth))
			{
				m_table.setData(m_stats.getByYearMonth());
				m_txtSQL.setText(SQL_YEAR_MONTH);
			}
			else if (e.getSource().equals(m_yearMonthExam))
			{
				m_table.setData(m_stats.getByYearMonthExam());
				m_txtSQL.setText(SQL_YEAR_MONTH_EXAM);
			}
			else if (e.getSource().equals(m_yearMonthExamModel))
			{
				m_table.setData(m_stats.getByYearMonthExamModel());
				m_txtSQL.setText(SQL_YEAR_MONTH_EXAM_MODEL);
			}
			else if (e.getSource().equals(m_yearMonthExamModelProtocol))
			{
				m_table.setData(m_stats.getByYearMonthExamModelProtocol());
				m_txtSQL.setText(SQL_YEAR_MONTH_EXAM_MODEL_PROTOCOL);
			}
		}
		catch (SQLException | NullPointerException ex)
		{
			JOptionPane.showMessageDialog(this, "Exception during query, check syntax.", "Exception occurred", JOptionPane.ERROR_MESSAGE);
			m_table.setModel(new DefaultTableModel());
		}
	}
}
