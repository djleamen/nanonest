package furnitureCatalogue;

import java.util.*;

import furnitureCatalogue.SearchPackage.*;

public class CatalogueUI {
    public HashMap<Integer, ArrayList<String>> catalogue;
    public CatalogueFileIO fileIO;
    public int[] maxLengths = new int[10];
    public String[] headers;
    private Scanner s;
    private SearchController c; // Pointer to SearchController object.
    private SearchView v; // Pointer to SearchView object.

    public static void main(String[] args) {
        CatalogueUI catalogueUI = new CatalogueUI();
    }

    public CatalogueUI() {
        fileIO = new CatalogueFileIO("Sample.csv", this);
        c = SearchController.getInstance();
        v = SearchView.getInstance();
//        c.searchQuery();
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
                    "Search by ID",
                    "Advanced Search (Through filtering and sorting)",
                    "Display Random Entry"  // My additon - Parish
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
                    advancedSearch();
                    break;
                case "8":
                    randomEntry();
                    break;
                case "9":
                    running = false;
                    break;
                default:
                    break;
            }
        }
        s.close();
    }

    /**
     * Prints one line to the console with all the headers, each with enough space to accommodate the longest entry.
     */
    private void printTableHeader() {
        StringBuilder headerString = new StringBuilder("id\t");
        for (int i = 1; i < headers.length; i++) {
            headerString.append(headers[i]);
            // calculate how much space each column needs, and insert that amount of space
            for (int j = 0; j <= Math.ceil((double) (maxLengths[i - 1]) / 4.0) - Math.floor((double) (headers[i].length()) / 4.0); j++) {
                headerString.append("\t");
            }
        }
        System.out.println(headerString);
    }

    /**
     * Prints one row of the output table to the console
     *
     * @param entry the entry to print a row for
     */
    private void printTableRow(Map.Entry<Integer, ArrayList<String>> entry) {
        Integer key = entry.getKey();
        ArrayList<String> value = entry.getValue();
        // prints each entry into a table
        StringBuilder itemString = new StringBuilder(key + "\t");
        for (int i = 0; i < value.size(); i++) {
            itemString.append(value.get(i));
            // calculate how much space each column needs, and insert that amount of space
            for (int j = 0; j <= Math.ceil((double) (maxLengths[i]) / 4.0) - Math.floor((double) (value.get(i).length()) / 4.0); j++) {
                itemString.append("\t");
            }
        }
        System.out.println(itemString);
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
        System.out.print("Choose Entry by ID: ");
        String inp = s.nextLine();
        if (catalogue.containsKey(Integer.parseInt(inp))) {
            ArrayList<String> value = catalogue.get(Integer.parseInt(inp));
            System.out.println(inp); // prints id
            // lists all attributes of entry
            for (int i = 0; i < value.size(); i++) {
                System.out.println("\t" + headers[i + 1] + ": " + value.get(i));
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
                System.out.print("Input new " + headers[i + 1] + " to replace " + value.get(i) + ": ");
                String input = s.nextLine();
                if (input.equals("")) {
                    value.set(i, value.get(i));
                } else {
                    value.set(i, input);
                }
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
        for (int i = 1; i < headers.length; i++) {
            System.out.print("Input " + headers[i] + ": ");
            String input = s.nextLine();
            value.add(input);
        }
        catalogue.put(Integer.parseInt(inp), value);
        fileIO.addCSVLine(inp + "," + String.join(",", value));
    }

    /**
     * Prompts the user to select a specific entry, then removes it from the Map and CSV
     */
    public void removeEntry() {
        System.out.print("Choose ID for Entry: ");
        String inp = s.nextLine();

        if (catalogue.containsKey(Integer.parseInt(inp))) {
            catalogue.remove(Integer.parseInt(inp));
            fileIO.deleteCSVLine(inp); // Pass only the ID
            System.out.println("Entry removed successfully.");
        } else {
            System.out.println("Entry not found.");
        }
    }

    /**
     * Allows the user to search based on the name of the item
     */
    public void specificSearch() {
        System.out.println("Enter Search: ");
        String inp = s.nextLine();

        for (ArrayList<String> value : catalogue.values()) {
            if (value.getFirst().equals(inp)) {
                System.out.println(inp); // prints id
                // lists all attributes of entry
                for (int i = 0; i < value.size(); i++) {
                    System.out.println("\t" + headers[i + 1] + ": " + value.get(i));
                }
            }
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

    // Sorts and Filters csv by any number of categories.
    public void advancedSearch() {
        v.filters.clear();
        v.ranges.clear();

        while(true) {
            System.out.println("Fields: " + String.join(", ", headers));
            System.out.print("Which field would you like to filter? (Or ENTER to proceed): ");
            String field = s.nextLine();
            if(field.isEmpty()) {
                break;
            }

            int index = Arrays.asList(headers).indexOf(field);
            if (index != -1 && index != 1) { // if field is valid for filtering

                if (index == 3 || index == 4 || index == 5 || index == 6 || index == 8 || index == 9) {
                    System.out.println("Enter value:");
                    String filterInput;

                    // get list of inputs from user to filter catalogue
                    filterInput = s.nextLine();
                    v.filters.put(field, filterInput);
                }
                else if (index == 0 || index == 2 || index == 7 || index == 10) {
                    System.out.println("Enter minimum value for " + field + ": ");
                    String minInput = s.nextLine();
                    System.out.println("Enter maximum value for " + field + ": ");
                    String maxInput = s.nextLine();
                    if (minInput.matches("-?\\d+") && maxInput.matches("-?\\d+")) {
                        // check that the inputs are integers (negative numbers allowed)
                        int minValue = Integer.parseInt(minInput);
                        int maxValue = Integer.parseInt(maxInput);

                        if (minValue > maxValue) {
                            // swap values if min is more than max
                            int temp = minValue;
                            minValue = maxValue;
                            maxValue = temp;
                        }
                        ArrayList<String> input = new ArrayList<>();
                        input.add(String.valueOf(minValue));
                        input.add(String.valueOf(maxValue));
                        v.ranges.put(field, input);
                    } else System.out.println("Invalid input (must be integers)");
                }
            }
            else {
                System.out.println(field + " is not a valid field to filter with.");
            }
        }

        // Feel free to change this however you like when adding prompts for filters
        System.out.println("Sort by what category?");
        v.sortCategory = s.nextLine();
        System.out.println("Ascending or Descending? (A/D)");
        String temp = s.nextLine();
        if(temp.equals("A")) {
            v.sortMode = true;
        }
        else if(temp.equals("D")) {
            v.sortMode = false;
        }
        else {
            System.out.println("Invalid Input.");
            return;
        }
        printTableHeader();
        c.searchQuery();
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
    }


}