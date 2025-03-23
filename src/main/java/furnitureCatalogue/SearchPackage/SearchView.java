/*
 * This class is responsible for handling the search functionality of the catalogue.
 * It interacts with the SearchModel and SearchView classes to perform the search and display the results.
 */
package furnitureCatalogue.SearchPackage;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SearchView extends JPanel {
    private static SearchView v;

    // In a full implementation, these would be replaced by actual Swing components.
    public String query = ""; // Actual search string
    public String sortCategory = "id";
    public boolean sortMode = true; // true = ascending, false = descending
    public HashMap<String, String> filters = new HashMap<>();
    public HashMap<String, ArrayList<String>> ranges = new HashMap<>();

    // Private constructor
    private SearchView() {
        super();
        v = this;
        // Here you could add text fields, labels, etc. For now we keep it simple.
    }

    // Singleton getter
    public static SearchView getInstance() {
        if (Objects.isNull(v)) {
            v = new SearchView();
        }
        return v;
    }

    public String getQuery() { return query; }
    public String getSortCategory() { return sortCategory; }
    public boolean getSortMode() { return sortMode; }
}