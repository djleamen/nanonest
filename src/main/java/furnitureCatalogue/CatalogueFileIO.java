package furnitureCatalogue;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

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
     * Loads the CSV file into a Hash Map
     */
    public void loadFile() {
        try {
            // load CSV file
            csvFile = new File("./src/main/resources/" + fileName);
            Scanner fileScanner = new Scanner(csvFile);
            this.UI.headers = fileScanner.nextLine().split(","); // Get headers from first line of CSV
            this.UI.catalogue = new HashMap<>(); // Initialize blank hash map
            // load each entry from CSV file into hash map
            while (fileScanner.hasNextLine()) {
                String[] line = fileScanner.nextLine().split(",");

                ArrayList<String> temp = new ArrayList<>(Arrays.asList(line).subList(1, line.length));

                UI.catalogue.put(Integer.parseInt(line[0]), temp);
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new line to the bottom of the CSV file
     *
     * @param newLine String to add to file
     */
    public void addCSVLine(String newLine) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true));
            writer.write(newLine);
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    /**
     * Finds a line in the CSV and changes its text
     *
     * @param lineID  the ID of the line to change
     * @param newLine the new text to use
     */
    public void editCSVLine(String lineID, String newLine) {
        try {
            ArrayList<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String currentLine = line.substring(0, 6);
                if (currentLine.equals(lineID)) {
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
     * Deletes a specified line from the CSV
     *
     * @param lineID ID of the line to remove
     */
    public void deleteCSVLine(String lineID) {
        try {
            ArrayList<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String currentLine = line.substring(0, 6);
                if (!currentLine.equals(lineID)) {
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
            System.err.println("Error deleting from CSV file: " + e.getMessage());
        }
    }
}
