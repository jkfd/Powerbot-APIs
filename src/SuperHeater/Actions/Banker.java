package SuperHeater.Actions;

import SuperHeater.Misc.Consts;
import SuperHeater.Misc.Log;
import SuperHeater.Misc.Methods;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.widget.WidgetChild;


public class Banker extends Node{
    private final WidgetChild PINPAD = Widgets.get(759, 0);
    
    @Override
    public boolean activate() {
        return (Consts.BANK_NOW && Consts.GO);
    }
    
    @Override
    public void execute() {
        // Precalculate variables
        int retries = Integer.parseInt(Consts.CONFIG.get("retries"));

        // Set Status
        Consts.CURRENT_STATUS = "Banking";

        // If we are banking, but we already have our stuff, exit.
        if(checkInvStatus()){

            if(Bank.isOpen()) {
                Bank.close();
            }   // Close only if bank is already open

            Consts.BANK_NOW = false;
            return;
        }

        // Open if not open
        for (int i = 0; !Bank.isOpen() && !PINPAD.isOnScreen() && i<retries; i++) {
            Bank.open();
            Task.sleep(Random.nextInt(20, 472));

            Methods.getREE("Open Bank", i);
        }

        // Reset status after pin
        Consts.CURRENT_STATUS = "Banking";

        // Do we have excess primary ore? If so deposit it
        while (Inventory.getCount(Consts.PRIMARY_ORE) > 0) {
            Log.info("Depositing Excess Primary Ore.");
            Bank.deposit(Consts.PRIMARY_ORE, 0);
            Task.sleep(Random.nextInt(20, 472));
        }

        // Do we have excess secondary ore? If so deposit it
        while (Inventory.getCount(Consts.SECONDARY_ORE) > 0) {
            Log.info("Depositing Excess Secondary Ore.");
            Bank.deposit(Consts.SECONDARY_ORE, 0);
            Task.sleep(Random.nextInt(17, 534));
        }

        // Do we have any unnecessary items in our Inventory? Deposit.
        depositForeignItems();

        // Check if we are out of ores. If we are, exit the script
        if(isOutOfOres()) {
            Log.severe("The script has detected that you are out of ores!");
            Consts.CURRENT_STATUS = "Out of ores! Stopping.";
            Consts.BANK_NOW = false;
            Task.sleep(500);
            Methods.stopSuperHeater();
            return;
        }
        
        // Check if we should have coal bag and if we actually have it. 
        // If not withdraw it.
        getCB();

        // Withdraw for and fill coal bag.
        withdrawForCB();
        fillCoalBag();

        // Withdraw ores
        withdrawPrimary();
        withdrawSecondary();


        // Withdraw Nats
        for (int i = 0; Inventory.getCount(Consts.NATURE_RUNE) < 1 && i < retries; i++) {
            Log.info("Withdrawing Nats. TRY: " + (i+1));
            Bank.withdraw(Consts.NATURE_RUNE, 0); // withdraw (NATS,Amount)

            Methods.getREE("Withdrawing Nats", i);
        }

        // Check the result of our withdrawal. If we're good, exit
        // if not, deposit everything and try again :(
        if(checkInvStatus()){
            Bank.close();
            Consts.BANK_NOW = false;
        } else {
            depositForeignItems();
            depositMistakes();
        }
    }

    private boolean isOutOfOres(){

        Log.info("Checking if you are out of ores...");

        Consts.NO_ORES = (
                (getBankCount(Consts.PRIMARY_ORE) == 0 && Inventory.getCount(Consts.PRIMARY_ORE) == 0) ||
                (getBankCount(Consts.SECONDARY_ORE) == 0 && Inventory.getCount(Consts.SECONDARY_ORE) == 0 && Methods.getSWA(false) != 0)||
                (Inventory.getCount(Consts.NATURE_RUNE) == 0)
                );

        return(Consts.NO_ORES);
    }

