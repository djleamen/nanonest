package furnitureCatalogue.SearchPackage;

import furnitureCatalogue.CatalogueUI;
import java.net.URL;
import java.sql.*;
import java.util.*;

/**
 * Uses the JDBC library to run SQL queries. Processes output for advanced search including quality of
 * life features such as filters, sorting, and relevancy searching all in one routine.
 * @author Ellie Cunningham
 * @author Lucas Fischer
 */
public class SearchModel {
    /**
     * Static reference of only instance of this object to be created.
     */
    private static SearchModel m;
    /**
     * Reference to only instance of controller.
     */
    private SearchController controller;
    /**
     * Name of file to be searched through.
     */
    private String fileName;

    /**
     * Constructor takes no inputs, can only be run through initial getInstance() call.
     */
    private SearchModel() {
        m = this;
        controller = SearchController.getInstance();
        fileName = "Sample.csv";
    }

    /**
     * On first call creates SearchModel instance. Should only ever be run from controller.
     * @return Only instance of SearchModel.
     */
    protected static SearchModel getInstance() {
        if (Objects.isNull(m)) {
            m = new SearchModel();
        }
        return m;
    }

    // Runs entire search routine, should only ever be run from searchController.

    /**
     * Emulates database file in memory from csv and executes SQL queries to generate and output results
     * for advanced search. Results are output to stream rather than returned.
     */
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

    /**
     * Formats search entry to include 'wildcard characters' and prebuild a segment of SQL code.
     * Leniency depends on length of query.
     * @param query Given search entry.
     * @return Segment of SQL query pertaining to relevancy search.
     */
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
            formattedQuery += " UNION SELECT * FROM t WHERE Name LIKE '%" + s + "%'";
        }
        return formattedQuery;
    }

    /**
     * Recursively adds wildcard character '_' to different positions in query.
     * @param query Given search entry, may contain wildcards depending on number of recursive calls.
     * @param depth Tracks remaining recursive calls. Subtracts 1 per iterations, breaks when > 0.
     * @return Permutations of wildcard combinations.
     */
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