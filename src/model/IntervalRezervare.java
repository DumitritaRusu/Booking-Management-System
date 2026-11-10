package model;

import java.time.LocalDate;
public class IntervalRezervare {
    private LocalDate dataStart;
    private LocalDate dataSfarsit;

    public IntervalRezervare(LocalDate dataStart, LocalDate dataSfarsit) {
        this.dataStart = dataStart;
        this.dataSfarsit = dataSfarsit;
    }
    public LocalDate getStart() {
        return dataStart;
    }

    public LocalDate getEnd() {
        return dataSfarsit; //
    }
    public boolean seSuprapuneCu(LocalDate startNou, LocalDate sfarsitNou) {
        return ! (sfarsitNou.isBefore(this.dataStart) || startNou.isAfter(this.dataSfarsit));
    }

}
