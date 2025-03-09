package furnitureCatalogue;

import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CatalogueUITest {
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

        // Set up sample headers and a sample catalogue
        ui.headers = new String[] {"ID", "Type", "Material"};
        ui.catalogue = new HashMap<>();
        ArrayList<String> entry = new ArrayList<>();
        entry.add("Chair");
        entry.add("Wooden");
        ui.catalogue.put(1, entry);

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
        assertTrue(output.contains("1: Chair"), "Output should display the entry with ID 1 and its type.");
    }

    @Test
    void testViewEntry() throws Exception {
        // Simulate user input "1" for viewEntry
        String simulatedInput = "1\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = CatalogueUI.class.getDeclaredField("s");
        scannerField.setAccessible(true);
        scannerField.set(ui, testScanner);

        outContent.reset();
        ui.viewEntry();
        String output = outContent.toString();
        assertTrue(output.contains("Type: Chair"), "Output should contain the type of the entry.");
        assertTrue(output.contains("Material: Wooden"), "Output should contain the material of the entry.");
    }

    @Test
    void testEditEntry() throws Exception {
        // Entry with ID 1 initially has: ["Chair", "Wooden"]
        // Simulate input: first the entry ID ("1"), then new data for each field.
        // For example, change "Chair" to "Armchair" and leave "Wooden" unchanged.
        String simulatedInput = "1\nArmchair\n\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = CatalogueUI.class.getDeclaredField("s");
        scannerField.setAccessible(true);
        scannerField.set(ui, testScanner);

        ui.editEntry();
        // Verify that the entry has been updated accordingly
        ArrayList<String> updatedEntry = ui.catalogue.get(1);
        assertEquals("Armchair", updatedEntry.get(0), "First field should be updated to Armchair.");
        assertEquals("Wooden", updatedEntry.get(1), "Second field should remain Wooden.");
    }

    @Test
    void testAddEntry() throws Exception {
        // Simulate adding an entry with ID "2" and data for each field.
        String simulatedInput = "2\nTable\nMetal\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = CatalogueUI.class.getDeclaredField("s");
        scannerField.setAccessible(true);
        scannerField.set(ui, testScanner);

        ui.addEntry();
        // Check that the new entry with ID 2 is added with the correct values.
        ArrayList<String> newEntry = ui.catalogue.get(2);
        assertNotNull(newEntry, "New entry should be added.");
        assertEquals("Table", newEntry.get(0), "Type should be Table.");
        assertEquals("Metal", newEntry.get(1), "Material should be Metal.");
    }

    @Test
    void testRemoveEntry() throws Exception {
        // Simulate removing the entry with ID "1"
        String simulatedInput = "1\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = CatalogueUI.class.getDeclaredField("s");
        scannerField.setAccessible(true);
        scannerField.set(ui, testScanner);

        outContent.reset();
        ui.removeEntry();
        // Verify that the entry with ID 1 no longer exists in the catalogue
        assertFalse(ui.catalogue.containsKey(1), "Entry with ID 1 should be removed.");
        String output = outContent.toString();
        assertTrue(output.contains("Entry removed successfully."), "Output should confirm deletion.");
    }

    @Test
    void testSpecificSearch() throws Exception {
        // The specificSearch method looks for an entry whose first field matches the input.
        // In our test data, the entry with ID 1 has "Chair" as its first field.
        String simulatedInput = "Chair\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = CatalogueUI.class.getDeclaredField("s");
        scannerField.setAccessible(true);
        scannerField.set(ui, testScanner);

        outContent.reset();
        ui.specificSearch();
        String output = outContent.toString();
        // Verify that the search output includes details of the matching entry.
        assertTrue(output.contains("Type: Chair"), "Search output should include the entry type.");
    }
}
