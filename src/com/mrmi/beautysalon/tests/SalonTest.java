package com.mrmi.beautysalon.tests;

import com.mrmi.beautysalon.main.entity.*;
import com.mrmi.beautysalon.main.manager.SalonManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SalonTest {
    private SalonManager salonManager;
    @BeforeEach
    public void setup() {
        Database database = new Database("test");
        salonManager = new SalonManager(database);
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
    public void testHours() {
        salonManager.setOpeningHour((byte) 10);
        assertEquals(10, salonManager.getOpeningHour());
        salonManager.setClosingHour((byte) 20);
        assertEquals(20, salonManager.getClosingHour());
    }

    @Test
    public void testLoyaltyThreshold() {
        salonManager.setLoyaltyThreshold(15000);
        assertEquals(15000, salonManager.getLoyaltyThreshold());
    }

    @Test
    public void testBonus() {
        salonManager.setBonus(10000);
        assertEquals(10000, salonManager.getBonus());
    }
}
