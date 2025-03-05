package furnitureCatalogue;

import org.junit.jupiter.api.*;

import java.io.*;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CatalogueFileIOTest {
    private CatalogueFileIO fileIO;
    private CatalogueUI mockUI;

    @BeforeEach
    void setUp() throws IOException {
        mockUI = mock(CatalogueUI.class);
        fileIO = new CatalogueFileIO("Sample.csv", mockUI);
    }

    @Test
    void testLoadFile() {
        fileIO.loadFile();
    }

    @Test
    void testAddCSVLine() throws IOException {
        String newLine = "500,Blue Cotton Sofa,319,Sofa,Blue,Cotton,Small,41,Leon's,Gothic,205";

        // Add the new line to the CSV file
        fileIO.addCSVLine(newLine);

        // Search the file to check if the line was added
        String foundLine = searchCSV("Sample.csv", "500");

        assertNotNull(foundLine, "The added line should exist in the CSV file.");

        // Split the expected and actual values
        String[] expectedValues = newLine.split(",");
        String[] actualValues = foundLine.split(",");

        // Check if both the lines are the same
        assertArrayEquals(expectedValues, actualValues, "Each column should match exactly.");
    }

    @Test
    void testEditCSVLine() throws IOException {
        String originalLine = "500,Blue Cotton Sofa,319,Sofa,Blue,Cotton,Small,41,Leon's,Gothic,205";
        String newLine = "500,Edited Sofa,319,Sofa,Red,Cotton,Small,41,Leon's,Gothic,205";

        // Edit the line with ID "500"
        fileIO.addCSVLine(originalLine);  // First add the original line
        fileIO.editCSVLine("500", newLine);  // Then edit it

        // Search the file to check if the line was updated
        String foundLine = searchCSV("Sample.csv", "500");

        assertNotNull(foundLine, "The edited line should exist in the CSV file.");
        String[] expectedValues = newLine.split(",");
        String[] actualValues = foundLine.split(",");

        // Ensure that the line has been properly updated
        assertArrayEquals(expectedValues, actualValues, "Each column should match exactly after editing.");
    }

    @Test
    void testDeleteCSVLine() throws IOException {
        String lineToDelete = "500,Blue Cotton Sofa,319,Sofa,Blue,Cotton,Small,41,Leon's,Gothic,205";

        // Add the line to the CSV file
        fileIO.addCSVLine(lineToDelete);

        // Delete the line with ID "500"
        fileIO.deleteCSVLine("500");

        // Search the file to check if the line was deleted
        String foundLine = searchCSV("Sample.csv", "500");

        assertNull(foundLine, "The line should have been deleted and not exist in the CSV file.");
    }

    private String getResourceFilePath(String resourcePath) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(resourcePath);
        if (resource == null) {
            throw new FileNotFoundException("File not found: " + resourcePath);
        }
        return new File(resource.getFile()).getAbsolutePath();
    }

    private String searchCSV(String resourcePath, String searchId) throws IOException {
        String filePath = getResourceFilePath(resourcePath);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
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
