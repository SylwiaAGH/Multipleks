package com.multipleks.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Bilet (albo rezerwacja) na konkretne miejsce na konkretnym seansie.
 * Klient (Customer) jest opcjonalny - bilet może zostać kupiony/zarezerwowany
 * bez zakładania konta (wtedy customer == null).
 */
public class Ticket {

    private final String id;
    private final Screening screening;
    private final Seat seat;
    private final Customer customer; // może być null - zakup/rezerwacja bez konta
    private TicketStatus status;
    private final double price;
    private final LocalDateTime createdAt;

    public Ticket(Screening screening, Seat seat, Customer customer, TicketStatus status, double price) {
        this.id = UUID.randomUUID().toString();
        this.screening = screening;
        this.seat = seat;
        this.customer = customer;
        this.status = status;
        this.price = price;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public Screening getScreening() {
        return screening;
    }

    public Seat getSeat() {
        return seat;
    }

    public Customer getCustomer() {
        return customer;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String owner = customer == null ? "gość (bez konta)" : customer.getFullName();
        return String.format("Bilet[%s]: %s | miejsce %s | %s | status: %s | cena: %.2f zł | klient: %s",
                id.substring(0, 8), screening.getMovie().getTitle(), seat.getCode(),
                screening.getDateTime(), status, price, owner);
    }
}
