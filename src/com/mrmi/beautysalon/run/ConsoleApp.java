package com.mrmi.beautysalon.run;

import com.mrmi.beautysalon.objects.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private final Scanner scanner = new Scanner(System.in);
    private final Database database = new Database();

    public void run() {
        System.out.println("Welcome to Mrmi/'s beauty salon app");

        String loggedOutOptions = "Options:\n0. Exit\n1. Login\n2. Register as a client";
        String clientOptions = "Options:\n0. Exit\n1. Logout\n2. Book treatment\n3. View due treatments\n4. View past treatments\n5. Cancel treatment";
        String receptionistOptions = "Options:\n0. Exit\n1. Logout\n2. Book treatment\n3. View all treatments\n3. Cancel treatment\n5. Update treatment";
        String managerOptions = "Options:\n0. Exit\n1. Logout\n2. View all employees\n3. View all clients with loyalty cards\n4. View salon income and expenses";
        String beauticianOptions = "Options:\n0. Exit\n1. Logout\n2. View due treatments\n3. View past treatments\n4. View schedule";
        byte selectedOption;
        boolean running = true;
        while (running) {
            if (Database.CurrentUser == null) {
                System.out.println(loggedOutOptions);
                selectedOption = getMenuOption();

                switch (selectedOption) {
                    case 0 -> running = false;
                    case 1 -> login();
                    case 2 -> registerClient();
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

    private void registerClient() {
        System.out.println("Enter your username:\n");
        String username = scanner.nextLine();
        System.out.println("Enter your password:\n");
        String password = scanner.nextLine();
        System.out.println("Enter your name:\n");
        String name = scanner.nextLine();
        System.out.println("Enter your surname:\n");
        String surname = scanner.nextLine();
        System.out.println("Enter your gender (M/F):\n");
        String genderInput = scanner.nextLine();
        boolean isMale;
        if (genderInput.equals("M")) {
            isMale = true;
        } else if (genderInput.equals("F")) {
            isMale = false;
        } else {
            return;
        }
        System.out.println("Enter your phone number:\n");
        String phoneNumber = scanner.nextLine();
        System.out.println("Enter your address:\n");
        String address = scanner.nextLine();

        database.addUser(new Client(username, password, name, surname, isMale, phoneNumber, address));
    }
    //endregion

    //region Client options
    private void bookTreatment(Client client) {
        Treatment treatment = new Treatment(new Date(), new TreatmentType("Massage", 100), client.getUsername());
        client.bookTreatment(treatment);
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
        client.cancelTreatment(id, database);
    }
    //endregion

    //region Receptionist options

    private void bookTreatment(Receptionist receptionist) {
        String clientUsername = scanner.nextLine();
        if (!database.clientExists(clientUsername)) {
            registerClient();
        }
        Treatment treatment = new Treatment(new Date(), new TreatmentType("Massage", 100), receptionist.getUsername());
        receptionist.bookTreatment(treatment, database.getClientByUsername(clientUsername));
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
        System.out.println("Enter the id of the treatment that you want to cancel\n");
        int id = Integer.parseInt(scanner.nextLine());
        receptionist.cancelTreatment(id, database);
    }

    private void updateTreatment(Receptionist receptionist) {
        printAllTreatments(receptionist);
        System.out.println("Enter the id of the treatment that you want to update\n");
        int treatmentId = Integer.parseInt(scanner.nextLine());

        Treatment treatment = database.getTreatmentById(treatmentId);

        Date date = treatment.getScheduledDate();
        System.out.println("If you want to change the treatment/'s date enter yes");
        if (scanner.nextLine().equals("yes")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            System.out.println("Enter the treatment date in the dd.MM.yyyy format");
            try {
                date = sdf.parse(scanner.nextLine());
            } catch (ParseException pe) {
                System.out.println("Bad date format");
            }
        }

        TreatmentType type = treatment.getType();
        System.out.println("If you want to change the treatment/'s type enter yes");
        if (scanner.nextLine().equals("yes")) {
            System.out.println("Available types: ");
            for (TreatmentType treatmentType : database.getTreatmentTypes()) {
                System.out.println(treatmentType);
            }
            System.out.println("Enter the new treatment type id");
            int treatmentTypeId = Integer.parseInt(scanner.nextLine());

            TreatmentType newType = database.getTreatmentTypeById(treatmentTypeId);
            if (newType == null ) {
                System.out.println("Bad treatment type id");
            } else {
                type = newType;
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

        receptionist.updateTreatment(treatment, date, type, clientUsername, beauticianUsername, database);
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
    //endregion
}
