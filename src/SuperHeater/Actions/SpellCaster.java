package SuperHeater.Actions;

import SuperHeater.Misc.Consts;
import SuperHeater.Misc.Log;
import SuperHeater.Misc.Methods;
import java.awt.event.KeyEvent;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.node.Item;

public class SpellCaster extends Node {
    
    @Override
    public boolean activate() {
        return (Consts.BANK_NOW == false && Consts.GO == true);
    }

    @Override
    public void execute() {
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
            // I don't like this solution. Check it.
            if (!Methods.waitForWidget(Widgets.get(640,70))) {
                Widgets.get(640,3).interact("expand");          // We should possibly check the visibility of
                Task.sleep(Random.nextInt(179, 230));           // Of this Wdg, but whenever the spell (70) is
            }                                                   // invisible, this MUST be visible.
            
            // Once the Action Bar is open, we tap the "1" key to select the spell.
            if (Methods.waitForWidget(Widgets.get(640,70))) {
                Keyboard.sendKey((char) KeyEvent.VK_1);
                Task.sleep(Random.nextInt(59, 398));

                // Cycle through inventory
                for (Item item : Methods.reverseItemArray(Inventory.getItems())) {

                    // If the item matches the primary ore, click it.
                    if (item.getId() == Consts.PRIMARY_ORE) {
                        item.getWidgetChild().click(true);
                        incrementBars();
                        Task.sleep(Random.nextInt(158, 282));
                        return;
                    }
                }
            } else {
                System.out.println("[ERROR] Can't see Action Bar. We are using the following widget: "
                        + Widgets.get(640,70).getText());
                System.out.println("Visible? " + Widgets.get(640,70).visible());
            }
        }
    }

    private void incrementBars() {
        Consts.BARS_MADE++;

        if(Consts.BAR_TARGET > 0 && Consts.BARS_MADE == Consts.BAR_TARGET){
            Log.severe("Target number of bars reached!");

            Methods.stopSuperHeater();
        }
    }
}
