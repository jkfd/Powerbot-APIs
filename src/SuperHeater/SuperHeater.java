package SuperHeater;

import SuperHeater.Actions.AntiBan;
import SuperHeater.Actions.Banker;
import SuperHeater.Actions.SpellCaster;
import SuperHeater.GUI.Component;
import SuperHeater.GUI.GUI;
import SuperHeater.GUI.Painter;
import SuperHeater.GUI.TextField;
import SuperHeater.GrandExchange.GE;
import SuperHeater.Misc.Consts;
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
import java.util.Iterator;
import javax.swing.JFrame;
import org.powerbot.core.Bot;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.util.net.GeItem;
import org.powerbot.game.client.Client;


@Manifest(  authors = { "jkfd" },
            name = "F2PSuperHeater",
            description = "Simply the best.",
            version = 1.61,
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
        Consts.RUNTIME = System.currentTimeMillis() - Consts.START_TIME;
        // Set up advanced graphics 
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(Font.decode("Arial"));
        
        // These are objects drawn only when the paint should be shown
        if (Consts.SHOW_PAINT) {
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
                Consts.RSBOT_FRAME = f;
            }
        }
        
        // Default Config Settings
        Consts.CONFIG.put("barType",            "Bronze");
        Consts.CONFIG.put("useCB",              "FALSE");
        Consts.CONFIG.put("retries",            "5");
        Consts.CONFIG.put("stopAction",         "nothing");
        Consts.CONFIG.put("barTargetEnabled",   "FALSE");
        Consts.CONFIG.put("barTarget",          "0");
        Consts.CONFIG.put("abFrequency",        "50");
        Consts.CONFIG.put("ABSlotInd",          "0");
        Consts.CONFIG.put("sellBars",           "FALSE");
        Consts.CONFIG.put("barPrice",           "100000");

        // Define HashMaps
        Consts.BARREQ.put("Bronze",    new Integer[][] {{436,1}, {438,1},  {2349, 2350}});
        Consts.BARREQ.put("Iron",      new Integer[][] {{440,1}, {0,0},    {2351, 2352}});
        Consts.BARREQ.put("Silver",    new Integer[][] {{442,1}, {0,0},    {2355, 2356}});
        Consts.BARREQ.put("Gold",      new Integer[][] {{444,1}, {0,0},    {2357, 2358}});
        Consts.BARREQ.put("Steel",     new Integer[][] {{440,1}, {453,2},  {2353, 2354}});
        Consts.BARREQ.put("Mithril",   new Integer[][] {{447,1}, {453,4},  {2359, 2360}});
        Consts.BARREQ.put("Adamantite",new Integer[][] {{449,1}, {453,6},  {2361, 2362}});
        Consts.BARREQ.put("Runite",    new Integer[][] {{451,1}, {453,8},  {2363, 2364}});

        Consts.BARXP.put("Bronze",     new Double[] {53.0, 6.20});
        Consts.BARXP.put("Iron",       new Double[] {53.0, 12.50});
        Consts.BARXP.put("Silver",     new Double[] {53.0, 13.67});
        Consts.BARXP.put("Gold",       new Double[] {53.0, 22.50});
        Consts.BARXP.put("Steel",      new Double[] {53.0, 17.50});
        Consts.BARXP.put("Mithril",    new Double[] {53.0, 30.00});
        Consts.BARXP.put("Adamantite", new Double[] {53.0, 37.50});
        Consts.BARXP.put("Runite",     new Double[] {53.0, 50.00});
        
        GE.SLOTS.put(1, new Integer[] {31, 32});
        GE.SLOTS.put(2, new Integer[] {47, 48});

        // Are we in the bank? Do we need to bank?
        Consts.BANK_NOW = (Bank.isOpen() || Methods.checkNeedBank());

        
        GUI.init();
    }
    
    @Override
    public int loop() {
        if (Consts.GO) {
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
        return 500;
    }
    
    @Override
    public void onStop(){
        Methods.stopSuperHeater();
    }

    public void mouseClicked(final MouseEvent e) {
       throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mousePressed(final MouseEvent e) {
        Painter.setMousePressed(true);
        
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
                        Consts.SHOW_PAINT = false;
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
                        Consts.SHOW_PAINT = true;
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
                                Log.dumpLog());
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
                        Consts.CONFIG.put("barType", k);
                        
                        // Shortcut relevant hashMap entries
                        Consts.PRIMARY_ORE     = Consts.BARREQ.get(Consts.CONFIG.get("barType"))[0][0];
                        Consts.SECONDARY_ORE   = Consts.BARREQ.get(Consts.CONFIG.get("barType"))[1][0];
                        Consts.BARID           = Consts.BARREQ.get(Consts.CONFIG.get("barType"))[2][0];
                        Consts.ACTIVE_ORE      = Consts.BARREQ.get(Consts.CONFIG.get("barType"));
                        break;
                    case 7:
                        // Set Config to use coal bag
                        Consts.CONFIG.put("useCB", "TRUE");
                        component.setSelected(true);
                        GUI.CBButtons.get("CBNo").setSelected(false);
                        break;
                    case 8:
                        // Set Config to not use coal bag
                        Consts.CONFIG.put("useCB", "FALSE");
                        component.setSelected(true);
                        GUI.CBButtons.get("CBYes").setSelected(false);
                        break;
                    case 9:
                        // Set Config to use target
                        Consts.CONFIG.put("barTargetEnabled", "TRUE");
                        component.setSelected(true);
                        GUI.targetButtons.get("targetNo").setSelected(false);
                        break;
                    case 10:
                        // Set Config to not use target
                        Consts.CONFIG.put("barTargetEnabled", "FALSE");
                        component.setSelected(true);
                        GUI.targetButtons.get("targetYes").setSelected(false);
                        break;
                    case 11:
                        // Start the script
                        component.setVisible(false);
                        GUI.stopGoButtons.get("Stop").setVisible(true);
                        Consts.GO = true;
                        
                        if (Consts.FIRST_START) {
                            Consts.START_TIME = System.currentTimeMillis();
                            Consts.FIRST_START = false;
                        }
                        break;
                    case 12:
                        // Pause Script
                        component.setVisible(false);
                        GUI.stopGoButtons.get("Start").setVisible(true);
                        Consts.GO = false;
                        break;
                    case 13:
                        // Set to logout on exit
                        Consts.CONFIG.put("stopAction", "logout");
                        component.setSelected(true);
                        GUI.logoutButtons.get("logoutNo").setSelected(false);
                        break;
                    case 14:
                        // Set to NOT logout on exit
                        Consts.CONFIG.put("stopAction", "none");
                        component.setSelected(true);
                        GUI.logoutButtons.get("logoutYes").setSelected(false);
                        break;
                    case 15:
                        // Set to sell on exit
                        Consts.CONFIG.put("sellBars", "TRUE");
                        component.setSelected(true);
                        GUI.GEButtons.get("GENo").setSelected(false);
                        break;
                    case 16:
                        // Set to sell on exit
                        Consts.CONFIG.put("sellBars", "FALSE");
                        component.setSelected(true);
                        GUI.GEButtons.get("GEYes").setSelected(false);
                        break;
                    case 17:
                        // Fetch current bar price from GE and set it in the GE Price field
                        int price = GeItem.lookup(Consts.BARID).getPrice();
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
        Painter.setMousePressed(false);
    }

    public void mouseEntered(final MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(final MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
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
                Consts.CONFIG.put("barTarget", tf.getValue());
                Consts.BAR_TARGET = Integer.decode(tf.getValue()).intValue();
            } else if ("PriceInput".equals(tf.getName())) {
                Consts.CONFIG.put("barPrice", tf.getValue());
                Consts.SELLING_PRICE = Integer.decode(tf.getValue()).intValue();
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}