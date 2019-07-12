package com.bkin.logviewer.database;

import com.bkin.logviewer.usage.UsageLine;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2017-Feb-06
 * Time: 9:51 AM
 *
 * Creates a temporary 'databaseTable' in memory.
 */
public class UsageMemoryDatabase
{
	private Connection m_connection;

	// https://bitbucket.org/xerial/sqlite-jdbc/wiki/Usage
	public UsageMemoryDatabase()
	{
		// Create a memory com.bkin.logviewer.database
		try
		{
			m_connection = DriverManager.getConnection("jdbc:sqlite:");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void populateDatabase(ArrayList<UsageLine> lines)
	{
		try
		{
			Statement statement = m_connection.createStatement();
			statement.executeUpdate("DROP TABLE IF EXISTS databaseTable");
			statement.executeUpdate("CREATE TABLE databaseTable(" +
					"year NUM," +
					"month NUM," +
					"day NUM," +
					"time TEXT," +
					"username TEXT," +
					"operator TEXT," +
					"exam TEXT," +
					"model TEXT," +
					"protocol TEXT," +
					"kst TEXT," +
					"study TEXT," +
					"physician TEXT," +
					"location TEXT)");

			for (UsageLine line : lines)
			{
				statement.executeUpdate("INSERT INTO databaseTable VALUES(" +
						line.getYearInt() + "," +
						line.getMonthInt() + "," +
						line.getDayInt() + "," +
						"\"" + line.getValue(UsageLine.FIELD.TIME) + "\"," +
						"\"" + line.getValue(UsageLine.FIELD.USERNAME) + "\"," +
						"\"" + line.getValue(UsageLine.FIELD.OPERATOR) + "\"," +
						"\"" + line.getValue(UsageLine.FIELD.EXAM) + "\"," +
						"\"" + line.getValue(UsageLine.FIELD.MODEL) + "\"," +
						"\"" + line.getValue(UsageLine.FIELD.PROTOCOL) + "\"," +
						"\"" + line.getValue(UsageLine.FIELD.KST) + "\"," +
						"\"" + line.getValue(UsageLine.FIELD.STUDY) + "\"," +
						"\"" + line.getValue(UsageLine.FIELD.PHYSICIAN) + "\"," +
						"\"" + line.getValue(UsageLine.FIELD.LOCATION) + "\")"
				);
			}

			//statement.executeUpdate("backup to backup.db");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public ResultSet select(String sql)
	{
		try
		{
			Statement statement = m_connection.createStatement();
			return statement.executeQuery(sql);
		}
		catch (SQLException e)
		{
			//e.printStackTrace();
		}

		return null;
	}
}
