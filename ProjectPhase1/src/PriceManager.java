import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * PriceManager manages the prices of all the product in the store. It keeps track of current items
 * on sale, the upcoming items on sale and can print out the price information for a product or a
 * list of products.
 */
public class PriceManager {

  /**
   * the list of products currents on sale
   */
  private ArrayList<Product> itemsOnSale;
  /**
   * the list of products that will be on sale in the future
   */
  private ArrayList<Product> upcomingSale;


  /**
   * Creates an PriceManager with an empty ItemsOnSale list.
   */
  PriceManager() {
    itemsOnSale = new ArrayList<>();
    upcomingSale = new ArrayList<>();
  }

  /**
   * Create a PriceManager from a file
   *
   * @param filePath file which the class was serialized to
   */
  PriceManager(String filePath) throws ClassNotFoundException, IOException {
    this();
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
   * Set the list of items on sale
   *
   * @param itemsOnSale the list of items on sale
   */
  public void setItemsOnSale(ArrayList<Product> itemsOnSale) {
    //register the items on sale list to make sure the items are appropriately placed
    registerProduct(itemsOnSale);
  }

  /**
   * Return an ArrayList of products that are on sale.
   */
  public ArrayList<Product> getItemsOnSale() {
    return itemsOnSale;
  }

  /**
   * Set the list of items in the upcoming sale list
   *
   * @param upcomingSale the ArrayList of items with a future sale date
   */
  public void setUpcomingSale(ArrayList<Product> upcomingSale) {
    //register the items in the upcoming sale list to make sure the items are appropriately placed
    registerProduct(upcomingSale);
  }

  /**
   * Return an ArrayList of future Sale items.
   */
  public ArrayList<Product> getUpcomingSale() {
    return upcomingSale;
  }

  /**
   * Register a list of products with priceManager to check if the items should be on sale or has a
   * future sale.
   *
   * @param products the ArrayList of products to be registered with priceManager
   */
  public void registerProduct(ArrayList<Product> products) {
    try {
      for (Product product : products) {
        registerProduct(product);
      }

      //log the event
      Logger.addEvent("PriceManager", "registerProduct",
          "registered the list of products to priceManager", false);
    } catch (NullPointerException e) {
      //log the event
      Logger.addEvent("PriceManager", "registerProduct",
          "NullPointerException - could not registered the list of products to priceManager", false);
    }
  }

  /**
   * Registers a product with priceManager. Checks if the price should be on sale currently or in
   * the future and adjust the itemsOnSale and upcomingSale list.
   *
   * @param product the product to be registered into the system
   */
  public void registerProduct(Product product) {
    Price price = product.getPrice();
    /*If the product has a sale date and has reached the end of the sale, change the price of the
    product to the regular price.*/
    if (price.checkEndOfSale() && !(price.getSaleStartDate() == null)) {
      Price regularPrice = new Price(price.getRegularPrice());
      changePrice(product, regularPrice);
      //log that the products sale has ended
      Logger.addEvent("PriceManager", "registerProduct",
          "Sale for " + product.getName() + ", " + product.getUpc()
              + " ended. The price was changed to the regular price", false);
    } else {
      sortProduct(product);
    }

  }

  /**
   * Helper method to sort the product into the appropriate list
   *
   * @param product the product that needs to be sorted
   */
  private void sortProduct(Product product) {
    Price price = product.getPrice();
    //If the current product should be on sale, add it to the ItemsOnSale list.
    if (price.getOnSale() && !itemsOnSale.contains(product)) {
      itemsOnSale.add(product);
    }
    //If there is a future sale date for the product, add it to the upComingSaleList
    else if (price.getFutureSale() && !upcomingSale.contains(product)) {
      upcomingSale.add(product);
    }
  }

  /**
   * Delete a product from the priceManager system.
   *
   * @param product the product that needs to be deleted
   */
  public void deleteProduct(Product product) {
    itemsOnSale.remove(product);
    upcomingSale.remove(product);
    //log the event
    Logger.addEvent("PriceManager", "deleteProduct",
        product.getName() + " was removed from priceManager", false);
  }

  /**
   * Change the price of a product and update it's price history.
   *
   * @param product the product that's price needs to be changed
   * @param newPrice the new price of the product
   */
  public void changePrice(Product product, Price newPrice) {
    product.setPrice(newPrice);
    deleteProduct(product); //delete the product from both lists
    sortProduct(product); //re-sort the product into a list according to the new price
    //log the event
    Logger.addEvent("PriceManager", "changePrice",
        "the price of " + product.getName() + " was changed", true);
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

      Logger.addEvent("PriceManager", "writer", "file not made", false);
      return null;

    }
  }


  /**
   * Print the price history of a specified product to text file.
   *
   * @param product the products that's price history is to be printed
   */
  public void printPriceHistory(Product product) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-uuuu h:mm:ssa");
    FileWriter writer = writer("PriceHistory",product.getName());
    try {

      writer.write("Price History for " + product + "\nTime Printed: " + LocalDateTime.now()
          .format(formatter) + "\n");
      for (Price price : product.getPriceHistory()) {
        writer.write(price.toString());
      }
      //log the event
      Logger.addEvent("PriceManager", "printPriceHistory",
          "the price history of " + product.getName() + " was printed to a file", true);
      writer.close();
    } catch (IOException e) {
      //log the event
      Logger.addEvent("PriceManager", "printPriceHistory",
          "IOException - the price history of " + product.getName()
              + " could not be printed to a file", true);
    }
  }

  /**
   * Print the list of items on sale to a text file.
   */
  public void printOnSaleItems() {
    printPricesOfList(getItemsOnSale(), "On-Sale-List");
  }

  /**
   * Print the list of items on sale to a text file.
   */
  public void printUpcomingSaleItems() {
    printPricesOfList(getUpcomingSale(), "Upcoming-Sales-List");
  }

  /**
   * Print the prices of a specifies list of products to a text file.
   *
   * @param productList the list of products who's current price needs to be printed
   * @param listName the name of the list that is being printed
   */
  public void printPricesOfList(ArrayList<Product> productList, String listName) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-uuuu h:mm:ssa");
    DateTimeFormatter date = DateTimeFormatter.ofPattern("MMM-dd-uuuu");
    FileWriter writer = writer("PriceList/" + LocalDate.now().format(date), listName);
    try {

      writer.write(listName + "\nTime Printed: " + LocalDateTime.now()
          .format(formatter));

      for (Product product : productList) {
        writer.write("\n\n" + product);
        writer.write(product.getPrice().toString());

      }
      //log the event
      Logger.addEvent("PriceManager", "printPricesOfList",
          "the price list " + listName
              + " was printed to a file", true);
      writer.close();
    } catch (IOException e) {
      //log the event
      Logger.addEvent("PriceManager", "printPricesOfList",
          "IOEXception - the price list " + listName
              + " was not printed to a file", true);
    }

  }

  /**
   * Used for de-serializing PriceManager
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
      setItemsOnSale((ArrayList<Product>) input.readObject());
      setUpcomingSale((ArrayList<Product>) input.readObject());
      //log the event
      Logger.addEvent("PriceManager", "readFromFile",
          "Loaded PriceManager from " + filePath, false);
    } catch (IOException e) {
      System.out.println("Unable to load Daily Sales Manager from file.");
      //log the event
      Logger.addEvent("PriceManager", "readFromFile",
          "IOException-unable to load PriceManager from " + filePath, false);
    }

  }
}

