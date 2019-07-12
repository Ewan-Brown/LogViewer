package com.bkin.logviewer.usage;

import com.bkin.logviewer.files.DisplayableFile;
import com.bkin.logviewer.files.Line;
import com.bkin.logviewer.gui.common.filters.FilterDate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class VersionFolder {


    private ArrayList<DisplayableFile> m_files;

    private String m_name;
    private String m_location;

    public String getSerial() {
        return m_serial;
    }

    private String m_serial;

    public String getLocation() {
        return m_location;
    }


    public VersionFolder(String name){
        m_name = name;
        m_files = new ArrayList<>();
    }

    public void addFile(DisplayableFile file) {
        m_files.add(file);
    }

    static class FileComparator implements Comparator<DisplayableFile> {
        @Override
        public int compare(DisplayableFile o1, DisplayableFile o2) {
            return (o1.getFirstDate().after(o2.getFirstDate())? 1 : -1);
        }
    }

    public void prepareData(){

        boolean xPCVersionFound = false;
        boolean serialFound = false;

        ArrayList<Line> versionChanges = new ArrayList<>();
        ArrayList<Line> extraData = new ArrayList<>();
        DisplayableFile dexLogTotal = concatenateFileType(DisplayableFile.FileType.LOGS,m_files);
        DisplayableFile externalLogTotal = concatenateFileType(DisplayableFile.FileType.EXTERNAL_LOG, m_files);
        if(externalLogTotal != null) {
            m_files.add(externalLogTotal);
        }
        if(dexLogTotal != null){
            m_files.add(0,dexLogTotal);
            String dexVersion = "";
            for(Line l : dexLogTotal.getLines()){
                if(l.getType() == Line.LineType.INFO){
                    String str = l.getText();
                    int dexVersionIndex = str.indexOf(dexVersionStringMatch);
                    if(dexVersionIndex != -1){
                        dexVersionIndex += (dexVersionStringMatch).length();
                        String end = str.substring(dexVersionIndex).split(" ")[0];
                        if(end.compareTo(dexVersion) != 0){
                            versionChanges.add(new Line(FilterDate.DATE_FORMAT_SECONDS.format(l.getParsedDate()) + " Version set to "+ end));
                            dexVersion = end;
                        }
                    }
                    int xPCVersionIndex = str.indexOf(xPCVersionStringMatch);
                    if(xPCVersionIndex != -1 && !xPCVersionFound){
                        xPCVersionFound = true;
                        xPCVersionIndex += xPCVersionStringMatch.length();
                        extraData.add(new Line("xPC Version used: " + str.substring(xPCVersionIndex)));
                    }

                    int serialIndex = str.indexOf(SystemSerialStringMatch);
                    if(serialIndex != -1  && !serialFound){
                        String end = str.substring(serialIndex);
                        if(containsDigits(end)) {
                            serialFound = true;
                            m_serial = end.substring(SystemSerialStringMatch.length());
                            extraData.add(new Line(end)); //TODO Sometimes serialIndex is empty, and (rarely) it can change.
                        }
                    }
                }

            }
            DisplayableFile versionChangeFile = new DisplayableFile(versionChanges,"Version Log", DisplayableFile.FileType.VERSION_LOG,dexLogTotal.getOriginalFileName());
            m_files.add(versionChangeFile);
        }
        boolean done = false;
        for(DisplayableFile f : m_files){
            if(f.getFileType() == DisplayableFile.FileType.INI){
                for(Line l : f.getLines()){
                    int locationIndex = l.getText().indexOf("location:");
                    if(locationIndex != -1){
//                        System.out.println(l.getText().substring(l.getText().indexOf(":") + 1));
                        String end = l.getText().substring(locationIndex + "location:".length());
                        m_location = end;
//                        extraData.add(new Line(end)); //TODO Sometimes serialIndex is empty, and (rarely) it can change.
                        done = true;
                        break;
                    }
                }
            }
            if(done)break;
        }
        DisplayableFile extraDataFile = new DisplayableFile(extraData,"Extra data", DisplayableFile.FileType.EXTRA_DATA,"data");
        m_files.add(extraDataFile);


    }

    private static DisplayableFile concatenateFileType(DisplayableFile.FileType ft, List<DisplayableFile> files){
        DisplayableFile concatenatedFile = null;
        Iterator<DisplayableFile> itr = files.iterator();

        //Get list of dex logs
        ArrayList<DisplayableFile> dexFiles = new ArrayList<>();
        while(itr.hasNext()) {
            DisplayableFile f = itr.next();
            if(f.getFileType() == ft){
                dexFiles.add(f);
                itr.remove();
            }
        }

        //Sort dex logs then concatenate
        dexFiles.sort(new FileComparator());
        for(DisplayableFile f : dexFiles){
            if(concatenatedFile == null){
                concatenatedFile = f;
            }else{
                concatenatedFile.addLines(f.getLines());
            }
        }
        return concatenatedFile;
    }

    private static boolean containsDigits(String str){
        boolean retval = false;
        for(int i = 0; i < str.length();i++){
            char c = str.charAt(i);
            if(Character.isDigit(c)){
                retval = true;
                break;
            }
        }
        return retval;
    }

    private static final String dexVersionStringMatch = "Dex version: ";
    private static final String xPCVersionStringMatch = "xPC used:";
    private static final String SystemSerialStringMatch = "System serial #: ";

    public String getDirectory(){
        return m_name;
    }

    public ArrayList<DisplayableFile> getAllFiles(){
        return m_files;
    }

}
