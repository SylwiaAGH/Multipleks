package com.multipleks.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Zarejestrowany klient. Posiadanie konta jest opcjonalne - system pozwala
 * też kupować/rezerwować bilety anonimowo (patrz: Screening#buyTickets(String...)).
 */
public class Customer {

    private final String id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final List<Ticket> tickets = new ArrayList<>();

    public Customer(String firstName, String lastName, String email) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    /**
     * Sprawdzenie swoich biletów przez klienta.
     */
    public List<Ticket> getTickets() {
        return Collections.unmodifiableList(tickets);
    }

    public void printTickets() {
        if (tickets.isEmpty()) {
            System.out.println(getFullName() + " nie posiada żadnych biletów.");
            return;
        }
        System.out.println("Bilety klienta " + getFullName() + ":");
        for (Ticket t : tickets) {
            System.out.println(" - " + t);
        }
    }

    @Override
    public String toString() {
        return getFullName() + " <" + email + ">";
    }
}
