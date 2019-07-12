package com.bkin.logviewer.resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Jul-18
 * Time: 2:26 PM
 *
 * Copied from JDexterit-E and repurposed
 */
public class Images
{
	public static final String SEARCH = "search.png";
	public static final String UP_ARROW = "up.png";
	public static final String DOWN_ARROW = "down.png";
	// --Commented out by Inspection (2019-06-03 3:16 PM):public static final String LEFT_ARROW = "left.png";
	public static final String CALENDAR = "Calendar.png";
	// --Commented out by Inspection (2019-06-03 3:16 PM):public static final String RIGHT_ARROW = "right.png";
	public static final String FILTER = "filter.png";
	// --Commented out by Inspection (2019-06-03 3:16 PM):public static final String DATA = "data.png";
	// --Commented out by Inspection (2019-06-03 3:16 PM):public static final String MINUS = "minus.png";
	public static final String PLUS = "plus.png";
	public static final String BKIN_SMALL_ICON = "bkin16x16.png";

	public static BufferedImage loadImage(String strName)
	{
		try
		{
			return ImageIO.read(Images.class.getResourceAsStream("images/" + strName));
		}
		catch (Exception ex)
		{
			// ignore
		}

		return null;
	}

	public static Icon getIcon(String strName)
	{
		return new ImageIcon(loadImage(strName));
	}
}
