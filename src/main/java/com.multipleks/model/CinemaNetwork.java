package com.multipleks.model;
import java.util.ArrayList;
import java.util.List;

/**
 * System obsługujący sieć multipleksów - agreguje dowolną liczbę obiektów
 * Cinema (co najmniej 2), umożliwiając np. wyszukiwanie filmu w całej sieci
 * czy wypisanie zbiorczego repertuaru.
 */
public class CinemaNetwork {

    private final String name;
    private final List<Cinema> cinemas = new ArrayList<>();

    public CinemaNetwork(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addCinema(Cinema cinema) {
        cinemas.add(cinema);
    }

    public List<Cinema> getCinemas() {
        return cinemas;
    }

    /** Wyszukanie filmu w dowolnym kinie należącym do sieci. */
    public Movie findMovie(String title) {
        for (Cinema cinema : cinemas) {
            Movie m = cinema.findMovie(title);
            if (m != null) {
                return m;
            }
        }
        return null;
    }

    /** Repertuar na najbliższy tydzień dla wszystkich kin w sieci. */
    public void printFullProgramme() {
        System.out.println("########## Repertuar sieci " + name + " ##########");
        for (Cinema cinema : cinemas) {
            cinema.printProgramme();
        }
    }

    @Override
    public String toString() {
        return "Sieć " + name + " (" + cinemas.size() + " kin)";
    }
}
