import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class main {
  public static void main (String[] args) {
    HashMap<Integer, ArrayList<String>> map = new HashMap<>();

    try {
      File dataSet = new File("Air_Quality.csv");
      Scanner fileScanner = new Scanner(dataSet);
      fileScanner.nextLine();

      while(fileScanner.hasNext()) {
        String line = fileScanner.nextLine();
        String splitLine[] = line.split(",");

        ArrayList<String> temp = new ArrayList<>();
        for(int i = 1; i < splitLine.length; i++) {
          temp.add(splitLine[i]);
        }

        map.put(Integer.parseInt(splitLine[0]), temp);
      }

      fileScanner.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    System.out.println(map.size());


  }
}