package com.bkin.logviewer.gui;

import com.bkin.logviewer.gui.notify.FileDropManager;
import com.bkin.logviewer.gui.utils.GUIUtils;
import com.bkin.logviewer.gui.utils.ZipUtils;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Jul-12
 * Time: 10:14 AM
 *
 * Main entry point to the application
 */
class LogViewer
{
	public static void main(String[] args)
	{
		if(ZipUtils.CUSTOM_PARSE_FLAG){
			System.out.println("---------------------------------------------" +
					"\nYOU HAVE ENABLED THE CUSTOM PARSE FLAG. " +
					"\n THIS MAY NOT WORK AND/OR MAY BE DISABLED" +
					"\n LOOK FOR ZipUtils.CUSTOM_PARSE_FLAG FOR DETAILS\n" + "\n");
		}

		GUIUtils.setLookAndFeel();

		MainFrame frame = new MainFrame();

		FileDropManager.setFrame(frame);

		GUIUtils.centerFrameOnScreen(frame);
		frame.setVisible(true);
	}
}
