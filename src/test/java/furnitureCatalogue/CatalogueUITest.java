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
        // Override the commandLineMenu to prevent the interactive loop from starting.
        ui = new CatalogueUI() {
            @Override
            public void commandLineMenu() {
                // No Testing.
            }
        };

        // Set up a sample entry for testing
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

        // Redirect System.out to capture printed output
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testDisplayEntries() {
        ui.displayEntries();
        String output = outContent.toString();
        // Expect the output to show the entry’s ID and its first field (i.e. "Type")
        assertTrue(output.contains("193\tBlue Wooden Chair"), "Output should display the entry with ID 193 and its type.");
    }

    @Test
    void testViewEntry() throws Exception {
        // Simulate user input "193" for viewEntry
        String simulatedInput = "193\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = CatalogueUI.class.getDeclaredField("s");
        scannerField.setAccessible(true);
        scannerField.set(ui, testScanner);

        outContent.reset();
        ui.viewEntry();
        String output = outContent.toString();
        assertTrue(output.contains("Name: Blue Wooden Chair"), "Output should contain the type of the entry.");
        assertTrue(output.contains("Price: 250"), "Output should contain the material of the entry.");
    }

    @Test
    void testEditEntry() throws Exception {
        // Entry with ID 193 initially has: ["Blue Wooden Chair", "250"]
        // Simulate input: first the entry ID ("193"), then new data for each field.
        // For example, change "Blue Wooden Chair" to "Blue Wooden Armchair" and leave "250" unchanged.
        String simulatedInput = "193\nBlue Wooden Armchair\n\n\n\n\n\n\n\n\n\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = CatalogueUI.class.getDeclaredField("s");
        scannerField.setAccessible(true);
        scannerField.set(ui, testScanner);

        ui.editEntry();
        // Verify that the entry has been updated accordingly
        ArrayList<String> updatedEntry = ui.catalogue.get(193);
        assertEquals("Blue Wooden Armchair", updatedEntry.get(0), "First field should be updated to Blue Wooden Armchair.");
        assertEquals("250", updatedEntry.get(1), "Second field should remain 250.");
    }

    @Test
    void testRemoveEntry() throws Exception {
        // place an entry to remove
        ui.fileIO.addCSVLine("192,Armchair,250,Chair,Blue,Wooden,Large,75,Leon's,Modern,122");
        ui.catalogue.put(192, new ArrayList<>(Arrays.asList("Armchair","250","Chair","Blue","Wooden","Large","75","Leon's","Modern","122")));
        // Simulate removing the entry with ID "192"
        String simulatedInput = "192\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = CatalogueUI.class.getDeclaredField("s");
        scannerField.setAccessible(true);
        scannerField.set(ui, testScanner);

        outContent.reset();
        ui.removeEntry();
        // Verify that the entry with ID 192 no longer exists in the catalogue
        assertFalse(ui.catalogue.containsKey(192), "Entry with ID 192 should be removed.");
        String output = outContent.toString();
        assertTrue(output.contains("Entry removed successfully."), "Output should confirm deletion.");
    }

    @Test
    void testAddEntry() throws Exception {
        // Simulate adding an entry with ID "192" and data for each field.
        String simulatedInput = "192\nTable\nMetal\n\n\n\n\n\n\n\n\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = CatalogueUI.class.getDeclaredField("s");
        scannerField.setAccessible(true);
        scannerField.set(ui, testScanner);

        ui.addEntry();
        // Check that the new entry with ID 192 is added with the correct values.
        ArrayList<String> newEntry = ui.catalogue.get(192);
        assertNotNull(newEntry, "New entry should be added.");
        assertEquals("Table", newEntry.get(0), "Type should be Table.");
        assertEquals("Metal", newEntry.get(1), "Material should be Metal.");
        // remove it from the file
        ui.fileIO.deleteCSVLine("192");
    }

    @Test
    void testSpecificSearch() throws Exception {
        // The specificSearch method looks for an entry whose first field matches the input.
        // In our test data, the entry with ID 193 has "Blue Wooden Chair" as its first field.
        String simulatedInput = "Blue Wooden Chair\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = CatalogueUI.class.getDeclaredField("s");
        scannerField.setAccessible(true);
        scannerField.set(ui, testScanner);

        outContent.reset();
        ui.specificSearch();
        String output = outContent.toString();
        // Verify that the search output includes details of the matching entry.
        assertTrue(output.contains("Name: Blue Wooden Chair"), "Search output should include the entry type.");
    }
}
