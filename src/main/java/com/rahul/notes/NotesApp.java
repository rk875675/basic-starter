package com.rahul.notes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotesApp {

    private static final String DB_URL = "jdbc:sqlite:basic-starter.db";

    public static void main(String[] args) {
        try (Connection c = DriverManager.getConnection(DB_URL)) {
            init(c);

            if (args.length == 0) {
                help();
                return;
            }

            String cmd = args[0].toLowerCase();

            switch (cmd) {
                case "add" -> {
                    String text = joinArgs(args, 1);
                    if (text.isBlank()) {
                        System.out.println("Missing text. Example: add \"buy milk\"");
                        return;
                    }
                    long id = addNote(c, text);
                    System.out.println("Added note id=" + id);
                }
                case "list" -> {
                    List<NoteRow> notes = listNotes(c);
                    if (notes.isEmpty()) {
                        System.out.println("(no notes)");
                        return;
                    }
                    for (NoteRow n : notes) {
                        System.out.println(n.id + " | " + n.createdAt + " | " + n.text);
                    }
                }
                case "delete" -> {
                    if (args.length < 2) {
                        System.out.println("Missing id. Example: delete 3");
                        return;
                    }
                    long id = Long.parseLong(args[1]);
                    int rows = deleteNote(c, id);
                    System.out.println("Deleted rows=" + rows);
                }
                default -> help();
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Example: delete 3");
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void init(Connection c) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS notes (
              id INTEGER PRIMARY KEY AUTOINCREMENT,
              text TEXT NOT NULL,
              created_at TEXT NOT NULL DEFAULT (datetime('now'))
            );
            """;
        try (Statement s = c.createStatement()) {
            s.execute(sql);
        }
    }

    private static long addNote(Connection c, String text) throws SQLException {
        String sql = "INSERT INTO notes(text) VALUES (?);";
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, text);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        throw new SQLException("Insert succeeded but no id returned.");
    }

    private static List<NoteRow> listNotes(Connection c) throws SQLException {
        String sql = "SELECT id, text, created_at FROM notes ORDER BY id DESC;";
        List<NoteRow> out = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new NoteRow(
                        rs.getLong("id"),
                        rs.getString("text"),
                        rs.getString("created_at")
                ));
            }
        }
        return out;
    }

    private static int deleteNote(Connection c, long id) throws SQLException {
        String sql = "DELETE FROM notes WHERE id = ?;";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate();
        }
    }

    private static void help() {
        System.out.println("""
            NotesApp commands:
              add <text>
              list
              delete <id>

            Examples:
              add "buy milk"
              list
              delete 1
            """);
    }

    private static String joinArgs(String[] args, int startIdx) {
        if (startIdx >= args.length) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = startIdx; i < args.length; i++) {
            if (i > startIdx) sb.append(' ');
            sb.append(args[i]);
        }
        return sb.toString().trim();
    }

    private record NoteRow(long id, String text, String createdAt) {}
}
