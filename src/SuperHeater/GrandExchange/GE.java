package SuperHeater.GrandExchange;

import SuperHeater.Misc.Logging.Log;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class GE {
    
    // Public constants
    public final static int ACTION_BUY                = 0;
    public final static int ACTION_SELL               = 1;
    public final static Map<Integer,Integer[]> SLOTS  = new HashMap<Integer,Integer[]>(); // <(1,2) , {buy,sell}>
    
    // Private Constants
    private final static Widget WC                     = new Widget(105);      // WC = WIDGET CONTAINER
    private final static WidgetChild WC_MAIN           = new Widget(105).getChild(1);
    private final static WidgetChild WC_CLOSE          = new Widget(105).getChild(14);
    private final static WidgetChild TITLEBAR          = new Widget(105).getChild(134);
    private final static WidgetChild WC_ITEM_CHOSEN    = new Widget(105).getChild(142);
    private final static WidgetChild WC_CONFIRM        = new Widget(105).getChild(187);
    private final static WidgetChild WC_SELL_INVENTORY = new Widget(107).getChild(18);
    
    // A list of FOUR (4) IDs that correspond to GE clerks.
    private final static Set<Integer> CLERKS           = new HashSet<Integer>(Arrays.asList(
                                                            new Integer[] {1419, 2240, 2241, 2593})
                                                        );
    
    // Filter allowing us to get only the GE clerks from all NPCs
    private final static Filter<NPC> clerkFilter = new Filter<NPC>() {
        public boolean accept(NPC n) {
            return CLERKS.contains(n.getId());
        }
    };
    
    /**
     * Iterates through the available slots to see if buy button is visible 
     * and therefore the slot is free.
     * @return 1 or 2 depending on the slot, 0 if none is free.
     */
    public static int getFreeSlot(){
        for (Map.Entry<Integer, Integer[]> entry : SLOTS.entrySet()) {
            
            if (Widgets.get(105,entry.getValue()[0]).visible()) {
                return entry.getKey().intValue();
            }
        }
        
        return 0;
    }
    
    /**
     * Boolean implementation of getFreeSlot(). 
     * @return FALSE if getFreeSlot() == 0, TRUE if higher
     */
    public static boolean hasFreeSlot(){
        return (getFreeSlot() > 0);
    }
    
    /**
     * Checks to see if the GE window is visible
     * @return TRUE if WidgetChild(105,0) is visible
     */
    public static boolean isOpen(){
        return (WC_MAIN.visible());
    }
    
    /**
     * Checks to see if a GE clerk is shown on screen.
     * @return TRUE if any clerk isOnScreen().
     */
    public static boolean isAtGe(){
        NPC clerk = NPCs.getNearest(clerkFilter);
        
        // If we see the clerk, we are at the GE for all intents and purposes.
        return clerk.isOnScreen();
    }
    
    /**
     * Attempts to open the GE window for 4 sec.
     * @return TRUE if the window is open.
     */
    public static boolean open(){
        int timer = 0;
        int clickSleep = Random.nextInt(1200, 2000);
        
        // If the GE is already open, return true.
        if (isOpen()) {
            Log.info("GE already open");
            return true;
        }
        
        // If we're not at the GE, let's not even try to open it.
        if (!isAtGe()) {
            Log.severe("You are NOT at the Grand Exchange. Could not open the GE window");
            return false;
        }
        
        NPC nearestClerk = NPCs.getNearest(clerkFilter);
         
        // Create a loop to keep clicking the GE until the window opens or 4 sec. passes. 
        while (!isOpen() && timer <= 4000) {
            nearestClerk.interact("Exchange");
            timer += clickSleep;
            Task.sleep(clickSleep);
        }
        
        return isOpen();
    }
    
    /**
     * Attempts to close the GE window for 2 sec,
     * @return TRUE if window is closed at the end
     */
    public static boolean close() {
        int timer = 0;
        int clickSleep = Random.nextInt(300, 1000);
        
        if (!isOpen()) {
            return true;
        }
        
        while (isOpen() && timer <= 2000) {
            WC_CLOSE.click(true);
            timer += clickSleep;
            Task.sleep(clickSleep);
        }
        
        return (!isOpen());
    }
    
    /**
     * Clicks either buy or sell for the first free slot
     * @param a ACTION_BUY or ACTION_SELL (0/1)
     * @return true on success, false on failure
     */
    public static boolean chooseAction(int a){
        
        // Nothing can be done until we know the GE is open
        if (!isOpen()) {
            return false;
        }
        
        int timer = 0;
        int freeSlot = getFreeSlot();
        int clickSleep = Random.nextInt(1200, 2000);
        
        // Return if we have no free slot
        if (freeSlot < 1) {
            return false;
        }
        
        // Loop to make sure that we have actually clicked the buy/sell button
        while (!TITLEBAR.visible() && timer <= 4000) {
            WC.getChild(SLOTS.get(freeSlot)[a]).click(true);
            timer += clickSleep;
            Task.sleep(clickSleep);
        }
        
        return true;
    }
    
    /**
     * Finds the WidgetChild in the inventory space that 
     * corresponds to the itemID given as an argument.
     * @param itemID The ID of the item you wish to find.
     * @return The WidgetChild with the ID of the item you're looking for. 
     */
    public static WidgetChild findItemInventory(int itemID){
        Log.info("Item we're looking for:" + itemID);
        for (WidgetChild w : WC_SELL_INVENTORY.getChildren()) {
            if (w.getChildId() == itemID) {
                return w;
            }
        }
        
        return null;
    }
    /**
     * Decodes the offer price of a current offer from widget text to a usable integer
     * @param perItem TRUE if you want the price per item, FALSE if you want total price
     * @return The price requested as an integer
     */
    public static int getOfferPrice(boolean perItem) {
        int wChild = (perItem == true) ? 153 : 185;
        String GEPrice = WC.getChild(wChild).getText().replaceAll(",", "").replaceAll(" gp", "");
        return Integer.decode(GEPrice).intValue();
    }
    
    /**
     * Sets the price on the item in the GE offer window to the specified int value.
     * @param price The price you want the item to be bought/sold at.
     * @return TRUE if at the end, the price in the window matches the price requested
     */
    public static boolean setOfferPrice(int price){
        String priceString = String.valueOf(price);
        int clickSleep = Random.nextInt(1800, 4000);
        int timer = 0;
        
        Log.info("Trying to set the offer price");
        
        // Exit if we are not in the right place
        if (!TITLEBAR.visible()) {
            Log.error("Cannot set offer price - Not in correct window.");
            return false;
        }
        
        // Exit the function if this price is already set
        if (getOfferPrice(true) == price) {
            Log.error("Correct price already set");
            return true;
        }
        
        // While the offer price is wrong, try to enter a new price.
        while (getOfferPrice(true) != price && timer <= 6000) {
            Log.info("Setting price...");
            
            // Click the "Enter x" button
            new Widget(105).getChild(177).click(true);
            Task.sleep(500,1200);
            
            Keyboard.sendText(priceString, true);
            timer += clickSleep;
            Task.sleep(clickSleep);
        }
        
        Log.info("Finished setting offer price. Price is: " + getOfferPrice(true));
        return getOfferPrice(true) == price;
    }
    
    /**
     * Places an offer in the GE to sell the item corresponding to the ID entered.
     * @param itemID The ID of the item you wish to sell.
     * @param itemName The name of the item you wish to sell
     * @return TRUE on completion. FALSE if something went wrong.
     */
    public static boolean placeSellOffer(int itemID, String itemName, int price){
        WidgetChild itemToSell = findItemInventory(itemID);
        itemName = itemName.toLowerCase();
        int clickSleep = Random.nextInt(1200, 2000);
        int timer = 0;
        
        // Choose to sell an item. Return if no available slot
        if (!TITLEBAR.visible() && !chooseAction(ACTION_SELL)) {
            Log.error("No slots available. Cannot place sell offer.");
            return false;
        }
        
        // Check that the GE window is open and that the correct action was chosen
        if (!TITLEBAR.visible() || !TITLEBAR.getText().equals("Sell Offer")) {
            Log.error("Cannot place sell offer because we have the wrong menu or the GE is not open");
            return false;
        }
        
        // Check to make sure we have our item in the inventory
        if (itemToSell == null) {
            Log.error("Could not find the specified item in your inventory.");
            return false;
        }
        
        // Loop to make sure the item is placed on offer
        while (!WC_ITEM_CHOSEN.getText().toLowerCase().equals(itemName) && timer <= 4000) {
            itemToSell.click(true); // Change this to allow user-chosen amount
            timer += clickSleep;
            Task.sleep(clickSleep);
        } 
        
        setOfferPrice(price);
        Log.info("Price has been set");
        
        if (getOfferPrice(true) == price) {
            confirmOffer();
            return true;
        }
        
        Log.error("Error placing a sell offer on the GE.");
        return false;
    }
    
    /**
     * Clicks the confirm button on the offer, 
     * finalizing the placement of the offer on the GE.
     * Certain things are not 100% reliable in this method.
     * @return TRUE if confirmButton disappears after click. FALSE otherwise.
     */
    public static boolean confirmOffer(){
        int clickSleep = Random.nextInt(1200, 2000);
        int timer = 0;
        
        Log.info("Confriming offer");
        
        // Check that the GE window is open and that the correct action was chosen
        while (TITLEBAR.visible() && 
              (TITLEBAR.getText().equals("Sell Offer") || TITLEBAR.getText().equals("Buy Offer")) &&
              (timer <= 4000)) 
        {
            WC_CONFIRM.click(true);
            timer += clickSleep;
            Task.sleep(clickSleep);
        }
        
        // THIS IS PROBABLY NOT RELIABLE. Should change.
        return !WC_CONFIRM.visible();
    }
    
    /**
     * Foolproof abstraction for selling an item to make things easier and prettier in scripts.
     * @param itemID ID of the item you wish to sell
     * @param itemName Name of item (Caps INsensitive)
     * @param price Price you wis to sell your item at
     * @return 
     */
    public static boolean sell(int itemID, String itemName, int price){
        if (!open()) {
            Log.error("Unable to open Grand Exchange");
            return false;
        }
        
        return placeSellOffer(itemID,itemName,price);
    }
}
