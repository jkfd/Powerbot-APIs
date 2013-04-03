/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SuperHeater.GUI;

import SuperHeater.Misc.Logging.Log;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
 *
 * @author jkfd
 */
public class Button{

    private Image icon;
    private String name;
    private int height;
    private int width;
    private int posX;
    private int posY;
    private boolean enabled;
    private boolean selected;
    private boolean visible;
    private int clickAction;

    public Button(Image pic, String n, int h, int w, int x, int y, boolean e, int ca, boolean v){
        clickAction = ca;
        icon = pic;
        name = n;
        height = h;
        width = w;
        posX = x;
        posY = y;
        enabled = e;
        selected = false;
        visible = v;
    }
    
    public Button(String url, String n, int h, int w, int x, int y, boolean e, int ca, boolean v){
        clickAction = ca;
        name = n;
        height = h;
        width = w;
        posX = x;
        posY = y;
        enabled = e;
        selected = false;
        visible = v;
        
        try {
            icon = fetchIcon(url);
        } catch(Exception ex) {
            Log.error("Could not fetch Button Icon from URL: " + url);
            Log.error(ex);
            icon = null;
        }
    }
    
    private static Image fetchIcon(String uString) throws Exception{
        URL url = new URL(uString);
        Image img = Toolkit.getDefaultToolkit().getImage(url);
        return img;
    }
    
    /**
     * Gets a string keyword that identifies the action to be
     * completed when the button is clicked.
     * @return 
     */
    public int getClickAction() {
        return this.clickAction;
    }
    
    /**
     * Detects if any MouseEvent occurs within the bounds of the button.
     * @param e The mouse event we want to test for
     * @return TRUE if Mouse Event occurred within the button.
     */
    public boolean collides (MouseEvent e){
        return(
            (e.getX() >= this.posX) &&
            (e.getX() <= (this.posX+this.width)) &&
            (e.getY() >= this.posY) &&
            (e.getY() <= (this.posY+this.height)) &&
            (this.enabled == true)
        );
    }
    
    /**
     * The icon is the Image that is used to draw the button. 
     * @param i Image the current icon should be changed to.
     */
    public void setIcon(Image i){
        this.icon = i;
    }
    
    /**
     * Returns the image that identifies the button on screen
     * as an Image object.
     * @return 
     */
    public Image getIcon(){
        return this.icon;
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public int getHeight(){
        return this.height;
    }
    
    public int getX(){
        return this.posX;
    }
    
    public int getY(){
        return this.posY;
    }
    
    /**
     * Visible parameter determines whether the button should be drawn on screen
     * @return 
     */
    public boolean visible(){
        return this.visible;
    }
    
    public void setVisible(boolean v){
        this.visible = v;
    }
    
    /**
     * The enabled flag determined whether or not the button can be clicked. 
     * If this flag is set to FALSE, Button.collides() will not return true 
     * even if the mouse event occurs on the button.
     * @param disabled TRUE or FALSE
     */
    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }
    
    public boolean isEnabled(){
        return this.enabled;
    }
    
    /**
     * The selected flag allows a button to act as a user selection tool
     * and may allow the user to identify an option they have selected.
     * @return TRUE or FALSE
     */
    public boolean isSelected(){
        return this.selected;
    }
    
    /**
     * The selected flag allows a button to act as a user selection tool
     * and may allow the user to identify an option they have selected.
     * @param selected TRUE or FALSE
     */
    public void setSelected(boolean selected){
        this.selected = selected;
    }
}
