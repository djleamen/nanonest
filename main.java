import java.io.*;
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
<<<<<<< HEAD

    System.out.println(map.size());
=======
>>>>>>> a1af7f0c2f5047a63bd05c312ee9758bec3b3dae
  }

  public static boolean addCSVLine(String filename, String newLine) {
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(filename, true));
      writer.write(newLine);
      writer.newLine();
      writer.flush();
      writer.close(); 
      return true;
    } catch (IOException e) {
      System.err.println("Error writing to CSV file: " + e.getMessage());
      return false;
    }
  }

}