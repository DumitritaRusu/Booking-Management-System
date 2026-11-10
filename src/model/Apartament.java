package model;


public class Apartament extends Proprietate {
    public Apartament(int id, String nume, String adresa, double pretNoapte, double rating,
                      boolean areParcare, boolean areMicDejun, double pretMicDejun, double pretParcare,
                      int nrCamere, boolean areBucatarie, double extra) {


        super(id, nume, adresa, pretNoapte, rating, areParcare, areMicDejun, pretMicDejun, pretParcare, nrCamere, areBucatarie);


    }


    @Override
    public double calculeazaPretTotal(int numarNopti, boolean doresteParcare, boolean doresteMicDejun) {
        double total = this.pretNoapte * numarNopti;
        if (doresteParcare && this.areParcare) total += this.pretParcare;
        if (doresteMicDejun && this.areMicDejun) total += (this.pretMicDejun * numarNopti);
        return total;

    }


}