package SuperHeater.Misc;

import java.awt.Frame;
import java.util.HashMap;
import java.util.Map;
import sk.action.Ability;
import sk.action.book.magic.Spell;


public class Globals {
    public static final Map<String,String>      CONFIG  = new HashMap<String,String>();
    public static final String                  VERSION = "1.61";
    
    public final static int   COAL_BAG            = 18339;
    public final static int   NATURE_RUNE         = 561;

    public static String      CURRENT_STATUS      = "";
    public static boolean     BANK_NOW;
    public static boolean     SHOW_PAINT          = true;
    public static boolean     GO                  = false;
    public static boolean     NO_ORES             = false;
    public static boolean     FIRST_START         = true;
    public static boolean     STOPPED             = false;
    public static long        START_TIME          = System.currentTimeMillis();
    public static long        RUNTIME             = 0;
    public static int         BARS_MADE           = 0;
    public static int         DEPOSIT_COUNT       = 0;
    public static int         PRIMARY_ORE         = 0;
    public static int         SECONDARY_ORE       = 0;
    public static int         BARID               = 2349;
    public static int         BAR_TARGET          = 0; 
    public static int         SELLING_PRICE       = 100000;
    public static BARREQ ACTIVE_ORE;
    
    public static Frame RSBOT_FRAME;
    
    //Placed here due to performance issues
    public static Ability     SUPERHEATABILITY    = Ability.ALL_ABILITIES.get(Spell.SUPERHEAT_ITEM.getAbilityId());
    
    public enum BARXP {
        BRONZE      (6.20),
        IRON        (12.50),
        SILVER      (13.67),
        GOLD        (22.50),
        STEEL       (17.50),
        MITHRIL     (30.00),
        ADAMANTITE  (37.50),
        RUNITE      (50.00);
        
        private double magicXP = 53.0;
        private double smithingXP;
        
        private BARXP(double smithing) {
            this.smithingXP = smithing;
        }
        
        public double getMagicXP(){
            return this.magicXP;
        }
        
        public double getSmithingXP(){
            return this.smithingXP;
        }
    }
    
    public enum BARREQ {
        BRONZE      (436,1, 438,1,  2349, 2350),
        IRON        (440,1, 0,0,    2351, 2352),
        SILVER      (442,1, 0,0,    2355, 2356),
        GOLD        (444,1, 0,0,    2357, 2358),
        STEEL       (440,1, 453,2,  2353, 2354),
        MITHRIL     (447,1, 453,4,  2359, 2360),
        ADAMANTITE  (449,1, 453,6,  2361, 2362),
        RNITE       (451,1, 453,8,  2363, 2364);
        
        private int pid; 
        private int pam; 
        private int sid; 
        private int sam; 
        private int bid; 
        private int nbid;
        
        private BARREQ(int pid, int pam, int sid, int sam, int bid, int nbid) {
            this.pid    = pid;
            this.pam    = pam;
            this.sid    = sid;
            this.sam    = sam;
            this.bid    = bid;
            this.nbid   = nbid;
        }
        
        public int getPrimaryID() {
            return this.pid;
        }
        
        public int getPrimaryAmount() {
            return this.pam;
        }
        
        public int getSecondaryID() {
            return this.sid;
        }
        
        public int getSecondaryAmount() {
            return this.sam;
        }
        
        public int getBarID() {
            return this.bid;
        }
        
        public int getNotedBarID() {
            return this.nbid;
        }
    }
}
