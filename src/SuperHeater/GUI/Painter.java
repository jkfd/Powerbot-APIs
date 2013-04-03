package SuperHeater.GUI;

import SuperHeater.Misc.Consts;
import SuperHeater.Misc.Logging.Log;
import SuperHeater.Misc.Methods;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
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
    private static boolean MOUSE_PRESSED = false;
    
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
        g2d.drawString("Version: " + Consts.VERSION, 15, 425);
        g2d.drawString("Making: " + Consts.CONFIG.get("barType") + " Bars", 70, 455);
        g2d.drawString("Status: " + Consts.CURRENT_STATUS, 255, 455);
        g2d.drawString("Ran for: "+ Time.format(Consts.RUNTIME), 70, 470);
        g2d.drawString("Bars made this session: " + Consts.BARS_MADE, 70,485);
        g2d.drawString("Remaining until target: " + (Methods.getDistanceToTarget()), 255,485);
        g2d.drawString("Bars / Hour: " + Methods.getBarsPerHour(), 70,500);

        g2d.drawString("Magic XP Gained: " + (Consts.BARS_MADE*53) + " xp", 70, 515);
        g2d.drawString("Until Level "
                + (Skills.getLevel(Skills.MAGIC)+1) + ": "
                + Methods.getXpToNextLevel(Skills.MAGIC)
                + "xp ("
                + Methods.getBarsToNextLevel(Skills.MAGIC, 0)
                + " Bars)", 255, 515);

        g2d.drawString("Smithing XP Gained: "
                    + Methods.getSmithingXp()
                    + " xp", 70, 530);
        g2d.drawString("Until Level "
                + (Skills.getLevel(Skills.SMITHING)+1) + ": "
                + Methods.getXpToNextLevel(Skills.SMITHING)
                + "xp ("
                + Methods.getBarsToNextLevel(Skills.SMITHING, 1)
                + " Bars)", 255, 530);
    }
    
    /**
     * Draws 2 circles around the mouse pointer for easy tracking
     * by the user.
     * @param g2d Graphics component
     */
    public static void drawMouse(Graphics2D g2d){
        g2d.setColor(Color.RED);
        
        if (MOUSE_PRESSED) {
            g2d.fillOval((Mouse.getX()-7), (Mouse.getY()-7), 14, 14);
        } else {
            g2d.drawOval((Mouse.getX()-7), (Mouse.getY()-7), 14, 14);
        }

        g2d.setColor(Color.GREEN);
        g2d.fillOval((Mouse.getX()-2), (Mouse.getY()-2), 4, 4);
    }
    
    /**
     * Draws all buttons stored in the GUI.buttonList
     * @param g2d 
     */
    public static void drawButtons(Graphics2D g2d){
        for (String k : GUI.buttons.keySet()) {
            
            Button b = GUI.buttons.get(k);
            
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
    
    /**
     * MOUSE_PRESSED is a flag that indicates when the mouse button 
     * is depressed. Used in drawing different symbols for the mouse
     * pointer when this is the case.
     * @param pressed TRUE or FALSE
     */
    public static void setMousePressed(boolean pressed) {
        MOUSE_PRESSED = pressed;
    }
}
