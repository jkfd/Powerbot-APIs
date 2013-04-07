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
 * @author Jkfd
 */
public class Component extends java.awt.Component {
    protected boolean visible;
    protected boolean selected;
    protected int clickAction;
    protected Image icon;
    protected int height;
    protected int width;
    protected int posX;
    protected int posY;
    
    /**
     * Cycles through each tab and the component list for each tab looking
     * for itself. Once found, it returns the tab where it was found. If
     * not found, returns null. Should always be tested for null.
     * @return Tab containing this component.
     */
    public Tab getParentTab(){
        for (String s : GUI.tabs.keySet()) {
            for (Component c : GUI.tabs.get(s).getComponents()) {
                if (c == this) {
                    return GUI.tabs.get(s);
                }
            }
        }
        
        Log.error("Component: " + this +" is not in a tab.\n Could not get parent tab");
        return null;
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
     * Returns the image that identifies the button on screen
     * as an Image object.
     * @return 
     */
    public Image getIcon(){
        return this.icon;
    }
    
    /**
     * Returns the image that identifies the button on screen
     * as an Image object.
     * @return 
     */
    public void setIcon(Image icon){
        this.icon = icon;
    }
    
    /**
     * Takes a String representation of a URL and fetches the
     * resource as an Image to return as an icon.
     * @param uString The String representation of the URL
     * @return Image object found at the URL
     * @throws Exception If the URL does not lead to a valid image.
     */
    protected static Image fetchIcon(String uString) throws Exception{
        URL url = new URL(uString);
        Image img = Toolkit.getDefaultToolkit().getImage(url);
        return img;
    }
    
    /**
     * Gives the width of the component as an integer.
     * This width may be used when drawing the component
     * together with the Image set as the icon.
     * @return width in integers
     */
    @Override
    public int getWidth(){
        return this.width;
    }
    
    /**
     * Gives the height of the component as an integer.
     * This height may be used when drawing the component
     * together with the Image set as the icon.
     * @return height as an integer
     */
    @Override
    public int getHeight(){
        return this.height;
    }
    
    /**
     * The position in the X-axis at which this component
     * is drawn. It is essential that this matches with 
     * where the component is actually drawn to avoid 
     * false positions being passed to the collides()
     * method
     * @return integer representation of position in the X-axis 
     */
    @Override
    public int getX(){
        return this.posX;
    }
    /**
     * The position in the Y-axis at which this component
     * is drawn. It is essential that this matches with 
     * where the component is actually drawn to avoid 
     * false positions being passed to the collides()
     * method
     * @return integer representation of position in the Y-axis 
     */
    @Override
    public int getY(){
        return this.posY;
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
            (this.visible == true)
        );
    }
    
    /**
     * Visible parameter determines whether the button should be drawn on screen
     * @return 
     */
    public boolean visible(){
        return this.visible;
    }
    
    /**
     * Visible parameter determines whether the button should be drawn on screen
     * @param v the boolean value to set the Component.visible flag to.
     */
    @Override
    public void setVisible(boolean v){
        this.visible = v;
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
