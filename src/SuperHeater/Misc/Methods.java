package SuperHeater.Misc;

import SuperHeater.GrandExchange.GE;
import SuperHeater.Misc.Logging.Log;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.powerbot.core.Bot;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class Methods {
    
    /**
     * Not working yet due to strange file permission errors.
     * More to come
     */
    public static void saveScreenShot(){
        try {
            Environment.getStorageDirectory();
            BufferedImage i = Environment.captureScreen();
            File f = new File("savedScreenShot.png");
            f.createNewFile();
            ImageIO.write(i, "png", f);
        } catch (IOException e) {
            Log.error(e);
        }
    }
    
    public static String getDistanceToTarget(){
        if (Consts.BAR_TARGET > 0) {
            return Integer.toString(Consts.BAR_TARGET-Consts.BARS_MADE);
        } else {
            return "N/A";
        }
    }

    

    public static boolean checkNeedBank(){
        // BANK IF:
        //      Can't make one bar (Pri || Sec < minimum)
        //      Primary is greater than withdrawal amount (Too many)
        //      Bars exceed the PWA (6 mith bars, 4 addy, 4 rune, et.)
        //      Banks for Natures as well. Should lead to REE.
        return(
                (Inventory.getCount(Consts.NATURE_RUNE) < 1)                       ||
                (Inventory.getCount(Consts.PRIMARY_ORE) < 1)                       ||
                (Inventory.getCount(Consts.PRIMARY_ORE) > (getPWA(false)))         ||
                (Inventory.getCount(Consts.SECONDARY_ORE) < (Consts.ACTIVE_ORE[1][1]))    ||
                (Inventory.getCount(Consts.ACTIVE_ORE[2][0]) > getPWA(false))
                );
    }
    public static boolean useCB(){
        
        return(
                (Consts.CONFIG.get("useCB").equals("TRUE")) && 
                (Inventory.getCount(Consts.COAL_BAG) > 0) && 
                (Consts.SECONDARY_ORE == 453)
              );
        
    }
    
    public static int getTotalInventorySpaces(){
        if(useCB()){
            return 52;
        } else {
            return 27;
        }
    }
    
    public static int getPWA(boolean withdraw){
        int amount = (int) Math.ceil(getTotalInventorySpaces()/(Consts.ACTIVE_ORE[0][1]+Consts.ACTIVE_ORE[1][1]));

        if(withdraw == true && amount >= 27) {
            return 0;
        }

        return amount;
    }

    public static int getSWA(boolean withdraw){
        int amount = Inventory.getCount(Consts.PRIMARY_ORE)*Consts.ACTIVE_ORE[1][1];
        
        if(useCB()){
            amount -= 26;
        }

        // If SWA can be expressed as "ALL", return 0
        if(withdraw == true && getTotalInventorySpaces()-getPWA(false) == amount) {
            return 0;
        }

        return amount;
    }

    public static float getBarsPerHour(){
        float secsRan = ((Consts.RUNTIME)/1000.0f);
        return Math.round((float)Consts.BARS_MADE/(secsRan/3600.0f));
    }

    /**
     *
     * @param skill
     * @return
     */
    public static int getXpToNextLevel(int skill){
        return (Skills.getExperienceToLevel(skill, Skills.getLevel(skill)+1));
    }

    public static long getBarsToNextLevel(int skill, int skillIndex){
        return (getXpToNextLevel(skill)/Math.round(Consts.BARXP.get(Consts.CONFIG.get("barType"))[skillIndex]));
    }

    public static double getSmithingXp(){
        return (Consts.BARS_MADE*Consts.BARXP.get(Consts.CONFIG.get("barType"))[1]);
    }

    public static Item[] reverseItemArray(Item[] a){
        for(int i = 0; i < a.length/2; i++){
            Item temp = a[i];
            a[i] = a[a.length - i - 1];
            a[a.length - i - 1] = temp;
        }
        return a;
    }

    /**
     * Called after casting a spell and waiting for the tabs to switch to Inv.
     * @param a
     * @return
     */
    public static boolean waitForTab(Tabs t) {
        int time = 0;

        while (Tabs.getCurrent() != t && time <= 1000) {
                time += 50;
                Task.sleep(50);
        }

        return (Tabs.getCurrent() == t);
    }
    
    public static boolean waitForWidget(WidgetChild w) {
        int time = 0;
        
        while (!w.visible() && time <= 1000) {
            time += 50;
            Task.sleep(50);
        }
        
        return (w.visible());
    }

    // REE = Retry Exhaustion Error (Most bank actions have a limited retry amount)
    public static void getREE(String error, int retry){
        if (retry == 5) {
            Log.severe("Retry Exhaustion Error: " + error + "\nStopping Script");
            stopSuperHeater();
        }
    }
    
    public static boolean withdrawBarsAsNotes(){
        int retries = 3;
        Log.info("Withdrawing all bars as notes");
        
        // Open Bank if not open
        for (int i = 0; !Bank.isOpen() && !Widgets.get(759, 0).isOnScreen() && i<retries; i++) {
            Bank.open();
            Task.sleep(Random.nextInt(20, 472));

            Methods.getREE("Open Bank", i);
        }
        
        // Deposit all unnoted bars in our inventory
        while (Inventory.getCount(Consts.BARID) > 0) {
            Log.info("Depositing Excess Primary Ore.");
            Bank.deposit(Consts.BARID, Bank.Amount.ALL);
            Task.sleep(Random.nextInt(20, 472));
        }
        
        // Find out how many bars we have
        int totalBarCount = Bank.getItem(Consts.BARID).getWidgetChild().getChildStackSize();
        
        // Get all the bars as notes
        Bank.setWithdrawNoted(true);
        
        // Try a max of 3 times to withdraw all bars while the 
        // inventory count is less than the total bar count
        for (int i=0; (Inventory.getCount(Consts.BARID+1) < totalBarCount) && i < 3; i++) {
             Bank.withdraw(Consts.BARID, Bank.Amount.ALL);
             Task.sleep(Random.nextInt(97, 424));
        }
        
        Log.info("We have a total of: " + Inventory.getCount(Consts.BARID+1) + " Bars.");
        Log.info("We should have: " + totalBarCount);
        
        if (!Bank.close()) {
            Log.error("Could not close bank after withdrawing bars.");
        }
        
        return (Inventory.getItem(Consts.BARID+1).getStackSize() >= totalBarCount);
        
    }
    
    public static boolean sellBars(int price){
        int timer = 0;
        int sellSleeper = Random.nextInt(1200, 2000);
        
        if (!withdrawBarsAsNotes()) {
            Log.error("Could not withdraw any bars.");
            return false;
        }
        
        while (!GE.sell(Consts.BARID+1, Consts.CONFIG.get("barType").concat(" Bar"), price) && timer <= 4000) {
            timer += sellSleeper;
            Task.sleep(sellSleeper);
        }
        
        GE.close();
        
        return true;
    }

    public static void stopSuperHeater(){
        
        // If script has already been stopped once, return out.
        if (Consts.STOPPED) {
            return;
        }
        
        Log.info("Stopping the script...");
        
        // Set strategy determinants to false to prevent infinite loops
        Consts.GO       = false;
        Consts.BANK_NOW = false;
        Consts.SHOW_GUI = false;
        Consts.STOPPED  = true;
        
        // Print info to console
        System.out.println("\n\n---------------------------------");
        Log.info("Ran for: " + Time.format(Consts.RUNTIME));
        Log.info("Bars made this session: " + Consts.BARS_MADE);
        Log.info("Bars / Hour:" + Methods.getBarsPerHour());
        System.out.println("---------------------------------\n\n"
                + "Thanks for using the script.\n"
                + "Be sure to suggest features and report bugs!\n\n");
        
        // Sell bars if asked to
        if (Consts.CONFIG.get("sellBars").equals("TRUE")) {
            Log.info("Selling Bars as requested");
            int price = Integer.decode(Consts.CONFIG.get("barPrice").toString());
            sellBars(price);
            Log.info("Bars sold.");
        }
        
        // Close Bank if necessary
        while (Bank.isOpen()) {
            Log.info("Closing bank");
            Bank.close();
            Task.sleep(Random.nextInt(223, 882));
        }

        // Logout on stop if told to.
        for (   int i = 0;
                Consts.CONFIG.get("stopAction").equals("logout") &&
                Game.isLoggedIn() &&
                i < Integer.parseInt(Consts.CONFIG.get("retries"));
                i++
             ){
            Log.severe("Logging out...");
            Game.logout(false);
            Task.sleep(Random.nextInt(579, 1235));
        }
        
        Log.severe("Shutting down. Bye-bye");
        Bot.instance().getScriptHandler().shutdown();

    }
}