    private int getBankCount(int id){

        Log.info("Checking how many ores are left in bank...");
        // ID = 0 shows that we don't need the ore.
        if (id == 0){
            Log.info("ID was 0. Returning 0");
            return 0;
        }

        // Force Bank Open
        while(!Bank.isOpen() && !PINPAD.isOnScreen()) {
            Log.info("Bank not open. Forcing open");
            Task.sleep(Random.nextInt(235, 823));
            Bank.open();
        }

        if(Bank.getItem(id) != null){
            Item bankItem = Bank.getItem(id);

            if(bankItem.getWidgetChild() != null){
                WidgetChild bankItemWC = bankItem.getWidgetChild();

                int amount =  bankItemWC.getChildStackSize();
                return amount;
            }
            return 0;
        }
        return 0;
    }

    private boolean checkInvStatus(){

        Log.info("Checking if we have the right inventory");

        if(getBankCount(Consts.PRIMARY_ORE) < Methods.getPWA(false) 
        || getBankCount(Consts.SECONDARY_ORE) < Methods.getSWA(false)){

            if(Inventory.getCount(Consts.PRIMARY_ORE) <= 0) {
                Log.info("Primary Ore Inventory amount can't be 0");
                Log.severe("Inventory status NOT approved");
                return false;
            }

            if(Inventory.getCount(Consts.PRIMARY_ORE) > Methods.getPWA(false)) {
                Log.info("Primary Ore Inventory amount ("
                        + Inventory.getCount(Consts.PRIMARY_ORE)
                        +") is not equal or less than target amount ("+Methods.getPWA(false)+")");
                Log.severe("Inventory status NOT approved");
                return false;
            }

            if(Inventory.getCount(Consts.SECONDARY_ORE) < Methods.getSWA(false)) {
                Log.info("Secondary Ore Inventory amount ("
                        + Inventory.getCount(Consts.SECONDARY_ORE)
                        +") is less than target amount ("+Methods.getSWA(false)+")");
                Log.severe("Inventory status NOT approved");
                return false;
            }

            Log.info("Inventory Status Approved");
            return true;
        } else {

            if(Inventory.getCount(Consts.PRIMARY_ORE) != Methods.getPWA(false)) {
                Log.info("Primary Ore Inventory amount ("
                        + Inventory.getCount(Consts.PRIMARY_ORE)
                        +") is not equal to target amount ("+Methods.getPWA(false)+")");
                Log.severe("Inventory status NOT approved");
                return false;
            }

            if(Inventory.getCount(Consts.SECONDARY_ORE) < Methods.getSWA(false)) {
                Log.info("Secondary Ore Inventory amount ("
                        + Inventory.getCount(Consts.SECONDARY_ORE)
                        +") is less than target amount ("+Methods.getSWA(false)+")");
                Log.severe("Inventory status NOT approved");
                return false;
            }

            if(Inventory.getCount(Consts.NATURE_RUNE) == 0) {
                Log.info("Out of nature runes");
                Log.severe("Inventory status NOT approved");
                return false;
            }

            Log.info("Inventory Status Approved");
            return true;
        }

    } 

    private void depositMistakes(){
        if(Bank.isOpen()) {
            Log.info("Recovering from mistaken withdrawal. Depositing Everything.");
            for(WidgetChild child : Inventory.getWidget(true).getChildren()) {

                if(
                        (child.getChildId() != Consts.NATURE_RUNE) && 
                        (child.getChildId() != Consts.COAL_BAG) && 
                        (child.getChildId() != -1)
                   ) {
                    Log.info("Depositing: " + child.getText());
                    Bank.deposit(child.getChildId(), 0);
                    Task.sleep(Random.nextInt(239, 782));
                }
            }
        } else {
            Log.severe("Tried to deposit mistakes from inventory, but bank was not open.");
            Methods.stopSuperHeater();
        }
    }

    private void depositForeignItems(){
        Log.info("Depositing all items NOT needed for making "
                + Consts.CONFIG.get("barType") + " bars");

        // Get all items in inventory that are NOT part of making the bar
        // (Ores prim and sec + nats)
        Item[] items = Inventory.getItems(new Filter<Item>() {
           public boolean accept(final Item item) {
                  return (
                          (item.getId() != Consts.ACTIVE_ORE[1][0]) && // Sec
                          (item.getId() != Consts.ACTIVE_ORE[0][0]) && // Pri
                          (item.getId() != Consts.NATURE_RUNE) &&
                          (item.getId() != Consts.COAL_BAG));   
           }
        });

        // Deposit these items while they are on the screen.
        for (Item it : items) {
            while (Inventory.getCount(it.getId()) > 0) {
                Log.info("Depositing " + it.getName());
                Bank.deposit(it.getId(), 0);
                Task.sleep(Random.nextInt(83, 262));
            }
        }

    }

