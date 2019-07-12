package com.bkin.logviewer.usage;

import com.bkin.exam.StandardTaskInfo;
import com.bkin.exam.StandardTaskInfoProtocol;

import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2017-Jan-17
 * Time: 4:27 PM
 *
 * Class to store and retrieve line values.
 */
public class UsageLine
{
	public enum FIELD
	{
		DATE("date"),
		TIME("time"),
		USERNAME("username"),
		OPERATOR("operator"),
		EXAM("exam"),
		MODEL("model"),
		PROTOCOL("protocol"),
		STUDY("study"),
		PHYSICIAN("physician"),
		LOCATION("location"),
		KST("kst")
		;

		private String m_name;

		FIELD(String name)
		{
			m_name = name;
		}

		String getName()
		{
			return m_name;
		}
	}

	private Properties m_values = new Properties();

	// Date,Time,Login,Operator,Exam,Protocol,Study,PI,Stored file
	public UsageLine(String text)
	{
        String[] lineSplit = text.split(",");
		if (lineSplit.length < 9)
		{
			System.out.println("Invalid line: " + text);
			return;
		}
		setValue(FIELD.DATE, lineSplit[0]);
		setValue(FIELD.TIME, lineSplit[1]);
		setValue(FIELD.USERNAME, lineSplit[2]);
		setValue(FIELD.OPERATOR, lineSplit[3]);

		String model = lineSplit[4];
		String protocol = lineSplit[5] + ".dtp";
		setValue(FIELD.MODEL, model);

		boolean kst = isStandardTask(model, protocol);
		setValue(FIELD.KST, kst ? "yes" : "no");
		setValue(FIELD.EXAM, getBlockName(model, protocol));

		//setValue(FIELD.KST, "yes");
		//setValue(FIELD.EXAM, "test");

		setValue(FIELD.MODEL, model);
		setValue(FIELD.PROTOCOL, lineSplit[5]);
		setValue(FIELD.STUDY, lineSplit[6]);
		setValue(FIELD.PHYSICIAN, lineSplit[7]);
		setValue(FIELD.LOCATION, lineSplit[8]);
	}

	public String getValue(FIELD field)
	{
		return m_values.getProperty(field.getName());
	}

	private void setValue(FIELD field, String value)
	{
		m_values.setProperty(field.getName(), value);
	}

	private String getYearString()
	{
		return getValue(FIELD.DATE).split("/")[2];
	}

	// Java seems to get confused about the 3 letter format. Probably a cleaner way to do this, but this works for now.
	// No idea why MMM always finds "12" for the month.
    private String getMonthString()
	{
        String[] months = new String[]{"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};
        String[] monthsNumbered = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
		String targetMonth = getValue(FIELD.DATE).split("/")[1];

		for (int x = 0; x < months.length; x++)
		{
			if (targetMonth.toLowerCase().equals(months[x]))
				return monthsNumbered[x];
		}

		return "UNK";
	}

	private String getDayString()
	{
		return getValue(FIELD.DATE).split("/")[0];
	}

	public int getDayInt()
	{
		return parseInt(getDayString());
	}

	public int getMonthInt()
	{
		return parseInt(getMonthString());
	}

	public int getYearInt()
	{
		return parseInt(getYearString());
	}

	private static int parseInt(String strVal)
	{
		int val = 0;

		try
		{
			val = Integer.parseInt(strVal);
		}
		catch(NumberFormatException e)
		{
			// ignore
		}

		return val;
	}

	private static boolean isStandardTask(String modelString, String protocolString)
	{
		return !getBlockName(modelString, protocolString).isEmpty();
	}

	private static String getBlockName(String modelString, String protocolString)
	{
        StandardTaskInfo[] tasks = StandardTaskInfo.getTaskInfo();

		for (StandardTaskInfo task : tasks)
		{
			if (task.containsModelName(modelString))
			{
				List<StandardTaskInfoProtocol> protocols = task.getProtocols();
				for (StandardTaskInfoProtocol protocol : protocols)
				{
					if(protocol.getFileName(false) != null && protocol.getFileName(true) != null && protocol != null) {
						try {
							if (protocol.getFileName(false).equalsIgnoreCase(protocolString) || protocol.getFileName(true).equalsIgnoreCase(protocolString)) {
								return task.getType();
							}
						}
						catch(NullPointerException e){
							e.printStackTrace();
						}
					}
				}
			}
		}

		return "";
	}
}