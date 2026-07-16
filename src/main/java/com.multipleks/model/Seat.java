package com.multipleks.model;

import java.util.Objects;

/**
 * Pojedyncze miejsce na sali kinowej (np. "A12").
 */
public class Seat {

    private final String row;
    private final int number;
    private final SeatType type;

    public Seat(String row, int number, SeatType type) {
        this.row = row;
        this.number = number;
        this.type = type;
    }

    public String getRow() {
        return row;
    }

    public int getNumber() {
        return number;
    }

    public SeatType getType() {
        return type;
    }

    /**
     * Kod miejsca w formacie "A12" - używany do rezerwacji po numerze.
     */
    public String getCode() {
        return row + number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat)) return false;
        Seat seat = (Seat) o;
        return number == seat.number && row.equals(seat.row);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, number);
    }

    @Override
    public String toString() {
        return getCode() + (type == SeatType.VIP ? " (VIP)" : "");
    }
}
