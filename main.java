import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class main {
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
  }
}