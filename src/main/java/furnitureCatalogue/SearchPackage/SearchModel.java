package furnitureCatalogue.SearchPackage;

import furnitureCatalogue.CatalogueUI;

import java.net.URL;
import java.sql.*;
import java.util.*;

public class SearchModel {
    private static SearchModel m; // This class is implemented as a singleton.
    private SearchController controller;

    private String fileName;

    // Private constructor, called at first request of class.
    private SearchModel() {
        m = this;
        controller = SearchController.getInstance();
        fileName = "Sample.csv";
    }

    // Returns reference to model (model is created on first call).
    protected static SearchModel getInstance() {
        if (Objects.isNull(m)) {
            m = new SearchModel();
        }
        return m;
    }

    public void query() {
        String url = "src/main/resources/" + fileName;
        String order;
        String filter = "";
        if (controller.sortMode) {
            order = " ASC";
        } else {
            order = " DESC";
        }

        for (String s : controller.filters.keySet()) {
            filter += s + " = '" + controller.filters.get(s) + "' AND ";
        }
        for (String s : controller.ranges.keySet()) {
            filter += s + " BETWEEN " + controller.ranges.get(s).get(0) + " AND " + controller.ranges.get(s).get(1) + " AND ";
        }
        filter = filter.substring(0, filter.length() - 4);

        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:")) {
            PreparedStatement load = connection.prepareStatement("CREATE TABLE t AS SELECT * FROM CSVREAD('" + url + "')");
            load.execute();

            PreparedStatement reformat = connection.prepareStatement("ALTER TABLE t ALTER COLUMN id INTEGER;" +
                    "ALTER TABLE t ALTER COLUMN Quantity INTEGER;" +
                    "ALTER TABLE t ALTER COLUMN Weight INTEGER;");
            reformat.execute();

//            PreparedStatement search = connection.prepareStatement("SELECT * FROM t WHERE " + filter + "Name = '%" + controller.query + "%' ORDER BY " + controller.sortCategory + order);
            PreparedStatement search = connection.prepareStatement("SELECT * FROM t WHERE " +
                    filter + " ORDER BY " + controller.sortCategory + order);
            ResultSet queryResult = search.executeQuery();
            List<String> headers = Arrays.asList("Name", "Price", "Furniture Type", "Colour", "Materials", "Size", "Quantity", "Company", "Style", "Weight");
            while (queryResult.next()) {
                StringBuilder itemString = new StringBuilder(queryResult.getInt("id") + "\t");
                for (int i = 0; i < headers.size(); i++) {
                    itemString.append(queryResult.getString(headers.get(i)));
                    for (int j = 0; j <= Math.ceil((double) (CatalogueUI.maxLengths[i]) / 4.0) - Math.floor((double) (queryResult.getString(headers.get(i)).length()) / 4.0); j++) {
                        itemString.append("\t");
                    }
                }
                System.out.println(itemString);
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
