package com.mrmi.beautysalon.tests;

import com.mrmi.beautysalon.main.objects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseTest {
    private Database database;

    @BeforeEach
    public void setup() {
        database = new Database("test" + System.getProperty("file.separator"), System.getProperty("file.separator"));
    }
    @Test
    public void emptyDataTest() {
        assertEquals(0, database.getUsers().size());
        assertEquals(0, database.getTreatments().size());
        assertEquals(0, database.getTreatmentTypes().size());
    }

    @Test
    public void addUsersTest() {
        database.addUser(new Manager("pass", "Manager", "McGee", "F", "1234", "Manager Street 50", (byte) 7, (byte) 5, 0, 100000), "Manager");
        database.addUser(new Beautician("pass", "Beautician", "McGee", "F", "123456", "Beautician Street 50", new ArrayList<>(), (byte) 7, (byte) 5, 0, 100000, 0), "Beautician");
        database.addUser(new Receptionist("pass", "Receptionist", "McGee", "F", "123456", "Receptionist Street 50", (byte) 7, (byte) 5, 0, 100000), "Receptionist");
        database.addUser(new Client("pass", "Client", "McGee", "F", "1234567", "Client Street 50"), "Client");
        assertEquals(4, database.getUsers().size());
    }
}
