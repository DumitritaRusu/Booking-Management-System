package model;

import java.time.LocalDate;

public class Rezervare {
    private Client client;
    private Proprietate proprietate;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double pretTotal;

    public Rezervare(Client client, Proprietate proprietate,
                     LocalDate checkIn, LocalDate checkOut, double pretTotal) {
        this.client = client;
        this.proprietate = proprietate;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.pretTotal = pretTotal;
    }

    public Client getClient() { return client; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public double getPretTotal() { return pretTotal; }
}
