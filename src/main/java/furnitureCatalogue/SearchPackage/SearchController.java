package furnitureCatalogue.SearchPackage;

import java.util.HashMap;
import java.util.Objects;
import java.util.ArrayList;

/**
 * Handles communication between SearchView and SearchModel classes.
 * @author Ellie Cunningham
 */
public class SearchController {
    /**
     * Static reference of only instance of this object to be created.
     */
    private static SearchController c;
    /**
     * Reference to only instance of model.
     */
    private SearchModel model;
    /**
     * Reference to only instance of view.
     */
    private SearchView view;

    /**
     * User inputted search entry.
     */
    protected String query;
    /**
     * User selected category to sort results by.
     */
    protected String sortCategory;
    /**
     * User selected sort order. true = ascending order, false = descending order.
     */
    protected boolean sortMode;
    /**
     * Hashmap storing all text based filters (e.g., <Colour, Blue>).
     */
    protected HashMap<String, String> filters;
    /**
     * Hashmap storing all integer based filters (e.g., <Price, [10, 50]>).
     */
    protected HashMap<String, ArrayList<String>> ranges;

    /**
     * Constructor takes no inputs, can only be run through initial getInstance() call.
     */
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

    /**
     * On first call creates SearchController instance.
     * @return Only instance of SearchController.
     */
    public static SearchController getInstance() {
        if (Objects.isNull(c)) {
            c = new SearchController();
        }
        return c;
    }

    /**
     * Grabs information from SearchView needed to execute query then calls query() in SearchModel.
     */
    public void searchQuery() {
        query = view.getQuery();
        sortCategory = view.getSortCategory();
        sortMode = view.getSortMode();
        this.filters = view.filters;
        this.ranges = view.ranges;
        model.query();
    }
}