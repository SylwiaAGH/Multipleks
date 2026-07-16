package com.multipleks.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Pojedyncze kino (lokalizacja). Sieć multipleksów (CinemaNetwork) może
 * agregować dowolną liczbę obiektów Cinema.
 */
public class Cinema {

    private final String name;
    private final String address;
    private final List<Hall> halls = new ArrayList<>();
    private final List<Screening> screenings = new ArrayList<>();

    public Cinema(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void addHall(Hall hall) {
        halls.add(hall);
    }

    public List<Hall> getHalls() {
        return halls;
    }

    public Screening addScreening(Movie movie, Hall hall, LocalDateTime dateTime,
                                   ScreeningType type, double basePrice) {
        Screening screening = new Screening(movie, hall, dateTime, type, basePrice);
        screenings.add(screening);
        return screening;
    }

    public Screening[] getScreenings() {
        return screenings.toArray(new Screening[0]);
    }

    /** Wyszukanie filmu w repertuarze tego kina po tytule. */
    public Movie findMovie(String title) {
        return screenings.stream()
                .map(Screening::getMovie)
                .filter(m -> m.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    public List<Screening> getScreeningsForMovie(String title) {
        List<Screening> result = new ArrayList<>();
        for (Screening s : screenings) {
            if (s.getMovie().getTitle().equalsIgnoreCase(title)) {
                result.add(s);
            }
        }
        return result;
    }

    /** Wypisuje repertuar na najbliższy tydzień. */
    public void printProgramme() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekAhead = now.plusDays(7);
        System.out.println("=== Repertuar kina " + name + " (" + address + ") na najbliższy tydzień ===");
        screenings.stream()
                .filter(s -> !s.getDateTime().isBefore(now) && s.getDateTime().isBefore(weekAhead))
                .sorted((a, b) -> a.getDateTime().compareTo(b.getDateTime()))
                .forEach(s -> System.out.println(" - " + s));
    }

    @Override
    public String toString() {
        return name + " (" + address + ")";
    }
}
