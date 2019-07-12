package com.bkin.logviewer.usage;

import com.bkin.logviewer.database.UsageMemoryDatabase;
import com.bkin.logviewer.files.DisplayableFile;
import com.bkin.logviewer.files.Line;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2017-Jan-18
 * Time: 10:39 AM
 *
 * Used to store statistics for easy display in the KST com.bkin.logviewer.usage dialog.
 * WTB SQL
 */
public class KSTStats
{
	// KST by Year, Month
	public static final String SQL_YEAR_MONTH = "SELECT year, month, count(*) AS \"count\" FROM databaseTable WHERE kst = 'yes' GROUP BY year, month";

	// KST by Year, Month, Exam
	public static final String SQL_YEAR_MONTH_EXAM = "SELECT year, month, exam, count(*) AS \"count\" FROM databaseTable WHERE kst = 'yes' GROUP BY year, month, exam";

	// KST by Year, Month, Exam, Model
	public static final String SQL_YEAR_MONTH_EXAM_MODEL = "SELECT year, month, exam, model, count(*) AS \"count\" FROM databaseTable WHERE kst = 'yes' GROUP BY year, month, exam, model";

	// KST by Year, Month, Exam, Model, Protocol
	public static final String SQL_YEAR_MONTH_EXAM_MODEL_PROTOCOL = "SELECT year, month, exam, model, protocol, count(*) AS \"count\" FROM com.bkin.logviewer.usage WHERE kst = 'yes' GROUP BY year, month, exam, model, protocol";

	private ArrayList<UsageLine> m_allUsageLines;
	private UsageMemoryDatabase m_db;

	public KSTStats(DisplayableFile usagecsv)
	{
		m_allUsageLines = new ArrayList<>();

		for (Line line : usagecsv.getLines())
		{
			if (line.getText().startsWith("Date,"))
				continue;

			m_allUsageLines.add(new UsageLine(line.getText()));
		}

		m_db = new UsageMemoryDatabase();
		m_db.populateDatabase(m_allUsageLines);
	}

	public ResultSet getByYearMonth()
	{
		return m_db.select(SQL_YEAR_MONTH);
	}

	public ResultSet getByYearMonthExam()
	{
		return m_db.select(SQL_YEAR_MONTH_EXAM);
	}

	public ResultSet getByYearMonthExamModel()
	{
		return m_db.select(SQL_YEAR_MONTH_EXAM_MODEL);
	}
	public ResultSet getByYearMonthExamModelProtocol()
	{
		return m_db.select(SQL_YEAR_MONTH_EXAM_MODEL_PROTOCOL);
	}

	public ResultSet customSelect(String sql)
	{
		return m_db.select(sql);
	}
}
