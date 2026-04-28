package ui;

import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Chinook Music Store");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Employees", new EmployeesTab());
        tabbedPane.addTab("Tracks", new TracksTab());
        tabbedPane.addTab("Report", new ReportTab());
        tabbedPane.addTab("Notifications", new NotificationsTab());
        tabbedPane.addTab("Recommendations", new RecommendationsTab());

        add(tabbedPane);
    }
}