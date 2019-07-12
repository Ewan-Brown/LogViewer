package com.bkin.logviewer.gui;

import com.bkin.logviewer.files.DisplayableFile;
import com.bkin.logviewer.gui.common.FileDrop;
import com.bkin.logviewer.gui.notify.FileDropManager;
import com.bkin.logviewer.gui.notify.NotificationManager;
import com.bkin.logviewer.gui.tabs.FileTabPanel;
import com.bkin.logviewer.gui.usage.KSTDialog;
import com.bkin.logviewer.gui.utils.ZipUtils;
import com.bkin.logviewer.resources.Images;
import com.bkin.logviewer.usage.VersionFolder;
import com.bkin.logviewer.usage.KSTStats;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.bkin.logviewer.resources.Setting.getAppTitle;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2016-Jul-12
 * Time: 10:59 AM
 *
 * This is the main screen for the application.
 */
public class MainFrame extends JFrame implements ActionListener, WindowListener
{
    private JMenuBar m_menuBar = new JMenuBar();
    private JMenu m_fileMenu = new JMenu("File");
    private JMenu m_verificationMenu = new JMenu("Verification");
    private JMenuItem m_exportDBItem = new JMenuItem("Export verification DB");

    private JMenuItem m_openMenuItem = new JMenuItem("Open");
    private JMenuItem m_closeMenuItem = new JMenuItem("Exit");

    private JMenu m_usageMenu = new JMenu("Usage");
    private JMenuItem m_kstMenuItem = new JMenuItem("KST");

    private JTabbedPane m_tabs;
    private List<VersionFolder> m_versionFolderList;
    private DisplayableFile m_usageCSV; //TODO why is this here and FRAME somewhere else? they should be together.

    String verificationDB;
    ZipFile zipFile;
    String location;
    String serialNum;

    private static final String VERIFICATION_DATABASE = "C:\\ProgramData\\BKIN Technologies\\verification_db\\";

    MainFrame()
    {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addWindowListener(this);

        styleControls();
        layoutControls();

        new FileDrop(this, true, new FileDropManager());
    }

    private void styleControls()
    {
        setSize(new Dimension(1350, 600));
        setTitle(getAppTitle());
        setIconImage(Images.loadImage(Images.BKIN_SMALL_ICON));
    }

    private void layoutControls()
    {
        setLayout(new BorderLayout());
        // Add the menu items to a menu structure
        m_fileMenu.add(m_openMenuItem);
        m_fileMenu.add(new JSeparator());
        m_fileMenu.add(m_closeMenuItem);
        m_menuBar.add(m_fileMenu);


        m_menuBar.add(m_usageMenu);
        m_usageMenu.add(m_kstMenuItem);

        m_menuBar.add(m_verificationMenu);
        m_verificationMenu.add(m_exportDBItem);

        m_verificationMenu.setEnabled(false);

        // Add the structure to the frame
        setJMenuBar(m_menuBar);

        // Add the tabs
        if (m_versionFolderList != null)
            createTabs();
        else
            m_tabs = new JTabbedPane(JTabbedPane.TOP);

        add(m_tabs, BorderLayout.CENTER);

        // Add action listeners for each menu item
        m_openMenuItem.addActionListener(this);
        m_closeMenuItem.addActionListener(this);
        m_kstMenuItem.addActionListener(this);
        m_exportDBItem.addActionListener(this);
    }

