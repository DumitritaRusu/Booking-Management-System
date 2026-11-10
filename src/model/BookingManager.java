package model;

import java.util.*;

public class BookingManager {

    private List<Proprietate> inventar;
    private Map<Integer, Proprietate> hartaProprietati;

    public BookingManager() {
        this.inventar = new ArrayList<>();
        this.hartaProprietati = new HashMap<>();
    }


    public List<Proprietate> getInventar() {
        return inventar;
    }

    public void adauga(Proprietate p) {
        inventar.add(p);
        hartaProprietati.put(p.getId(), p);
    }


    public Proprietate gasesteDupaId(int id) {
        return hartaProprietati.get(id);
    }

    public void sorteazaDupaRating() {
        inventar.sort((p1, p2) -> Double.compare(p2.getRating(), p1.getRating()));
    }

    public void afiseazaToate() {
        for (Proprietate p : inventar) {

            System.out.println(p.getNume() + " - " + p.getPretNoapte() + " RON/noapte");
        }
    }
    private int clientCounter = 1;

    public int getNextClientId() {
        return clientCounter++;
    }


}