    private void withdrawPrimary(){
        int timeout = 0;

        if(Inventory.getCount(Consts.PRIMARY_ORE) == Methods.getPWA(false)){
            return;
        }

        if(getBankCount(Consts.PRIMARY_ORE) == 0){
            return;
        }

        while ((Inventory.getCount(Consts.PRIMARY_ORE) < Methods.getPWA(false)) && (timeout < 3)){
            Log.info("Withdrawing Primary Ores");

            // Exit the loop if we can pass the inv. check
            if(checkInvStatus()) {
                return;
            }

            Bank.withdraw(Consts.PRIMARY_ORE, Methods.getPWA(true));
            timeout++;
            Task.sleep(Random.nextInt(432, 886));
        }

    }

    private void withdrawSecondary(){
        int timeout = 0;

        if (Methods.getSWA(false) == 0) {
            return;
        }

        if(Inventory.getCount(Consts.SECONDARY_ORE) == Methods.getSWA(false)) {
            return;
        }

        if(getBankCount(Consts.SECONDARY_ORE) == 0) {
            return;
        }

        while ((Inventory.getCount(Consts.SECONDARY_ORE) < Methods.getSWA(false)) && (timeout < 3)){
            Log.info("Withdrawing Secondary Ores");

            // Exit the loop if we can pass the inv. check
            if(checkInvStatus()) {
                return;
            }

            Bank.withdraw(Consts.SECONDARY_ORE, Methods.getSWA(true));
            timeout++;
            Task.sleep(Random.nextInt(432, 886));
        }

        if(Inventory.getCount(Consts.SECONDARY_ORE) != Methods.getSWA(false)) {
            depositMistakes();
        }

    }

    private void fillCoalBag(){
        if ((Inventory.getCount(Consts.COAL_BAG) > 0) && Consts.CONFIG.get("useCB").equals("TRUE")) {

            for (int i = 0; (Inventory.getCount(Consts.SECONDARY_ORE) > 0) && i < 3; i++) {
                Inventory.getItem(Consts.COAL_BAG).getWidgetChild().interact("Fill");
                Task.sleep(Random.nextInt(107, 728));
            }

            // Added to fix issue where script does not recognize the coal bag being full. 
            // Script will now deposit any excess coal that has not entered the bag after the 3 
            // tries in the above for-loop and continue.
            if (Inventory.getCount(Consts.SECONDARY_ORE) > 0) {
                Log.severe("SUSPECTING that coal bag is full. "
                        + "Depositing remaining coal through the depositMistakes() method.");
                depositMistakes();
            }

            Log.info("Filled Coal Bag");
        } else {
            Log.info("Did not fill coal bag.");
        }
    }   

    private void withdrawForCB(){
        if (!Methods.useCB()) {
            Log.info("Not using CB. Cannot withdraw for it.");
            return;
        }

        if (Inventory.getCount(453) >= 26) {
            Log.info("Filling Coal bag. Not withdrawing.");
            fillCoalBag();
            return;
        }

        // Actually withdraw the coal...
        for (int i=0; (Inventory.getCount(453) < 26) && i < 3; i++) {
            Bank.withdraw(453, 0);
            Task.sleep(Random.nextInt(97, 424));
        }
    }
    
   private void getCB(){
       // Do not get if we're not supposed to be using it.
       if (!Consts.CONFIG.get("useCB").equals("TRUE") || Consts.SECONDARY_ORE != 453) {
           return;
       }
       
       // Do not get if we already have.
       if (Inventory.getCount(Consts.COAL_BAG) > 0) {
           return;
       }
       
       // Actually withdraw coal bag.
       for (int i=0; (Inventory.getCount(18339) < 1) && i < 3; i++) {
            Bank.withdraw(18339, 0);
            Task.sleep(Random.nextInt(97, 424));
        }
   }
}
