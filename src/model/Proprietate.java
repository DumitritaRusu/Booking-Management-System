package model;


import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public abstract class Proprietate implements Rezervabil {
    protected List<IntervalRezervare> rezervariOcupate = new ArrayList<>();
    protected List<Rezervare> rezervari = new ArrayList<>();
    protected int id;
    protected String nume;
    protected String adresa;
    protected double pretNoapte;
    protected double rating;
    protected boolean areParcare;
    protected double pretParcare;
    protected boolean areMicDejun;
    protected double pretMicDejun;
    protected int nrCamere;
    protected boolean areBucatarie;
    public Proprietate(int id, String nume, String adresa, double pretNoapte, double rating,boolean areParcare,boolean areMicDejun,double pretMicDejun,double pretParcare,int nrCamere, boolean areBucatarie) {
        this.id = id;
        this.nume = nume;
        this.adresa = adresa;
        this.pretNoapte = pretNoapte;
        this.rating = rating;
        this.areParcare = areParcare;
        this.areMicDejun = areMicDejun;
        this.nrCamere = nrCamere;
        this.areBucatarie = areBucatarie;
        if(this.areMicDejun==true){
            this.pretMicDejun=pretMicDejun;
        }
        else
            {
            this.pretMicDejun=0;
            }
        if(this.areParcare==true){
            this.pretParcare=pretParcare;
        }
        else{
            this.pretParcare=0;
        }

    }
    public String getAdresa() { return adresa; }
    public int getId() { return id; }
    public void setId(int id) {
        this.id = id;
    }

    public String getNume() { return nume; }
    public int getNrCamere() { return nrCamere; }
    public boolean isAreBucatarie() { return areBucatarie; }
    public double getPretNoapte() { return pretNoapte; }
    public boolean isAreParcare() { return areParcare; }
    public double getPretParcare() { return pretParcare; }
    public boolean isAreMicDejun() { return areMicDejun; }
    public double getPretMicDejun() { return pretMicDejun; }

    public boolean verificaDisponibilitate(String startStr, String sfarsitStr) throws RezervareInvalida {
        LocalDate startNou = LocalDate.parse(startStr);
        LocalDate sfarsitNou = LocalDate.parse(sfarsitStr);


        if (sfarsitNou.isBefore(startNou)) {
            throw new RezervareInvalida("Data de plecare este inaintea sosirii!");
        }

        for (Rezervare r : rezervari) {
            if (!(sfarsitNou.isBefore(r.getCheckIn())
                    || startNou.isAfter(r.getCheckOut()))) {
                return false;
            }
        }
        return true;
    }

public void adaugaRezervare(Rezervare rezervare) {
    rezervari.add(rezervare);
}

    public abstract double calculeazaPretTotal(int numarNopti, boolean doresteParcare, boolean doresteMicDejun);
    public double getRating() {
        return rating;
    }

    public void afiseazaInfo() {
        System.out.println("ID: " + id + " | " + nume + " (" + adresa + ") - " + pretNoapte + " RON/noapte");
    }


}
