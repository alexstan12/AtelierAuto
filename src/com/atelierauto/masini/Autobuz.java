package com.atelierauto.masini;

import java.time.Year;

public class Autobuz extends Masina{

    private int ID;
    private int numarLocuri;

    public Autobuz(long kilometri, Year anulFabricatiei, boolean esteDiesel, int numarLocuri) {
        super(kilometri, anulFabricatiei, esteDiesel);
        this.numarLocuri = numarLocuri;
        setID();
    }

    public int getNumarLocuri() {
        return numarLocuri;
    }

    public void setNumarLocuri(int numarLocuri) {
        this.numarLocuri = numarLocuri;
    }

    @Override
    protected void setID() {
        this.ID = Masina.ID++;
    }

    @Override
    public double calculeazaAsigurare(boolean discount) {
        double costAsigurare = 0;
        int vechime = this.getAnulFabricatiei().minusYears(Year.now().getValue()).getValue();
        if(this.esteDiesel) costAsigurare+= 1000;
        if(this.getKilometri() >=100000) {
            if (this.getKilometri() >= 200000){
                costAsigurare += 500;
            }else{
                costAsigurare+=1000;
            }
        }
        costAsigurare+= vechime * 200;
        if(discount) costAsigurare*= 90/100;
        return costAsigurare;
    }

    @Override
    public String toString() {
        return super.toString() +
                "ID=" + ID +
                ", numarLocuri=" + numarLocuri +
                '}';
    }
}
