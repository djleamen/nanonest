/*
 * This class is responsible for handling the file I/O operations of the catalogue.
 * It interacts with the CatalogueUI class to load the catalogue from a CSV file, 
 * add, edit, delete entries, and perform searches.  
 * It also provides a method to get a random entry from the catalogue.
 */
package furnitureCatalogue;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class CatalogueFileIO {
    private String fileName;
    public CatalogueUI UI;
    private File csvFile;

    public CatalogueFileIO(String fileName, CatalogueUI catalogueUI) {
        this.UI = catalogueUI;
        this.fileName = fileName;
        this.loadFile();
    }

    /**
     * Loads the CSV file into a HashMap.
     */
    public void loadFile() {
        try {
            URL resource = getClass().getClassLoader().getResource(fileName);
            if (resource == null) {
                throw new FileNotFoundException("File not found: " + fileName);
            }
            // If running from a jar, the protocol will be "jar"
            if ("jar".equals(resource.getProtocol())) {
                InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
                if (is == null) {
                    throw new FileNotFoundException("Resource stream not found: " + fileName);
                }
                // Create a temporary file to copy the CSV resource
                csvFile = File.createTempFile("temp", ".csv");
                Files.copy(is, csvFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                is.close();
            } else {
                csvFile = new File(resource.toURI());
            }
    
            // Load the file into memory
            Scanner fileScanner = new Scanner(csvFile);
            this.UI.headers = fileScanner.nextLine().split(",");
            this.UI.catalogue = new HashMap<>();
            while (fileScanner.hasNextLine()) {
                String[] line = fileScanner.nextLine().split(",");
                ArrayList<String> temp = new ArrayList<>(Arrays.asList(line).subList(1, line.length));
                UI.catalogue.put(Integer.parseInt(line[0]), temp);
            }
            fileScanner.close();
    
            // Compute maxLengths for formatting
            for (int i = 1; i < UI.headers.length; i++) {
                int maxLength = 0;
                for (ArrayList<String> entry : UI.catalogue.values()) {
                    if (entry.get(i - 1).length() > maxLength) {
                        maxLength = entry.get(i - 1).length();
                    }
                }
                UI.maxLengths[i - 1] = maxLength;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading file: " + e.getMessage(), e);
        }
    }

    /**
     * Appends a new line to the CSV file.
     */
    public void addCSVLine(String newLine) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true))) {
            writer.write(newLine);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    /**
     * Edits an existing CSV line (matching by first field).
     */
    public void editCSVLine(String lineID, String newLine) {
        try {
            ArrayList<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(lineID)) {
                    lines.add(newLine);
                } else {
                    lines.add(line);
                }
            }
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile));
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error editing CSV file: " + e.getMessage());
        }
    }

    /**
     * Deletes a line from the CSV file.
     */
    public void deleteCSVLine(String lineID) {
        try {
            ArrayList<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && !parts[0].equals(lineID)) {
                    lines.add(line);
                }
            }
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile));
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error deleting lineID=" + lineID + ": " + e.getMessage());
        }
    }

    /**
     * Returns a random entry from the in-memory catalogue.
     */
    public ArrayList<String> getRandomEntry() {
        if (UI.catalogue.isEmpty()) {
            return null;
        }
        Random rand = new Random();
        Integer randomId = (Integer) UI.catalogue.keySet().toArray()[rand.nextInt(UI.catalogue.size())];
        return UI.catalogue.get(randomId);
    }

    /**
     * Stub for relevancy search (can be enhanced later).
     */
    public List<String> relevancySearch(String search) {
        // Implementation omitted; returns an empty list.
        return new ArrayList<>();
    }
}