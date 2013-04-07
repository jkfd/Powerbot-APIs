/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SuperHeater.GUI;

import SuperHeater.Misc.Logging.Log;
import java.awt.Image;

/**
 *
 * @author jkfd
 */
public class Button extends Component{

    private String name;

    public Button(Image pic, String n, int h, int w, int x, int y, int ca, boolean v, boolean s){
        this.clickAction = ca;
        this.icon = pic;
        this.name = n;
        this.height = h;
        this.width = w;
        this.posX = x;
        this.posY = y;
        this.visible = v;
        this.selected = s;
    }
    
    public Button(String url, String n, int h, int w, int x, int y, int ca, boolean v, boolean s){
        this.clickAction = ca;
        this.name = n;
        this.height = h;
        this.width = w;
        this.posX = x;
        this.posY = y;
        this.visible = v;
        this.selected = s;
        
        try {
            icon = fetchIcon(url);
        } catch(Exception ex) {
            Log.error("Could not fetch Button Icon from URL: " + url);
            Log.error(ex);
            icon = null;
        }
    }
    
    @Override
    public String getName(){
        return this.name;
    }
    
}
