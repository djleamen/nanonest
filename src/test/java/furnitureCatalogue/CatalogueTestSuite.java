/*
 * This file is used to run all the tests in the suite.
 */

package furnitureCatalogue;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({CatalogueUITest.class, CatalogueFileIOTest.class, LoginTest.class})
public class CatalogueTestSuite {
    // Runs all tests in the suite.
}