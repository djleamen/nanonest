import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class main {
  public static void main (String[] args) {
    HashMap<Integer, ArrayList<String>> map = new HashMap<>();

    //File dataSet = new File("Air_Quality.csv");
    Scanner fileScanner = new Scanner("Air_Quality.csv");
    while(fileScanner.hasNextLine()) {
      String line = fileScanner.nextLine();
      String splitLine[] = line.split(",");

      ArrayList<String> temp = new ArrayList<>();
      for(int i = 1; i < splitLine.length; i++) {
        temp.add(splitLine[i]);
      }

      map.put(Integer.valueOf(splitLine[0]), temp);
    }

    System.out.println(map.size());

  }
}