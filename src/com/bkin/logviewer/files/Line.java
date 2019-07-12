package com.bkin.logviewer.files;

import com.bkin.logviewer.gui.utils.ResultsProvider;
import com.bkin.logviewer.gui.utils.ZipUtils;

import java.awt.*;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Jul-14
 * Time: 1:28 PM
 *
 * Represents a line of text on the table, with a type and Date.
 */
public class Line
{

	/**
	 * Used for line selection (using the mouse to click and drag, selecting multiple lines at once.)
	 */
    private int m_id;

	private boolean m_isMultiLine = false;
	private String m_text;
	private Date parsedDate;
	private LineType m_lineType;



	//TODO only some lineType filters should show up for the appropriate file... Maybe just make them opaque?

	/**
	 * The 'type' of this line. Allows filter buttons to filter selected lines types.
	 * <p><ul>
	 * <li>DEBUG -      DEX log output type
	 * <li>DEBUG_INI -  INI titles.
	 * <li>ERROR -      DEX log output type
	 * <li>FRAME_FAIL - Lines in the Frame file that are tagged as a 'failure', see frameFailTest
 	 * <li>INFO -       DEX log output type
	 * <li>MISC -       DEX log output type and anything that doesn't fit any of the other categories
	 * <li>WARNING -    DEX log output type
	 *</ul>
	 * {@link ZipUtils#frameFailTest(DisplayableFile.FileType fileType, String str, Line l)}
	 */
	public enum LineType {
		DEBUG("DEBUG", new Color(181, 126, 174)),
		DEBUG_INI("DEBUG_INI", new Color(181, 126, 174)), //TODO This shouldn't be a filterable line.
		ERROR("ERROR", new Color(255, 105, 97)),
		FRAME_FAIL("FRAME FAILURE", new Color(255, 87, 31)),
		INFO("INFO", new Color(119, 158, 203)),
		MISC("MISC", new Color(255, 255, 255)),
		WARNING("WARNING", new Color(253, 253, 150));

		private String m_text;
		private Color m_bgColour;

		LineType(String text, Color background) {
			m_text = text;
			m_bgColour = background;
		}

		@Override
		public String toString() {
			return m_text;
		}


		/**
		 * Returns the pre-defined color for this line, such that any lines of this type will be
		 * drawn with this color for ease of use
		 * @return m_bgColour
		 */
		public Color getColour() {
			return m_bgColour;
		}
	}

	/**
	 * Sets the line's type to the given type
	 * @param lineType
	 */
	public void setLineType(LineType lineType) {
		this.m_lineType = lineType;
	}


	/**
	 * Returns the stored date generated for this line on file-load. Just a getter, does not do any calculations.
	 * @return m_parsedDate
	 */
	public Date getParsedDate() {
		return parsedDate;
	}

	/**
	 * Sets the stored date for this line
	 * @param date
	 */
	public void setParsedDate(Date date) {
		parsedDate = date;
	}

	/**
	 * Represents the date this line contained when read (if it exists).
	 * otherwise it is just set to new Date(0) TODO Should have a constant dummy object representing 'nonexistant' date
	 */
	public Line(String text)
	{
		setText(text);
		m_lineType = LineType.MISC;
		m_id = ResultsProvider.getNextID();
	}

	/**
	 * Appends the current contained text with a newline character then adds the given text.
	 * This is the text that will be shown in the lower 'extended view' window when a line is clicked on.
	 * lines that have this extra 'hidden' text also have the green 'multi-line' icon in the left icon.
	 * @param text String to be appended to this line
	 */
	public void addText(String text){
		String stringBuilder = m_text + "\n" +
				text;
		m_text = stringBuilder;
	}

	/**
	 * Gets the line's ID - used for line selection
	 * @return m_id
	 */
	public int getID()
	{
		return m_id;
	}

	/**
	 * Sets the contained text to the given String
	 *
	 * @param text String to be set to
	 */
    private void setText(String text)
	{
		m_text = text;
		m_lineType = LineType.MISC;
	}

	/**
	 * Returns the text contained in this line
	 *
	 * @return m_text
	 */
	public String getText()
	{
		return m_text;
	}

	/**
	 * Returns the Type of this line
	 * @return m_lineType
	 */
	public LineType getType()
	{
		return m_lineType;
	}

	@Override
	public String toString()
	{
		return m_text;
	}

	/**
	 * Returns whether or not this Line is a multi-line, i.e contains multiple lines but is only displaying the first (errors stacktraces for example)
	 * @return isMultiLine
	 */
	public boolean isMultiLine() {
		return m_isMultiLine;
	}

	/**
	 * Sets whether or not this line is multi-line, i.e contains multiple lines but is only displaying the first (errors stacktraces for example)
	 * @param isMultiLine value to set
	 */
	public void setMultiLine(boolean isMultiLine) {
		this.m_isMultiLine = isMultiLine;
	}

}
