package com.atelierauto.main;

import com.atelierauto.angajati.Angajat;
import com.atelierauto.angajati.Asistent;
import com.atelierauto.angajati.Director;
import com.atelierauto.angajati.Mecanic;
import com.atelierauto.masini.Autobuz;
import com.atelierauto.masini.Camion;
import com.atelierauto.masini.Masina;
import com.atelierauto.masini.Standard;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String option;

        List<Angajat> angajati = new ArrayList<>();
        List<Masina> masini = new ArrayList<>();
        HashMap<Integer, DelayQueue<Masina>> listaReparatii = new HashMap<>();

        String meniu = "", line = null;
        String meniuService = "";
        try (BufferedReader reader = new BufferedReader(new FileReader("src/meniu.txt"));
             BufferedReader reader1 = new BufferedReader(
                     new FileReader("src/meniuIntroducereService.txt"))) {
            do {
                while ((line = reader.readLine()) != null) {
                    meniu += line + "\n";
                }
                clearScreen();
                System.out.println(meniu);
                option = scanner.next();
                Angajat angajat;
                int ID;
                switch (option) {
                    case "0":
                        angajat = generareAngajat();
                        if (angajat != null) {
                            angajati.add(angajat);
                            System.out.println("Angajatul a fost adaugat.");
                        }
                        break;
                    case "1":
                        System.out.println("Afisare angajati: ");
                        for (int i = 0; i < angajati.size(); i++) {
                            System.out.println(angajati.get(i).toString());
                        }
                        break;
                    case "2":
                        System.out.println("Stergere angajat: ");
                        ID = verificareID(angajati);
                        if (ID != -1) {
                            ListIterator iterator = angajati.listIterator();
                            while (iterator.hasNext()) {
                                angajat = (Angajat) iterator.next();
                                if (angajat.getID() == ID) {
                                    iterator.remove();
                                    System.out.println("Angajat sters din registru.");
                                }
                            }
                        }
                        break;

                    case "3":
                        System.out.println("Editare angajat: ");
                        editareAngajat(angajati);
                        break;

                    case "4":
                        System.out.println("Calcul salariu angajat: ");
                        ID = verificareID(angajati);
                        if (ID != -1) {
                            for (int i = 0; i < angajati.size(); i++) {
                                if (angajati.get(i).getID() == ID) {
                                    double salariu = Period.between(angajati.get(i).getDataAngajarii(), LocalDate.now()).getYears()
                                            * angajati.get(i).getCoeficientSalarial() * 1000;
                                    System.out.println("Salariul angajatului este: " + salariu);
                                }
                            }
                        }
                        break;

                    case "5":
                        System.out.println("Introducerea masinii in service: ");
                        if (!angajati.isEmpty()) {
                            Masina masina;
                            //String optiune;
                            do {
                                while ((line = reader1.readLine()) != null) {
                                    meniuService += line + "\n";
                                }
                                clearScreen();
                                System.out.println("Programarile actuale din service sunt:");
                                for (int i = 0; i < listaReparatii.size(); i++) {
                                    System.out.println("Angajat " + i + " ");
                                    for (Masina m : listaReparatii.get(i))
                                        System.out.println(m.toString());
                                }
                                System.out.println(meniuService);
                                System.out.println("Doriti sa fiti programat la un anumit mecanic?");
                                option = scanner.next().toLowerCase();
                                masina = generareMasina();
                                if (option.equals("da")) {
                                    System.out.println("Introdu ID-ul angajatului dorit: ");
                                    ID = verificareID(angajati);
                                    if (ID == -1) {
                                        System.out.println("Angajatul cu ID-ul ales nu exista");
                                    } else {
                                        if (masina != null) {
                                            if (verificareDisponibilitateAngajat(listaReparatii, ID, masina) == true) {
                                                if (listaReparatii.get(ID) == null) {
                                                    listaReparatii.put(ID, new DelayQueue<Masina>());
                                                    listaReparatii.get(ID).put(masina);
                                                    System.out.println("Masina a intrat in procesul de reparare!");
                                                } else {
                                                    listaReparatii.get(ID).put(masina);
                                                    System.out.println("Masina a intrat in procesul de reparare!");
                                                }
                                            } else {
                                                if (listaReparatii.get(ID) == null) {
                                                    listaReparatii.put(ID, new DelayQueue<Masina>());
                                                    listaReparatii.get(ID).put(masina);
                                                    System.out.println("Masina a intrat in procesul de reparare!");
                                                } else {
                                                    System.out.println("Doriti sa asteptati la randul acestui angajat ?");
                                                    option = scanner.next().toLowerCase();
                                                    if (option.equals("da")) {
                                                        listaReparatii.get(ID).offer(masina, listaReparatii.get(ID).peek()
                                                                .getDelay(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
                                                        System.out.println("Masina a intrat in linia de asteptare!");
                                                    }
                                                }
                                            }
                                        } else System.out.println("Masina nu are format valid");
                                    }
                                } else {
                                    if (masina != null) {
                                        //int lungimeaCoziiDeAsteptare = 0;
                                        int IDLiber = listaReparatii.keySet().stream()
                                                .filter(angajatID -> listaReparatii.get(angajatID) == null)
                                                .findFirst().orElseGet(() -> -1);
                                        if (IDLiber != -1) {
                                            listaReparatii.put(IDLiber, new DelayQueue<Masina>());
                                            listaReparatii.get(IDLiber).put(masina);
                                            System.out.println("Masina a intrat in procesul de reparare!");

                                        } else {
                                            Random r = new Random();
                                            int randomAngajatID = r.nextInt(angajati.size());
                                            if (listaReparatii.get(randomAngajatID) == null) {
                                                listaReparatii.put(randomAngajatID, new DelayQueue<Masina>());
                                                listaReparatii.get(randomAngajatID).put(masina);
                                                System.out.println("Masina a intrat in procesul de reparare!");
                                            } else {
                                                if (verificareDisponibilitateAngajat(listaReparatii, randomAngajatID, masina)) {
                                                    listaReparatii.get(randomAngajatID).put(masina);
                                                    System.out.println("Masina a intrat in procesul de reparare!");
                                                } else {
                                                    System.out.println("Angajatii sunt ocupati. Doriti sa asteptati eliberarea unui loc" +
                                                            "sau parasiti atelierul? ( raman|plec)");
                                                    option = scanner.next().toLowerCase();
                                                    if (option.equals("raman")) {
                                                        listaReparatii.get(randomAngajatID).offer(masina, listaReparatii.get(randomAngajatID).peek()
                                                                .getDelay(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
                                                        System.out.println("Masina a intrat in linia de asteptare!");
                                                    } else {
                                                        System.out.println("La revedere !");
                                                    }
                                                }
                                            }
                                        }
                                    } else System.out.println("Masina nu are format valid");
                                }
                                System.out.println("-1 pentru a parasi sectiunea.");
                                option = scanner.next();
                            } while (Integer.parseInt(option) != -1);
                        } else {
                            System.out.println("Ne pare rau, atelierul este inchis.");
                        }
                        break;
                    default:
                        System.out.print("Optiunea introdusa nu este valida. Reintrodu:");
                        break;
                }

                Thread.sleep(1000);

            } while (option != "-1");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Nu s-a putut deschide meniul !");
            return;
        }
    }

    private static boolean verificareDisponibilitateAngajat(HashMap<Integer, DelayQueue<Masina>> listaReparatii, int ID, Masina masina) {
        if (listaReparatii.get(ID) != null) {
            int numarMasiniStandard = 0;
            int numarAutobuze = 0;
            int numarCamioane = 0;
            for (Masina m : listaReparatii.get(ID)) {
                if (m.getClass().getSimpleName().equals("Autobuz")) numarAutobuze++;
                if (m.getClass().getSimpleName().equals("Camion")) numarCamioane++;
                if (m.getClass().getSimpleName().equals("Standard")) numarMasiniStandard++;
            }
            System.out.println("tip masina: " + masina.getClass().getSimpleName());
            if (masina != null) {
                switch (masina.getClass().getSimpleName()) {
                    case "Autobuz":
                        if (numarAutobuze < 1) {
                                /*listaReparatii.get(ID).put(masina);
                                System.out.println("Masina a intrat in procesul de reparare!");*/
                            return true;
                            //}else{
                                /*System.out.println("Angajatul cu ID " + ID + " are agenda plina.");
                                listaReparatii.get(ID).offer(masina, listaReparatii.get(ID).peek()
                                        .getDelay(TimeUnit.MILLISECONDS),TimeUnit.MILLISECONDS);*/

                        }
                        break;
                    case "Camion":
                        if (numarCamioane < 1) {/*
                                listaReparatii.get(ID).put(masina);
                                System.out.println("Masina a intrat in procesul de reparare!");
                                */
                            return true;
                            //}else{
                                /*
                                System.out.println("Angajatul cu ID " + ID + " are agenda plina.");
                                listaReparatii.get(ID).offer(masina, listaReparatii.get(ID).peek()
                                        .getDelay(TimeUnit.MILLISECONDS),TimeUnit.MILLISECONDS);*/

                        }
                        break;
                    case "Standard":
                        if (numarMasiniStandard < 3) {/*
                                listaReparatii.get(ID).put(masina);
                                System.out.println("Masina a intrat in procesul de reparare!");*/
                            return true;
                            /*}else{
                                System.out.println("Angajatul cu ID " + ID + " are agenda plina.");
                                listaReparatii.get(ID).offer(masina, listaReparatii.get(ID).peek()
                                        .getDelay(TimeUnit.MILLISECONDS),TimeUnit.MILLISECONDS);*/

                        }
                        break;
                    default:
                        System.out.println("Angajatul este ocupat.");
                        break;
                }
            }

        }
        return false;
    }

    private static void editareAngajat(List<Angajat> angajati) {
        int ID;
        ID = verificareID(angajati);
        if (ID != -1) {
            for (int i = 0; i < angajati.size(); i++) {
                if (angajati.get(i).getID() == ID) {
                    String prenume;
                    String dataAngajariiString;
                    String nume;
                    String dataNasteriiString;
                    System.out.println("Care este functia ocupata? ");
                    String functieOcupata = scanner.next().toLowerCase();
                    System.out.print("Numele angajatului: ");
                    nume = scanner.next();
                    System.out.print("Prenumele angajatului: ");
                    prenume = scanner.next();
                    System.out.print("Data nasterii angajatului(zi.luna.an) : ");
                    dataNasteriiString = scanner.next();
                    System.out.print("Data angajarii(zi.luna.an) : ");
                    dataAngajariiString = scanner.next();
                    if (validareDateAngajat(nume, prenume, dataNasteriiString, dataAngajariiString)) {
                        angajati.get(i).setNume(nume);
                        angajati.get(i).setPrenume(prenume);
                        angajati.get(i).setDataNasterii(dataNasteriiString);
                        angajati.get(i).setDataAngajarii(dataAngajariiString);
                        System.out.println("Datele angajatului au fost editate");
                    }
                    break;
                }
            }
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static int verificareID(List<Angajat> angajati) {
        System.out.print("Introdu ID-ul angajatului: ");
        int ID = scanner.nextInt();
        boolean foundID = false;
        while (!foundID) {
            for (int i = 0; i < angajati.size(); i++) {
                if (angajati.get(i).getID() == ID) {
                    foundID = true;
                    break;
                }
            }
            if (foundID == false) {
                System.out.println("ID-ul angajatului nu a putut fi gasit. Te rog reintrodu-l !");
                System.out.print("Introdu ID-ul angajatului sau -1 pentru a parasi cautarea: ");
                ID = scanner.nextInt();
                if (ID == -1) break;
            }
        }
        return ID;
    }

    private static Masina generareMasina() {
        Masina masina = null;
        System.out.println("Alege tipul masinii pe care doresti sa o introduci (Standard|Autobuz|Camion)");
        String tipMasina = scanner.next().toLowerCase();
        System.out.println("Cati km are?");
        long km = scanner.nextLong();
        System.out.println("Care e anul fabricatiei ?");
        Year anulFabricatiei = Masina.fromIntegerToYear(scanner.nextInt());
        System.out.println("Este diesel?");
        boolean esteDiesel;
        String esteDieselString = scanner.next().toLowerCase();
        while (!esteDieselString.equals("da") && !esteDieselString.equals("nu")) {
            System.out.println("Valoarea introdusa este incorecta. Introdu DA sau NU .");
            esteDieselString = scanner.next().toLowerCase();
        }
        if (esteDieselString.equals("da")) {
            esteDiesel = true;
        } else {
            esteDiesel = false;
        }
        if (anulFabricatiei != null && anulFabricatiei.isBefore(Year.now())) {
            switch (tipMasina) {
                case "standard":
                    System.out.println("Alege tipul transmisiei: " + Standard.TipTransmisie.MANUAL.toString() +
                            " sau " + Standard.TipTransmisie.AUTOMAT.toString());
                    String tipTransmisie = scanner.next().toUpperCase();
                    while (!tipTransmisie.equals("AUTOMAT") && !tipTransmisie.equals("MANUAL")) {
                        System.out.println("Valoarea introdusa este incorecta. Introdu AUTOMAT sau MANUAL .");
                        tipTransmisie = scanner.next().toUpperCase();
                    }
                    masina = new Standard(km, anulFabricatiei, esteDiesel, Standard.TipTransmisie.valueOf(tipTransmisie));
                    break;
                case "autobuz":
                    System.out.println("Introdu numarul de locuri al autobuzului: ");
                    Integer numarLocuri = scanner.nextInt();
                    masina = new Autobuz(km, anulFabricatiei, esteDiesel, numarLocuri);
                    break;
                case "camion":
                    System.out.println("Introdu tonajul camionului: ");
                    double tonaj = scanner.nextDouble();
                    masina = new Camion(km, anulFabricatiei, esteDiesel, tonaj);
                    break;
                default:
                    System.out.println("Tipul de masina ales nu este unul valid.");
                    break;
            }
        }
        return masina;
    }


    private static Angajat generareAngajat() {
        String prenume;
        String dataAngajariiString;
        String nume;
        String dataNasteriiString;
        System.out.println("Adaugare angajat: ");
        System.out.println("Care este functia ocupata? (mecanic|director|asistent) ");
        String functieOcupata = scanner.next().toLowerCase();
        System.out.print("Numele angajatului: ");
        nume = scanner.next();
        System.out.print("Prenumele angajatului: ");
        prenume = scanner.next();
        System.out.print("Data nasterii angajatului(zi.luna.an) : ");
        dataNasteriiString = scanner.next();
        System.out.print("Data angajarii(zi.luna.an) : ");
        dataAngajariiString = scanner.next();

        Angajat angajat = null;
        switch (functieOcupata) {
            case ("director"):
                if (validareDateAngajat(nume, prenume, dataNasteriiString, dataAngajariiString))
                    angajat = new Director(nume, prenume, dataNasteriiString, dataAngajariiString);
                break;
            case ("mecanic"):
                if (validareDateAngajat(nume, prenume, dataNasteriiString, dataAngajariiString))
                    angajat = new Mecanic(nume, prenume, dataAngajariiString, dataAngajariiString);
                break;
            case ("asistent"):
                if (validareDateAngajat(nume, prenume, dataNasteriiString, dataAngajariiString))
                    angajat = new Asistent(nume, prenume, dataNasteriiString, dataAngajariiString);
                break;
            default:
                System.out.println("Nu este o functie valida !");
                break;
        }
        return angajat;
    }

    public static boolean validareDateAngajat(String nume, String prenume, String dataNasteriiString, String dataAngajariiString) {
        if (nume != null && prenume != null && nume.length() <= 30 && prenume.length() <= 30
                && dataNasteriiString != null && dataAngajariiString != null) {
            LocalDate dataNasterii = Angajat.fromStringToLocalDate(dataNasteriiString);
            LocalDate dataAngajarii = Angajat.fromStringToLocalDate(dataAngajariiString);
            if (dataAngajarii == null && dataNasterii == null) {
                System.out.println("Data nasterii / Data angajarii au format incorect !");
                return false;
            }
            if (Math.abs(Period.between(dataAngajarii, dataNasterii).getYears()) >= 18 &&
                    dataAngajarii.isAfter(dataNasterii) && dataAngajarii.isBefore(LocalDate.now())) {
                System.out.println("Datele introduse sunt corecte !");
                return true;
            }
        }
        System.out.println("Datele introduse nu sunt corecte.");
        return false;

    }
}
