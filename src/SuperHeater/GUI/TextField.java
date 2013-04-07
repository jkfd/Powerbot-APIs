/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SuperHeater.GUI;

import SuperHeater.Misc.Logging.Log;
import java.awt.Image;
import java.awt.event.KeyEvent;

/**
 *
 * @author Sverre
 */
public class TextField extends Component
{
    
    private String value;
    private String name;
    private String ammendment;
    private boolean numeric;
    
    public TextField(Image pic, String name, String value, String ammendment, boolean numeric, 
            int height, int width, int x, int y, int clickAction, boolean visible, boolean selected){
        
        this.clickAction = clickAction;
        this.icon = pic;
        this.name = name;
        this.value = value;
        this.ammendment = ammendment;
        this.numeric = numeric;
        this.height = height;
        this.width = width;
        this.posX = x;
        this.posY = y;
        this.visible = visible;
        this.selected = selected;
    }
    
    public TextField(String url, String name, String value, String ammendment, boolean numeric, 
            int height, int width, int x, int y, int clickAction, boolean visible, boolean selected){
        
        this.clickAction = clickAction;
        this.name = name;
        this.value = value;
        this.ammendment = ammendment;
        this.numeric = numeric;
        this.height = height;
        this.width = width;
        this.posX = x;
        this.posY = y;
        this.visible = visible;
        this.selected = selected;
        
        try {
            icon = fetchIcon(url);
        } catch(Exception ex) {
            Log.error("Could not fetch Button Icon from URL: " + url);
            Log.error(ex);
            icon = null;
        }
    }
    
    public boolean getNumeric(){
        return this.numeric;
    }
    
    public String getAmmendment(){
        return this.ammendment;
    }
    
    /**
     * The name of the TextField should be a unique identifier used to access
     * the object
     * @return String name.
     */
    @Override
    public String getName(){
        return this.name;
    }
    
    /**
     * Gets the value of the text field, which is the string currently
     * entered into the text field.
     * @return String value contained in the text field
     */
    public String getValue(){
        return this.value;
    }
    
    /**
     * Allows setting of the value associated with the text field.
     * This is the text that is currently displayed in the field.
     * @param value 
     */
    public void setValue(String value){
        this.value = value;
    }
    
    public void enterKey(KeyEvent e){
        // If the field is not currently selected, return out.
        if (!this.isSelected()) {
            return;
        }
            
        // If backspace is pressed, remove the last character from the current string.
        // Unless the string length is zero.
        if (e.getKeyChar() == 8) {      // KeyChar 8 == backspace

            if (this.getValue().length() == 0) {
                return;
            }

            this.setValue(this.getValue().substring(0, (this.getValue().length()-1)));
            return;
        }
            
        // If the current value is longer than or equal to 6, return. 
        if ((this.getValue().length() >= 6)) {
            return;
        }

        // If the field is set as numeric, and the key is not a digit, return.
        if (this.getNumeric() == true && !String.valueOf(e.getKeyChar()).matches("[0-9]")) {
            Log.error("This text field only takes numeric characeters");
            return;
        }

        // Otherwise, we set the value to the current value, plus our new key.
        this.setValue(this.getValue().concat(String.valueOf(e.getKeyChar())));
    }
    
}
