package com.bkin.logviewer.files;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * Represents a 'file', whether loaded from zip or generated. Has a type, and contains lines.
 * Each button on the gui has an attached DisplayableFile that it will display on the table when pressed
 *
 */
public class DisplayableFile
{

	private Date firstDate;
	private ArrayList<Line> m_lines;
	private String m_name;
	private FileType m_fileType;
	private String m_originalFileName;


	/**
	 * The type of this file, inferred from the original file name, or it's purpose if generated.
	 * Used to rename file buttons in obvious titles if they are important (e.x: 'LOGS', 'INI')
	 * and to do special operations, like concatenating logs into a single file.
	 */
	public enum FileType
	{
		LOGS("Logs","Dex.log"),
		INI("INI","Dex.ini"),
		USAGE("Usage","usage.csv"),
		SUBJECT("Subject Log","Subject Log"),
		FRAMES("Frames","frame.log"),
		SYS_INFO("System Info","sys_info.log"),
		EXPORT_CSV("Export CSV Log","csvexport.log"),
		ANALYSIS_LOG("Analysis Log","analysis.log"),
		EXTERNAL_LOG("External Log","dex_external.log"),
		VERSION_LOG("Version Log",null),
		EXTRA_DATA("Extra data",null),
		OTHER("Other",null);

		private String m_name;
		private String m_fileName;

		public String getFileName(){return m_fileName;}

		FileType(String name, String fileName)
		{
			m_name = name;
			m_fileName = fileName;
		}

		public String getName()
		{
			return m_name;
		}
	}


	/**
	 * Get the 'first date', the parsed date of the first line of text in this file. used to compare to other logs in order to sort them into chronological order.
	 * @return the first line's parsed date from this file. if nonexistant returns new Date(0) //TODO Change this to something global.
	 */
	public Date getFirstDate(){
		return firstDate;
	}

	public DisplayableFile(ArrayList<Line> lines, String name, FileType fileType, String fileName){
		m_lines = lines;
		m_name = name;
		m_fileType = fileType;
		m_originalFileName = fileName;
		if(lines.size() > 0) {
			firstDate = lines.get(0).getParsedDate();
		}else{
			firstDate = null;
		}
	}

	/**
	 * Adds extra lines to end this file. used in log concatenation
	 * @param lines list of lines to be added to this file.
	 */
	public void addLines(ArrayList<Line> lines){
		m_lines.addAll(lines);
	}

	public ArrayList<Line> getLines()
	{
		return m_lines;
	}

	public FileType getFileType()
	{
		return m_fileType;
	}

	public String getName()
	{
		return m_name;
	}
	public String getOriginalFileName() {
		return m_originalFileName;
	}
}