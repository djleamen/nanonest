package furnitureCatalogue;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({CatalogueUITest.class, CatalogueFileIOTest.class})
public class CatalogueTestSuite {
    // Runs both test files
}
