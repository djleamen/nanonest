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

    // Returns reference to model (model is created on first call).
    protected static SearchModel getInstance() {
        if(Objects.isNull(m)) {
            m = new SearchModel();
        }
        return m;
    }

    public void query() {
        String url = "src/main/resources/" + fileName;
        String order;
        if(controller.sortMode) {
            order = " ASC";
        }
        else {
            order = " DESC";
        }
        try(Connection connection = DriverManager.getConnection("jdbc:h2:mem:")) {
            PreparedStatement load = connection.prepareStatement("CREATE TABLE t AS SELECT * FROM CSVREAD('" + url + "')");
            load.execute();

//            PreparedStatement reformat = connection.prepareStatement("ALTER TABLE t MODIFY COLUMN id INTEGER");
//            reformat.execute();

            PreparedStatement search = connection.prepareStatement("SELECT * FROM t ORDER BY " + controller.sortCategory + order);
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
