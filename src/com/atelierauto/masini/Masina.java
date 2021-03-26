package com.atelierauto.masini;

import java.time.DateTimeException;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public abstract class Masina implements Delayed {

    protected static int ID = 0;
    private long kilometri;
    private Year anulFabricatiei;
    protected boolean esteDiesel;
    private long delayTime;

    public Masina(long kilometri, Year anulFabricatiei, boolean esteDiesel) {
        this.kilometri = kilometri;
        this.anulFabricatiei = anulFabricatiei;
        this.esteDiesel = esteDiesel;
        this.delayTime = System.currentTimeMillis() + 30*1000;
    }

    protected abstract void setID();

    public static Year fromIntegerToYear(int yearString){
        Year year;
        try {
            year = Year.of((yearString));
        }catch (DateTimeException e){
            System.out.println("Formatul e invalid.");
            return null;
        }
        return year;
    }

    public long getKilometri() {
        return kilometri;
    }

    public void setKilometri(long kilometri) {
        this.kilometri = kilometri;
    }

    public Year getAnulFabricatiei() {
        return anulFabricatiei;
    }

    public void setAnulFabricatiei(Year anulFabricatiei) {
        this.anulFabricatiei = anulFabricatiei;
    }

    public boolean isEsteDiesel() {
        return esteDiesel;
    }

    public void setEsteDiesel(boolean esteDiesel) {
        this.esteDiesel = esteDiesel;
    }

    public abstract double calculeazaAsigurare(boolean discount);

    @Override
    public String toString() {
        return "Masina:" +
                "kilometri=" + kilometri +
                ", anulFabricatiei=" + anulFabricatiei +
                ", esteDiesel=" + esteDiesel +
                ' ';
    }

    @Override
    public long getDelay(TimeUnit timeUnit) {
        long difference = delayTime - System.currentTimeMillis();
        return timeUnit.convert(difference, TimeUnit.SECONDS);
    }

    @Override
    public int compareTo(Delayed delayed) {
        Masina anotherTask = (Masina) delayed;

        if (this.delayTime < anotherTask.delayTime) {
            return -1;
        }

        if (this.delayTime > anotherTask.delayTime) {
            return 1;
        }

        return 0;
    }
}
