import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class main {
  public static Scanner s;
  public static void main (String[] args) {
    HashMap<Integer, ArrayList<String>> map = new HashMap<>();

    try {
      // Initialize File scanner
      File dataSet = new File("Air_Quality.csv");
      Scanner fileScanner = new Scanner(dataSet);
      fileScanner.nextLine(); // Skips over first line with column labels, should be changed later

      while(fileScanner.hasNext()) { // For each line in the csv
        String line = fileScanner.nextLine();
        String splitLine[] = line.split(","); // Columns split into string array

        // Then placed into list
        ArrayList<String> temp = new ArrayList<>();
        for(int i = 1; i < splitLine.length; i++) {
          temp.add(splitLine[i]);
        }

        // And finally added to the hashmap
        map.put(Integer.parseInt(splitLine[0]), temp);
      }

      fileScanner.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    s = new Scanner(System.in);
    Boolean running = true;
    while (running) {
        
        System.out.println("1. Display all Entries");
        System.out.println("2. Edit an entry");
        System.out.println("3. Add an entry");
        System.out.println("4. View Specific Entry");
        System.out.println("5. Exit");
        System.out.print("Input: ");

        String inp = s.nextLine();
        switch (inp) {
            case "1":
                displayEntries(map);
                break;
            case "2":
                // editEntry();
                break;
            case "3":
                // addEntry();
                break;
            case "4":
                viewEntry(map);
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

  public static void displayEntries(Map<Integer, ArrayList<String>> map) {
        System.out.println();
        for (Map.Entry<Integer, ArrayList<String>> entry : map.entrySet()) {
            Int key = entry.getKey();
            ArrayList<String> value = entry.getValue();
            System.out.println(key + ": " + value);
        }
        System.out.println();
    }

    public static void viewEntry(Map<Integer, ArrayList<String>> map) {
        System.out.print("Choose Entry by ID: ");
        String inp = s.nextLine();
        if (map.containsKey(Integer.parseInt(inp))) {
            ArrayList<String> value = map.get(Integer.parseInt(inp));
            System.out.println(inp + ": " + value);
        }
    }

    public static void editEntry(Map<Integer, ArrayList<String>> map) {
        System.out.print("Choose Entry by ID: ");
        String inp = s.nextLine();
        if (map.containsKey(Integer.parseInt(inp))) {
            ArrayList<String> value = map.get(Integer.parseInt(inp));

        }

    }
}