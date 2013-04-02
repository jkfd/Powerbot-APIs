package SuperHeater.Misc.Logging;

import java.util.Date;


public class Entry {
    
    private Date time;
    private String type;
    private Object message;
    
    public Entry(Date tm, String ty, Object o){
        time        = tm;
        type        = ty.toUpperCase();
        message     = o;        
    }
    
    public Date getTime(){
        return this.time;
    }
    
    public String getType(){
        return this.type;
    }
    
    public Object getMessage(){
        return this.message;
    }
}
