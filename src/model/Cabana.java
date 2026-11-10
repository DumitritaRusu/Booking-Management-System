package model;

public class Cabana extends Proprietate{
    private boolean arePiscina;
    private double taxaPiscina;
    public Cabana(int id, String nume, String adresa, double pretNoapte, double rating,
                boolean areParcare, boolean areMicDejun, double pretMicDejun, double pretParcare,
                int nrCamere, boolean areBucatarie, boolean arePiscina, double taxaPiscina) {


        super(id, nume, adresa, pretNoapte, rating, areParcare, areMicDejun, pretMicDejun, pretParcare, nrCamere, areBucatarie);


        this.arePiscina = arePiscina;


        if (this.arePiscina) {
            this.taxaPiscina = taxaPiscina;
        } else {
            this.taxaPiscina = 0;
        }
    }
    public boolean isArePiscina() { return arePiscina; }
    public double getTaxaPiscina() { return taxaPiscina; }

    @Override
    public double calculeazaPretTotal(int numarNopti, boolean doresteParcare, boolean doresteMicDejun) {
        double total = this.pretNoapte * numarNopti;
        if (doresteParcare && this.areParcare) total += this.pretParcare;
        if (doresteMicDejun && this.areMicDejun) total += (this.pretMicDejun * numarNopti);
        return total + this.taxaPiscina;
    }
}