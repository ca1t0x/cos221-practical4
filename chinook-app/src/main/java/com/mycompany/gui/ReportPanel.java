package com.mycompany.gui;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.mycompany.DatabaseConnection.DatabaseConnection;

public class ReportPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public ReportPanel() {
        setLayout(new BorderLayout());

        JButton generate = new JButton("Generate Report");

        model = new DefaultTableModel();
        table = new JTable(model);

        add(generate, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        generate.addActionListener(e -> loadReport());
    }

    private void loadReport() {

        model.setRowCount(0);
        model.setColumnIdentifiers(new String[]{"Genre", "Revenue"});

        String sql =
            "SELECT g.Name AS Genre, SUM(il.UnitPrice * il.Quantity) AS Revenue " +
            "FROM InvoiceLine il " +
            "JOIN Track t ON il.TrackId = t.TrackId " +
            "JOIN Genre g ON t.GenreId = g.GenreId " +
            "GROUP BY g.Name " +
            "ORDER BY Revenue DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("Genre"),
                        rs.getDouble("Revenue")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}