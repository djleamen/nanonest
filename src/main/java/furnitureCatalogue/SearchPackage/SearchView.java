package furnitureCatalogue.SearchPackage;

import java.util.HashMap;
import java.util.Objects;

// Placeholder to be properly implemented in iteration 3.
public class SearchView {
    private static SearchView v;

    // All these variables should be text fields or other swing objects in final product.
    public String query; // Actual search string
    public String sortCategory;
    public boolean sortMode; // true = ascending order, false = descending order.
    public HashMap<String, String> filters = new HashMap<>();

    // Private constructor, called at first request of class.
    private SearchView() {
        v = this;
    }

    // Returns reference to view (view is created on first call).
    public static SearchView getInstance() {
        if(Objects.isNull(v)) {
            v = new SearchView();
        }
        return v;
    }

    public String getQuery() {return query;}
    public String getSortCategory() {return sortCategory;}
    public boolean getSortMode() {return sortMode;}
}
