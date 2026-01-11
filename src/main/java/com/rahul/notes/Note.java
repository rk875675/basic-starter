package com.rahul.notes;

public class Note {
    public final long id;
    public final String text;
    public final String createdAt;

    public Note(long id, String text, String createdAt) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
    }
}