package com.bkin.logviewer.resources;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Aug-24
 * Time: 2:46 PM
 *
 * Dummy class to allow more than just true or false settings in a class.
 * Could potentially use this for application settings too
 * Can modify later to add additional states or text / numbers...etc
 */
public class Setting
{
	private static final String VERSION = "1.2.2";
	private static final String TITLE = "BKIN Log Viewer";

	public static String getAppTitle()
	{
		return TITLE + " v" + VERSION;
	}
	public enum State
	{

		ON,
		OFF;

		static State getValue(boolean on)
		{
			if (on) return State.ON;
			return State.OFF;
		}
	}

	private State m_state;

	public Setting(boolean on)
	{
		m_state = State.getValue(on);
	}

	public Setting(State state)
	{
		m_state = state;
	}

	public boolean isOn()
	{
		return m_state.equals(State.ON);
	}

	public boolean isOff()
	{
		return m_state.equals(State.OFF);
	}

	public State getState()
	{
		return m_state;
	}

	public void disable()
	{
		m_state = State.OFF;
	}

	public void enable()
	{
		m_state = State.ON;
	}
}
