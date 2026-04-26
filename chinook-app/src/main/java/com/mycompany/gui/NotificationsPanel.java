package com.mycompany.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.mycompany.DatabaseConnection.DatabaseConnection;

public class NotificationsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JTextField firstName, lastName, email, phone, country;
    private int selectedId = -1;

    public NotificationsPanel() {
        setLayout(new BorderLayout());

        // FORM
        JPanel form = new JPanel(new GridLayout(2,5));

        firstName = new JTextField();
        lastName = new JTextField();
        email = new JTextField();
        phone = new JTextField();
        country = new JTextField();

        form.add(new JLabel("First"));
        form.add(new JLabel("Last"));
        form.add(new JLabel("Email"));
        form.add(new JLabel("Phone"));
        form.add(new JLabel("Country"));

        form.add(firstName);
        form.add(lastName);
        form.add(email);
        form.add(phone);
        form.add(country);

        // BUTTONS
        JPanel buttons = new JPanel();

        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton inactive = new JButton("Show Inactive");

        buttons.add(add);
        buttons.add(update);
        buttons.add(delete);
        buttons.add(inactive);

        // TABLE
        model = new DefaultTableModel();
        table = new JTable(model);

        add(form, BorderLayout.NORTH);
        add(buttons, BorderLayout.CENTER);
        add(new JScrollPane(table), BorderLayout.SOUTH);

        loadCustomers();

        // EVENTS
        add.addActionListener(e -> addCustomer());
        update.addActionListener(e -> updateCustomer());
        delete.addActionListener(e -> deleteCustomer());
        inactive.addActionListener(e -> loadInactiveCustomers());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                selectedId = (int) model.getValueAt(row, 0);
                firstName.setText(model.getValueAt(row,1).toString());
                lastName.setText(model.getValueAt(row,2).toString());
                email.setText(model.getValueAt(row,3).toString());
                phone.setText(model.getValueAt(row,4).toString());
                country.setText(model.getValueAt(row,5).toString());
            }
        });
    }

    private void loadCustomers() {
        model.setRowCount(0);
        model.setColumnIdentifiers(new String[]{"ID","First","Last","Email","Phone","Country"});

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Customer")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("CustomerId"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Country")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCustomer() {
        String sql = "INSERT INTO Customer (FirstName, LastName, Email, Phone, Country) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, firstName.getText());
            ps.setString(2, lastName.getText());
            ps.setString(3, email.getText());
            ps.setString(4, phone.getText());
            ps.setString(5, country.getText());

            ps.executeUpdate();
            loadCustomers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCustomer() {
        String sql = "UPDATE Customer SET FirstName=?, LastName=?, Email=?, Phone=?, Country=? WHERE CustomerId=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, firstName.getText());
            ps.setString(2, lastName.getText());
            ps.setString(3, email.getText());
            ps.setString(4, phone.getText());
            ps.setString(5, country.getText());
            ps.setInt(6, selectedId);

            ps.executeUpdate();
            loadCustomers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteCustomer() {
        String sql = "DELETE FROM Customer WHERE CustomerId=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, selectedId);
            ps.executeUpdate();
            loadCustomers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadInactiveCustomers() {

    String sql =
        "SELECT c.CustomerId, c.FirstName, c.LastName, MAX(i.InvoiceDate) AS LastPurchase " +
        "FROM Customer c " +
        "LEFT JOIN Invoice i ON c.CustomerId = i.CustomerId " +
        "GROUP BY c.CustomerId " +
        "HAVING LastPurchase IS NULL OR LastPurchase < DATE_SUB(NOW(), INTERVAL 2 YEAR)";

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        model.setRowCount(0);

        while (rs.next()) {
            model.addRow(new Object[]{
                    rs.getInt("CustomerId"),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getString("LastPurchase")
            });
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}