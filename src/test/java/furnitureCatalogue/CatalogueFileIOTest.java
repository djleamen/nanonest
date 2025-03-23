/*
 * This file contains tests for the CatalogueFileIO class.
 * It tests the loading of the CSV file, adding, editing, and deleting entries.
 */
package furnitureCatalogue;

import org.junit.jupiter.api.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

public class CatalogueFileIOTest {
    private CatalogueFileIO fileIO;
    private CatalogueUI ui;
    private File tempCsvFile;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("Sample.csv");
        assertNotNull(resource, "Sample.csv resource not found");
        File originalFile = new File(resource.toURI());
        tempCsvFile = File.createTempFile("Sample", ".csv");
        Files.copy(originalFile.toPath(), tempCsvFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        ui = new CatalogueUI() {
            @Override
            protected boolean inputLogin() {
                this.role = "admin";
                return false;
            }
            @Override
            protected void initSwingUI() {
                // Prevent actual UI from displaying in tests.
            }
        };
        fileIO = new CatalogueFileIO("Sample.csv", ui);
        try {
            java.lang.reflect.Field csvFileField = CatalogueFileIO.class.getDeclaredField("csvFile");
            csvFileField.setAccessible(true);
            csvFileField.set(fileIO, tempCsvFile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection error: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        if (tempCsvFile.exists()) {
            tempCsvFile.delete();
        }
    }

    @Test
    void testLoadFile() {
        assertNotNull(fileIO);
        assertNotNull(ui.headers, "Headers should be loaded");
        assertNotNull(ui.catalogue, "Catalogue should be initialized");
    }

    @Test
    void testAddCSVLine() throws IOException {
        String newLine = "500,Blue Cotton Sofa,319,Sofa,Blue,Cotton,Small,41,Leon's,Gothic,205";
        fileIO.addCSVLine(newLine);
        String foundLine = searchCSV("500");
        assertNotNull(foundLine, "The added line should exist in the CSV file.");
        String[] expectedValues = newLine.split(",");
        String[] actualValues = foundLine.split(",");
        assertArrayEquals(expectedValues, actualValues, "Each column should match exactly.");
    }

    @Test
    void testEditCSVLine() throws IOException {
        String originalLine = "500,Blue Cotton Sofa,319,Sofa,Blue,Cotton,Small,41,Leon's,Gothic,205";
        String newLine = "500,Edited Sofa,319,Sofa,Red,Cotton,Small,41,Leon's,Gothic,205";
        fileIO.addCSVLine(originalLine);
        fileIO.editCSVLine("500", newLine);
        String foundLine = searchCSV("500");
        assertNotNull(foundLine, "The edited line should exist in the CSV file.");
        String[] expectedValues = newLine.split(",");
        String[] actualValues = foundLine.split(",");
        assertArrayEquals(expectedValues, actualValues, "Each column should match exactly after editing.");
    }

    @Test
    void testDeleteCSVLine() throws IOException {
        String lineToDelete = "500,Blue Cotton Sofa,319,Sofa,Blue,Cotton,Small,41,Leon's,Gothic,205";
        fileIO.addCSVLine(lineToDelete);
        fileIO.deleteCSVLine("500");
        String foundLine = searchCSV("500");
        assertNull(foundLine, "The line should have been deleted and not exist in the CSV file.");
    }

    private String searchCSV(String searchId) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(tempCsvFile));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");
            if (values.length > 0 && values[0].trim().equals(searchId)) {
                reader.close();
                return line;
            }
        }
        reader.close();
        return null;
    }
}