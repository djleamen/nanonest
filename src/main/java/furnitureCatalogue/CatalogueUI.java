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

    public void displayEntries() {
        System.out.println();
        for (Map.Entry<Integer, ArrayList<String>> entry : catalogue.entrySet()) {
            Integer key = entry.getKey();
            ArrayList<String> value = entry.getValue();
            System.out.println(key + ": " + value.get(2) + " " + value.get(1));
        }
        System.out.println();
    }

    public void viewEntry() {
        System.out.print("Choose Entry by ID: ");
        String inp = s.nextLine();
        if (catalogue.containsKey(Integer.parseInt(inp))) {
            ArrayList<String> value = catalogue.get(Integer.parseInt(inp));
            System.out.println(inp);
            for (int i = 0; i < value.size(); i++) {
                System.out.println("\t" + headers[i] + ": " + value.get(i));
            }
        }
    }

    public void editEntry() {
        System.out.print("Choose Entry by ID: ");
        String inp = s.nextLine();
        if (catalogue.containsKey(Integer.parseInt(inp))) {
            ArrayList<String> value = catalogue.get(Integer.parseInt(inp));
            for (int i = 0; i < value.size(); i++) {
                System.out.print("Input new " + headers[i] + " to replace " + value.get(i) + ": ");
                String input = s.nextLine();
                value.set(i, input);
            }
            catalogue.put(Integer.parseInt(inp), value);
            fileIO.editCSVLine(inp, inp + "," + String.join(",", value));
        }
    }

    public void addEntry() {
        System.out.print("Choose ID for Entry: ");
        String inp = s.nextLine();
        ArrayList<String> value = new ArrayList<>();
        for (String header : headers) {
            System.out.print("Input " + header + ": ");
            String input = s.nextLine();
            value.add(input);
        }
        catalogue.put(Integer.parseInt(inp), value);
        fileIO.addCSVLine(inp + "," + String.join(",", value));
    }

    private void printMenu() {
        for (int i = 0; i < menuOptions.length; i++) {
    private void printMenu(String[] menuOptions) {
            System.out.println((i + 1) + ". " + menuOptions[i]);
        }
        System.out.print((menuOptions.length + 1) + ". Exit\nInput: ");
    }
}