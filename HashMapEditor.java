import java.util.HashMap;

public class HashMapEditor {

    public static boolean editCSVLine(HashMap<Integer, String> data, int lineID, String newLine) {
        if (data.containsKey(lineID)) {
            data.put(lineID, newLine);
            return true;
        }
        return false;
    }

    public static boolean addCSVLine(HashMap<Integer, String> data, int lineID, String newLine) {
        if (!data.containsKey(lineID)) {
            data.put(lineID, newLine);
            return true;
        }
        return false;
    }

    public static boolean deleteCSVLine(HashMap<Integer, String> data, int lineID) {
        if (data.containsKey(lineID)) {
            data.remove(lineID);
            return true;
        }
        return false;
    }
}
