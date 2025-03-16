package furnitureCatalogue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;

public class CatalogueUI {
    public HashMap<Integer, ArrayList<String>> catalogue;
    public CatalogueFileIO fileIO;
    public String[] headers;
    private Scanner s;

    public static void main(String[] args) {
        CatalogueUI catalogueUI = new CatalogueUI();
    }

    public CatalogueUI() {
        fileIO = new CatalogueFileIO("Sample.csv", this);
        commandLineMenu();
    }

    /**
     * Prints a simple command line interface
     * that allows for the user to choose what they want to do
     */
    protected void commandLineMenu() {
        s = new Scanner(System.in);
        boolean running = true;
        while (running) {
            String[] menuOptions = {
                    "Display all Entries",
                    "Edit an entry",
                    "Add an entry",
                    "Remove an entry",
                    "View Specific Entry",
                    "Search",
                    "Display Random Entry"
            };
            printMenu(menuOptions);
            String inp = s.nextLine();
            switch (inp) {
                case "1":
                    displayEntries();
                    break;
                case "2":
                    editEntry();
                    break;
                case "3":
                    addEntry();
                    break;
                case "4":
                    removeEntry();
                    break;
                case "5":
                    viewEntry();
                    break;
                case "6":
                    specificSearch();
                    break;
                case "7":
                    randomEntry();
                    break;
                case "8":
                    running = false;
                    break;
                default:
                    break;
            }
        }
        s.close();
    }

    // Helper method for PotatoFix, making it so that inputs for the fields that should use numbers can ONLY be numbers
    private String getValidatedNumericInput(String fieldName, String currentValue) {
        while (true) {
            if (currentValue != null) {
                System.out.print("Input new " + fieldName + " to replace " + currentValue + ": ");
            } else {
                System.out.print("Input " + fieldName + ": ");
            }
            String input = s.nextLine();
            if (input.isEmpty()) {
                if (currentValue != null) {
                    return currentValue;
                } else {
                    // For adding an items only, allowing for blank fields
                    return "";
                }
            }
            try {
                Double.parseDouble(input);
                return input;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. " + fieldName + " must be a number.");
            }
        }
    }


    /**
     * Prints out the entire catalogue to the command line without showing all the details of every item
     */
    public void displayEntries() {
        System.out.println();
        for (Map.Entry<Integer, ArrayList<String>> entry : catalogue.entrySet()) {
            Integer key = entry.getKey();
            ArrayList<String> value = entry.getValue();
            // prints each entry by id, type and color
            System.out.println(key + ": " + value.getFirst());
        }
        System.out.println();
    }

