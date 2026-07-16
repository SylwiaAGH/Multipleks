package com.multipleks.model;

/**
 * Film, który może być wyświetlany na seansach.
 */
public class Movie {

    private final String title;
    private final String genre;
    private final int durationMinutes;
    private final String description;

    public Movie(String title, String genre, int durationMinutes, String description) {
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return title + " (" + genre + ", " + durationMinutes + " min)";
    }
}
