package com.bkin.logviewer.gui.utils;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Jul-12
 * Time: 11:02 AM
 *
 * Some GUI methods very similar to DexEx
 */
public class GUIUtils
{
	public static void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void centerFrameOnScreen(JFrame frame)
	{
		frame.setLocationRelativeTo(null);
	}
}