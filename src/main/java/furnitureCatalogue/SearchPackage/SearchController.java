package furnitureCatalogue.SearchPackage;

import java.util.HashMap;
import java.util.Objects;
import java.util.ArrayList;

public class SearchController {
    private static SearchController c; // This class is implemented as a singleton.

    private SearchModel model;
    private SearchView view;

    protected String query; // Actual search string
    protected String sortCategory;
    protected boolean sortMode; // true = ascending order, false = descending order.
    protected HashMap<String, String> filters; // key = csv category, value = quality that should be included (ex. <Colour, Blue>).
    protected HashMap<String, ArrayList<String>> ranges; // key = cdv category, value = number range (ex. <Price, [10, 50]>

    // Private constructor, called at first request of class.
    private SearchController () {
        c = this;

        model = SearchModel.getInstance();
        view = SearchView.getInstance();

        query = "";
        sortCategory = "id";
        sortMode = true; // Ascending order by default.
        filters = new HashMap<>();
        ranges = new HashMap<>();
    }

    // Returns reference to controller (controller is created on first call).
    public static SearchController getInstance() {
        if(Objects.isNull(c)) {
            c = new SearchController();
        }
        return c;
    }

    public void searchQuery() {
        this.filters.clear(); // Clear previous filters before repopulating
        this.ranges.clear();
        // filters.add(header, textfield.getText());
        // repeat for every text field

        // Placeholder for compatibility with command line ui.
        query = view.getQuery();
        sortCategory = view.getSortCategory();
        sortMode = view.getSortMode();
        this.filters = view.filters;
        this.ranges = view.ranges;

//        sortCategory = "Name";
        model.query();
    }

}
