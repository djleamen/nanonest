/*
 * This class is responsible for handling the search functionality of the catalogue.
 * It interacts with the SearchModel and SearchView classes to perform the search and display the results.
 */

package furnitureCatalogue.SearchPackage;

import java.util.HashMap;
import java.util.Objects;
import java.util.ArrayList;

public class SearchController {
    private static SearchController c; // Singleton instance
    private SearchModel model;
    private SearchView view;
    
    protected String query; // Actual search string
    protected String sortCategory;
    protected boolean sortMode; // true = ascending order, false = descending order.
    protected HashMap<String, String> filters; // e.g., <Colour, Blue>
    protected HashMap<String, ArrayList<String>> ranges; // e.g., <Price, [10, 50]>

    // Private constructor
    private SearchController() {
        c = this;
        model = SearchModel.getInstance();
        view = SearchView.getInstance();
        query = "";
        sortCategory = "id";
        sortMode = true; // default: ascending
        filters = new HashMap<>();
        ranges = new HashMap<>();
    }
    
    // Singleton getter
    public static SearchController getInstance() {
        if (Objects.isNull(c)) {
            c = new SearchController();
        }
        return c;
    }
    
    public void searchQuery() {
        query = view.getQuery();
        sortCategory = view.getSortCategory();
        sortMode = view.getSortMode();
        this.filters = view.filters;
        this.ranges = view.ranges;
        model.query();
    }
}