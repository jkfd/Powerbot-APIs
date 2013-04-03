package SuperHeater.GUI;

import SuperHeater.Misc.Consts;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


public class GUI {
    private static GridLayout tabLayout   = new GridLayout(4, 2);
    public static Map<String, Button> buttons = new HashMap<String, Button>();
    
    /**
     * Function is called in onStart() and should include everything that
     * needs to be to the GUI done before the script commences.
     */
    public static void init(){
        // Initialize buttons in hashmap
        buttons.put("show", new Button(
                "https://i.minus.com/ixQDSw2wJUuee.png", "Show Paint",
                42, 42, 470, 390, false, ClickAction.ACTION_SHOW, false));
        
        buttons.put("hide", new Button(
                "https://i.minus.com/ib1muD0YQfFNCD.png", "Hide Paint", 
                42, 42, 470, 390, true, ClickAction.ACTION_HIDE, true));
        
        buttons.put("bug", new Button(
                "https://i.minus.com/iM40j1pOQoZhT.png", "Report Bug", 
                42, 42, 425, 390, true, ClickAction.ACTION_BUG, true));
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

    /**
     * Creates the GUI window where the user selects configuration
     * options for the script. Should be phased out before V. 1.60
     */
    public static void createWindow(){
        // Configure MainFrame
        final JFrame main = new JFrame ("F2P SuperHeater");
        main.setSize(500,300);
        main.setAlwaysOnTop(true);

        // Configure main Panel
        JPanel mainP = new JPanel();
        mainP.setSize(490,200);

            // Configure Settings Tab
            JTabbedPane settingTab = new JTabbedPane();

                // Add a panel to the TabbedPane
                JPanel settings = new JPanel();
                settings.setLayout(tabLayout);

                    // Ore Selection
                    JLabel selOre           = new JLabel("Ore to superheat:");
                    Object[] oreKeySet      = Consts.BARREQ.keySet().toArray();
                    String[] oreOpts        =  Arrays.copyOf(oreKeySet,
                                                        oreKeySet.length,
                                                        String[].class
                                                    );
                    final JComboBox<String> ores    = new JComboBox<String>(oreOpts);

                    // Coalbag checkbox
                    JLabel cbL              = new JLabel("Use Coal Bag");
                    final JCheckBox cbC      = new JCheckBox();

                    // Logout on stop Checkbox
                    JLabel logoutL          = new JLabel("Logout when stopping?");
                    final JCheckBox logout  = new JCheckBox();

                    // Row 1
                    settings.add(selOre);
                    settings.add(ores);

                    // Row 4
                    settings.add(cbL);
                    settings.add(cbC);

                    // Row 4
                    settings.add(logoutL);
                    settings.add(logout);


            // Add the AntiBan panel to the TabbedPane
                JPanel antiban = new JPanel();
                antiban.setLayout(tabLayout);

                JLabel Laf = new JLabel("Antiban Frequency:");
                final JSlider Saf = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
                Saf.setMajorTickSpacing(10);
                Saf.setMinorTickSpacing(5);
                Saf.setPaintTicks(true);
                Saf.setSnapToTicks(true);

                antiban.add(Laf);
                antiban.add(Saf);

            // Add the Target panel to TabbedPane
                JPanel target = new JPanel();
                target.setLayout(tabLayout);

                JLabel Lta = new JLabel("Use Target?");
                JLabel Lte = new JLabel("Enter Target Bars:");
                final JCheckBox Cta = new JCheckBox();
                final JTextField Tte = new JTextField();

                target.add(Lta);
                target.add(Cta);
                target.add(Lte);
                target.add(Tte);
                
            // Add GE panel to TabbedPane
                JPanel GE = new JPanel();
                GE.setLayout(tabLayout);
                
                JLabel Lge = new JLabel("Sell Bars on Finish?");
                JLabel Lpr = new JLabel("Price:");
                final JCheckBox Cge = new JCheckBox();
                final JTextField Tpr = new JTextField();
                
                GE.add(Lge);
                GE.add(Cge);
                GE.add(Lpr);
                GE.add(Tpr);


            // Add panels to Tabbed panes
            settingTab.addTab("Settings", settings);
            settingTab.addTab("Antiban", antiban);
            settingTab.addTab("Targets", target);
            settingTab.addTab("GrandExchange", GE);

            // Go Button
            JButton go = new JButton("Start Script");
            go.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Add configuration
                    Consts.CONFIG.put("barType", ores.getSelectedItem().toString());
                    Consts.CONFIG.put("abFrequency", Integer.toString(Saf.getValue()));

                    if (cbC.isSelected()) {
                        Consts.CONFIG.put("useCB", "TRUE");
                    }

                    if (logout.isSelected()) {
                        Consts.CONFIG.put("stopAction", "logout");
                    }

                    if (Cta.isSelected() && Tte.getText().length() > 0){
                        Consts.CONFIG.put("barTargetEnabled", "True");
                        Consts.CONFIG.put("barTarget", Tte.getText());
                        Consts.BAR_TARGET  = Integer.parseInt(Consts.CONFIG.get("barTarget"));
                    }
                    
                    if (Cge.isSelected() && Tpr.getText().length() > 0) {
                        Consts.CONFIG.put("sellBars", "TRUE");
                        Consts.CONFIG.put("barPrice", Tpr.getText());
                    }

                    // Shortcut relevant hashMap entries
                    Consts.PRIMARY_ORE     = Consts.BARREQ.get(Consts.CONFIG.get("barType"))[0][0];
                    Consts.SECONDARY_ORE   = Consts.BARREQ.get(Consts.CONFIG.get("barType"))[1][0];
                    Consts.BARID           = Consts.BARREQ.get(Consts.CONFIG.get("barType"))[2][0];
                    Consts.ACTIVE_ORE      = Consts.BARREQ.get(Consts.CONFIG.get("barType"));

                    // Start script and hide GUI
                    Consts.GO = true;
                    Consts.START_TIME = System.currentTimeMillis();
                    main.setVisible(false);
                }
            });


        mainP.add(settingTab, BorderLayout.NORTH);
        main.add(mainP, BorderLayout.NORTH);
        main.add(go, BorderLayout.SOUTH);
        main.pack();
        main.setVisible(true);
    }

}
