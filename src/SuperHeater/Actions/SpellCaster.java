package SuperHeater.Actions;

import SuperHeater.Misc.Consts;
import SuperHeater.Misc.Logging.Log;
import SuperHeater.Misc.Methods;
import java.awt.event.KeyEvent;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.node.Item;
import sk.action.ActionBar;
import sk.action.book.magic.Spell;

public class SpellCaster extends Node {
    
    @Override
    public boolean activate() {
        return (Consts.BANK_NOW == false && Consts.GO == true);
    }

    @Override
    public void execute() {
        int ABIndex = Integer.parseInt(Consts.CONFIG.get("ABSlotInd"));
        Filter<Item> pOreFilter = new Filter<Item>() {

            public boolean accept(Item t) {
                return t.getId() == Consts.PRIMARY_ORE;
            }
        };
        
        // Set Status
        Consts.CURRENT_STATUS = "Superheating";

        // Open inventory tab if not already open
        if (Tabs.INVENTORY != Tabs.getCurrent()) {
            Tabs.INVENTORY.open();
            Task.sleep(Random.nextInt(167, 256));
        }

        // Wait for the Inventory tab to open.
        if (Methods.waitForTab(Tabs.INVENTORY)) {

            // If we need to bank, return out of the current function.
            // Also return if we are already in the bank as we shouldn't be in this loop.
            Consts.BANK_NOW = Methods.checkNeedBank();
            
            if(Consts.BANK_NOW == true || Bank.isOpen()) {
                return;
            }
            
            // Open Action Bar if not visible.
            // Uses Strikeskids API
            if (!Methods.waitForWidget(Widgets.get(640,70))) {
                ActionBar.setExpanded(true);          
                Task.sleep(Random.nextInt(179, 230));           
            }
            
            // Check if the superheat spell is set in the action bar
            // If not, set it.
            if (ActionBar.getAbilityId(ABIndex) != Spell.SUPERHEAT_ITEM.getAbilityId()) {
                ActionBar.dragToSlot(Consts.SUPERHEATABILITY,ABIndex);
                Task.sleep(Random.nextInt(147, 259));
            }
            
            // Once the Action Bar is open, we use the slot in which we stored the spell.
            if (Methods.waitForWidget(Widgets.get(640,70))) {
                
                while (Inventory.getCount(Consts.PRIMARY_ORE) > 0
                        && Inventory.getCount(Consts.NATURE_RUNE) > 0
                        && Inventory.getCount(Consts.SECONDARY_ORE) >= Consts.ACTIVE_ORE[1][1]) {
                    
                    // Tap the ActionBar slot key
                    Keyboard.sendKey((char) KeyEvent.VK_1);
                    Task.sleep(Random.nextInt(19, 79));

                    // Get array of all primary ores and click the last one
                    Item[] primaryOres = Inventory.getItems(pOreFilter);
                    primaryOres[primaryOres.length-1].getWidgetChild().click(true);
                    
                    // Sleep shortly to limit clicking
                    // Keeping in mind that Superheat takes 2 "clics" (1200 sec)
                    // We wait for 1/2 to 1 "clic".
                    Task.sleep(300, 600);
                    
                    // Increment the bar count after because the incrementer relies on
                    // counting actual bars. These will not show up before the wait is
                    // over, and MAY not show up after. In this case, we verify the 
                    // bar count when depositing. 
                    incrementBars();
                }
                
            } else {
                Log.error("Can't see Action Bar.");
            }
        }
    }

    /**
     * Increments the bar count by one and stops if target is reached.
     * @param startCount: The bar count at before superheat is cast.
     */
    private void incrementBars(){
        
        // Increase the global bar count
        Consts.BARS_MADE = Consts.DEPOSIT_COUNT + Inventory.getCount(Consts.BARID);
        
        // Check if we have reached our target number of bars. If so STOP.
        if(Methods.isTargetReached()){
            Log.severe("Target number of bars reached!");
            Methods.stopSuperHeater();
        }
    }
    
}
