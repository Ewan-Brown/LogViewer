package com.bkin.logviewer.gui.utils;

import com.bkin.logviewer.files.Line;
import com.bkin.logviewer.files.DisplayableFile;
import com.bkin.logviewer.gui.common.filters.FilterDate;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipUtils {

    private static final Pattern DATE_PATTERN_DEX = Pattern.compile("^[0-9]{4}");
    private static final Pattern DATE_PATTERN_DEXEX = Pattern.compile("^\\[[0-9]{4}");

    public static class ReturnData{

        private ArrayList<DisplayableFile> displayableFiles;
        private DisplayableFile usageCSV; //TODO Isn't there only one of these?
        private String vDB = null;

        public ArrayList<DisplayableFile> getDisplayableFiles() {
            return displayableFiles;
        }

        public String getVerificationDB(){
            return vDB;
        }

        public ZipFile getZipFile() {
            return zipFile;
        }

        ZipFile zipFile;

        public DisplayableFile getUsageCSV() {
            return usageCSV;
        }

    }

    public static ReturnData readZipFile(File zip) {
        ArrayList<DisplayableFile> files = new ArrayList<>();
        ReturnData returnData = new ReturnData();
        try {
            returnData.zipFile = new ZipFile(zip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zip)))){
            for (ZipEntry zipEntry;(zipEntry = zin.getNextEntry()) != null; )
            {
                String entryName = zipEntry.getName();
                String[] directorySplit = entryName.split("/");
                if (directorySplit[0].equals("app") || directorySplit[0].equals("user")) { //Magic strings?
                    DisplayableFile rf = createReadableFile(zin,zipEntry);
                    if(rf.getFileType() == DisplayableFile.FileType.USAGE){
                        returnData.usageCSV = rf;
                    }
                    else {
                        files.add(rf);
                    }
                }
                else if(directorySplit[0].equals("verification_db")){
                    returnData.vDB = entryName;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        returnData.displayableFiles = files;
        return returnData;
    }

    private static DisplayableFile createReadableFile(ZipInputStream zin, ZipEntry zipEntry){

        DisplayableFile.FileType fileType = getFileType(zipEntry);
        return new DisplayableFile(getLines(zin,fileType),
                (new File(zipEntry.getName())).getName(),//Need just the file name, not the entire path.
                fileType,zipEntry.getName());
    }

    private static DisplayableFile.FileType getFileType(ZipEntry zipEntry){
        String fileName = new File(zipEntry.getName()).getName(); //Need just the file name, not the entire path.
        DisplayableFile.FileType retVal = DisplayableFile.FileType.OTHER;
        for (DisplayableFile.FileType dt : DisplayableFile.FileType.values()) {
            if(dt == DisplayableFile.FileType.INI){
                if (fileName.equals(dt.getFileName())) {
                    retVal = dt;
                    break;
                }
            }
            else if (dt.getFileName() != null && fileName.startsWith(dt.getFileName())) {
                retVal = dt;
                break;
            }
        }
        return retVal;
    }

    private static ArrayList<Line> getLines(ZipInputStream z, DisplayableFile.FileType fileType){
        ArrayList<Line> lines = new ArrayList<>();
        BufferedReader b = new BufferedReader(new InputStreamReader(z));
        String str;
        Line lastReal = null;
        while((str = getNextLine(b)) != null){
            if(str.isEmpty()){
                continue;
            }
            Line.LineType t = parseLineType(str, fileType);
            if(lastReal != null && (fileType == DisplayableFile.FileType.LOGS && t == Line.LineType.MISC)){
                lastReal.addText(str);
                lastReal.setMultiLine(true);
            }
            else {
                Line l = createLine(str);
                l.setLineType(t);
                Date d = findDate(str);
                frameFailTest(fileType, str, l);
                setLineDate(l, d);
                addLineToList(fileType, l, lines);
                lastReal = l;
            }
        }
        return lines;
    }

    private static Line createLine(String str){
        return new Line(str);
    }

    private static String getNextLine(BufferedReader s){
        try {
            return s.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; //poor error handling
    }

    private static Date findDate(String str) {
        Date d = null;
        int dateCharIndex = doesStartWithBracket(str) ? 1 : 0;
        if(CUSTOM_PARSE_FLAG)
        {
            d = customParseDate(str,dateCharIndex);
        }
        else {
            if (Character.isDigit(str.charAt(dateCharIndex))) {
                try {
                    d = parseDate(str, dateCharIndex);
                } catch (ParseException e) {
                    //Some lines just do not contain a date. This is an expected error with no seen consequences.
                }
            }
        }
        return d;
    }

    private static boolean doesStartWithBracket(String str){
        return (str.startsWith("["));
    }

    public static boolean CUSTOM_PARSE_FLAG = false;

    private static Date parseDate(String s, int ind) throws ParseException {
        return FilterDate.DATE_FORMAT_SECONDS.parse(s.substring(ind));
    }

    private static int[] mults = new int[]{1000,100,10,1};
    private static Calendar c = new GregorianCalendar();

    //yyyy-MM-dd HH:mm:ss;
    private static Date customParseDate(String s, int ind){ //FIXME Seems to be a month off? maybe only sometimes?
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int min = 0;
        int sec = 0;

        if(s.length() < 20){
            return new Date(0);
        }

        for(int i = 0; i < 4;i++){
            int c = s.charAt(i+ind) - 48; //adjust for ascii value, 0 == 48
            year += mults[i] * c;
        }

        ind += 5;

        for(int i = 0; i < 2;i++){
            int c = s.charAt(i+ind) - 48; //adjust for ascii value, 0 == 48
            month += mults[i+2] * c;
        }

        ind += 3;

        for(int i = 0; i < 2;i++){
            int c = s.charAt(i+ind) - 48; //adjust for ascii value, 0 == 48
            day += mults[i+2] * c;
        }
        ind += 3;

        for(int i = 0; i < 2;i++){
            int c = s.charAt(i+ind) - 48; //adjust for ascii value, 0 == 48
            hour += mults[i+2] * c;
        }
        ind += 3;
        for(int i = 0; i < 2;i++){
            int c = s.charAt(i+ind) - 48; //adjust for ascii value, 0 == 48
            min += mults[i+2] * c;
        }
        ind += 3;
        for(int i = 0; i < 2;i++){
            int c = s.charAt(i+ind) - 48; //adjust for ascii value, 0 == 48
            sec += mults[i+2] * c;
        }
        setCalendar(year,month,day,hour,min,sec);
//        System.out.println(s);
//        System.out.println("============================>"+c.getTime().toString());
        return getTheTime();
    }

    private static Date getTheTime(){
        return c.getTime();
    }
    private static void setCalendar(int year, int month, int day, int hour, int min, int sec){
        c.set(year ,month,day, hour, min ,sec);
    }

    private static Line.LineType parseLineType(String str, DisplayableFile.FileType fileType){
        if(fileType == DisplayableFile.FileType.INI){
            if (str.startsWith("[") && str.endsWith("]"))
            {
                return Line.LineType.DEBUG_INI; // purple headers in the config file
            }
        }
        else if (DATE_PATTERN_DEX.matcher(str).find() || DATE_PATTERN_DEXEX.matcher(str).find()) //TODO replace regex
        {
            // Try to determine what type of line this is
            for (Line.LineType type : Line.LineType.values())
            {
                if (type.equals(Line.LineType.MISC))
                    continue;

                if (str.contains(type.toString()))
                {
                   return type;
                }
            }
            return Line.LineType.MISC;
        }
        return Line.LineType.MISC;
    }
    private static void frameFailTest(DisplayableFile.FileType fileType, String str, Line l){
        if (fileType == DisplayableFile.FileType.FRAMES){

            boolean failure = false;

            int lastCommaIndex = str.lastIndexOf(","); //to remove the :Testing:3.6.2 things
            String str2 = str.substring(0,lastCommaIndex+1);
            int lastColonIndex = str2.lastIndexOf(":"); //Seems to be in every line and works so far

            String cutString = str2.substring(lastColonIndex+1,lastCommaIndex);
            cutString = cutString.replace(" ","");

            String[] spliterated = cutString.split(",");
            int[] numbers = Arrays.stream(spliterated).mapToInt(Integer::parseInt).toArray();
            int totalFrame = numbers[0];
            int framesFailed = 0;

            for(int i = 1; i < numbers.length;i++){
                if(i > 2 && numbers[i] != 0) failure = true;
                framesFailed += numbers[i];
            }

            if(framesFailed * 100 > totalFrame){ //If over 1% of frames failed
                failure = true;
            }

            if(failure){
                l.setLineType(Line.LineType.FRAME_FAIL);
            }
        }
    }
    private static void setLineDate(Line l, Date d){
        l.setParsedDate(d);

    }

    private static void addLineToList(DisplayableFile.FileType fileType, Line l, List<Line> lines){
        if(l.toString().isEmpty()){
            return;
        }
        if(fileType == DisplayableFile.FileType.LOGS){
            //if(l.getType() != Line.LineType.MISC){
                lines.add(l);
            //}
        }
        else {
            lines.add(l);
        }
    }

}
