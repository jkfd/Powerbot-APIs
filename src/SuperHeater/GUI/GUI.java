package SuperHeater.GUI;

import SuperHeater.Misc.Consts;
import SuperHeater.Misc.Logging.Log;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;


public class GUI {
    public static Map<String, Component> components = new HashMap<String, Component>();
    public static Map<String, Button> menuButtons = new HashMap<String, Button>();
    public static Map<String, Button> tabButtons = new HashMap<String, Button>();
    public static Map<String, Button> CBButtons = new HashMap<String, Button>();
    public static Map<String, Button> targetButtons = new HashMap<String, Button>();
    public static Map<String, Button> logoutButtons = new HashMap<String, Button>();
    public static Map<String, Button> GEButtons = new HashMap<String, Button>();
    public static Map<String, Button> barButtons = new LinkedHashMap<String, Button>();
    public static Map<String, Button> stopGoButtons = new HashMap<String, Button>();
    public static Map<String, TextField> textFields = new HashMap<String, TextField>();
    public static Map<String, Tab> tabs = new HashMap<String, Tab>();
    
    /**
     * Function is called in onStart() and should include everything that
     * needs to be to the GUI done before the script commences.
     */
    public static void init(){
        // Initialize tabs
        Tab ov = new Tab("Overview", false);
        Tab se = new Tab("Settings", true);
        
        // Initialize buttons in section based HashMaps
        // This is done to allow separate drawing of different
        // Button section
        menuButtons.put("show", new Button(
                "https://i.minus.com/ixQDSw2wJUuee.png", "Show Paint",
                42, 42, 470, 390, ClickAction.ACTION_SHOW, false, false));
        
        menuButtons.put("hide", new Button(
                "https://i.minus.com/ib1muD0YQfFNCD.png", "Hide Paint", 
                42, 42, 470, 390, ClickAction.ACTION_HIDE, true, false));
        
        menuButtons.put("bug", new Button(
                "https://i.minus.com/iM40j1pOQoZhT.png", "Report Bug", 
                42, 42, 425, 390, ClickAction.ACTION_BUG, true, false));
        
        tabButtons.put("overview", new Button(
                "https://i.minus.com/ibtEqcfhpIt51K.png", "Overview Tab", 
                42, 42, -2, 450, ClickAction.TAB_OVERVIEW, true, false));
        
        tabButtons.put("settings", new Button(
                "https://i.minus.com/i2evjuIkoqLNc.png", "Settings Tab", 
                42, 42, -2, 490, ClickAction.TAB_SETTINGS, true, false));
        
        CBButtons.put("CBYes", new Button(
                "https://i.minus.com/ibs9UyzwzjkrW4.png", "CBYes", 
                20, 20, 140, 445, ClickAction.CB_YES, true, Consts.CONFIG.get("useCB").equals("TRUE")));
        
        CBButtons.put("CBNo", new Button(
                "https://i.minus.com/i59bVziKkYXaV.png", "CBNo", 
                20, 20, 160, 445, ClickAction.CB_NO, true, Consts.CONFIG.get("useCB").equals("FALSE")));
        
        targetButtons.put("targetYes", new Button(
                "https://i.minus.com/ibs9UyzwzjkrW4.png", "targetYes", 
                20, 20, 140, 472, ClickAction.TA_YES, true, Consts.CONFIG.get("barTargetEnabled").equals("TRUE")));
        
        targetButtons.put("targetNo", new Button(
                "https://i.minus.com/i59bVziKkYXaV.png", "targetNo", 
                20, 20, 160, 472, ClickAction.TA_NO, true, Consts.CONFIG.get("barTargetEnabled").equals("FALSE")));
        
        logoutButtons.put("logoutYes", new Button(
                "https://i.minus.com/ibs9UyzwzjkrW4.png", "logoutYes", 
                20, 20, 290, 445, ClickAction.LO_YES, true, Consts.CONFIG.get("stopAction").equals("logout")));
        
        logoutButtons.put("logoutNo", new Button(
                "https://i.minus.com/i59bVziKkYXaV.png", "logoutNo", 
                20, 20, 310, 445, ClickAction.LO_NO, true, !Consts.CONFIG.get("stopAction").equals("logout")));
        
        GEButtons.put("GEYes", new Button(
                "https://i.minus.com/ibs9UyzwzjkrW4.png", "GEYes", 
                20, 20, 470, 445, ClickAction.GE_YES, true, Consts.CONFIG.get("sellBars").equals("TRUE")));
        
        GEButtons.put("GENo", new Button(
                "https://i.minus.com/i59bVziKkYXaV.png", "GENo", 
                20, 20, 490, 445, ClickAction.GE_NO, true, Consts.CONFIG.get("sellBars").equals("FALSE")));
        
        GEButtons.put("GEPrice", new Button(
                "https://i.minus.com/ibc5H3DhHgpA01.png", "GEPrice", 
                30, 70, 454, 467, ClickAction.GE_GET_PRICE, true, false));
        
        barButtons.put("Bronze", new Button(
                "https://i.minus.com/ibnxSOtmgUqkpx.gif", "Bronze", 
                33, 33, 0, 520, ClickAction.BAR_SELECT, true, Consts.CONFIG.get("barType").equals("Bronze")));
        
        barButtons.put("Iron", new Button(
                "https://i.minus.com/ib2jhXk8eHiS0E.gif", "Iron", 
                33, 33, 0, 520, ClickAction.BAR_SELECT, true, Consts.CONFIG.get("barType").equals("Iron")));
        
        barButtons.put("Silver", new Button(
                "https://i.minus.com/ikgPVMVZnLMZL.gif", "Silver", 
                33, 33, 0, 520, ClickAction.BAR_SELECT, true, Consts.CONFIG.get("barType").equals("Silver")));
        
        barButtons.put("Steel", new Button(
                "https://i.minus.com/ic5oU6LlbiSDg.gif", "Steel", 
                33, 33, 0, 520, ClickAction.BAR_SELECT, true, Consts.CONFIG.get("barType").equals("Steel")));
        
        barButtons.put("Gold", new Button(
                "https://i.minus.com/iFdcm56Xjue0x.gif", "Gold", 
                33, 33, 0, 520, ClickAction.BAR_SELECT, true, Consts.CONFIG.get("barType").equals("Gold")));
        
        barButtons.put("Mithril", new Button(
                "https://i.minus.com/iwDB3Unb1Zv8L.gif", "Mithirl", 
                33, 33, 0, 520, ClickAction.BAR_SELECT, true, Consts.CONFIG.get("barType").equals("Mithril")));
        
        barButtons.put("Adamantite", new Button(
                "https://i.minus.com/ib2qa1QrlUPaYQ.gif", "Adamantite", 
                33, 33, 0, 520, ClickAction.BAR_SELECT, true, Consts.CONFIG.get("barType").equals("Adamantite")));
        
        barButtons.put("Runite", new Button(
                "https://i.minus.com/iI5hNjQyrfCsF.gif", "Runite", 
                33, 33, 247, 520, ClickAction.BAR_SELECT, true, Consts.CONFIG.get("barType").equals("Runite")));
        
        stopGoButtons.put("Start", new Button(
                "https://i.minus.com/ibbbYlblkqsTfo.png", "Start", 
                37, 72, 430, 520, ClickAction.SCRIPT_START, true, false));
        
        stopGoButtons.put("Stop", new Button(
                "https://i.minus.com/ibhgGbaK56T1rU.png", "Stop", 
                37, 72, 430, 520, ClickAction.SCRIPT_STOP, false, false));
        
        textFields.put("TargetInput", new TextField(
                "https://i.minus.com/jIK4eYBV54HEi.png", "TargetInput", String.valueOf(Consts.BAR_TARGET), 
                " Bars", true, 30, 100, 190, 467, 0, true, false));
        
        textFields.put("PriceInput", new TextField(
                "https://i.minus.com/jIK4eYBV54HEi.png", "PriceInput", String.valueOf(Consts.SELLING_PRICE), 
                " Gp", true, 30, 100, 360, 467, 0, true, false));
        
        sortButtonRow(50);
        
        // Join tabbed buttons in their respective tabs
        for (String s : barButtons.keySet()) {
            se.addComponent(barButtons.get(s));
        }
        for (String s : CBButtons.keySet()) {
            se.addComponent(CBButtons.get(s));
        }
        for (String s : targetButtons.keySet()) {
            se.addComponent(targetButtons.get(s));
        }
        for (String s : logoutButtons.keySet()) {
            se.addComponent(logoutButtons.get(s));
        }
        for (String s : GEButtons.keySet()) {
            se.addComponent(GEButtons.get(s));
        }
        for (String s : textFields.keySet()) {
            se.addComponent(textFields.get(s));
        }
        
        
        // We keep one master HashMap that joins all other button maps
        // as this must be used to cycle through mouse clicks
        components.putAll(menuButtons);
        components.putAll(tabButtons);
        components.putAll(CBButtons);
        components.putAll(targetButtons);
        components.putAll(logoutButtons);
        components.putAll(GEButtons);
        components.putAll(barButtons);
        components.putAll(stopGoButtons);
    }
    
