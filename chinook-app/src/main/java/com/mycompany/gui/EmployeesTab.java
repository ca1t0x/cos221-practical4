package ui;

import db.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class EmployeesTab extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public EmployeesTab() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search by name or city: "));

        searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");

        topPanel.add(searchField);
        topPanel.add(searchBtn);

        add(topPanel, BorderLayout.NORTH);


        String[] columns = {
            "First Name", "Last Name", "Title",
            "City", "Country", "Phone",
            "Supervisor", "Active"
        };

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);
        loadEmployees("");

//EVENT LISTENERS
        searchBtn.addActionListener(e ->
            loadEmployees(searchField.getText().trim())
        );

        searchField.addActionListener(e ->
            loadEmployees(searchField.getText().trim())
        );

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
    public void insertUpdate(javax.swing.event.DocumentEvent e) {
        filter();
    }

    public void removeUpdate(javax.swing.event.DocumentEvent e) {
        filter();
    }

    public void changedUpdate(javax.swing.event.DocumentEvent e) {
        filter();
    }

    private void filter() {
        loadEmployees(searchField.getText().trim());
    }
    });
    }


    private void loadEmployees(String search) {

        tableModel.setRowCount(0);

        String sql =
            "SELECT e.FirstName, e.LastName, e.Title, e.City, e.Country, e.Phone, " +
            "m.FirstName AS SupervisorFirst, m.LastName AS SupervisorLast, " +
            "CASE WHEN EXISTS (" +
            "   SELECT 1 FROM customer c WHERE c.SupportRepId = e.EmployeeId" +
            ") THEN 'Yes' ELSE 'No' END AS Active " +
            "FROM employee e " +
            "LEFT JOIN employee m ON e.ReportsTo = m.EmployeeId " +
            "WHERE e.FirstName LIKE ? OR e.LastName LIKE ? OR e.City LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String pattern = "%" + search + "%";

            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                String supervisor;
                if (rs.getString("SupervisorFirst") != null) {
                    supervisor = rs.getString("SupervisorFirst") + " " +
                                 rs.getString("SupervisorLast");
                } else {
                    supervisor = "None";
                }

                tableModel.addRow(new Object[]{
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getString("Title"),
                    rs.getString("City"),
                    rs.getString("Country"),
                    rs.getString("Phone"),
                    supervisor,
                    rs.getString("Active")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading employees: " + e.getMessage());
        }
    }
}