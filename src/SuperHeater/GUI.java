package SuperHeater;

import SuperHeater.Misc.Consts;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;


public class GUI {
    private static GridLayout tabLayout   = new GridLayout(4, 2);

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

                    // PIN Entry
                    JLabel pinL             = new JLabel("Enter Your Bank PIN:");
                    final JTextField PIN    = new JTextField();

                    // Coalbag checkbox
                    JLabel cbL              = new JLabel("Use Coal Bag");
                    final JCheckBox cbC      = new JCheckBox();

                    // Logout on stop Checkbox
                    JLabel logoutL          = new JLabel("Logout when stopping?");
                    final JCheckBox logout  = new JCheckBox();

                    // Row 1
                    settings.add(selOre);
                    settings.add(ores);

                    // Row 2
                    settings.add(pinL);
                    settings.add(PIN);

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


            // Add panels to Tabbed panes
            settingTab.addTab("Settings", settings);
            settingTab.addTab("Antiban", antiban);
            settingTab.addTab("Targets", target);

            // Go Button
            JButton go = new JButton("Start Script");
            go.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Add configuration
                    Consts.CONFIG.put("barType", ores.getSelectedItem().toString());
                    Consts.CONFIG.put("PIN", PIN.getText());
                    Consts.CONFIG.put("abFrequency", Integer.toString(Saf.getValue()));

                    if(cbC.isSelected()) {
                        Consts.CONFIG.put("useCB", "TRUE");
                    }

                    if(logout.isSelected()) {
                        Consts.CONFIG.put("stopAction", "logout");
                    }

                    if(Cta.isSelected() && Tte.getText().length() > 0){
                        Consts.CONFIG.put("barTargetEnabled", "True");
                        Consts.CONFIG.put("barTarget", Tte.getText());
                        Consts.BAR_TARGET  = Integer.parseInt(Consts.CONFIG.get("barTarget"));
                    }

                    // Shortcut relevant hashMap entries
                    Consts.PRIMARY_ORE     = Consts.BARREQ.get(Consts.CONFIG.get("barType"))[0][0];
                    Consts.SECONDARY_ORE   = Consts.BARREQ.get(Consts.CONFIG.get("barType"))[1][0];
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
