package SuperHeater.GUI;

import SuperHeater.Misc.Globals;
import SuperHeater.Misc.Logging.Log;
import SuperHeater.Misc.Methods;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Map;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.Time;

/**
 *
 * @author JKFD
 */
public class Painter {
    
    private static Image BGIMG;
    private static Image MENUBG;
    
    /**
     * Gets and draws the background image if possible. 
     * If not, draws a semi-transparent rounded rectangle as background
     * @param g 
     */
    public static void drawBackground(Graphics2D g2d){
        
        // Fetches the image from URL only if needed.
        setupBgImg();
        
        // Once again checks for a null value and draws the appropriate BG.
        if (BGIMG != null) {
            g2d.drawImage(BGIMG,0,390,null);
        } else {
            g2d.setColor(Color.BLUE);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.7f));
            g2d.fillRoundRect(0, 390, 520, 165, 5, 5);
        }
    }
    
    /**
     * Draws the overview tab showing essential information.
     * @param g 
     */
    public static void drawOverview(Graphics2D g2d){
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
        g2d.setColor(new Color(228, 202, 31));
        g2d.drawString("Version: " + Globals.VERSION, 15, 425);
        g2d.drawString("Making: " + Globals.CONFIG.get("barType") + " Bars", 70, 455);
        g2d.drawString("Status: " + Globals.CURRENT_STATUS, 255, 455);
        g2d.drawString("Ran for: "+ Time.format(Globals.RUNTIME), 70, 470);
        g2d.drawString("Bars made this session: " + Globals.BARS_MADE, 70,485);
        g2d.drawString("Remaining until target: " + (Methods.getDistanceToTarget()), 255,485);
        g2d.drawString("Magic XP Gained: " + (Globals.BARS_MADE*53) + " xp", 70, 500);
        g2d.drawString("Until Level "
                + (Skills.getLevel(Skills.MAGIC)+1) + ": "
                + Methods.getXpToNextLevel(Skills.MAGIC)
                + "xp ("
                + Methods.getBarsToNextLevel(Skills.MAGIC)
                + " Bars)", 255, 500);

        g2d.drawString("Smithing XP Gained: "
                    + Methods.getSmithingXp()
                    + " xp", 70, 515);
        g2d.drawString("Until Level "
                + (Skills.getLevel(Skills.SMITHING)+1) + ": "
                + Methods.getXpToNextLevel(Skills.SMITHING)
                + "xp ("
                + Methods.getBarsToNextLevel(Skills.SMITHING)
                + " Bars)", 255, 515);
        
        g2d.drawString("Bars / Hour: " + Methods.getBarsPerHour(), 70,530);
    }
    
    /**
     * Draws 2 circles around the mouse pointer for easy tracking
     * by the user.
     * @param g2d Graphics component
     */
    public static void drawMouse(Graphics2D g2d){
        g2d.setColor(Color.RED);
        g2d.drawOval((Mouse.getX()-7), (Mouse.getY()-7), 14, 14);
        
        g2d.setColor(Color.GREEN);
        g2d.fillOval((Mouse.getX()-2), (Mouse.getY()-2), 4, 4);
    }
    
    /**
     * Draws all buttons in the specified HashMap
     * @param g2d Graphics component
     * @param buttons HashMap containing buttons. Key is the name of the button.
     */
    public static void drawButtons(Graphics2D g2d, Map<String, Button> buttons){
        for (String k : buttons.keySet()) {
            
            Button b = buttons.get(k);
            
            if (b.getIcon() == null) {
                g2d.setColor(Color.red);
                g2d.drawRect(b.getX(), b.getY(), b.getWidth(), b.getWidth());
                return;
            }
            
            if (b.visible()) {
                g2d.drawImage(b.getIcon(), b.getX(), b.getY(), null);
            }
        }
    }
    
    /**
     * Draws the black menu background in the top right corner
     * @param g2d graphics component
     */
    public static void drawMenu(Graphics2D g2d){
        if (MENUBG == null) {
            try{
                MENUBG = fetchImage("https://i.minus.com/iMay8jut1Bdmj.png");
            } catch (Exception e) {
                Log.error(e);
            }
        }
        
        g2d.drawImage(MENUBG, 383, 390, null);
    }
    
    /**
     * Draws all visible tabs. Tabs themselves are transparent
     * but contain a set of components and related draw functions
     * these are drawn in their respective places. The tab itself
     * is never a visible entity, just a collection of components.
     * @param g2d Graphics component
     */
    public static void drawTabs(Graphics2D g2d){
        Tab t = GUI.getCurrentTab();

        if (t.getName().equals("Overview")) {
            drawOverview(g2d);
        } else if (t.getName().equals("Settings")) {
            g2d.setColor(new Color(228, 202, 31));
            g2d.drawString("SETTINGS", 50, 425);
            g2d.drawString("Use Coal Bag?", 50, 460);
            g2d.drawString("Use Target?", 50.0f, 487.5f);
            g2d.drawString("Select the bar to make:", 50, 515);
            g2d.drawString("Logout on Exit?", 195, 460);
            g2d.drawString("Sell Bars on GE?", 365, 460);
        }

        for (Component c : t.getComponents()) {
            if (c.visible) {
                g2d.drawImage(c.getIcon(), c.getX(), c.getY(), null);
                
                if (c instanceof TextField) {
                    TextField tf = (TextField) c;
                    g2d.drawString(tf.getValue().concat(tf.getAmmendment()), tf.getX()+10, tf.getY()+20);
                }
            }
            
            if (c.isSelected()) {
                g2d.setColor(Color.YELLOW);
                g2d.drawRect(c.getX(), c.getY(), c.getWidth(), c.getHeight());
            }
        }
    }
    
    /**
     * Fetches the image from the image host.
     * The image must always be tested for null value.
     * @param sURL String representation of URL
     * @return Image object returned from the queried URL
     * @throws Exception If unable to fetch the image.
     */
    private static Image fetchImage(String sURL) throws Exception{
        URL url = new URL(sURL);
        Image img = Toolkit.getDefaultToolkit().getImage(url);
        return img;
    }
    
    /**
     * Sets the private member BGIMG that holds the background
     * image in memory. The image is only fetched from the URL
     * if the BGIMG member is null. This is done to prevent 
     * unnecessary URL queries.
     */
    private static void setupBgImg(){
        if (BGIMG == null) {
            try {
                BGIMG = fetchImage("https://i.minus.com/iniB7ejQC4jng.png");
            } catch (Exception e) {
                Log.error("Could not fetch the background image. Continuing with backup");
                Log.error("Exception Caught: " + e);
            }
        }
    } 
}
