package furnitureCatalogue;

import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTests {
    private Login login;
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
        login = new Login() {
            @Override
            public String readPassword(String prompt) {
                return scanner.nextLine();
            }
        };
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testSystemEntry() throws Exception {
        String simulatedInput = "Admin\nadmin123\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = Login.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(login, testScanner);
        ui.displayEntriesSwing();
        String role = login.authenticate();
        assertEquals("admin", role, "Admin should be able to login successfully.");
        String output = outContent.toString();
        assertTrue(output.contains("191\tRed Metal Sofa"),
                "Output should display the entry with ID 191 and name 'Red Metal Sofa'.");
    }

    @Test
    public void testDisplayedEntries() {
        ui.displayEntriesSwing();
        String output = outContent.toString();
        assertTrue(output.contains("191\tRed Metal Sofa"),
                "Output should display the entry with ID 191 and name 'Red Metal Sofa'.");
}

    @Test
    public void testEditUI() throws Exception {
        assertTrue(ui.catalogue.containsKey(101));
        assertEquals("Red Wood Table", ui.catalogue.get(101).get(0));

        ArrayList<String> updatedEntry = new ArrayList<>();
        updatedEntry.add("Red Leather Chair");
        updatedEntry.add("Metal");
        updatedEntry.add("300.50");
        ui.catalogue.put(101, updatedEntry);

        // Verify the updated data
        assertEquals("Red Leather Chair", ui.catalogue.get(101).get(0));
        assertEquals("Metal", ui.catalogue.get(101).get(1));
        assertEquals("300.50", ui.catalogue.get(101).get(2));
    }

    @Test
    public void testRemoveUI() {
        assertTrue(ui.catalogue.containsKey(101));
        ui.catalogue.remove(101);
        assertFalse(ui.catalogue.containsKey(101), "Entry should be removed from the catalogue");
    }

    @Test
    public void testAddUI() {
        assertFalse(ui.catalogue.containsKey(202));
        ArrayList<String> newEntry = new ArrayList<>();
        newEntry.add("Blue Velvet Sofa");
        newEntry.add("Fabric");
        newEntry.add("500.75");
        ui.catalogue.put(202, newEntry);

        assertTrue(ui.catalogue.containsKey(202));
        assertEquals("Blue Velvet Sofa", ui.catalogue.get(202).get(0));
        assertEquals("Fabric", ui.catalogue.get(202).get(1));
        assertEquals("500.75", ui.catalogue.get(202).get(2));
    }

    @Test
    public void testAdminCredentials() throws Exception {
        String simulatedInput = "Admin\nadmin123\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = Login.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(login, testScanner);

        String role = login.authenticate();
        assertEquals("admin", role, "Admin should have admin privileges");
    }

    @Test
    public void testUserCredentials() throws Exception {
        String simulatedInput = "User\nuser123\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = Login.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(login, testScanner);

        String role = login.authenticate();
        assertEquals("user", role, "User should have user privileges.");
    }

    @Test
    public void testSearchUI() {
        ui.catalogue.put(303, new ArrayList<>(List.of("Black Wooden Desk", "Wood", "250.00")));
        assertTrue(ui.catalogue.containsKey(303), "Catalogue should contain the added entry.");

        boolean found = false;
        for (ArrayList<String> entry : ui.catalogue.values()) {
            if (entry.contains("Black Wooden Desk")) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Search should successfully locate the Black Wooden Desk entry.");
    }

    @Test
    public void testSystemRunThrough() throws Exception {
        testSystemEntry();
        testDisplayedEntries();
        testEditUI();
        testRemoveUI();
        testAddUI();
        testSearchUI();
    }
}
