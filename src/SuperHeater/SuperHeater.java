package SuperHeater;

import SuperHeater.Actions.AntiBan;
import SuperHeater.Actions.Banker;
import SuperHeater.Actions.SpellCaster;
import SuperHeater.GUI.Component;
import SuperHeater.GUI.GUI;
import SuperHeater.GUI.Painter;
import SuperHeater.GUI.TextField;
import SuperHeater.GrandExchange.GE;
import SuperHeater.Misc.Globals;
import SuperHeater.Misc.Logging.Log;
import SuperHeater.Misc.Methods;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.swing.JFrame;
import org.powerbot.core.Bot;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.core.script.util.Random;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.util.net.GeItem;
import org.powerbot.game.client.Client;


@Manifest(  authors = { "jkfd" },
            name = "F2PSuperHeater",
            description = "SET INPUT TO 'BLOCK' BEFORE TYPING IN PAINT \nHandles everything for you. "
                        + "Just wield a firestaff and have your nats!",
            version = 1.61,
            topic   = 11761326,
            website = "https://www.powerbot.org/community/topic/964475-beta-f2p-superheater-free/")

public class SuperHeater extends ActiveScript implements PaintListener, MouseListener, KeyListener
{
    	public static Tree jobContainer = null;
        public static ArrayList<Node> jobs = new ArrayList<Node>();
        static Client client;

    /**
     * Draws the "paint" of the script and updates the time
     * @param g
     */
    @Override
    public void onRepaint(Graphics g) {
        // Update Time
        Globals.RUNTIME = System.currentTimeMillis() - Globals.START_TIME;
        // Set up advanced graphics 
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(Font.decode("Arial"));
        
        // These are objects drawn only when the paint should be shown
        if (Globals.SHOW_PAINT) {
            Painter.drawBackground(g2d);
            Painter.drawTabs(g2d);
            Painter.drawButtons(g2d, GUI.tabButtons);
        }
        
        // These are always drawn
        Painter.drawMenu(g2d);
        Painter.drawButtons(g2d, GUI.stopGoButtons);
        Painter.drawButtons(g2d, GUI.menuButtons);
        Painter.drawMouse(g2d);
    }


    @Override
    public void onStart() {
        
        for(Frame f : JFrame.getFrames()) {
            if (f.getTitle().contains("RSBot")) {
                Globals.RSBOT_FRAME = f;
            }
        }
        
        // Default Config Settings
        Globals.CONFIG.put("barType",            "BRONZE");
        Globals.CONFIG.put("useCB",              "FALSE");
        Globals.CONFIG.put("retries",            "5");
        Globals.CONFIG.put("stopAction",         "NONE");
        Globals.CONFIG.put("barTargetEnabled",   "FALSE");
        Globals.CONFIG.put("barTarget",          "0");
        Globals.CONFIG.put("abFrequency",        "50");
        Globals.CONFIG.put("ABSlotInd",          "0");
        Globals.CONFIG.put("sellBars",           "FALSE");
        Globals.CONFIG.put("barPrice",           "100000");
        
        GE.SLOTS.put(1, new Integer[] {31, 32});
        GE.SLOTS.put(2, new Integer[] {47, 48});

        // Are we in the bank? Do we need to bank?
        Globals.BANK_NOW = (Bank.isOpen() || Methods.checkNeedBank());

        
        GUI.init();
    }
    
    @Override
    public int loop() {
        if (Globals.GO) {
            if (Game.getClientState() != Game.INDEX_MAP_LOADED) {
                return 2500;
            }

            if (client != Bot.client()) {
                WidgetCache.purge();
                Bot.instance().getEventManager().addListener(this);
                client = Bot.client();
            }

            if (jobContainer != null) {
                final Node job = jobContainer.state();

                if (job != null) {
                    jobContainer.set(job);
                    getContainer().submit(job);
                    job.join();
                }

            } else {
                jobs.add(new AntiBan());
                jobs.add(new SpellCaster());
                jobs.add(new Banker());                
                jobContainer = new Tree(jobs.toArray(new Node[jobs.size()]));
            }
        }
        
        return Random.nextInt(300, 600);    // 1/2 to 1 "click"
    }
    
