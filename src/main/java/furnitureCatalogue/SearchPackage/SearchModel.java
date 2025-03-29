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

    // Runs entire search routine, should only ever be run from searchController.
    protected void query() {
        String url = "src/main/resources/" + fileName;
        String order = controller.sortMode ? " ASC" : " DESC";
        String query = controller.query.replace(" ","%");
        // Filter requires additional formatting. Filters are used to build chunk of an sql query
        String filter = "";
        for (String s : controller.filters.keySet()) {
            filter += s + " = '" + controller.filters.get(s) + "' AND ";
        }
        for (String s : controller.ranges.keySet()) {
            filter += s + " BETWEEN " + controller.ranges.get(s).get(0)
                    + " AND " + controller.ranges.get(s).get(1) + " AND ";
        }

        // Fix any lingering formatting issues from concatenations
        if(!filter.isEmpty()) {
            filter = filter.substring(0, filter.length() - 4);
            filter = "WHERE " + filter;
        }

        // Database is created locally in memory only reading from the main csv
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:")) {
            // Load main csv into table
            PreparedStatement load = connection.prepareStatement(
                    "CREATE TABLE t AS SELECT * FROM CSVREAD('" + url + "')");
            load.execute();

            // Integer columns are by default loaded as strings, this converts them back to integers
            PreparedStatement reformat = connection.prepareStatement(
                    "ALTER TABLE t ALTER COLUMN id INTEGER;" +
                    "ALTER TABLE t ALTER COLUMN Quantity INTEGER;" +
                    "ALTER TABLE t ALTER COLUMN Weight INTEGER;" +
                    "ALTER TABLE t ALTER COLUMN Price INTEGER;");
            reformat.execute();

            // Main query that generates final result
            String format = formatQuery(query);
            PreparedStatement searchFilter = connection.prepareStatement("(" + format +
                    ") INTERSECT SELECT * FROM t " + filter + "ORDER BY " + controller.sortCategory + order);
            ResultSet queryResult = searchFilter.executeQuery();

            // Output results
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

    // Generates chunk of sql query that allows for some typos in search entry
    private String formatQuery(String query) {
        int depth = (query.length()/3) + 1; // Represents how many wrong characters are permitted
        String formattedQuery = "SELECT * FROM t WHERE Name LIKE '%" + query + "%'";
        if(query.length() < 4) {
            return formattedQuery;
        }
        else if(query.length() == 6) {
            depth = 2;
        }
        else if(query.length() > 9) {
            depth = 3;
        }

        List<String> permutationsBase = recursiveFormatQuery(query, depth); // Recursive function
        // Many results from recursion are unnecessary duplicates, this filters them out.
        List<String> permutations = new ArrayList<>();
        for(String s : permutationsBase) {
            if(!permutations.contains(s)) {
                permutations.add(s);
            }
        }

        // Building the sql chunk
        for(String s : permutations) {
            System.out.println(s);
            formattedQuery += " UNION SELECT * FROM t WHERE Name LIKE '%" + s + "%'";
        }
        return formattedQuery;
    }

    // Recursively adds different combinations of wildcards and stores them in a list
    private List<String> recursiveFormatQuery (String query, int depth) {
        List<String> permutations = new ArrayList<>();
        if(depth > 0) {
            for (int i = 0; i < query.length(); i++) {
                permutations.addAll(recursiveFormatQuery(query.substring(0, i) + "_" + query.substring(i + 1), depth - 1));
            }
        }
        else {
            permutations.add(query);
        }
        return permutations;
    }
}