    /**
     * Takes a Map as an input and sets each button to be invisible
     * (disabled). This prevents selections from happening when the 
     * paint is hidden. User be aware that this method is created 
     * specifically for that purpose.
     * @param map Map mapping Strings to Buttons that needs to be hidden
     */
    public static void disableButtons(Map<String, Button> map){
        for (String s : map.keySet()) {
            map.get(s).setVisible(false);
        }
    }
    
    /**
     * Takes a Map as an input and sets each Button to be visible
     * (disabled). This enables selections from happening after the 
     * paint has been redisplayed. User be aware that this method is 
     * created specifically for that purpose.
     * @param map Map mapping Strings to Buttons that needs to have Buttons shown
     */
    public static void enableButtons(Map<String, Button> map){
        for (String s : map.keySet()) {
            map.get(s).setVisible(true);
        }
    }
    
    /**
     * Sorts the bar selection buttons so that they appear in a straight line
     * horizontally accross the screen.
     * @param startPos X-coordinate that the first (left-most) bar in the line should start at.
     */
    public static void sortButtonRow(int startPos){
        for (int i = 0; i < barButtons.keySet().size(); i++) {
            Button b = barButtons.get(barButtons.keySet().toArray()[i].toString());
            b.posX = (startPos+(b.getWidth()*i));
        }
    }
    
    /**
     * Gets the currently visible tab by looping through all
     * tabs and checking visibility. Only one tab should ever
     * be visible at the same time. Null if no tabs are visible.
     * This should also never happen. 
     * @return the Tab that is currently visible.
     */
    public static Tab getCurrentTab(){
        for (String key : tabs.keySet()) {
            if (tabs.get(key).visible()) {
                return tabs.get(key);
            }
        }
        Log.error("No tab visible");
        return null;
    }
    
    /**
     * Shows a popup window capable of showing large amounts of text through 
     * a JTextArea();
     * @param title String printed in the Popup titlebar
     * @param s String information to be displayed in the JTextArea.
     */
    public static void showTextPopup(String title, String s){
        JFrame f = new JFrame(title);
        JTextArea ta = new JTextArea();
        JPanel p = new JPanel();
        
        ta.setText(s);
        ta.setColumns(50);
        ta.setRows(10);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        
        JScrollPane sp = new JScrollPane(ta);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        p.add(sp);
        f.add(p);
        f.setSize(450, 220);
        f.setVisible(true);
    }

}
