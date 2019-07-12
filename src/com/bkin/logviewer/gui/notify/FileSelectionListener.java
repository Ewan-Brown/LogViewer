package com.bkin.logviewer.gui.notify;

import com.bkin.logviewer.files.DisplayableFile;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Jul-12
 * Time: 3:49 PM
 *
 * Fire a method when a new file is chosen for viewing.
 */
public interface FileSelectionListener
{
	void fileSelectionChange(DisplayableFile file, UUID uuid);
}