    @Override
    public void onStop(){
        Methods.stopSuperHeater();
        return;
    }

    public void mouseClicked(final MouseEvent e) {
       return;
    }

    public void mousePressed(final MouseEvent e) {
        
        /*
         * Loop through each component drawn on the GUI to see if we have clicked it.
         * If we have, execute the specified clickAction.
         */
        for (Iterator<String> it = GUI.components.keySet().iterator(); it.hasNext();) {
            String k = it.next();
            Component component = GUI.components.get(k);
            
            if (component.collides(e)) {
                switch (component.getClickAction()) {
                    case 0:
                        // This is the Do Nothing click action for components that have no specified click
                        // action
                        break;
                    case 1:
                        // Switch hide button for show button when GUI no longer visible
                        Globals.SHOW_PAINT = false;
                        component.setVisible(false);
                        GUI.components.get("show").setVisible(true);
                        GUI.disableButtons(GUI.tabButtons);
                        GUI.disableButtons(GUI.CBButtons);
                        GUI.disableButtons(GUI.barButtons);
                        GUI.disableButtons(GUI.targetButtons);
                        GUI.disableButtons(GUI.logoutButtons);
                        GUI.disableButtons(GUI.GEButtons);
                        break;
                    case 2:
                        // Switch show button for hide button when GUI is visible
                        Globals.SHOW_PAINT = true;
                        component.setVisible(false);
                        GUI.components.get("hide").setVisible(true);
                        GUI.enableButtons(GUI.tabButtons);
                        GUI.enableButtons(GUI.CBButtons);
                        GUI.enableButtons(GUI.barButtons);
                        GUI.enableButtons(GUI.targetButtons);
                        GUI.enableButtons(GUI.logoutButtons);
                        GUI.enableButtons(GUI.GEButtons);
                        break;
                    case 3:
                        // Make a display a popup with instructions and console output
                        GUI.showTextPopup(
                                "Paste this into your post when reporting a bug", 
                                Log.dumpLog(new Date(System.currentTimeMillis()- 1*60*60*1000)));
                        break;
                    case 4:
                        // Change the tab to the overview tab.
                        if(!GUI.tabs.get("Overview").switchTo()) {
                            Log.error("Could not switch to Overview tab");
                        }
                        break;
                    case 5:
                        // Change the tab to the Settings tab
                        if(!GUI.tabs.get("Settings").switchTo()) {
                            Log.error("Could not switch to Settings tab");
                        }
                        break;
                    case 6:
                        // Select the bar 
                        for (String s : GUI.barButtons.keySet()) {
                            if (GUI.barButtons.get(s).isSelected()) {
                                GUI.barButtons.get(s).setSelected(false);
                            }
                        }
                        component.setSelected(true);
                        Globals.CONFIG.put("barType", k.toUpperCase());
                        
                        Log.info("barType" + k.toUpperCase());
                        
                        // Shortcut relevant hashMap entries
                        Globals.ACTIVE_ORE      = Globals.BARREQ.valueOf(Globals.CONFIG.get("barType"));
                        Globals.PRIMARY_ORE     = Globals.ACTIVE_ORE.getPrimaryID();
                        Globals.SECONDARY_ORE   = Globals.ACTIVE_ORE.getSecondaryID();
                        Globals.BARID           = Globals.ACTIVE_ORE.getBarID();
                        
                        Log.info("ACT:" + Globals.ACTIVE_ORE.toString() + " - " + Globals.BARREQ.valueOf(Globals.CONFIG.get("barType")).toString());
                        Log.info("PRI:" + Globals.PRIMARY_ORE + " - " + Globals.ACTIVE_ORE.getPrimaryID());
                        Log.info("SEC:" + Globals.SECONDARY_ORE + " - " + Globals.ACTIVE_ORE.getSecondaryID());
                        Log.info("BAR:" + Globals.BARID + " - " + Globals.ACTIVE_ORE.getBarID());
                        break;
                    case 7:
                        // Set Config to use coal bag
                        Globals.CONFIG.put("useCB", "TRUE");
                        component.setSelected(true);
                        GUI.CBButtons.get("CBNo").setSelected(false);
                        break;
                    case 8:
                        // Set Config to not use coal bag
                        Globals.CONFIG.put("useCB", "FALSE");
                        component.setSelected(true);
                        GUI.CBButtons.get("CBYes").setSelected(false);
                        break;
                    case 9:
                        // Set Config to use target
                        Globals.CONFIG.put("barTargetEnabled", "TRUE");
                        component.setSelected(true);
                        GUI.targetButtons.get("targetNo").setSelected(false);
                        break;
                    case 10:
                        // Set Config to not use target
                        Globals.CONFIG.put("barTargetEnabled", "FALSE");
                        component.setSelected(true);
                        GUI.targetButtons.get("targetYes").setSelected(false);
                        break;
                    case 11:
                        // Start the script
                        component.setVisible(false);
                        GUI.stopGoButtons.get("Stop").setVisible(true);
                        Globals.GO = true;
                        
                        if (Globals.FIRST_START) {
                            Globals.START_TIME = System.currentTimeMillis();
                            Globals.FIRST_START = false;
                        }
                        break;
                    case 12:
                        // Pause Script
                        component.setVisible(false);
                        GUI.stopGoButtons.get("Start").setVisible(true);
                        Globals.GO = false;
                        break;
                    case 13:
                        // Set to logout on exit
                        Globals.CONFIG.put("stopAction", "LOGOUT");
                        component.setSelected(true);
                        GUI.logoutButtons.get("logoutNo").setSelected(false);
                        break;
                    case 14:
                        // Set to NOT logout on exit
                        Globals.CONFIG.put("stopAction", "NONE");
                        component.setSelected(true);
                        GUI.logoutButtons.get("logoutYes").setSelected(false);
                        break;
                    case 15:
                        // Set to sell on exit
                        Globals.CONFIG.put("sellBars", "TRUE");
                        component.setSelected(true);
                        GUI.GEButtons.get("GENo").setSelected(false);
                        break;
                    case 16:
                        // Set to sell on exit
                        Globals.CONFIG.put("sellBars", "FALSE");
                        component.setSelected(true);
                        GUI.GEButtons.get("GEYes").setSelected(false);
                        break;
                    case 17:
                        // Fetch current bar price from GE and set it in the GE Price field
                        int price = GeItem.lookup(Globals.BARID).getPrice();
                        GUI.textFields.get("PriceInput").setValue(String.valueOf(price));
                        break;
                    default:
                        Log.error("Some error occured getting the clickAction of button");
                        break;
                }
                
                return;
            }
        }
        
        // For the text fields, we have our own loop
        for (Iterator<String> it = GUI.textFields.keySet().iterator(); it.hasNext();) {
            String k = it.next();
            TextField tf = GUI.textFields.get(k);
            
            // Select the text field if we click inside it.
            // If we click anywhere else, deselect it.
            if (tf.collides(e)) {
                tf.setSelected(true);
            } else {
                tf.setSelected(false);
            }
        }
    }

    public void mouseReleased(final MouseEvent e) {
        return;
    }

    public void mouseEntered(final MouseEvent e) {
        return;
    }

    public void mouseExited(final MouseEvent e) {
        return;
    }

    public void keyTyped(KeyEvent e) {
        // Loop through each text field
        for (Iterator<String> it = GUI.textFields.keySet().iterator(); it.hasNext();) {
            String k = it.next();
            TextField tf = GUI.textFields.get(k); 
            
            // Enter key to TextField
            tf.enterKey(e);
            
            // Then  set the corresponding CONFIG value.
            if ("TargetInput".equals(tf.getName())) {
                Globals.CONFIG.put("barTarget", tf.getValue());
                Globals.BAR_TARGET = Integer.decode(tf.getValue()).intValue();
            } else if ("PriceInput".equals(tf.getName())) {
                Globals.CONFIG.put("barPrice", tf.getValue());
                Globals.SELLING_PRICE = Integer.decode(tf.getValue()).intValue();
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        return;
    }

    public void keyReleased(KeyEvent e) {
        return;
    }
}