// Run: mvn -Dtest=LoginTest test

package furnitureCatalogue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    private Login login;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        login = new Login() {
            @Override
            public String readPassword(String prompt) {
                return scanner.nextLine();
            }
        };
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testAdminLogin() throws Exception {
        // Simulate user input for admin login
        String simulatedInput = "Admin\nadmin123\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = Login.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(login, testScanner);

//        System.setIn(new java.io.ByteArrayInputStream("admin\nadmin123\n".getBytes()));
        String role = login.authenticate();
        assertEquals("admin", role, "Admin should be able to login successfully.");
    }

    @Test
    public void testUserLogin() throws Exception {
        // Simulate user input for user login
        String simulatedInput = "User\nuser123\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = Login.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(login, testScanner);
        String role = login.authenticate();
        assertEquals("user", role, "User should be able to login successfully.");
    }

    @Test
    public void testInvalidLogin() throws Exception {
        // Simulate user input for invalid login
        String simulatedInput = "invalid\ninvalid\n";
        Scanner testScanner = new Scanner(simulatedInput);
        Field scannerField = Login.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(login, testScanner);

        String role = login.authenticate();
        assertNull(role, "Invalid credentials should return null.");
    }
}
