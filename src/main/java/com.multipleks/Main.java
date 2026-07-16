package com.multipleks;

import com.multipleks.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        // 1. Konfiguracja sieci kin (system obsługuje >= 2 lokalizacje)
        CinemaNetwork network = new CinemaNetwork("MultiKino");

        Cinema cinema1 = new Cinema("Super Tarasy", "ul. Akademicka 5");
        Cinema cinema2 = new Cinema("Stare Miasto", "ul. Rynkowa 1");
        network.addCinema(cinema1);
        network.addCinema(cinema2);

        // --- sale w kinie 1: sala standardowa (2D/3D) oraz sala VIP ---
        Hall hallStandard = new Hall("Sala 1", 10, 12,
                Set.of(ScreeningType.STANDARD_2D, ScreeningType.THREE_D));
        Hall hallVip = new Hall("Sala VIP", 5, 8,
                Set.of(ScreeningType.VIP), Set.of("A", "B")); // rzędy A i B to miejsca VIP
        cinema1.addHall(hallStandard);
        cinema1.addHall(hallVip);

        // --- sala w kinie 2 ---
        Hall hall2 = new Hall("Sala A", 8, 10,
                Set.of(ScreeningType.STANDARD_2D));
        cinema2.addHall(hall2);

        // --- filmy ---
        Movie bond = new Movie("James Bond: No Time To Die", "Akcja", 163,
                "Ostatnia misja Jamesa Bonda.");
        Movie animation = new Movie("Toy Story 5", "Animacja", 100,
                "Kolejna przygoda zabawek Andy'ego.");

        // --- seanse (w tym VIP i 3D) ---
        Screening screening1 = cinema1.addScreening(bond, hallStandard,
                LocalDateTime.now().plusDays(1).withHour(18).withMinute(0),
                ScreeningType.STANDARD_2D, 22.0);
        cinema1.addScreening(bond, hallVip,
                LocalDateTime.now().plusDays(2).withHour(20).withMinute(30),
                ScreeningType.VIP, 35.0);
        cinema1.addScreening(animation, hallStandard,
                LocalDateTime.now().plusDays(3).withHour(12).withMinute(0),
                ScreeningType.THREE_D, 25.0);
        cinema2.addScreening(animation, hall2,
                LocalDateTime.now().plusDays(1).withHour(16).withMinute(0),
                ScreeningType.STANDARD_2D, 20.0);


        // 2. Sprawdzenie repertuaru na najbliższy tydzień (cała sieć)
        network.printFullProgramme();


        // 3. Przykładowe wywołania - rezerwacje i zakup biletów
        Screening screening = cinema1.getScreenings()[0];

        screening.reservePlaces("A1", "A2", "A3"); // rezerwacja po numerach miejsc

        Seat seat4 = hallStandard.findSeat("A4");
        Seat seat5 = hallStandard.findSeat("A5");
        screening.reservePlaces(seat4, seat5); // rezerwacja po obiektach Seat

        Customer customer = new Customer("Jan", "Kowalski", "jan.kowalski@example.com");
        screening.reservePlaces(customer, "B1", "B2"); // rezerwacja dla zarejestrowanego klienta

        screening.buyTickets("C1"); // zakup biletu bez konta
        screening.buyTickets(customer, "B3"); // zakup biletu dla zarejestrowanego klienta

        Movie found = network.findMovie("James Bond: No Time To Die");
        System.out.println("\nZnaleziony film w sieci: " + found);


        // 4. Sprawdzenie swoich biletów przez klienta
        System.out.println();
        customer.printTickets();


        // 5. Dostępne miejsca na dany seans
        List<Seat> free = screening.getAvailableSeats();
        System.out.println("\nWolne miejsca na seans \"" + screening.getMovie().getTitle() +
                "\": " + free.size() + " z " + hallStandard.getCapacity());
        System.out.println("Przykładowe wolne miejsca: " + free.subList(0, Math.min(5, free.size())));
    }
}
