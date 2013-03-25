package SuperHeater.Misc;

import java.util.HashMap;
import java.util.Map;


public class Consts {
    public static final Map<String,String>      CONFIG  = new HashMap<String,String>();
    public static final Map<String,Integer[][]> BARREQ  = new HashMap<String,Integer[][]>();
    public static final Map<String,Double[]>    BARXP   = new HashMap<String,Double[]>();
    public static final String                  VERSION = "1.40";

    public static String      CURRENT_STATUS      = "";
    public static boolean     BANK_NOW;
    public static boolean     SHOW_GUI            = true;
    public static boolean     GO                  = false;
    public static boolean     NO_ORES             = false;
    public static long        START_TIME          = System.currentTimeMillis();
    public static long        RUNTIME             = 0;
    public static int         BARS_MADE           = 0;
    public static int         NATURE_RUNE         = 561;
    public static int         PRIMARY_ORE         = 0;
    public static int         SECONDARY_ORE       = 0;
    public static int         BAR_TARGET          = 0;
    public static int         COAL_BAG            = 18339;
    public static Integer[][] ACTIVE_ORE;
}
