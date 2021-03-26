package com.atelierauto.angajati;

public class Director extends Angajat{

    private int ID;
    private final double coeficientSalarial = 2.0;

    public Director(String nume, String prenume, String dataNasteriiString, String dataAngajariiString) {
        super(nume, prenume, dataNasteriiString, dataAngajariiString);
        setID();
    }

    protected void setID() {
        this.ID = Angajat.ID++;
    }

    public int getID() {
        return this.ID;
    }

    @Override
    public String toString() {
        return  super.toString() +" ID: "+ ID ;
    }

    public double getCoeficientSalarial() {
        return coeficientSalarial;
    }
}
