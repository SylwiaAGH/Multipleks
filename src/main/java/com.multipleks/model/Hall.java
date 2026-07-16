package com.multipleks.model;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Sala kinowa. Ma siatkę miejsc (rzędy x miejsca w rzędzie) oraz zbiór typów
 * seansów, jakie może obsługiwać (np. sala VIP obsługuje tylko seanse VIP,
 * standardowa sala może obsługiwać 2D i 3D).
 */
public class Hall {

    private final String name;
    private final List<Seat> seats = new ArrayList<>();
    private final Set<ScreeningType> supportedScreeningTypes;

    public Hall(String name, int rows, int seatsPerRow, Set<ScreeningType> supportedScreeningTypes) {
        this(name, rows, seatsPerRow, supportedScreeningTypes, Set.of());
    }

    /**
     * @param vipRows etykiety rzędów (np. "A", "B"), których miejsca mają być typu VIP
     */
    public Hall(String name, int rows, int seatsPerRow,
                Set<ScreeningType> supportedScreeningTypes, Set<String> vipRows) {
        this.name = name;
        this.supportedScreeningTypes = EnumSet.copyOf(supportedScreeningTypes);
        for (int r = 0; r < rows; r++) {
            String rowLabel = String.valueOf((char) ('A' + r));
            SeatType seatType = vipRows.contains(rowLabel) ? SeatType.VIP : SeatType.STANDARD;
            for (int n = 1; n <= seatsPerRow; n++) {
                seats.add(new Seat(rowLabel, n, seatType));
            }
        }
    }

    public String getName() {
        return name;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public int getCapacity() {
        return seats.size();
    }

    public Set<ScreeningType> getSupportedScreeningTypes() {
        return supportedScreeningTypes;
    }

    public boolean supports(ScreeningType type) {
        return supportedScreeningTypes.contains(type);
    }

    public Seat findSeat(String code) {
        return seats.stream()
                .filter(s -> s.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Sala " + name + " nie ma miejsca o kodzie: " + code));
    }

    @Override
    public String toString() {
        return name + " (" + getCapacity() + " miejsc, obsługuje: " + supportedScreeningTypes + ")";
    }
}
