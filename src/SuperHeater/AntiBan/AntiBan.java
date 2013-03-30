package SuperHeater.AntiBan;

import SuperHeater.Misc.Consts;
import SuperHeater.Misc.Logging.Log;
import SuperHeater.Misc.Methods;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.widget.WidgetChild;


public class AntiBan extends Node{
    private int ABFREQ = getFrequency();
    
    // REGift = Random Event Gift.
    
    @Override
    public boolean activate() {
        return (((Random.nextInt(0, ABFREQ) == 1) && !Consts.BANK_NOW && Consts.GO) || hasREGift());
    }
    
    @Override
    public void execute() {
        int r = Random.nextInt(0, 3);


        // If we have a REGift, deal with it and RETURN!
        if (hasREGift()) {
            handleREGift();
            return;
        }

        switch (r) {
            case 0:
                // Set Status
                Consts.CURRENT_STATUS = "ANTIBAN: Looking around";
                lookAround();
                break;

            case 1:
                // Set Status
                Consts.CURRENT_STATUS = "ANTIBAN: Checking Stats";
                checkStats();
                break;

            case 2:
                // Set Status
                Consts.CURRENT_STATUS = "ANTIBAN: Examining local players";
                checkRandomPlayer();
                break;

            default:
                // Set Status
                Consts.CURRENT_STATUS = "ANTIBAN: Tried, but couldn't do it";
                break;
        }
    }

    private int getFrequency(){
        return 125 - Integer.parseInt(Consts.CONFIG.get("abFrequency"));
    }

    private void lookAround() {

        do {
            Camera.setAngle(Random.nextInt(0, 483));
            Task.sleep(Random.nextInt(10, 692));
            Camera.setPitch(Random.nextInt(0, 90));
            Task.sleep(Random.nextInt(223, 492));
        } while (Random.nextInt(0, 2) != 1);
    }

    private void checkStats() {

        do {
            Tabs.STATS.open();
            Widgets.get(320, Random.nextInt(1, 115)).hover();
            Task.sleep(Random.nextInt(513, 1238));
        } while (Random.nextInt(0, 2) != 1);
    }

    private void checkRandomPlayer() {

        do {
            Player[] ps = Players.getLoaded(Players.ALL_FILTER);
            Player p = ps[Random.nextInt(0, ps.length)];

            if(p.isOnScreen()) {
                p.click(false);
                Task.sleep(Random.nextInt(182, 1267));
            }

        } while (Random.nextInt(0, 2) != 1);
    }

    /**
     * Checks for Random Event Gift if inventory is open.
     * @return TRUE if REGift is in inventory. FALSE otherwise.
     */
    private boolean hasREGift(){
        // Only run the check if the Inventory tab is open.
        if(!Tabs.INVENTORY.isOpen() && !Bank.isOpen()){
            return false;
        }

        if(Inventory.getCount(14664) > 0) {
            Log.info("Found REGift in inventory.");
            return true;
        }

        return false;

    }

    /**
     * Handles the reception of a random event gift by opening it and getting
     * the coin-reward.
     */
    private void handleREGift(){

        // Makes sure that the bank is closed
        if(Bank.isOpen()){
            Bank.close();
        }

        // Opens the Random Event Gift in your Inventory
        for (int i = 0; (!Widgets.get(202).getChild(0).isOnScreen()) && (i < 3); i++){
            Inventory.getItem(14664).getWidgetChild().interact("Open");
            Log.info("Opening Random Event gift");
            Task.sleep(Random.nextInt(828, 1551));
        }

        WidgetChild REGIFT_COINS    = Widgets.get(202, 15).getChild(0); // The coin option for REGIFT

        // If we can see the coins (which should always be the case when opening the REGIFT), Select them
        if (REGIFT_COINS.isOnScreen()){
            REGIFT_COINS.interact("Choose");
            Log.info("Choosing coins...");
            Task.sleep(Random.nextInt(328, 551));
        }

        WidgetChild CONFIRM_BUTTON  = Widgets.get(202, 26).getChild(0); // Confirm button for REGIFT

        // Confirm the selection of coins.
        for (int i = 0; (CONFIRM_BUTTON.isOnScreen()) && (i < 3); i++){
            CONFIRM_BUTTON.click(true);
            Log.info("Confirming coins...");
            Task.sleep(Random.nextInt(728, 351));
        }

        // Stop the script if something went wrong.
        if (REGIFT_COINS.isOnScreen()) {
            Log.error("ERROR while handling the Random Event Gift. Quitting.");
            Methods.stopSuperHeater();
        }

        return;
    }
}
