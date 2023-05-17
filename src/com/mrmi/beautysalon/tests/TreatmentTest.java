package com.mrmi.beautysalon.tests;

import com.mrmi.beautysalon.main.entity.*;
import com.mrmi.beautysalon.main.exceptions.TreatmentNotFoundException;
import com.mrmi.beautysalon.main.exceptions.TreatmentTypeCategoryNotFoundException;
import com.mrmi.beautysalon.main.exceptions.TreatmentTypeNotFoundException;
import com.mrmi.beautysalon.main.manager.SalonManager;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TreatmentTest {
    private TreatmentManager treatmentManager;
    private UserManager userManager;

    @BeforeEach
    public void setup() {
        Database database = new Database("test");
        SalonManager salonManager = new SalonManager(database);
        treatmentManager = new TreatmentManager(database, salonManager);
        userManager = new UserManager(database, salonManager);

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
    public void testCRUDTreatmentTypeCategory() throws TreatmentTypeCategoryNotFoundException {
        HashMap<Integer, TreatmentTypeCategory> categories = treatmentManager.getTreatmentTypeCategories();
        assertThrows(TreatmentTypeCategoryNotFoundException.class, () -> treatmentManager.deleteTreatmentTypeCategory(0));

        assertEquals(0, categories.size());
        assertEquals(0, treatmentManager.getAvailableTreatmentTypeCategories().size());

        treatmentManager.addTreatmentTypeCategory("Massage");
        assertEquals(1, categories.size());
        assertEquals(1, treatmentManager.getAvailableTreatmentTypeCategories().size());

        categories.get(0).setName("Hair");
        treatmentManager.updateTreatmentTypeCategory(categories.get(0));
        assertEquals("Hair", categories.get(0).getName());

        treatmentManager.deleteTreatmentTypeCategory(0);
        assertEquals(0, treatmentManager.getAvailableTreatmentTypeCategories().size());
    }

    @Test
    public void testCRUDTreatmentType() throws TreatmentTypeNotFoundException {
        HashMap<Integer, TreatmentType> types = treatmentManager.getTreatmentTypes();
        assertThrows(TreatmentTypeNotFoundException.class, () -> treatmentManager.deleteTreatmentType(0));

        assertEquals(0, types.size());
        assertEquals(0, treatmentManager.getAvailableTreatmentTypes().size());
        assertThrows(TreatmentTypeNotFoundException.class, () -> treatmentManager.getTreatmentTypeName(treatmentManager.getTreatmentType(0).getCategoryId()));
        assertEquals(0, treatmentManager.getTimesBooked(0));
        assertEquals(0, treatmentManager.getProfit(0));

        treatmentManager.addTreatmentType("Sport massage", 1000, 0, 45);
        assertEquals(1, types.size());
        assertEquals(1, treatmentManager.getAvailableTreatmentTypes().size());

        types.get(0).setName("Couples massage");
        treatmentManager.updateTreatmentType(types.get(0));
        assertEquals("Couples massage", treatmentManager.getTreatmentType(0).getName());

        treatmentManager.deleteTreatmentType(0);
        assertEquals(0, treatmentManager.getAvailableTreatmentTypes().size());

        assertThrows(TreatmentTypeCategoryNotFoundException.class, () -> treatmentManager.getTreatmentTypeCategoryName(types.get(0).getCategoryId()));
    }

    @Test
    public void testCRUDTreatment() throws TreatmentNotFoundException {
        HashMap<Integer, Treatment> treatments = treatmentManager.getTreatments();
        assertEquals(0, treatments.size());
        assertThrows(TreatmentNotFoundException.class, () -> treatmentManager.deleteTreatment(0));
        assertThrows(TreatmentNotFoundException.class, () -> treatmentManager.cancelTreatment(0, 0, true, "Unexpected duties", userManager));

        Treatment treatment = new Treatment(Calendar.getInstance(), "Client", "Beautician", 0, 1000);
        userManager.bookTreatment(treatment, treatmentManager);
        assertEquals(1, treatments.size());
        assertEquals(Treatment.Status.SCHEDULED, treatments.get(0).getStatus());

        treatmentManager.cancelTreatment(0, 0, true, "Unexpected duties", userManager);
        assertEquals(Treatment.Status.CANCELLED_BY_CLIENT, treatments.get(0).getStatus());

        treatmentManager.deleteTreatment(0);
        assertEquals(0, treatmentManager.getAvailableTreatmentTypes().size());
    }

    @Test
    public void testTotalTreatmentCost() {
        HashMap<Integer, Treatment> treatments = treatmentManager.getTreatments();
        assertEquals(0, treatmentManager.getTotalCost(treatments));

        Treatment treatment = new Treatment(Calendar.getInstance(), "Client", "Beautician", 0, 1000);
        userManager.bookTreatment(treatment, treatmentManager);
        treatment = new Treatment(Calendar.getInstance(), "Client", "Beautician", 0, 1000);
        userManager.bookTreatment(treatment, treatmentManager);

        assertEquals(2000, treatmentManager.getTotalCost(treatments));
    }
}