    /**
     * Prompts the user to select a specific entry, and then displays all its information
     */
    public void viewEntry() {
        int id;
        while (true) {
            System.out.print("Choose Entry by ID: ");
            String inp = s.nextLine();
            if (inp.isEmpty()) {
                System.out.println("Blank input detected (Returning).");
                return;
            }
            try {
                id = Integer.parseInt(inp);
                if (!catalogue.containsKey(id)) {
                    System.out.println("ID not found. Please try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. ID must be a number. Please try again.");
            }
        }
        ArrayList<String> value = catalogue.get(id);
        System.out.println("ID: " + id);
        for (int i = 0; i < value.size(); i++) {
            System.out.println("\t" + headers[i + 1] + ": " + value.get(i));
        }
    }

    /**
     * Prompts the user to select a specific entry, then prompts the user to provide new information for each field
     */
    public void editEntry() {
        int id;
        while (true) {
            System.out.print("Choose Entry by ID: ");
            String inp = s.nextLine();
            if (inp.isEmpty()) {
                System.out.println("Blank input detected (Returning).");
                return;
            }
            try {
                id = Integer.parseInt(inp);
                if (!catalogue.containsKey(id)) {
                    System.out.println("ID not found. Please try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. ID must be a number. Please try again.");
            }
        }
        ArrayList<String> value = catalogue.get(id);
        for (int i = 0; i < value.size(); i++) {
            String field = headers[i + 1];
            if (field.equalsIgnoreCase("Price") ||
                    field.equalsIgnoreCase("Quantity") ||
                    field.equalsIgnoreCase("Weight")) {
                String input = getValidatedNumericInput(field, value.get(i));
                value.set(i, input);
            } else {
                System.out.print("Input new " + field + " to replace " + value.get(i) + ": ");
                String input = s.nextLine();
                if (!input.equals("")) {
                    value.set(i, input);
                }
            }
        }
        catalogue.put(id, value);
        fileIO.editCSVLine(String.valueOf(id), id + "," + String.join(",", value));
    }

    /**
     * Prompts the user for information to create a new entry
     */
    public void addEntry() {
        int id;
        while (true) {
            System.out.print("Choose ID for Entry: ");
            String inp = s.nextLine();
            if (inp.isEmpty()) {
                System.out.println("Blank input detected (Returning).");
                return;
            }
            try {
                id = Integer.parseInt(inp);
                if (catalogue.containsKey(id)) {
                    System.out.println("ID already exists. Please enter a new ID.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. ID must be a number. Please try again.");
            }
        }
        ArrayList<String> value = new ArrayList<>();
        for (int i = 1; i < headers.length; i++) {
            String field = headers[i];
            if (field.equalsIgnoreCase("Price") ||
                    field.equalsIgnoreCase("Quantity") ||
                    field.equalsIgnoreCase("Weight")) {
                String input = getValidatedNumericInput(field, null);
                value.add(input);
            } else {
                System.out.print("Input " + field + ": ");
                String input = s.nextLine();
                value.add(input);
            }
        }
        catalogue.put(id, value);
        fileIO.addCSVLine(id + "," + String.join(",", value));
    }

    /**
     * Prompts the user to select a specific entry, then removes it from the Map and CSV
     */
    public void removeEntry() {
        int id;
        while (true) {
            System.out.print("Choose ID for Entry to remove: ");
            String inp = s.nextLine();
            if (inp.isEmpty()) {
                System.out.println("Blank input detected (Returning).");
                return;
            }
            try {
                id = Integer.parseInt(inp);
                if (!catalogue.containsKey(id)) {
                    System.out.println("ID not found. Please try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. ID must be a number. Please try again.");
            }
        }
        catalogue.remove(id);
        fileIO.deleteCSVLine(String.valueOf(id));
        System.out.println("Entry removed successfully.");
    }

    /**
     * Allows the user to search based on the name of the item
     */
    public void specificSearch() {
        int id;
        while (true) {
            System.out.print("Enter ID to search: ");
            String inp = s.nextLine();
            if (inp.isEmpty()) {
                System.out.println("Blank input detected (Returning).");
                return;
            }
            try {
                id = Integer.parseInt(inp);
                if (!catalogue.containsKey(id)) {
                    System.out.println("ID not found. Please try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. ID must be a number. Please try again.");
            }
        }
        ArrayList<String> value = catalogue.get(id);
        System.out.println("ID: " + id);
        for (int i = 0; i < value.size(); i++) {
            System.out.println("\t" + headers[i + 1] + ": " + value.get(i));
        }
    }

    /**
     * Prints a numbered list of options, adding an option to exit, and prompts the user to input a number
     *
     * @param menuOptions List of options to print (excluding the "exit" option)
     */
    private void printMenu(String[] menuOptions) {
        for (int i = 0; i < menuOptions.length; i++) { // loop through provided options, prepending each with a number
            System.out.println((i + 1) + ". " + menuOptions[i]);
        }
        System.out.print((menuOptions.length + 1) + ". Exit\nInput: "); // add exit and input prompts to the end of the menu
    }

    public void randomEntry() {
        if (catalogue.isEmpty()) {
            System.out.println("No items in the catalogue to select from");
            return;
        }
        Random rand = new Random(); // Selects a random item from the csv
        Integer randomId = (Integer) catalogue.keySet().toArray()[rand.nextInt(catalogue.size())];
        ArrayList<String> randomEntry = catalogue.get(randomId);

        System.out.println("Here is a random item from the furniture catalogue: ID #" + randomId + ", The item is a " + randomEntry.get(0)); ; //Print statement
        for (int i = 0; i < randomEntry.size(); i++) {
            System.out.println("\t" + headers[i + 1] + ": " + randomEntry.get(i));
        }
        System.out.println();
    }


}