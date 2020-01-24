import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Inventory Manager hold all products in the Stores inventory. It is used to add and remove
 * products from the system.
 */
public class InventoryManager {

  /**
   * the list of all the store products
   */
  private ArrayList<Product> storeProducts = new ArrayList<>();
  /**
   * a HashMap of the UPC code to the product
   */
  private HashMap<String, Product> UPCToProducts = new HashMap<>();
  /**
   * the current StockManager of the system
   */
  private StockManager stockManager;
  /**
   * the current PriceManager of the system
   */
  private PriceManager priceManager;

  /**
   * Create a InventoryManager with the stockManager and priceManager.
   *
   * @param stockManager the stockManager for the system
   * @param priceManager the priceManager for the system
   */
  InventoryManager(StockManager stockManager, PriceManager priceManager) {
    this.stockManager = stockManager;
    this.priceManager = priceManager;
  }


  /**
   * Create a InventoryManager from a file
   *
   * @param filePath file which the class was serialized to
   */
  InventoryManager(String filePath, StockManager stockManager, PriceManager priceManager)
      throws ClassNotFoundException, IOException {

    this(stockManager, priceManager);
    //  Taken from StudentManager.java in CSC207 lecture example
    // Reads serializable objects from filePath.
    File file = new File(filePath);

    if (file.exists()) {
      readFromFile(filePath);
    } else {
      file.createNewFile();
      //log the event
      Logger.addEvent("InventoryManager", "InventoryManager",
          "created a new file for serialization", false);
    }
  }

  /**
   * Set the store's ArrayList of products in inventory.
   *
   * @param products the ArrayList of inventory product.
   */
  public void setStoreProducts(ArrayList<Product> products) {
    this.storeProducts = products;
    for (Product product : products) {
      UPCToProducts.put(product.getUpc(), product);
    }
    //add the list of products into PriceManager and StockManager
    try {
      priceManager.registerProduct(products);
      stockManager.registerProduct(products);
      //log the event
      Logger.addEvent("InventoryManager", "setStoreProducts",
          "the store products were set", false);
    } catch (NullPointerException e) {
      //log the event
      Logger.addEvent("InventoryManager", "setStoreProducts",
          "NullPointerException - the store products could not be set", false);
    }
  }

  /**
   * Return an ArrayList of the store's products.
   */
  public ArrayList<Product> getStoreProducts() {
    return storeProducts;
  }

  /**
   * Return the HashMap of the product UPC number to the product
   */
  public HashMap<String, Product> getUPCToProducts() {
    return UPCToProducts;
  }

  /**
   * Returns the stockManager for this inventory
   * @return the stockManager for the inventory
   */
  public StockManager getStockManager(){ return stockManager; }

  /**
   * Returns the priceManager for this inventory
   * @return the priceManager for the inventory
   */
  public PriceManager getPriceManager(){ return priceManager; }

  /**
   * Find a product based on the UPC.
   *
   * @param UPC the UPC of the product
   */
  public Product findProduct(String UPC) {
    try {
      Logger.addEvent("InventoryManager", "findProduct",
          "searched for the UPC inputted: " + UPC, false);
      return UPCToProducts.get(UPC);
    } catch (NullPointerException e) {
      Logger.addEvent("InventoryManager", "findProduct",
          "an invalid UPC was inputted: " + UPC, true);
      return null;
    }
  }

  /**
   * Add a product to the system.
   *
   * @param product the product being introduced to the system
   */
  public void addProduct(Product product) {
    if (!storeProducts.contains(product)) {
      storeProducts.add(product);
      UPCToProducts.put(product.getUpc(), product);
      //update the priceManager and the stockManager
      priceManager.registerProduct(product);
      stockManager.registerProduct(product);
      //log the event
      Logger.addEvent("InventoryManager", "addProduct",
          product.getName() + " was added to the system", true);
    }
    //log the event
    Logger.addEvent("InventoryManager", "addProduct",
        product.getName() + " already exists. It was not added to the system.", true);
  }

  /**
   * Remove a product from the system.
   *
   * @param product the product being removed from the system
   */
  public void removeProduct(Product product) {
    try {
      storeProducts.remove(product);
      UPCToProducts.remove(product.getUpc());
      //update the priceManager and the stockManager
      priceManager.deleteProduct(product);
      stockManager.deleteProduct(product);
      Logger.addEvent("InventoryManager", "removeProduct",
          product.getName() + " was removed from the system", true);
    } catch (NullPointerException e) {
      //log the event
      Logger.addEvent("InventoryManager", "removeProduct",
          "NullPointException - the product being removed does not exist", true);
    }
  }

  /**
   * Used for de-serializing InventoryManager
   *
   * @param filePath the path where the file is saved
   */
  private void readFromFile(String filePath) throws ClassNotFoundException {
    try {
      // Taken from StudentManager.java in CSC207 lecture example
      InputStream file = new FileInputStream(filePath);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);

      // Deserialize
      storeProducts = ((ArrayList<Product>) input.readObject());
      UPCToProducts = ((HashMap<String, Product>) input.readObject());
      //log the event
      Logger.addEvent("InvetoryManager", "readFromFile",
          "loaded Inventory Manager from " + filePath, false);
    } catch (IOException e) {
      //log the event
      Logger.addEvent("InventoryManager", "readFromFile",
          "IOException-unable to load Inventory Manager from " + filePath, false);
    }
  }
}
