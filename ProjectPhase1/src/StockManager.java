import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * StockManager modifies the quantity of products and keeps track of whether they are in or out of
 * stock.
 */
public class StockManager {

  private ArrayList<Product> inStock; // Arraylist of products that are currently in stock
  private ArrayList<Product> outOfStock;  // Arraylist of products that are currently out of stock
  private OrderManager orderManager;

  /**
   * Create a new StockManager.
   */
  public StockManager(OrderManager orderManager) {
    this.inStock = new ArrayList<>();
    this.outOfStock = new ArrayList<>();
    this.orderManager = orderManager;
  }

  /**
   * Create a new StockManager using the serialized information stored in file with path filePath.
   */
  public StockManager(String filePath, OrderManager orderManager)
      throws ClassNotFoundException, IOException {
    this.orderManager = orderManager;
    //  Taken from StudentManager.java in CSC207 lecture example
    // Reads serializable objects from filePath.
    File file = new File(filePath);

    if (file.exists()) {
      readFromFile(filePath);
    } else {
      file.createNewFile();
    }
  }

  /**
   * Register a new Product product into this StockManager.
   */
  void registerProduct(Product product) {

    if (inStock.contains(product) && outOfStock.contains(product)) {
      Logger.addEvent("StockManager", "registerProduct",
          "Product " + product.getUpc() + " is already registered in stock manager.",
          true);
    }

    if (product.getQuantity() > 0) {
      inStock.add(product);
      Logger.addEvent("StockManager", "registerProduct",
          "Product " + product.getUpc() + " is in stock.", false);
    } else {
      outOfStock.add(product);
      Logger.addEvent("StockManager", "registerProduct",
          "Product " + product.getUpc() + " is out of stock.", false);
    }

    Logger.addEvent("StockManager", "registerProduct",
        "Product " + product.getUpc() + " was registered into stock manager.",
        true);
  }

  /**
   * Register every Product in products into this StockManager.
   */
  void registerProduct(ArrayList<Product> products) {
    for (Product product : products) {
      registerProduct(product);
    }
  }

  /**
   * Delete an existing Product product from this StockManager.
   */
  void deleteProduct(Product product) {
    if (inStock.contains(product)) {
      inStock.remove(product);
      Logger.addEvent("StockManager", "removeProduct",
          "Product " + product.getUpc() + " was removed from stock manager.",
          true);
    } else if (outOfStock.contains(product)) {
      outOfStock.remove(product);
      Logger.addEvent("StockManager", "removeProduct",
          "Product " + product.getUpc() + " was removed from stock manager.",
          true);
    } else {
      Logger.addEvent("StockManager", "removeProduct",
          "Product " + product.getUpc() + " could not be removed from stock manager.",
          true);
    }
  }

  /**
   * Increase Product product's quantity by one. Update inStock and outOfStock.
   */
  public void add(Product product) {
    add(product, 1);
  }

  /**
   * Increase Product product's quantity by one. Update inStock and outOfStock.
   */
  public void add(Product product, int quantity) {

    product.addQuantity(quantity);

    if (outOfStock.contains(product)) {
      isInStock(product);
    }

    try {
      inStock.contains(product);
    } catch (NoSuchElementException e) {
      System.out.println("Product is not registered in stock manager.");
    }

    Logger.addEvent("StockManager", "add",
        "Product " + product.getUpc() + "'s quantity was increased by " + quantity + ".",
        true);
  }

  public void remove(Product product) {

    remove(product, 1);
  }

  /**
   * Returns the list of items in stock
   */
  ArrayList<Product> getInStock() {
    return inStock;
  }

  /**
   * Returns the list of items out of stock
   */
  ArrayList<Product> getOutOfStock() {
    return outOfStock;
  }

  /**
   * Decrease Product product's quantity by int quantity. Update inStock and outOfStock.
   * Place an order for product if its new quantity is below its threshold quantity.
   */
  void remove(Product product, int quantity) {

    try {
      product.removeQuantity(quantity);
    } catch (InvalidAmountException e) {
      System.out.println("Amount being sold exceeds quantity of product in stock.");
    }

    if (product.getQuantity() < product.getThreshold()) {
      orderManager.makeOrder(product, 3 * product.getThreshold());

      if (product.getQuantity() == 0) {
        isOutOfStock(product);
        Logger.addEvent("StockManager", "remove",
            "Product " + product.getUpc() + " is out of stock.", false);
      }
    }

    Logger.addEvent("StockManager", "remove",
        "Product " + product.getUpc() + "'s quantity was decreased by " + quantity + ".",
        true);
  }

