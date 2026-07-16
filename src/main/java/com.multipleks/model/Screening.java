package com.multipleks.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Pojedynczy seans - konkretny film wyświetlany w konkretnej sali, o konkretnej
 * porze. Odpowiada za dostępność miejsc, rezerwacje oraz sprzedaż biletów
 * (z kontem klienta lub bez).
 */
public class Screening {

    private final String id;
    private final Movie movie;
    private final Hall hall;
    private final LocalDateTime dateTime;
    private final ScreeningType type;
    private final double basePrice;

    // klucz = kod miejsca (np. "A12"); wartość = aktualny bilet/rezerwacja na to miejsce
    private final Map<String, Ticket> ticketsBySeatCode = new LinkedHashMap<>();

    public Screening(Movie movie, Hall hall, LocalDateTime dateTime, ScreeningType type, double basePrice) {
        if (!hall.supports(type)) {
            throw new IllegalArgumentException(
                    "Sala " + hall.getName() + " nie obsługuje typu seansu " + type);
        }
        this.id = UUID.randomUUID().toString();
        this.movie = movie;
        this.hall = hall;
        this.dateTime = dateTime;
        this.type = type;
        this.basePrice = basePrice;
    }

    public String getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public Hall getHall() {
        return hall;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public ScreeningType getType() {
        return type;
    }

    public double getBasePrice() {
        return basePrice;
    }

    private double priceFor(Seat seat) {
        double price = basePrice;
        if (seat.getType() == SeatType.VIP) price += 10.0;
        if (type == ScreeningType.THREE_D) price += 5.0;
        if (type == ScreeningType.VIP) price += 15.0;
        return price;
    }

    public List<Seat> getAvailableSeats() {
        List<Seat> free = new ArrayList<>();
        for (Seat seat : hall.getSeats()) {
            Ticket t = ticketsBySeatCode.get(seat.getCode());
            if (t == null || t.getStatus() == TicketStatus.CANCELLED) {
                free.add(seat);
            }
        }
        return free;
    }

    // ---- rezerwacja miejsc przed seansem ----

    /** Rezerwacja bez konta, po numerach miejsc, np. reservePlaces("H34", "H35"). */
    public List<Ticket> reservePlaces(String... seatCodes) {
        return createTickets(null, TicketStatus.RESERVED, seatCodes);
    }

    /** Rezerwacja bez konta, po obiektach Seat. */
    public List<Ticket> reservePlaces(Seat... seats) {
        return createTickets(null, TicketStatus.RESERVED, toCodes(seats));
    }

    /** Rezerwacja dla zarejestrowanego klienta. */
    public List<Ticket> reservePlaces(Customer customer, String... seatCodes) {
        return createTickets(customer, TicketStatus.RESERVED, seatCodes);
    }

    // ---- kupno biletów z wyprzedzeniem ----

    /** Zakup biletów bez zakładania konta. */
    public List<Ticket> buyTickets(String... seatCodes) {
        return createTickets(null, TicketStatus.PURCHASED, seatCodes);
    }

    /** Zakup biletów dla zarejestrowanego klienta. */
    public List<Ticket> buyTickets(Customer customer, String... seatCodes) {
        return createTickets(customer, TicketStatus.PURCHASED, seatCodes);
    }

    /** Zamiana wcześniejszej rezerwacji na zakupiony bilet. */
    public void confirmReservation(String seatCode) {
        Seat seat = hall.findSeat(seatCode);
        Ticket t = ticketsBySeatCode.get(seat.getCode());
        if (t == null || t.getStatus() != TicketStatus.RESERVED) {
            throw new IllegalStateException("Brak aktywnej rezerwacji dla miejsca " + seatCode);
        }
        t.setStatus(TicketStatus.PURCHASED);
    }

    public void cancelReservation(String seatCode) {
        Seat seat = hall.findSeat(seatCode);
        Ticket t = ticketsBySeatCode.get(seat.getCode());
        if (t == null) {
            throw new IllegalStateException("Brak rezerwacji/biletu dla miejsca " + seatCode);
        }
        t.setStatus(TicketStatus.CANCELLED);
    }

    private String[] toCodes(Seat... seats) {
        String[] codes = new String[seats.length];
        for (int i = 0; i < seats.length; i++) {
            codes[i] = seats[i].getCode();
        }
        return codes;
    }

    private void checkAvailable(Seat seat) {
        Ticket existing = ticketsBySeatCode.get(seat.getCode());
        if (existing != null && existing.getStatus() != TicketStatus.CANCELLED) {
            throw new IllegalStateException("Miejsce " + seat.getCode() + " jest już zajęte.");
        }
    }

    private List<Ticket> createTickets(Customer customer, TicketStatus status, String... seatCodes) {
        List<Seat> seatsToBook = new ArrayList<>();
        for (String code : seatCodes) {
            Seat seat = hall.findSeat(code);
            checkAvailable(seat);
            seatsToBook.add(seat);
        }
        List<Ticket> created = new ArrayList<>();
        for (Seat seat : seatsToBook) {
            Ticket ticket = new Ticket(this, seat, customer, status, priceFor(seat));
            ticketsBySeatCode.put(seat.getCode(), ticket);
            if (customer != null) {
                customer.addTicket(ticket);
            }
            created.add(ticket);
        }
        return created;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | sala: %s | %s | od %.2f zł",
                movie.getTitle(), dateTime, hall.getName(), type, basePrice);
    }
}
