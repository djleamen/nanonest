/*
 * This file contains tests for the CatalogueUI class.
 * It tests the display, view, edit, remove, add, and search functionality of the catalogue.
 */

package furnitureCatalogue;

import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

public class CatalogueUITest {
    private CatalogueUI ui;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        ui = new CatalogueUI() {
            @Override
            public void commandLineMenu() {
                // Override to prevent interactive loop
            }
            @Override
            protected boolean inputLogin() {
                role = "admin";
                return false;
            }
        };
        ArrayList<String> entry = new ArrayList<>();
        entry.add("Blue Wooden Chair");
        entry.add("250");
        entry.add("Chair");
        entry.add("Blue");
        entry.add("Wooden");
        entry.add("Large");
        entry.add("75");
        entry.add("Leon's");
        entry.add("Modern");
        entry.add("122");
        ui.catalogue.put(193, entry);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testDisplayEntries() {
        ui.displayEntriesSwing();
        String output = outContent.toString();
        assertTrue(output.contains("193\tBlue Wooden Chair"),
                   "Output should display the entry with ID 193 and name 'Blue Wooden Chair'.");
    }

    @Test
    void testViewEntry() throws Exception {
        String simulatedInput = "193\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = CatalogueUI.class.getDeclaredField("s");
        scannerField.setAccessible(true);
        scannerField.set(ui, testScanner);
        outContent.reset();
        ui.viewEntry();
        String output = outContent.toString();
        assertTrue(output.contains("Blue Wooden Chair"),
                   "Output should contain the entry name.");
        assertTrue(output.contains("250"),
                   "Output should contain the entry price.");
    }

    @Test
    void testEditEntry() throws Exception {
        String simulatedInput = "193\nBlue Wooden Armchair\n\n\n\n\n\n\n\n\n\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = CatalogueUI.class.getDeclaredField("s");
        scannerField.setAccessible(true);
        scannerField.set(ui, testScanner);
        ui.editEntry();
        ArrayList<String> updatedEntry = ui.catalogue.get(193);
        assertEquals("Blue Wooden Armchair", updatedEntry.get(0),
                     "First field should be updated to Blue Wooden Armchair.");
        assertEquals("250", updatedEntry.get(1),
                     "Second field should remain 250.");
    }

    @Test
    void testRemoveEntry() throws Exception {
        ui.fileIO.addCSVLine("192,Armchair,250,Chair,Blue,Wooden,Large,75,Leon's,Modern,122");
        ui.catalogue.put(192, new ArrayList<>(Arrays.asList("Armchair","250","Chair","Blue","Wooden","Large","75","Leon's","Modern","122")));
        String simulatedInput = "192\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = CatalogueUI.class.getDeclaredField("s");
        scannerField.setAccessible(true);
        scannerField.set(ui, testScanner);
        outContent.reset();
        ui.removeEntry();
        assertFalse(ui.catalogue.containsKey(192), "Entry with ID 192 should be removed.");
        String output = outContent.toString();
        assertTrue(output.contains("Entry removed successfully."), "Output should confirm deletion.");
    }

    @Test
    void testAddEntry() throws Exception {
        String simulatedInput = "192\nTable\n250\n\n\n\n\n\n\n\n\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = CatalogueUI.class.getDeclaredField("s");
        scannerField.setAccessible(true);
        scannerField.set(ui, testScanner);
        ui.addEntry();
        ArrayList<String> newEntry = ui.catalogue.get(192);
        assertNotNull(newEntry, "New entry should be added.");
        assertEquals("Table", newEntry.get(0), "First field should be 'Table'.");
        assertEquals("250", newEntry.get(1), "Price should be 250.");
        ui.fileIO.deleteCSVLine("192");
    }

    @Test
    void testSpecificSearch() throws Exception {
        String simulatedInput = "Blue Wooden Chair\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = CatalogueUI.class.getDeclaredField("s");
        scannerField.setAccessible(true);
        scannerField.set(ui, testScanner);
        outContent.reset();
        ui.specificSearch();
        String output = outContent.toString();
        assertTrue(output.contains("Blue Wooden Chair"), "Search output should include the entry name.");
    }
}