package furnitureCatalogue.SearchPackage;

import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Objects;

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

    // Returns reference to controller (controller is created on first call).
    public static SearchModel getInstance() {
        if(Objects.isNull(m)) {
            m = new SearchModel();
        }
        return m;
    }

    public void query() {
        //String url = getClass().getClassLoader().getResource(fileName).toString();
        String url = "src/main/resources/" + fileName;
        try(Connection connection = DriverManager.getConnection("jdbc:h2:mem:")) {
            PreparedStatement load = connection.prepareStatement("CREATE TABLE t AS SELECT * FROM CSVREAD('" + url + "')");
            load.execute();

            PreparedStatement search = connection.prepareStatement("SELECT * FROM t ORDER BY Name");
//            PreparedStatement search = connection.prepareStatement("SELECT * FROM t ORDER BY ?");
//            search.setString(1, controller.sortCategory);
            ResultSet queryResult = search.executeQuery();
            while(queryResult.next()) {
                System.out.println(queryResult.getInt("id") + " - " + queryResult.getString("Name"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
