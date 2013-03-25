package SuperHeater.Misc;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.Context;

public class Methods {
    
    public static Image getBackgroundImage() throws Exception{
        URL url = new URL("http://i.imgur.com/8IaeTV2.png");
        Image img = Toolkit.getDefaultToolkit().getImage(url);
        return img;
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

    public static void stopSuperHeater(){

        // Set strategy determinants to false to prevent infinite loops
        Consts.GO       = false;
        Consts.BANK_NOW = false;
        Consts.SHOW_GUI = false;

        // Close Bank if necessary
        while (Bank.isOpen()) {
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

        Log.severe("Stopping Script");
        Context.get().getScriptHandler().shutdown();

    }
}
