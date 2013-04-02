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
    
    public static String getTimestamp(String format, Date d){
        
        if (d == null) {
            d = new Date();
        }
        
        SimpleDateFormat f = new SimpleDateFormat(format);
        
        return ("[" + f.format(d) + "]");
    }
    
    public static List<Entry> getEntriesByType(String type){
        List <Entry> results = new ArrayList<Entry>();
        
        for (Entry e : completeLog) {
            if (e.getType().equals(type.toUpperCase())) {
                results.add(e);
            }
        }
        
        return results;
    }
    
    public static void printToConsole(Entry logEntry) {
        System.out.println(
            getTimestamp("HH:mm:ss", logEntry.getTime())
            + "[" + logEntry.getType() + "] "
            + logEntry.getMessage());
    }
    
    private static void addToLog(String type, Object o, boolean printToConsole){
        Entry logEntry = new Entry(new Date(), type, o);
        completeLog.add(logEntry);
        
        if (printToConsole) {
            printToConsole(logEntry);
        }
    }
}
