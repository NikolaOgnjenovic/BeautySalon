package com.mrmi.beautysalon.run;

import com.mrmi.beautysalon.objects.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ConsoleApp {
    private final Scanner scanner = new Scanner(System.in);
    private final Database database = new Database();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public void run() {
        System.out.println("Welcome to Mrmi/'s beauty salon app");

        String loggedOutOptions = "Options:\n0. Exit\n1. Login\n2. Register as a client";
        String clientOptions = "Options:\n0. Exit\n1. Logout\n2. Book treatment\n3. View due treatments\n4. View past treatments\n5. Cancel treatment\n6. View loyalty card status";
        String beauticianOptions = "Options:\n0. Exit\n1. Logout\n2. View due treatments\n3. View past treatments\n4. View schedule";
        String receptionistOptions = "Options:\n0. Exit\n1. Logout\n2. Book treatment\n3. View all treatments\n4. Cancel treatment\n5. Update treatment";
        String managerOptions = "Options:\n0. Exit\n1. Logout\n2. View all employees\n3. View all clients with loyalty cards\n4. View salon income and expenses\n5. Register employee\n6. Add treatment type\n7. Set loyalty card threshhold";
        byte selectedOption;
        boolean running = true;
        while (running) {
            if (Database.CurrentUser == null) {
                System.out.println(loggedOutOptions);
                selectedOption = getMenuOption();

                switch (selectedOption) {
                    case 0 -> running = false;
                    case 1 -> login();
                    case 2 -> registerUser("C");
                    default -> System.out.println("Invalid option.\n");
                }
            } else if (Database.CurrentUser.getClass().equals(Client.class)) {
                System.out.println(clientOptions);
                selectedOption = getMenuOption();

                Client client = (Client) Database.CurrentUser;
                switch (selectedOption) {
                    case 0 -> running = false;
                    case 1 -> logout();
                    case 2 -> bookTreatment(client);
                    case 3 -> printDueTreatments(client);
                    case 4 -> printPastTreatments(client);
                    case 5 -> cancelTreatment(client);
                    case 6 -> viewLoyaltyStatus(client, database);
                    default -> System.out.println("Invalid option.\n");
                }
            } else if (Database.CurrentUser.getClass().equals(Beautician.class)) {
                System.out.println(beauticianOptions);
                selectedOption = getMenuOption();

                Beautician beautician = (Beautician) Database.CurrentUser;
                switch (selectedOption) {
                    case 0 -> running = false;
                    case 1 -> logout();
                    case 2 -> printDueTreatments(beautician);
                    case 3 -> printPastTreatments(beautician);
                    case 4 -> printSchedule(beautician);
                    default -> System.out.println("Invalid option.\n");
                }
            } else if (Database.CurrentUser.getClass().equals(Receptionist.class)) {
                System.out.println(receptionistOptions);
                selectedOption = getMenuOption();

                Receptionist receptionist = (Receptionist) Database.CurrentUser;
                switch (selectedOption) {
                    case 0 -> running = false;
                    case 1 -> logout();
                    case 2 -> bookTreatment(receptionist);
                    case 3 -> printAllTreatments(receptionist);
                    case 4 -> cancelTreatment(receptionist);
                    case 5 -> updateTreatment(receptionist);
                    default -> System.out.println("Invalid option.\n");
                }
            } else if (Database.CurrentUser.getClass().equals(Manager.class)) {
                System.out.println(managerOptions);
                selectedOption = getMenuOption();

                Manager manager = (Manager) Database.CurrentUser;
                switch (selectedOption) {
                    case 0 -> running = false;
                    case 1 -> logout();
                    case 2 -> printEmployees(manager);
                    case 3 -> printLoyalClients(manager);
                    case 4 -> printIncome(manager);
                    case 5 -> registerEmployee();
                    case 6 -> addTreatmentType();
                    case 7 -> setLoyaltyThreshold();
                    default -> System.out.println("Invalid option.\n");
                }
            }
        }
        exit();
    }

    private void exit() {
        scanner.close();
        System.out.println("Thank you for using Mrmi's beauty salon.");
        System.exit(0);
    }

    private byte getMenuOption() {
        try {
            return Byte.parseByte(scanner.nextLine());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    //region Auth options

    private void login() {
        System.out.println("Enter your username:\n");
        String username = scanner.nextLine();
        System.out.println("Enter your password:\n");
        String password = scanner.nextLine();

        database.login(username, password);
    }

    private void logout() {
        database.logout();
    }

    private void registerUser(String userType) {
        System.out.println("Enter your username:\n");
        String username = scanner.nextLine();
        System.out.println("Enter your password:\n");
        String password = scanner.nextLine();
        System.out.println("Enter your name:\n");
        String name = scanner.nextLine();
        System.out.println("Enter your surname:\n");
        String surname = scanner.nextLine();
        System.out.println("Enter your gender (M/F):\n");
        String gender = scanner.nextLine();
        if (!gender.equals("M") && !gender.equals("F")) {
            System.out.println("Invalid gender");
            return;
        }
        System.out.println("Enter your phone number:\n");
        String phoneNumber = scanner.nextLine();
        System.out.println("Enter your address:\n");
        String address = scanner.nextLine();

        User newUser = null;
        switch (userType) {
            case "C" -> newUser = new Client(username, password, name, surname, gender, phoneNumber, address);
            case "B" -> {
                System.out.println("Available treatment types: ");
                for (TreatmentType treatmentType : database.getTreatmentTypes()) {
                    System.out.println(treatmentType);
                }
                System.out.println("Enter treatment type ids separated by commas (0,1,2)");
                String[] types = scanner.nextLine().split(",");

                List<Byte> treatmentTypeIDs = new ArrayList<>();
                for (String type : types) {
                    TreatmentType treatmentType = database.getTreatmentTypeById(Integer.parseInt(type));
                    if (treatmentType == null) {
                        System.out.println("Invalid treatment type id");
                    } else {
                        treatmentTypeIDs.add(Byte.parseByte(type));
                    }
                }

                newUser = new Beautician(username, password, name, surname, gender, phoneNumber, address, treatmentTypeIDs);
                setEmployeeAttributes((Beautician) newUser);
            }
            case "R" -> {
                newUser = new Receptionist(username, password, name, surname, gender, phoneNumber, address);
                setEmployeeAttributes((Receptionist) newUser);
            }
            case "M" -> {
                newUser = new Manager(username, password, name, surname, gender, phoneNumber, address);
                setEmployeeAttributes((Manager) newUser);
            }
        }
        database.addUser(newUser);
    }

    private void setEmployeeAttributes(Employee employee) {
        System.out.println("Enter qualification level");
        byte qualificationLevel = Byte.parseByte(scanner.nextLine());
        System.out.println("Enter years of experience");
        byte yearsOfExperience = Byte.parseByte(scanner.nextLine());
        System.out.println("Enter bonus");
        double bonus = Double.parseDouble(scanner.nextLine());
        System.out.println("Enter monthly salary");
        double monthlySalary = Double.parseDouble(scanner.nextLine());

        employee.setQualificationLevel(qualificationLevel);
        employee.setYearsOfExperience(yearsOfExperience);
        employee.setBonus(bonus);
        employee.setMonthlySalary(monthlySalary);
    }

    private Treatment inputTreatment(String clientUsername) {
        System.out.println("Pick a treatment type");
        byte treatmentTypeId;
        System.out.println("Available treatment types: ");
        for (TreatmentType treatmentType : database.getTreatmentTypes()) {
            System.out.println(treatmentType);
        }
        System.out.println("Enter the new treatment type id");
        treatmentTypeId = Byte.parseByte(scanner.nextLine());

        TreatmentType treatmentType = database.getTreatmentTypeById(treatmentTypeId);
        if (treatmentType == null) {
            System.out.println("Invalid type id");
            return null;
        }

        List<Beautician> beauticians = database.getBeauticiansByTreatmentType(treatmentTypeId);
        if (beauticians.size() < 1) {
            System.out.println("No beauticians available");
            return null;
        }
        System.out.println("Available beauticians:");
        for (Beautician b : beauticians) {
            System.out.println(b);
        }

        System.out.println("Pick a beautician by entering one's username or get a random one by inserting enter");
        String beauticianUsername = scanner.nextLine();
        if (beauticianUsername.length() < 1) {
            beauticianUsername = beauticians.get(0).getUsername();
        }

        /*
        TODO:
        Zatim korisnik bira termin – datum i vreme (od dostupnih termina kada je dostupan
        odabrani kozmetičar, u toku radnog vremena kozmetičkog salona).
        Zbog pojednostavljivanja, smatrati da tretmani počinju uvek na pun sat.
         */
        System.out.println("Enter date in dd.MM.yyyy format");
        Date scheduledDate;
        try {
            scheduledDate = sdf.parse(scanner.nextLine());
        } catch (ParseException e) {
            System.out.println("Invalid date");
            return null;
        }

        return new Treatment(database.getNextTreatmentId(), scheduledDate, false, clientUsername, beauticianUsername, treatmentTypeId, treatmentType.getPrice());
    }
    //endregion

    //region Client options
    private void bookTreatment(Client client) {
        Treatment treatment = inputTreatment(client.getUsername());
        if (treatment != null) {
            client.bookTreatment(treatment, database);
        }
    }

    private void printDueTreatments(Client client) {
        List<Treatment> dueTreatments = client.getDueTreatments(database);
        printDueTreatments(dueTreatments);
    }

    private void printPastTreatments(Client client) {
        List<Treatment> pastTreatments = client.getPastTreatments(database);
        printPastTreatments(pastTreatments);
    }

    private void cancelTreatment(Client client) {
        printDueTreatments(client);
        System.out.println("Enter the id of the treatment that you want to cancel\n");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.println("Why do you want to cancel the treatment?");
        String cancellationReason = scanner.nextLine();
        client.cancelTreatment(id, database, cancellationReason);
    }
    //endregion

    //region Beautician options
    private void printDueTreatments(Beautician beautician) {
        List<Treatment> dueTreatments = beautician.getDueTreatments(database);
        printDueTreatments(dueTreatments);
    }

    private void printPastTreatments(Beautician beautician) {
        List<Treatment> pastTreatments = beautician.getPastTreatments(database);
        printPastTreatments(pastTreatments);
    }

    private void printSchedule(Beautician beautician) {
        List<Treatment> dueTreatments = beautician.getDueTreatments(database);
        if (dueTreatments.size() < 1) {
            System.out.println("You have no due treatments");
            return;
        }
        dueTreatments.sort(Comparator.comparing(Treatment::getScheduledDate));
        System.out.println("Schedule:\n");
        int day = dueTreatments.get(0).getScheduledDate().getDay();
        for (Treatment t : dueTreatments) {
            System.out.println("Day " + day);
            System.out.println(t);
        }
    }

    private void printDueTreatments(List<Treatment> dueTreatments) {
        if (dueTreatments.size() < 1) {
            System.out.println("You have no due treatments.");
            return;
        }
        System.out.println("Due treatments: ");
        for (Treatment t : dueTreatments) {
            System.out.println(t);
        }
    }

    private void printPastTreatments(List<Treatment> pastTreatments) {
        if (pastTreatments.size() < 1) {
            System.out.println("You have no past treatments.");
            return;
        }
        System.out.println("Past treatments: ");
        for (Treatment t : pastTreatments) {
            System.out.println(t);
        }
    }

    private void viewLoyaltyStatus(Client client, Database database) {
        if (client.hasLoyaltyCard()) {
            System.out.println("You have a loyalty card which grants you a 10% discount on all treatments!");
        } else {
            System.out.println("You need to spend " + (database.getLoyaltyThreshold() - client.getMoneySpent()) + " more money in order to get a loyalty card.");
        }
    }
    //endregion

    //region Receptionist options

    private void bookTreatment(Receptionist receptionist) {
        System.out.println("Enter client username");
        String clientUsername = scanner.nextLine();
        if (!database.clientExists(clientUsername)) {
            registerUser("C");
        }

        Treatment treatment = inputTreatment(clientUsername);
        if (treatment != null) {
            receptionist.bookTreatment(treatment, database.getClientByUsername(clientUsername), database);
        }
    }

    private void printAllTreatments(Receptionist receptionist) {
        List<Treatment> treatments = receptionist.getAllTreatments();
        if (treatments.size() < 1) {
            System.out.println("You have no treatments.");
            return;
        }
        System.out.println("All treatments: ");
        for (Treatment t : treatments) {
            System.out.println(t);
        }
    }

    private void cancelTreatment(Receptionist receptionist) {
        printAllTreatments(receptionist);
        System.out.println("Enter the id of the treatment that the client wants to cancel\n");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.println("Why does the client want to cancel the treatment?");
        String cancellationReason = scanner.nextLine();
        receptionist.cancelTreatment(id, database, cancellationReason);
    }

    private void updateTreatment(Receptionist receptionist) {
        printAllTreatments(receptionist);
        System.out.println("Enter the id of the treatment that you want to update\n");
        int treatmentId = Integer.parseInt(scanner.nextLine());

        Treatment treatment = database.getTreatmentById(treatmentId);

        Date date = treatment.getScheduledDate();
        System.out.println("If you want to change the treatment/'s date enter yes");
        if (scanner.nextLine().equals("yes")) {
            System.out.println("Enter the treatment date in the dd.MM.yyyy format");
            try {
                date = sdf.parse(scanner.nextLine());
            } catch (ParseException pe) {
                System.out.println("Bad date format");
            }
        }

        int treatmentTypeId = treatment.getTreatmentTypeId();
        System.out.println("If you want to change the treatment/'s type enter yes");
        if (scanner.nextLine().equals("yes")) {
            System.out.println("Available types: ");
            for (TreatmentType treatmentType : database.getTreatmentTypes()) {
                System.out.println(treatmentType);
            }
            System.out.println("Enter the new treatment type id");
            int newTreatmentTypeId = Integer.parseInt(scanner.nextLine());

            if (database.getTreatmentTypeById(newTreatmentTypeId) == null) {
                System.out.println("Bad treatment type id");
            } else {
                treatmentTypeId = newTreatmentTypeId;
            }
        }

        // TODO: verification
        String clientUsername = treatment.getClientUsername();
        System.out.println("If you want to change the treatment/'s client username enter yes");
        if (scanner.nextLine().equals("yes")) {
            System.out.println("Enter the new client username");
            clientUsername = scanner.nextLine();
        }

        // TODO: verification
        String beauticianUsername = treatment.getBeauticianUsername();
        System.out.println("If you want to change the treatment/'s beautician username enter yes");
        if (scanner.nextLine().equals("yes")) {
            System.out.println("Enter the new beautician username");
            beauticianUsername = scanner.nextLine();
        }

        receptionist.updateTreatment(treatment, date, treatmentTypeId, clientUsername, beauticianUsername, database);
    }
    //endregion

    //region Manager options
    private void printEmployees(Manager manager) {
        System.out.println("Employees:");
        List<Employee> employees = manager.getEmployees(database);
        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    private void printLoyalClients(Manager manager) {
        System.out.println("Clients:");
        List<Client> clients = manager.getLoyalClients(database);
        for (Client c : clients) {
            System.out.println(c);
        }
    }

    private void printIncome(Manager manager) {
        System.out.println("Total income: " + manager.getIncome(new Date(), new Date()));
    }

    private void registerEmployee() {
        System.out.println("Do you want to register a manager [M], beautician [B] or receptionist [R]?");
        String choice = scanner.nextLine();
        switch (choice) {
            case "B" -> registerUser("B");
            case "R" -> registerUser("R");
            case "M" -> registerUser("M");
            default -> System.out.println("Invalid choice");
        }
    }

    private void addTreatmentType() {
        System.out.println("Enter the treatment type name");
        String name = scanner.nextLine();
        System.out.println("Enter the treatment type price");
        int price = Integer.parseInt(scanner.nextLine());
        database.addTreatmentType(new TreatmentType(name, price, database.getNextTreatmentTypeId()));
    }

    private void setLoyaltyThreshold() {
        System.out.println("Enter the new loyalty card threshold");
        database.setLoyaltyThreshold(Double.parseDouble(scanner.nextLine()));
    }

    /*
    Potrebno je uraditi izveštaje:
        ● koliko je kozmetičkih tretmana svaki kozmetičar izvršio i koliko je prihodovao za
        izabrani opseg datuma,
        ● koliko kozmetičkih tretmana je potvrđeno, a koliko otkazano (po razlozima) za
        odabrani opseg datuma
        ● za prikaz kozmetičke usluge, što podrazumeva prikaz podataka o samoj usluzi i
        njenom tipu, ukupan broj zakaznih tretmana za tu uslugu i ostvarene prihode za
        izabrani opseg datuma.
        ●
        Klijenata koji ispunjavaju uslove za karticu lojalnosti (potrošili su na tretmane više
        novca od iznosa koji zadaje menadžer).
     */
    //endregion
}
