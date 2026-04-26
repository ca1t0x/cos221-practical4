package com.mycompany.gui;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.mycompany.DatabaseConnection.DatabaseConnection;

public class RecommendationsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JComboBox<Integer> customerBox;

    public RecommendationsPanel() {
        setLayout(new BorderLayout());

        customerBox = new JComboBox<>();
        JButton load = new JButton("Load Recommendations");

        JPanel top = new JPanel();
        top.add(customerBox);
        top.add(load);

        model = new DefaultTableModel();
        table = new JTable(model);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadCustomers();

        load.addActionListener(e -> loadRecommendations());
    }

    private void loadCustomers() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT CustomerId FROM Customer")) {

            while (rs.next()) {
                customerBox.addItem(rs.getInt("CustomerId"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadRecommendations() {

        int id = (int) customerBox.getSelectedItem();

        String sql =
            "SELECT t.Name FROM Track t " +
            "WHERE t.GenreId = (" +
            "SELECT g.GenreId FROM Invoice i " +
            "JOIN InvoiceLine il ON i.InvoiceId = il.InvoiceId " +
            "JOIN Track tr ON il.TrackId = tr.TrackId " +
            "JOIN Genre g ON tr.GenreId = g.GenreId " +
            "WHERE i.CustomerId = ? " +
            "GROUP BY g.GenreId ORDER BY COUNT(*) DESC LIMIT 1" +
            ")";

        model.setColumnIdentifiers(new String[]{"Recommended Tracks"});
        model.setRowCount(0);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("Name")});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}