package com.bkin.logviewer.gui.notify;

import com.bkin.logviewer.files.DisplayableFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Jul-12
 * Time: 3:49 PM
 *
 * Will hold all the modules that are notified on certain commands
 */

//TODO this class should not be needed. Previously caused huge memory leaks, likely still causing small ones.
public class NotificationManager
{
	private static List<FileSelectionListener> m_allCategoryListeners = new ArrayList<>();
	private static List<RowSelectionListener> m_allRowSelectionListeners = new ArrayList<>();

	//Lists of components to be notified when the user opens a new file, or selects a row. Must be cleared with clear(),
	//or they will linearly increase in size each time a file is opened.

	public static void addFileCategoryListener(FileSelectionListener listener)
	{
		m_allCategoryListeners.add(listener);
	}

	public static void addRowSelectionListener(RowSelectionListener listener)
	{
		m_allRowSelectionListeners.add(listener);
	}

	public static void notifyCategoryChange(DisplayableFile file, UUID uuid)
	{
		for (FileSelectionListener listener : m_allCategoryListeners)
		{
			listener.fileSelectionChange(file, uuid);
		}
	}

	public static void notifyRowChange(List<String> lines, UUID uuid)
	{
		for (RowSelectionListener listener : m_allRowSelectionListeners)
		{
			listener.rowSelectionChange(lines, uuid);
		}
	}

	/**
	 * This has to be called or else when a new file is opened all the old tabs are kept in memory
	 * A temporary fix for memory leaks, however the NotificationManager class should be removed altogether.
	 */
	public static void clear(){
		m_allCategoryListeners.clear();
		m_allRowSelectionListeners.clear();
	}


}
