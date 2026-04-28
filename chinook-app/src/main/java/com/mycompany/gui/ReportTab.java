package ui;

import db.DatabaseConnection;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReportTab extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public ReportTab() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"Genre", "Revenue"}, 0);
        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load report when tab becomes visible
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                loadReport();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {}

            @Override
            public void ancestorMoved(AncestorEvent event) {}
        });
    }

    private void loadReport() {

        model.setRowCount(0);

        String sql =
            "SELECT g.Name AS Genre, " +
            "SUM(il.UnitPrice * il.Quantity) AS Revenue " +
            "FROM invoiceline il " +
            "JOIN track t ON il.TrackId = t.TrackId " +
            "JOIN genre g ON t.GenreId = g.GenreId " +
            "GROUP BY g.Name " +
            "ORDER BY Revenue DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("Genre"),
                    String.format("$%.2f", rs.getDouble("Revenue"))
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading report: " + e.getMessage());
        }
    }
}