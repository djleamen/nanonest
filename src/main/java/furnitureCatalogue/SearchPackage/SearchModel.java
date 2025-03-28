/*
 * This class is responsible for handling the search functionality of the catalogue.
 * It interacts with the SearchModel and SearchView classes to perform the search and display the results.
 */

package furnitureCatalogue.SearchPackage;

import furnitureCatalogue.CatalogueUI;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class SearchModel {
    private static SearchModel m; // Singleton instance
    private SearchController controller;
    private String fileName;

    // Private constructor
    private SearchModel() {
        m = this;
        controller = SearchController.getInstance();
        fileName = "Sample.csv";
    }

    // Singleton getter
    protected static SearchModel getInstance() {
        if (Objects.isNull(m)) {
            m = new SearchModel();
        }
        return m;
    }

    public void query() {
        String url = "src/main/resources/" + fileName;
        String order = controller.sortMode ? " ASC" : " DESC";
        String query = "%" + controller.query + "%";
        String filter = "";
        for (String s : controller.filters.keySet()) {
            filter += s + " = '" + controller.filters.get(s) + "' AND ";
        }
        for (String s : controller.ranges.keySet()) {
            filter += s + " BETWEEN " + controller.ranges.get(s).get(0)
                    + " AND " + controller.ranges.get(s).get(1) + " AND ";
        }
        // Remove trailing " AND "

        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:")) {
            PreparedStatement load = connection.prepareStatement(
                    "CREATE TABLE t AS SELECT * FROM CSVREAD('" + url + "')");
            load.execute();

            PreparedStatement reformat = connection.prepareStatement(
                    "ALTER TABLE t ALTER COLUMN id INTEGER;" +
                    "ALTER TABLE t ALTER COLUMN Quantity INTEGER;" +
                    "ALTER TABLE t ALTER COLUMN Weight INTEGER;" +
                    "ALTER TABLE t ALTER COLUMN Price INTEGER;");
            reformat.execute();
            System.out.println((query.length()/3) + 1);
//            String format = formatQuery("astic", connection);

            PreparedStatement search = connection.prepareStatement(
                    "SELECT * FROM t WHERE " + filter + "Name LIKE ? ORDER BY " + controller.sortCategory + order);
            search.setString(1, query);
            ResultSet queryResult = search.executeQuery();

            List<String> headers = Arrays.asList("Name", "Price", "Furniture Type", "Colour",
                                                   "Materials", "Size", "Quantity", "Company", "Style", "Weight");
            while (queryResult.next()) {
                StringBuilder itemString = new StringBuilder(queryResult.getInt("id") + "\t");
                for (int i = 0; i < headers.size(); i++) {
                    itemString.append(queryResult.getString(headers.get(i)));
                    // Use CatalogueUI.maxLengths for spacing (assumes these are set)
                    for (int j = 0; j <= Math.ceil((double)(CatalogueUI.maxLengths[i]) / 8.0)
                                        - Math.floor((double)(queryResult.getString(headers.get(i)).length()) / 8.0); j++) {
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

    private String formatQuery(String query, Connection conn) throws SQLException {
        int depth = (query.length()/3) + 1;
        String formattedQuery = "SELECT * FROM t WHERE Name LIKE '%" + query + "%'";
        if(query.length() < 4) {
            return formattedQuery;
        }
        else if(query.length() == 6) {
            depth = 2;
        }
        System.out.println(depth);

        return recursiveFormatQuery(formattedQuery, query, depth, conn);
    }

    private String recursiveFormatQuery (String formattedQuery, String query, int depth, Connection conn) throws SQLException {
//        System.out.println(query);
        formattedQuery += " UNION SELECT * FROM t WHERE Name LIKE '%" + query + "%'";
        if(depth > 0) {
            for (int i = 0; i < query.length(); i++) {
                query += recursiveFormatQuery(formattedQuery, query.substring(0, i) + "_" + query.substring(i + 2), depth - 1, conn);
            }
        }
        return query;
    }
}