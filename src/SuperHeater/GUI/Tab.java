/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SuperHeater.GUI;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jkfd
 */
public class Tab {
    private boolean visible;
    private String name;
    private List<Component> componentList = new ArrayList<Component>();
    
    public Tab(String n){
        this.name = n;
        this.visible = false;
        GUI.tabs.put(n, this);
    }
    
    public Tab(String n, boolean v){
        this.name = n;
        this.visible = v;
        GUI.tabs.put(n, this);
    }
    
    /**
     * TRUE if the tab is visible. Only 1 tab should ever be
     * visible at the same time. The Tab.switchTo() method
     * should be used to switch tabs to ensure this.
     * @return TRUE or FALSE
     */
    public boolean visible(){
        return this.visible;
    }
    
    /**
     * Gets the name of the tab. This allows a tab to be identified,
     * thus names must be unique and descriptive. Uniqueness is not
     * currently enforced, but is good practice.
     * @return 
     */
    public String getName(){
        return this.name;
    }
    
    /**
     * Switches to the current tab in a "Safe" way.
     * Automatically sets the current tab and its components to invisible
     * before making the switched tab, and its components, visible.
     * @return TRUE if the switched-to tab is the current tab on completion.
     */
    public boolean switchTo(){
        Tab current = GUI.getCurrentTab();
        
        current.visible = false;
        
        for (Component c : current.componentList) {
            c.setVisible(false);
        }
        
        
        this.visible = true;
        
        for (Component c : this.componentList) {
            c.setVisible(true);
        }
        
        return (GUI.getCurrentTab() == this);
    }
    
    /**
     * Adds a Component to this tabs List componentList.
     * This is a List that holds all Components that are to 
     * be painted when the tab is visible. 
     * @param c the Component to be added.
     */
    public void addComponent(Component c) {
        this.componentList.add(c);
    }
    
    /**
     * Returns a complete list of all Components added to this tab.
     * This is a List that holds all Components that are to 
     * be painted when the tab is visible. 
     * @return The List of components 
     */
    public List<Component> getComponents(){
        return this.componentList;
    }
}