  /**
   * Set Product product as in stock by updating inStock and outOfStock.
   */
  private void isInStock(Product product) {
    try {
      outOfStock.remove(product);
      inStock.add(product);
      Logger.addEvent("StockManager", "isInStock",
          "Product " + product.getUpc() + " is in stock.", false);
    } catch (NoSuchElementException e) {
      System.out.println("Product is not registered in stock manager.");
      Logger.addEvent("StockManager", "isInStock",
          "Product " + product.getUpc() + " could not be placed in stock.",
          true);
    }
  }

  /**
   * Set Product product as out of Stock by updating inStock and outOfStock.
   */
  private void isOutOfStock(Product product) {
    try {
      inStock.remove(product);
      outOfStock.add(product);
      Logger.addEvent("StockManager", "isOutOfStock",
          "Product " + product.getUpc() + " is out of stock.", false);
    } catch (NoSuchElementException e) {
      System.out.println("Product is not registered in stock manager.");
      Logger.addEvent("StockManager", "isOutOfStock",
          "Product " + product.getUpc() + " could not be placed out of stock.",
          true);
    }
  }

  /**
   * Helper method to make a file writer for all the print services in this class.
   *
   * @param directory the name of the directory
   * @param fileName the name of the file
   * @return the writer for the required file
   */
  private FileWriter writer(String directory, String fileName) {
    try {
      //create the directory for the file
      File file = new File(directory);
      file.mkdirs();
      //create the file
      FileWriter writer = new FileWriter(directory + "/" + fileName + ".txt");
      return writer;
    } catch (IOException e) {
      Logger.addEvent("StockManager", "writer",
          "file " + fileName + " not made", false);
      return null;
    }
  }

  /**
   * Print the products that are currently in stock to a text file according to the date
   */
  public void printInStockItems() {
    DateTimeFormatter date = DateTimeFormatter.ofPattern("MMM-dd-uuuu");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-uuuu h:mm:ssa");
    FileWriter writer = writer("InStockItems", LocalDate.now().format(date));
    try {
      writer.write("In Stock Items" + "\nTime Printed: " + LocalDateTime.now()
          .format(formatter) + "\n");
      //print out the products that are in stock
      for (Product product : inStock) {
        writer.write("\n\n" + product);
        writer.write("\nQuantity: " + product.getQuantity());
      }
      writer.close();
      //log the event
      Logger.addEvent("StockManager", "printInStockItems",
          "the out of stock items could not be printed to a file", false);
    } catch (IOException e) {
      //log the event
      Logger.addEvent("StockManager", "printInStockItems",
          "the in stock items was printed to a file", false);
    }
  }

  /**
   * Print the products that are currently out of stock to a text file according to the date
   */
  public void printOutOfStockItems() {
    DateTimeFormatter date = DateTimeFormatter.ofPattern("MMM-dd-uuuu");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-uuuu h:mm:ssa");
    FileWriter writer = writer("OutOfStockItems", LocalDate.now().format(date));
    try {
      writer.write("Out of Stock Items" + "\nTime Printed: " + LocalDateTime.now()
          .format(formatter) + "\n");
      //print out the products that are out of stock
      for (Product product : outOfStock) {
        writer.write("\n\n" + product);
        writer.write("\nQuantity: " + product.getQuantity());
      }
      writer.close();
      //log the event
      Logger.addEvent("StockManager", "printOutOfStockItems",
          "the out of stock items could not be printed to a file", false);
    } catch (IOException e) {
      //log the event
      Logger.addEvent("StockManager", "printOutOfStockItems",
          "the out of stock items was printed to a file", false);
    }
  }

  /**
   * Read an .ser file with file path filePath and update the contents of this StockManager.
   */
  private void readFromFile(String filePath) throws ClassNotFoundException {
    try {
      // Taken from StudentManager.java in CSC207 lecture example
      InputStream file = new FileInputStream(filePath);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);

      // Deserialize
      // OrderManager is not serializable and is never read.
      inStock = (ArrayList<Product>) input.readObject();
      outOfStock = (ArrayList<Product>) input.readObject();

      Logger.addEvent("StockManager", "readFromFile",
          "Stock manager was loaded from " + filePath + ".", false);
    } catch (IOException e) {
      Logger.addEvent("StockManager", "readFromFile",
          "Unable to load stock manager from " + filePath + ".", true);
    }
  }
}