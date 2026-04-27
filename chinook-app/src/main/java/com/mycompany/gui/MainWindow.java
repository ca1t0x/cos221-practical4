package com.mycompany.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("COS221 Music Store");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Employees", new JPanel());
        tabs.addTab("Tracks", new JPanel());
        tabs.addTab("Report", new JPanel());
        tabs.addTab("Notifications", new JPanel());
        tabs.addTab("Recommendations", new JPanel());
        

        add(tabs);
    }
}