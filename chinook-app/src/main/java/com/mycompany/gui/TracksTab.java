package ui;

import db.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class TracksTab extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    static class ComboItem {
        int id;
        String name;

        ComboItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    public TracksTab() {
        setLayout(new BorderLayout());

        JButton addBtn = new JButton("Add New Track");

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(addBtn);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {
            "ID", "Name", "Album", "Genre",
            "Media Type", "Duration", "Price"
        };

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadTracks();

        addBtn.addActionListener(e -> showAddTrackDialog());
    }

    private void loadTracks() {
        tableModel.setRowCount(0);

        String sql =
            "SELECT t.TrackId, t.Name, al.Title AS Album, g.Name AS Genre, " +
            "mt.Name AS MediaType, t.Milliseconds, t.UnitPrice " +
            "FROM track t " +
            "JOIN album al ON t.AlbumId = al.AlbumId " +
            "JOIN genre g ON t.GenreId = g.GenreId " +
            "JOIN mediatype mt ON t.MediaTypeId = mt.MediaTypeId " +
            "ORDER BY t.TrackId ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("TrackId"),
                    rs.getString("Name"),
                    rs.getString("Album"),
                    rs.getString("Genre"),
                    rs.getString("MediaType"),
                    rs.getInt("Milliseconds"),
                    String.format("$%.2f", rs.getDouble("UnitPrice"))
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading tracks: " + e.getMessage());
        }
    }

    private void showAddTrackDialog() {

        JDialog dialog = new JDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            "Add New Track",
            true
        );

        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(8, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField millisField = new JTextField("0");
        JTextField priceField = new JTextField("0.99");

        JComboBox<ComboItem> albumCombo = new JComboBox<>();
        JComboBox<ComboItem> genreCombo = new JComboBox<>();
        JComboBox<ComboItem> mediaCombo = new JComboBox<>();


        try (Connection conn = DatabaseConnection.getConnection()) {

            ResultSet rs = conn.createStatement()
                .executeQuery("SELECT AlbumId, Title FROM album ORDER BY Title");
            while (rs.next()) {
                albumCombo.addItem(new ComboItem(
                    rs.getInt("AlbumId"),
                    rs.getString("Title")
                ));
            }

            rs = conn.createStatement()
                .executeQuery("SELECT GenreId, Name FROM genre ORDER BY Name");
            while (rs.next()) {
                genreCombo.addItem(new ComboItem(
                    rs.getInt("GenreId"),
                    rs.getString("Name")
                ));
            }

            rs = conn.createStatement()
                .executeQuery("SELECT MediaTypeId, Name FROM mediatype ORDER BY Name");
            while (rs.next()) {
                mediaCombo.addItem(new ComboItem(
                    rs.getInt("MediaTypeId"),
                    rs.getString("Name")
                ));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(dialog,
                "Error loading dropdowns: " + e.getMessage());
        }

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        dialog.add(new JLabel("Track Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Album:"));
        dialog.add(albumCombo);
        dialog.add(new JLabel("Genre:"));
        dialog.add(genreCombo);
        dialog.add(new JLabel("Media Type:"));
        dialog.add(mediaCombo);
        dialog.add(new JLabel("Duration (ms):"));
        dialog.add(millisField);
        dialog.add(new JLabel("Price:"));
        dialog.add(priceField);
        dialog.add(saveBtn);
        dialog.add(cancelBtn);

        cancelBtn.addActionListener(e -> dialog.dispose());

        saveBtn.addActionListener(e -> {

            String name = nameField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Track name is required!");
                return;
            }

            try {
                int millis = Integer.parseInt(millisField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());

                ComboItem album = (ComboItem) albumCombo.getSelectedItem();
                ComboItem genre = (ComboItem) genreCombo.getSelectedItem();
                ComboItem media = (ComboItem) mediaCombo.getSelectedItem();

                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO track (Name, AlbumId, MediaTypeId, GenreId, Milliseconds, UnitPrice) VALUES (?, ?, ?, ?, ?, ?)"
                     )) {

                    stmt.setString(1, name);
                    stmt.setInt(2, album.id);
                    stmt.setInt(3, media.id);
                    stmt.setInt(4, genre.id);
                    stmt.setInt(5, millis);
                    stmt.setDouble(6, price);

                    stmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(dialog,
                    "Track added successfully!");

                dialog.dispose();
                loadTracks();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Please enter valid numbers for duration and price.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error saving track: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }
}