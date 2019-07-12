package com.bkin.logviewer.gui.notify;

import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Jul-14
 * Time: 11:53 AM
 *
 * Responsible for updating a panel when a row is selected. The panel will show the full text
 * of the error.
 */
public interface RowSelectionListener
{
	void rowSelectionChange(List<String> lines, UUID uuid);
}
