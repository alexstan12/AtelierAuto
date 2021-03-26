package com.atelierauto.angajati;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public abstract class Angajat {
    protected static int ID = 0;

    private String nume = "";
    private String prenume= "";
    private LocalDate dataNasterii;
    private LocalDate dataAngajarii;

    public Angajat(String nume, String prenume, String dataNasteriiString, String dataAngajariiString) {
            this.nume = nume;
            this.prenume = prenume;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            dataNasterii = fromStringToLocalDate(dataNasteriiString);
            dataAngajarii = fromStringToLocalDate(dataAngajariiString);

    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    @Override
    public String toString() {
        return "Angajat " +
                ": nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' + ", ";
    }

    public int getID() {
        return ID;
    }

    public LocalDate getDataAngajarii(){
        return this.dataAngajarii;
    }

    public abstract double getCoeficientSalarial();
    protected abstract void setID();
    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public void setDataNasterii(String dataNasterii) {
        LocalDate resultDate = fromStringToLocalDate(dataNasterii);
        if(resultDate!=null){
            this.dataNasterii = resultDate;

        }
    }

    public void setDataAngajarii(String dataAngajarii) {
        LocalDate resultDate = fromStringToLocalDate(dataAngajarii);
        if(resultDate!=null){
            this.dataAngajarii = resultDate;

        }
    }

    public static LocalDate fromStringToLocalDate(String dataString){
        LocalDate data;
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            data = dateFormatter.parse(dataString, LocalDate::from);
        } catch (DateTimeParseException exception) {
            System.out.println("Data nasterii / Data angajarii au format incorect !");
            return null;
        }
        return data;
    }


}
