package SuperHeater.Misc.Logging;

import java.util.Date;


public class Entry {
    
    private static Date time;
    private static String type;
    private static Object message;
    
    public Entry(Date tm, String ty, Object o){
        time        = tm;
        type        = ty.toUpperCase();
        message     = o;        
    }
    
    public Date getTime(){
        return time;
    }
    
    public String getType(){
        return type;
    }
    
    public Object getMessage(){
        return message;
    }
}
