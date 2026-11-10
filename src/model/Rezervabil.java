package model;


public interface Rezervabil {
    boolean verificaDisponibilitate(String dataSosire ,String dataPlecare)
        throws RezervareInvalida;

    double calculeazaPretTotal(int numarNopti, boolean doresteParcare, boolean doresteMicDejun) ;

}
