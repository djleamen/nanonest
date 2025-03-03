package furnitureCatalogue;

public class TestUnit {
    public static void main(String[] args) {
        CatalogueUI catalogueUI = new CatalogueUI();
        CatalogueFileIO catalogueFileIO = new CatalogueFileIO("Sample.csv", catalogueUI);

        catalogueUI.testCatalogueUI();
        catalogueFileIO.testCatalogueFileIO();
    }
}
