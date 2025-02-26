package furnitureCatalogue;

import java.util.*;

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
    private void commandLineMenu() {
        s = new Scanner(System.in);
        boolean running = true;
        while (running) {
            String[] menuOptions = {
                    "Display all Entries",
                    "Edit an entry",
                    "Add an entry",
                    "View Specific Entry"
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
                    viewEntry();
                    break;
                case "5":
                    running = false;
                    break;
                default:
                    break;
            }
        }
        s.close();
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
            System.out.println(key + ": " + value.get(2) + " " + value.get(1));
        }
        System.out.println();
    }

    /**
     * Prompts the user to select a specific entry, and then displays all its information
     */
    public void viewEntry() {
        System.out.print("Choose Entry by ID: ");
        String inp = s.nextLine();
        if (catalogue.containsKey(Integer.parseInt(inp))) {
            ArrayList<String> value = catalogue.get(Integer.parseInt(inp));
            System.out.println(inp); // prints id
            // lists all attributes of entry
            for (int i = 0; i < value.size(); i++) {
                System.out.println("\t" + headers[i] + ": " + value.get(i));
            }
        }
    }

    /**
     * Prompts the user to select a specific entry, then prompts the user to provide new information for each field
     */
    public void editEntry() {
        System.out.print("Choose Entry by ID: ");
        String inp = s.nextLine();
        if (catalogue.containsKey(Integer.parseInt(inp))) {
            ArrayList<String> value = catalogue.get(Integer.parseInt(inp));
            // loops through all headers, prompting the user for data to replace previous data
            for (int i = 0; i < value.size(); i++) {
                System.out.print("Input new " + headers[i] + " to replace " + value.get(i) + ": ");
                String input = s.nextLine();
                value.set(i, input);
            }
            catalogue.put(Integer.parseInt(inp), value);
            fileIO.editCSVLine(inp, inp + "," + String.join(",", value));
        }
    }

    /**
     * Prompts the user for information to create a new entry
     */
    public void addEntry() {
        System.out.print("Choose ID for Entry: ");
        String inp = s.nextLine();
        ArrayList<String> value = new ArrayList<>();
        // loops through all headers, prompting the user for data
        for (String header : headers) {
            System.out.print("Input " + header + ": ");
            String input = s.nextLine();
            value.add(input);
        }
        catalogue.put(Integer.parseInt(inp), value);
        fileIO.addCSVLine(inp + "," + String.join(",", value));
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
}