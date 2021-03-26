package com.atelierauto.masini;

import java.time.Year;

public class Camion extends Masina{

    private double tonaj;
    private int ID;

    public Camion(long kilometri, Year anulFabricatiei, boolean esteDiesel, double tonaj) {
        super(kilometri, anulFabricatiei, esteDiesel);
        this.tonaj = tonaj;
        setID();
    }

    public double getTonaj() {
        return tonaj;
    }

    public void setTonaj(double tonaj) {
        this.tonaj = tonaj;
    }

    @Override
    protected void setID() {
        this.ID = Masina.ID++;
    }

    @Override
    public double calculeazaAsigurare(boolean discount) {
        double costAsigurare = 0;
        int vechime = this.getAnulFabricatiei().minusYears(Year.now().getValue()).getValue();
        if(this.getKilometri() >= 800000) costAsigurare+= 700;
        costAsigurare+= vechime * 300;
        if(discount) costAsigurare*= 85/100;
        return costAsigurare;
    }

    @Override
    public String toString() {
        return super.toString() +
                "tonaj=" + tonaj +
                ", ID=" + ID +
                ' ';
    }
}
