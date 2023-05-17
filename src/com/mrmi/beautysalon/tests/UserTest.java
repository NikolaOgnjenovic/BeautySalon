package com.mrmi.beautysalon.tests;

import com.mrmi.beautysalon.main.entity.*;
import com.mrmi.beautysalon.main.manager.SalonManager;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.exceptions.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private UserManager userManager;
    private TreatmentManager treatmentManager;

    @BeforeEach
    public void setup() {
        Database database = new Database("test");
        SalonManager salonManager = new SalonManager(database);

        userManager = new UserManager(database, salonManager);
        treatmentManager = new TreatmentManager(database, salonManager);

        userManager.addUser(new Client("Client", "password", "Client", "username", "M", "123456", "Address 1"));
        userManager.addUser(new Manager("Manager", "password", "Manager", "username", "M", "123456", "Address 2", (byte) 6, (byte) 5, 90000));
        userManager.addUser(new Beautician("Beautician", "password", "Beautician", "username", "M", "123456", "Address 3", (byte) 6, (byte) 5, 90000));
        userManager.addUser(new Receptionist("Receptionist", "password", "Receptionist", "username", "M", "123456", "Address 4", (byte) 6, (byte) 5, 90000));
    }

    private boolean deleteFile(File file) {
        File[] allContents = file.listFiles();
        if (allContents != null) {
            for (File child : allContents) {
                deleteFile(child);
            }
        }
        return file.delete();
    }

    @AfterEach()
    public void teardown() {
        File testData = new File("testdata");
        assertTrue(deleteFile(testData));
    }

    @Test
    public void testAddUser() {
        assertEquals(userManager.getUsers().size(), 4);
    }

    @Test
    public void testRemoveUser() throws UserNotFoundException {
        assertThrows(UserNotFoundException.class, () -> userManager.deleteUser(-1));
        assertEquals(4, userManager.getUsers().size());
        userManager.deleteUser(0);
        assertEquals(3, userManager.getUsers().size());
        userManager.deleteUser(1);
        assertEquals(2,userManager.getUsers().size());
        userManager.deleteUser(2);
        assertEquals(1, userManager.getUsers().size());
        userManager.deleteUser(3);
        assertEquals(0, userManager.getUsers().size());
    }

    @Test
    public void testGetUserName() {
        assertEquals(userManager.getUsers().get(0).getName(), "Client");
    }

    @Test
    public void testGetClients() {
        ArrayList<Client> clients = userManager.getClients();
        HashMap<Integer, User> users = userManager.getUsers();

        assertTrue(clients.contains(users.get(0)));
        assertFalse(clients.contains(users.get(1)));
        assertFalse(clients.contains(users.get(2)));
        assertFalse(clients.contains(users.get(3)));
    }

    @Test
    public void testGetClient() {
        assertThrows(UserNotFoundException.class, () -> userManager.getClient(1));
        assertThrows(UserNotFoundException.class, () -> userManager.getClient(-1));
    }

    @Test
    public void testGetClientIdByUsername() throws UserNotFoundException {
        assertThrows(UserNotFoundException.class, () -> userManager.getClientIdByUsername("Random username"));
        assertEquals(0, userManager.getClientIdByUsername("Client"));
    }

    @Test
    public void testClientMoneySpent() {
        ArrayList<Client> clients = userManager.getClients();
        Client client = clients.get(0);
        assertEquals(0, client.getMoneySpent());
        userManager.changeMoneySpent(client, 900);
        assertEquals(900, client.getMoneySpent());
    }

    @Test
    public void testGetBeauticians() {
        ArrayList<Beautician> beauticians = userManager.getBeauticians();
        HashMap<Integer, User> users = userManager.getUsers();

        assertFalse(beauticians.contains(users.get(0)));
        assertFalse(beauticians.contains(users.get(1)));
        assertTrue(beauticians.contains(users.get(2)));
        assertFalse(beauticians.contains(users.get(3)));
    }

    @Test
    public void testTeachTreatment() throws UserNotFoundException {
        assertEquals(0, userManager.getBeautician(2).getTreatmentTypeCategoryIDs().size());
        userManager.teachTreatment(2, (byte) 0);
        assertEquals(1, userManager.getBeautician(2).getTreatmentTypeCategoryIDs().size());
    }

    @Test
    public void testBookTreatment() throws UserNotFoundException {
        Treatment treatment = new Treatment(Calendar.getInstance(), "Client", "Beautician", 0, 1000);
        userManager.bookTreatment(treatment, treatmentManager);

        Beautician beautician = userManager.getBeautician(2);
        Client client = userManager.getClient(0);

        assertEquals(1, userManager.getBeauticianTreatments(beautician.getUsername()).size());
        assertEquals(1, userManager.getClientTreatments(client.getUsername()).size());
        assertEquals(0, userManager.getBeauticianDueTreatments(beautician.getUsername()).size());
        assertEquals(0, userManager.getClientDueTreatments(client.getUsername()).size());
        assertEquals(1, userManager.getBeauticianPastTreatments(beautician.getUsername()).size());
        assertEquals(1, userManager.getClientPastTreatments(client.getUsername()).size());
        assertEquals(0, userManager.getFinishedTreatments(beautician.getUsername()));
    }
}
