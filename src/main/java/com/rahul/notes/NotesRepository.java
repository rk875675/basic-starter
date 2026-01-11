package com.rahul.notes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotesRepository {
    private final String dbUrl;

    public NotesRepository(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }

    public void init() {
        String sql = """
            CREATE TABLE IF NOT EXISTS notes (
              id INTEGER PRIMARY KEY AUTOINCREMENT,
              text TEXT NOT NULL,
              created_at TEXT NOT NULL DEFAULT (datetime('now'))
            );
            """;
        try (Connection c = connect(); Statement s = c.createStatement()) {
            s.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("DB init failed", e);
        }
    }

    public long add(String text) {
        String sql = "INSERT INTO notes(text) VALUES (?)";
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, text);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
                throw new RuntimeException("No generated key returned");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Add failed", e);
        }
    }

    public List<Note> list() {
        String sql = "SELECT id, text, created_at FROM notes ORDER BY id DESC";
        List<Note> out = new ArrayList<>();
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Note(
                    rs.getLong("id"),
                    rs.getString("text"),
                    rs.getString("created_at")
                ));
            }
            return out;
        } catch (SQLException e) {
            throw new RuntimeException("List failed", e);
        }
    }

    public int delete(long id) {
        String sql = "DELETE FROM notes WHERE id = ?";
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Delete failed", e);
        }
    }
}
