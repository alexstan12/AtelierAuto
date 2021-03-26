package com.atelierauto.masini;

import java.time.LocalDate;
import java.time.Period;
import java.time.Year;

public class Standard extends Masina {

    public enum TipTransmisie {
        MANUAL,
        AUTOMAT
    }

    private int ID;
    private TipTransmisie transmisie;

    @Override
    protected void setID() {
        this.ID = Masina.ID++;
    }

    @Override
    public double calculeazaAsigurare(boolean discount) {
        double costAsigurare = 0;
        int vechime = this.getAnulFabricatiei().minusYears(Year.now().getValue()).getValue();
        if(this.esteDiesel) costAsigurare+= 500;
        if(this.getKilometri() >= 200000) costAsigurare+= 500;
        costAsigurare+= vechime * 100;
        if(discount) costAsigurare*= 95/100;
        return costAsigurare;
    }

    public Standard(long kilometri, Year anulFabricatiei, boolean esteDiesel, TipTransmisie transmisie) {
        super(kilometri, anulFabricatiei, esteDiesel);
        setTransmisie(transmisie);
        setID();
    }

    public void setTransmisie(TipTransmisie transmisie) {
        if (transmisie != TipTransmisie.MANUAL && transmisie != TipTransmisie.AUTOMAT) {
            System.out.println("Tipul transmisiei este invalid");
        } else {
            this.transmisie = transmisie;
        }

    }

    public TipTransmisie getTransmisie() {
        return transmisie;
    }

    @Override
    public String toString() {
        return super.toString() +
                "ID=" + ID +
                ", transmisie=" + transmisie +
                '}';
    }
}
