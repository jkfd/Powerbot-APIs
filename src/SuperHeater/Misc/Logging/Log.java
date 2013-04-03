package SuperHeater.Misc.Logging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Log {
    private static List<Entry> completeLog = new ArrayList<Entry>();
    
    public static void severe(Object o){
        addToLog("severe", o, true);
        
    }
    
    public static void info(Object o){
        addToLog("info", o, true);
    }
    
    public static void error(Object o){
        addToLog("error", o, true);
    }
    
    public static void antiban(Object o){
        addToLog("antiban", o, true);
    }
    
    /**
     * Returns a time in the format specified in @param format.
     * @param format The format of the time to be displayed (HH:mm:ss);
     * @param d The Date object to be formated.
     * @return 
     */
    public static String getTimestamp(String format, Date d){
        
        if (d == null) {
            d = new Date();
        }
        
        SimpleDateFormat f = new SimpleDateFormat(format);
        
        return ("[" + f.format(d) + "]");
    }
    
    /**
     * Returns only log entries with a type that matches the argument.
     * @param type
     * @return 
     */
    public static List<Entry> getEntriesByType(String type){
        List <Entry> results = new ArrayList<Entry>();
        
        for (Entry e : completeLog) {
            if (e.getType().equals(type.toUpperCase())) {
                results.add(e);
            }
        }
        
        return results;
    }
    
    /**
     * Gives a String type list of entries separated by a 
     * newline character regardless of type or time.
     * @return 
     */
    public static String dumpLog(){
        String result = "------- LOG DUMP: -------\n";
        
        for (Entry e : completeLog) {
            result = result.concat(e.stringEntry().concat(System.lineSeparator()));
        }
        
        return result;
    }
    
    /**
     * Adds an object to the completeLog list as a Log.Entry and prints it to
     * the console as a string if asked to.
     * @param type Type of log entry (info, severe, error, etc.)
     * @param o The object that makes up the main message part of the log entry.
     * @param printToConsole Prints log entry to console if TRUE
     */
    private static void addToLog(String type, Object o, boolean printToConsole){
        Entry logEntry = new Entry(new Date(), type, o);
        completeLog.add(logEntry);
        
        System.out.println(logEntry.stringEntry());
    }
}
