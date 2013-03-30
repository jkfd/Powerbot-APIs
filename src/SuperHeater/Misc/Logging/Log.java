package SuperHeater.Misc.Logging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Log {
    public static List<Entry> completeLog = new ArrayList<Entry>();
    
    public static void severe(Object o){
        addToLog("severe", o, true);
        
    }
    
    public static void info(Object o){
        addToLog("info", o, true);
    }
    
    public static void error(Object o){
        addToLog("error", o, true);
    }
    
    public static String getTimestamp(String format, Date d){
        
        if (d == null) {
            d = new Date();
        }
        
        SimpleDateFormat f = new SimpleDateFormat(format);
        
        return ("[" + f.format(d) + "]");
    }
    
    private static void addToLog(String type, Object o, boolean printToConsole){
        Entry logEntry = new Entry(new Date(), type, o);
        completeLog.add(logEntry);
        
        if (printToConsole) {
            System.out.println(
                    getTimestamp("HH:mm:ss", logEntry.getTime())
                    + "[" + logEntry.getType() + "] "
                    + logEntry.getMessage());
        }
    }
}
