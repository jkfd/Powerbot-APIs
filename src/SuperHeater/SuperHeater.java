package SuperHeater;

import SuperHeater.Actions.AntiBan;
import SuperHeater.Actions.Banker;
import SuperHeater.Actions.SpellCaster;
import SuperHeater.GUI.Button;
import SuperHeater.GUI.GUI;
import SuperHeater.GUI.Painter;
import SuperHeater.GrandExchange.GE;
import SuperHeater.Misc.Consts;
import SuperHeater.Misc.Logging.Log;
import SuperHeater.Misc.Methods;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import org.powerbot.core.Bot;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.client.Client;


@Manifest(  authors = { "jkfd" },
            name = "F2PSuperHeater",
            description = "Simply the best.",
            version = 1.53,
            website = "https://www.powerbot.org/community/topic/964475-beta-f2p-superheater-free/")

public class SuperHeater extends ActiveScript implements PaintListener, MouseListener
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
        
        if (Consts.SHOW_PAINT) {
            Painter.drawBackground(g2d);
            Painter.drawOverview(g2d);
        }
        
        Painter.drawMenu(g2d);
        Painter.drawButtons(g2d);
        Painter.drawMouse(g2d);
    }


    @Override
    public void onStart() {
        GUI.init();
        
        // Default Config Settings
        Consts.CONFIG.put("barType",   "Bronze");
        Consts.CONFIG.put("retries",   "5");
        Consts.CONFIG.put("stopAction","nothing");
        Consts.CONFIG.put("abFrequency", "50");
        Consts.CONFIG.put("ABSlotInd", "0");
        Consts.CONFIG.put("sellBars", "FALSE");
        Consts.CONFIG.put("barPrice", "1000000");

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
        
        // Show the GUI
        if (Consts.SHOW_GUI) {
            GUI.createWindow();
            Consts.SHOW_GUI = false;
        }
    }
    
    @Override
    public int loop() {
        if (Consts.GO && !Consts.SHOW_GUI) {
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
                jobs.add(new SpellCaster());
                jobs.add(new Banker());
                jobs.add(new AntiBan());
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
        for (String k : GUI.buttons.keySet()) {
            
            Button but = GUI.buttons.get(k);
            
            if (but.collides(e)) {
                switch (but.getClickAction()) {
                    case 0:
                        // Switch hide button for show button when GUI no longer visible
                        Consts.SHOW_PAINT = false;
                        but.setEnabled(false);
                        but.setVisible(false);
                        GUI.buttons.get("show").setEnabled(true);
                        GUI.buttons.get("show").setVisible(true);
                        break;
                    case 1:
                        // Switch show button for hide button when GUI is visible
                        Consts.SHOW_PAINT = true;
                        but.setEnabled(false);
                        but.setVisible(false);
                        GUI.buttons.get("hide").setEnabled(true);
                        GUI.buttons.get("hide").setVisible(true);
                        break;
                    case 2:
                        // Make a display a popup with instructions and console output
                        GUI.showTextPopup(
                                "Paste this into your post when reporting a bug", 
                                Log.dumpLog());
                        break;
                    default:
                        Log.error("Some error occured getting the clickAction of button");
                        break;
                }
                
                return;
            }
        }
    }

    public void mousePressed(final MouseEvent e) {
        Painter.setMousePressed(true);
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
}