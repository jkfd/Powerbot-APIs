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
            g2d.fillRoundRect(0, 390, 520, 235, 5, 5);
        }
    }
    
    /**
     * Draws the overview tab showing essential information.
     * @param g 
     */
    public static void drawOverview(Graphics2D g2d){
        
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
    }
    
    public static void drawMouse(Graphics2D g2d){
        g2d.setColor(Color.RED);
        g2d.drawOval((Mouse.getX()-7), (Mouse.getY()-7), 14, 14);

        g2d.setColor(Color.GREEN);
        g2d.fillOval((Mouse.getX()-2), (Mouse.getY()-2), 4, 4);
    }
    
    /**
     * Fetches the background image from the image host.
     * The image must always be tested for null value.
     * @return Image object returned from the queried URL
     * @throws Exception If unable to fetch the image.
     */
    private static Image fetchBackgroundImage() throws Exception{
        URL url = new URL("https://i.minus.com/ipVXV3GYRqxyK.png");
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
                BGIMG = fetchBackgroundImage();
            } catch (Exception e) {
                Log.error("Could not fetch the background image. Continuing with backup");
                Log.error("Exception Caught: " + e);
            }
        }
    }
    
}
