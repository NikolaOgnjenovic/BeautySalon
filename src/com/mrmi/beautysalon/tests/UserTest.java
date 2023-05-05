package com.mrmi.beautysalon.tests;

import com.mrmi.beautysalon.main.manager.SalonManager;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.Client;
import com.mrmi.beautysalon.main.entity.Database;
import com.mrmi.beautysalon.main.entity.Manager;
import com.mrmi.beautysalon.main.exceptions.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private UserManager userManager;

    // TODO: introduce a test suite to run all test files together

    @BeforeEach
    public void setup() {
        Database database = new Database("test");

        SalonManager salonManager = new SalonManager("");
        TreatmentManager treatmentManager = new TreatmentManager(database, salonManager);
        userManager = new UserManager(database, treatmentManager, salonManager);
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
        userManager.addUser("Client", new Client("password", "name", "username", "M", "123456", "Address 3"));
        userManager.addUser("Manager", new Manager("password", "name", "username", "M", "123456", "Address 3", (byte) 6, (byte) 5, 10000, 90000));
        assertEquals(userManager.getUsers().size(), 2);
    }

    @Test
    public void testRemoveUser() {
        assertThrows(UserNotFoundException.class, () -> userManager.deleteUser("Client"));
        assertEquals(userManager.getUsers().size(), 0);
    }
}
