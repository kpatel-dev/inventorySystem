import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * InventoryLoader parses information from .txt files and creates Product objects.
 */
class InventoryLoader extends FileLoader {

  /**
   * Holds an InventoryManager object to load Product objects into.
   */
  private InventoryManager inventoryManager;

  /**
   * Create a new InventoryLoader.
   *
   * @param fileInput: Open file to read information from
   * @param locationManager:  LocationManager object that holds all Section objects
   * @param inventoryManager: InventoryManager object to load Product objects into
   */
  InventoryLoader(BufferedReader fileInput, LocationManager locationManager,
      InventoryManager inventoryManager) {
    super(fileInput, locationManager);
    this.inventoryManager = inventoryManager;
  }

  /**
   * Parse every line from fileInput and convert every line formatted as:
   * UPC | name | quantity | price | section | threshold | distributor | cost
   * into a Product object.
   *
   * @return An arraylist of Products to be added to inventoryLoader.
   */
  @Override
  ArrayList<Product> loadItems() throws IOException {
    ArrayList<Product> products = new ArrayList<>();
    String line = fileInput.readLine();   // First line contains headers only; skip
    line = fileInput.readLine();

    while(line != null) {
      String[] productInfo = line.trim().split("\\|");

      for(int i = 0; i < productInfo.length; i++) {
        productInfo[i] = productInfo[i].trim();
      }

      Product currentProduct = new Product(productInfo[0], productInfo[1],
          Integer.parseInt(productInfo[2]), new Price(Double.parseDouble(productInfo[3])),
          locationManager.getSection(productInfo[4]), Integer.parseInt(productInfo[5]),
          productInfo[6], Double.parseDouble(productInfo[7]), Integer.parseInt(productInfo[8]));
      products.add(currentProduct);
      line = fileInput.readLine();
    }

    return products;
  }

  /**
   * Send the loadedItems to inventoryManager to be added.
   *
   * @param loadedItems: An arraylist of Products to be added to inventoryLoader.
   */
  void sendProducts(ArrayList<Product> loadedItems) {
    inventoryManager.setStoreProducts(loadedItems);
    Logger.addEvent("InventoryLoader", "sendProducts",
        "Initial inventory was loaded from a .txt file.", false);
  }
}
