package furnitureCatalogue;

import org.junit.jupiter.api.*;
import java.io.*;
import java.net.URISyntaxException;
import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTests {
    private CatalogueUITest uiTest;
    private CatalogueFileIOTest fileIOTest;
    private LoginTest loginTest;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        fileIOTest = new CatalogueFileIOTest();
        uiTest = new CatalogueUITest();
        loginTest = new LoginTest();

        uiTest.setUp();
        fileIOTest.setUp();
        loginTest.setUp();
    }

    @Test
    public void testSystemEntry() throws Exception {
        loginTest.testUserLogin();
        uiTest.setUp();
        uiTest.testDisplayEntries();
    }

    @Test
    public void testDisplayedEntries() {
        uiTest.setUp();
        uiTest.testDisplayEntries();
    }

    @Test
    public void testEditUI() throws Exception {
        uiTest.setUp();
        uiTest.testEditEntry();
    }

    @Test
    public void testRemoveUI() throws Exception {
        uiTest.setUp();
        uiTest.testRemoveEntry();
    }

    @Test
    public void testAddUI() throws Exception {
        uiTest.setUp();
        uiTest.testAddEntry();
    }

    @Test
    public void testAdminCredentials() throws Exception {
        loginTest.setUp();
        loginTest.testAdminLogin();
        uiTest.setUp();
        uiTest.testDisplayEntries();
    }

    @Test
    public void testUserCredentials() throws Exception {
        loginTest.setUp();
        loginTest.testUserLogin();
        uiTest.setUp();
        uiTest.testDisplayEntries();
    }

    @Test
    public void testSearchUI() {}

    @Test
    public void testSystemRunThrough() throws Exception {
        loginTest.setUp();
        loginTest.testAdminLogin();
        uiTest.setUp();
        uiTest.testDisplayEntries();
        uiTest.testAddEntry();
        uiTest.testRemoveEntry();
        uiTest.testEditEntry();
    }
}
