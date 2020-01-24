import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SerializationStrategy {

  /**
   * the serial file for LocationManager
   */
  String locationManagerPath = "LocationManager.ser";

  /**
   * the serial file for PriceManager
   */
  String priceManagerPath = "PriceManager.ser";

  /**
   * the serial file for StockManager
   */
  String stockManagerPath = "StockManager.ser";

  /**
   * the serial file for InventoryManager
   */
  String inventoryManagerPath = "InventoryManager.ser";

  /**
   * the serial file for DailySalesManager
   */
  String dailySalesManagerPath = "DailySalesManager.ser";

  /**
   * the serial file for UserManager
   */
  String userManagerPath = "UserManager.ser";

  /**
   * the serial file for OrderManager
   */
  String orderManagerPath = "OrderManager.ser";

  /**
   * Constructor creates a direcotry to hold all the serial files
   */
  SerializationStrategy() {
    File file = new File("Serialize");
    file.mkdir();
  }

  /**
   * Serialize a DailySalesManager object
   *
   * @param dailySalesManager the DailySaleManager to serialize
   */
  public void execute(DailySalesManager dailySalesManager) throws IOException {
    ObjectOutput output = getObjectOutput(dailySalesManagerPath);

    // Serialize all desired data
    output.writeObject(dailySalesManager.getDailySales());
    output.writeObject(dailySalesManager.getTransactionHashMap());
    output.writeObject(dailySalesManager.getTotalTransactions());
    output.close();
    logSerialization("DailySalesManager", dailySalesManagerPath);
  }

  /**
   * Serialize a PriceManager object
   *
   * @param priceManager the PriceManager to serialize
   */
  public void execute(PriceManager priceManager) throws IOException {
    ObjectOutput output = getObjectOutput(priceManagerPath);

    // Serialize all desired data
    output.writeObject(priceManager.getItemsOnSale());
    output.writeObject(priceManager.getUpcomingSale());
    //log the event
    logSerialization("PriceManager", priceManagerPath);
    output.close();
  }

  /**
   * Serialize a StockManager object
   *
   * @param stockManager the StockManager to serialize
   */
  public void execute(StockManager stockManager) throws IOException {
    ObjectOutput output = getObjectOutput(stockManagerPath);

    // Serialize all desired data
    // OrderManager is not serializable and is never saved.
    output.writeObject(stockManager.getInStock());
    output.writeObject(stockManager.getOutOfStock());
    output.close();

    logSerialization("StockManager", stockManagerPath);

  }

  /**
   * Serialize a InventoryManager object
   *
   * @param inventoryManager the InventoryManager to serialize
   */
  public void execute(InventoryManager inventoryManager) throws IOException {
    ObjectOutput output = getObjectOutput(inventoryManagerPath);

    // Serialize all desired data
    output.writeObject(inventoryManager.getStoreProducts());
    output.writeObject(inventoryManager.getUPCToProducts());
    output.close();

    //log the event
    logSerialization("InventoryManager", inventoryManagerPath);

  }

  /**
   * Serialize a LocationManager object
   *
   * @param locationManager the LocationManager to serialize
   */
  public void execute(LocationManager locationManager) throws IOException {
    ObjectOutput output = getObjectOutput(locationManagerPath);

    output.writeObject(locationManager.getSectionList());
    output.writeObject(locationManager.getSectionProductHashMap());

    logSerialization("LocationManager", locationManagerPath);

  }

  /**
   * Serialize a UserManager object
   *
   * @param userManager the UserManager to serialize
   */
  public void execute(UserManager userManager) throws IOException {

    ObjectOutput output = getObjectOutput(userManagerPath);

    // Serialize all desired data
    // kbd is not serializable and is never saved
    output.writeObject(userManager.getUsers());
    output.writeObject(userManager.getUserType());
    output.close();

    logSerialization("UserManager", userManagerPath);

  }

  /**
   * Serialize an OrderManager object
   *
   * @param orderManager the OrderManager to serialize
   */
  public void execute(OrderManager orderManager) throws IOException {

    ObjectOutput output = getObjectOutput(orderManagerPath);

    // Serialize the pending orders array list
    output.writeObject(orderManager.getPendingOrders());
    output.close();

    logSerialization("OrderManager", orderManagerPath);

  }

  /**
   * Helper method to create ObjectOutput for serializing
   *
   * @param fileName the .ser file that the class is serialized to
   * @return the ObjectOutput to write the class object's too
   */
  private ObjectOutput getObjectOutput(String fileName) throws IOException {
    // Taken from StudentManager.java in CSC207 lecture example
    OutputStream file = new FileOutputStream("Serialize/" + fileName);
    OutputStream buffer = new BufferedOutputStream(file);
    ObjectOutput output = new ObjectOutputStream(buffer);
    return output;
  }

  /**
   * Helper method to print the serialization to the logs
   *
   * @param managerName the manager class that was serialized
   * @param pathName the file name that the class is being serialized too
   */
  private void logSerialization(String managerName, String pathName) {
    Logger.addEvent("SerializationStrategy", "execute",
        managerName + " was added to " + pathName, false);
  }
}

