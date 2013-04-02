package SuperHeater;

import SuperHeater.Actions.AntiBan;
import SuperHeater.Actions.Banker;
import SuperHeater.Actions.SpellCaster;
import SuperHeater.GrandExchange.GE;
import SuperHeater.Misc.Consts;
import SuperHeater.Misc.Logging.Log;
import SuperHeater.Misc.Methods;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import org.powerbot.core.Bot;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.client.Client;


@Manifest(  authors = { "jkfd" },
            name = "F2PSuperHeater",
            description = "Simply the best.",
            version = 1.50,
            website = "https://www.powerbot.org/community/topic/964475-beta-f2p-superheater-free/")

public class SuperHeater extends ActiveScript implements PaintListener
{
    	public static Tree jobContainer = null;
        public static ArrayList<Node> jobs = new ArrayList<Node>();
        static Client client;

    /**
     * Draws the "paint" of the script
     * @param g
     */
    @Override
    public void onRepaint(Graphics g) {
        // Update Time
        Consts.RUNTIME = System.currentTimeMillis() - Consts.START_TIME;

        // Set up advanced Graphics
        Graphics2D g2d = (Graphics2D)g;
        
        // Draw Background
        if (Consts.BGIMG != null) {
            g2d.drawImage(Consts.BGIMG,0,390,null);
        } else {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.7f));
            g2d.fillRoundRect(0, 390, 520, 235, 5, 5);
        }

        // Draw Info
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
        g2d.setColor(new Color(228, 202, 31));
        g2d.drawString("Version: " + Consts.VERSION, 15, 415);
        g2d.drawString("Making: " + Consts.CONFIG.get("barType") + " Bars", 15, 455);
        g2d.drawString("Status: " + Consts.CURRENT_STATUS, 255, 455);
        g2d.drawString("Ran for: "+ Time.format(Consts.RUNTIME), 15, 470);
        g2d.drawString("Bars made this session: " + Consts.BARS_MADE, 15,485);
        g2d.drawString("Remaining until target: " + (Methods.getDistanceToTarget()), 255,485);
        g2d.drawString("Bars / Hour: " + Methods.getBarsPerHour(), 15,500);

        g2d.drawString("Magic XP Gained: " + (Consts.BARS_MADE*53) + " xp", 15, 515);
        g2d.drawString("Until Level "
                + (Skills.getLevel(Skills.MAGIC)+1) + ": "
                + Methods.getXpToNextLevel(Skills.MAGIC)
                + "xp ("
                + Methods.getBarsToNextLevel(Skills.MAGIC, 0)
                + " Bars)", 255, 515);

        g2d.drawString("Smithing XP Gained: "
                    + Methods.getSmithingXp()
                    + " xp", 15, 530);
        g2d.drawString("Until Level "
                + (Skills.getLevel(Skills.SMITHING)+1) + ": "
                + Methods.getXpToNextLevel(Skills.SMITHING)
                + "xp ("
                + Methods.getBarsToNextLevel(Skills.SMITHING, 1)
                + " Bars)", 255, 530);
        
        // Draw Mouse
        g2d.setColor(Color.RED);
        g2d.drawOval((Mouse.getX()-7), (Mouse.getY()-7), 14, 14);

        g2d.setColor(Color.GREEN);
        g2d.fillOval((Mouse.getX()-2), (Mouse.getY()-2), 4, 4);

        g2d.setColor(Color.BLUE);
    }

    /**
     *
     */
    @Override
    public void onStart() {
        // Default Config Settings
        Consts.CONFIG.put("barType",   "Bronze");
        Consts.CONFIG.put("retries",   "5");
        Consts.CONFIG.put("stopAction","nothing");
        Consts.CONFIG.put("abFrequency", "50");
        Consts.CONFIG.put("ABSlotInd", "0");
        Consts.CONFIG.put("sellBars", "TRUE");
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
        
        // Get Background Image from server
        try {
            Consts.BGIMG = Methods.getBackgroundImage();
        } catch (Exception e) {
            Log.error("Error getting BG from Internet: " + e);
        }

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
}