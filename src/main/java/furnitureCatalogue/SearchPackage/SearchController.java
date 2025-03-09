package furnitureCatalogue.SearchPackage;

import java.util.HashMap;
import java.util.Objects;

public class SearchController {
    private static SearchController c; // This class is implemented as a singleton.

    private SearchModel model;
    private SearchView view;

    protected String query; // Actual search string
    protected String sortCategory;
    protected boolean sortMode; // true = ascending order, false = descending order.
    protected HashMap<String, String> filters;

    // Private constructor, called at first request of class.
    private SearchController () {
        c = this;

        model = SearchModel.getInstance();
        view = SearchView.getInstance();

        query = "";
        sortCategory = "id";
        sortMode = true;
        filters = new HashMap<>();
    }

    // Returns reference to controller (controller is created on first call).
    public static SearchController getInstance() {
        if(Objects.isNull(c)) {
            c = new SearchController();
        }
        return c;
    }

    public void searchQuery() {
        filters.clear(); // Clear previous filters before repopulating
        // filters.add(header, textfield.getText());
        // repeat for every text field

        // Placeholder for compatibility with command line ui.
        query = view.getQuery();
        sortCategory = view.getSortCategory();
        sortMode = view.getSortMode();

//        sortCategory = "Name";
        model.query();
    }

}
