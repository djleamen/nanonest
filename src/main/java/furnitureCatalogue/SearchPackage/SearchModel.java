package furnitureCatalogue.SearchPackage;

import java.sql.*;
import java.util.HashMap;
import java.util.Objects;

public class SearchModel {
    private static SearchModel m; // This class is implemented as a singleton.

    private String fileName;

    // Private constructor, called at first request of class.
    private SearchModel () {
        fileName = "";
    }

    // Returns reference to controller (controller is created on first call).
    public static SearchModel getInstance() {
        if(Objects.isNull(m)) {
            m = new SearchModel();
        }
        return m;
    }
}
