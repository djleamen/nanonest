package furnitureCatalogue.SearchPackage;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Intended to be JPanel added to CatalogueUI with text fields for user to make search. To maintain
 * compatibility with current ui values are received directly from CatalogueUI and sent to SearchController.
 * @author Ellie Cunningham
 */
public class SearchView extends JPanel {
    /**
     * Static reference of only instance of this object to be created.
     */
    private static SearchView v;

    // In a proper implementation, these would be replaced by actual Swing components.
    /**
     * User inputted search entry.
     */
    public String query = "";
    /**
     * User selected category to sort results by. By default, set to id.
     */
    public String sortCategory = "id";
    /**
     * User selected sort order. true = ascending order, false = descending order. By default, set to ascending.
     */
    public boolean sortMode = true;
    /**
     * Hashmap storing all text based filters (e.g., <Colour, Blue>).
     */
    public HashMap<String, String> filters = new HashMap<>();
    /**
     * Hashmap storing all integer based filters (e.g., <Price, [10, 50]>).
     */
    public HashMap<String, ArrayList<String>> ranges = new HashMap<>();

    /**
     * Constructor takes no inputs, can only be run through initial getInstance() call.
     */
    private SearchView() {
        super();
        v = this;
        // Here you could add text fields, labels, etc. For now we keep it simple.
    }

    /**
     * On first call creates SearchView instance.
     * @return Only instance of SearchView.
     */
    public static SearchView getInstance() {
        if (Objects.isNull(v)) {
            v = new SearchView();
        }
        return v;
    }

    /**
     * Getter for query.
     * @return query User inputted search entry.
     */
    public String getQuery() { return query; }

    /**
     * Getter for sortCategory.
     * @return User selected category to sort results by. By default, set to id.
     */
    public String getSortCategory() { return sortCategory; }

    /**
     * Getter for sortMode.
     * @return User selected sort order. true = ascending order, false = descending order. By default, set to ascending.
     */
    public boolean getSortMode() { return sortMode; }
}