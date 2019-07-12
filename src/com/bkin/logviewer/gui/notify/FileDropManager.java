package com.bkin.logviewer.gui.notify;

import com.bkin.logviewer.gui.MainFrame;
import com.bkin.logviewer.gui.common.FileDrop;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Jul-21
 * Time: 3:54 PM
 *
 * Class to notify something when a file is dragged into the GUI
 */
public class FileDropManager implements FileDrop.Listener
{
    private static MainFrame m_mainFrame;

    public static void setFrame(MainFrame mainFrame)
    {
        m_mainFrame = mainFrame;
    }

    @Override
    public void filesDropped(File[] files)
    {
        if (files.length > 1)
            m_mainFrame.readZip(files[0]);
        else
            m_mainFrame.readZip(files[0]);
    }
}
