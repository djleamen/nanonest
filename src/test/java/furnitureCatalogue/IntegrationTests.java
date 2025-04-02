package furnitureCatalogue;

import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
    public void testEditUI() {

    }

    @Test
    public void testRemoveUI() {

    }

    @Test
    public void testAddUI() {

    }

    @Test
    public void testAdminCredentials() {
    }

    @Test
    public void testUserCredentials() {

    }

    @Test
    public void testSearchUI() {

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