    private void createTabs()
    {
        remove(m_tabs);
        NotificationManager.clear();
        m_tabs.removeAll();

        // Add the tabs
        m_tabs = new JTabbedPane(JTabbedPane.TOP);
        for(VersionFolder d : m_versionFolderList) {
            String name = d.getDirectory();
            m_tabs.add(name, new FileTabPanel(d));
        }
        add(m_tabs, BorderLayout.CENTER);
        invalidate();
        revalidate();
        repaint();
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(m_openMenuItem)) // open
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setFileFilter(new FileFilter()
            {
                @Override
                public boolean accept(File f)
                {
                    return f.isDirectory() || f.getName().endsWith(".zip");
                }

                @Override
                public String getDescription()
                {
                    return "Log Folder";
                }
            });

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            {
                File f = chooser.getSelectedFile();
                readZip(f);
            }
        }
        else if (e.getSource().equals(m_closeMenuItem)) // exit
        {
            System.exit(0);
        }
        else if (e.getSource().equals(m_kstMenuItem))
        {
            if (m_usageCSV == null)
                return;

            KSTStats stats = new KSTStats(m_usageCSV);
            KSTDialog dlg = new KSTDialog(stats);
            dlg.setModal(true);
            dlg.setLocationRelativeTo(this);
            dlg.setVisible(true);
            dlg.setVisible(false);
            dlg.dispose();
        }
        else if(e.getSource().equals(m_exportDBItem)){

            try {
                ZipEntry entry = zipFile.getEntry(verificationDB);
                InputStream input = zipFile.getInputStream(entry);
                String fileDir = VERIFICATION_DATABASE + "\\Verify_"+location+"_"+serialNum+"\\";
                if(!new File(fileDir).exists()){
                    new File(fileDir).mkdir();
                }
                OutputStream out = new FileOutputStream(new File(fileDir +verificationDB.split("/")[1]));
                byte[] buffer = new byte[9000];
                int len;
                while ((len = input.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                input.close();;
                out.close();

                File patFile = new File(fileDir + "pat.dat");
                if(!patFile.exists()){
                    PrintWriter writer = new PrintWriter(patFile);
                    writer.write(
                            "lname=Verify" +
                                "\nweight=0" +
                                "\nid=0" +
                                "\nfname=" + location +
                                "\nheight=1" +
                                "\ndob=0"
                    );
                    writer.close();
                    writer.flush();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void readZip(File f){

        if (zipFile != null) {
            try {
                zipFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setTitle(getAppTitle() + " - " + f.getName());
        HashMap<String, VersionFolder> versionMap = new HashMap<>();
        ZipUtils.ReturnData returnData = ZipUtils.readZipFile(f);
        List<DisplayableFile> files = returnData.getDisplayableFiles();
        m_usageCSV = returnData.getUsageCSV();
        zipFile = returnData.getZipFile();
        for(DisplayableFile fl : files){
            String[] strSplit= fl.getOriginalFileName().split("/");
            int strlen = (strSplit).length;
            if(fl.getOriginalFileName().startsWith("app")){
                if(strlen == 2){
                    if(!versionMap.containsKey("Root")){
                        versionMap.put("Root",new VersionFolder("Application Root"));
                    }
                    versionMap.get("Root").addFile(fl);
                }
                else if(strlen == 3){
                    String dir = strSplit[1];
                    if(dir.startsWith("Dex")){
                        if(!versionMap.containsKey(dir)){
                            versionMap.put(dir,new VersionFolder(dir));
                        }
                        versionMap.get(dir).addFile(fl);
                    }
                }
            }
        }
        String s = returnData.getVerificationDB();
        if(s != null){
            m_verificationMenu.setEnabled(true);
            verificationDB = s;
        }
        else{
            m_verificationMenu.setEnabled(false);
            verificationDB = null;
        }
        if(m_versionFolderList != null) {
            m_versionFolderList.clear();
        }
        m_versionFolderList = new ArrayList<>(versionMap.values());
        m_versionFolderList.sort(Comparator.comparing(VersionFolder::getDirectory));
        for (VersionFolder d : m_versionFolderList) {
            d.prepareData();
            if(d.getLocation() != null && !(d.getLocation().equals(" ''"))){
                location = d.getLocation().replace("'","").replace(" ","");; //Remove apostrophes and spaces
            }
            if(d.getSerial() != null){
                serialNum= d.getSerial();
            }
        }
        if(location == null){
            location = "XXX";
        }
        if(serialNum== null){
            serialNum= "XXX";
        }


        changeLogFile();
    }

    private void changeLogFile()
    {
        createTabs();
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
