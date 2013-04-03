package SuperHeater.Misc;

import java.util.HashMap;
import java.util.Map;
import sk.action.Ability;
import sk.action.book.magic.Spell;


public class Consts {
    public static final Map<String,String>      CONFIG  = new HashMap<String,String>();
    public static final Map<String,Integer[][]> BARREQ  = new HashMap<String,Integer[][]>();
    public static final Map<String,Double[]>    BARXP   = new HashMap<String,Double[]>();
    public static final String                  VERSION = "1.53";
    
    public final static int   COAL_BAG            = 18339;
    public final static int   NATURE_RUNE         = 561;

    public static String      CURRENT_STATUS      = "";
    public static boolean     BANK_NOW;
    public static boolean     SHOW_GUI            = true;
    public static boolean     SHOW_PAINT          = true;
    public static boolean     GO                  = false;
    public static boolean     NO_ORES             = false;
    public static boolean     STOPPED             = false;
    public static long        START_TIME          = System.currentTimeMillis();
    public static long        RUNTIME             = 0;
    public static int         BARS_MADE           = 0;
    public static int         PRIMARY_ORE         = 0;
    public static int         SECONDARY_ORE       = 0;
    public static int         BARID               = 0;
    public static int         BAR_TARGET          = 0; 
    public static int         SELLING_PRICE       = 0;
    public static Integer[][] ACTIVE_ORE;
    
    //Placed here due to performance issues
    public static Ability     SUPERHEATABILITY    = Ability.ALL_ABILITIES.get(Spell.SUPERHEAT_ITEM.getAbilityId());
}
