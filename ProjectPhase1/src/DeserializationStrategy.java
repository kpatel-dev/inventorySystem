import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 * DeserializationStrategy provides the service of make an object by calling the constructor for it
 * that deserializes it.
 */
public class DeserializationStrategy {

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

  private String directoryPath;

  DeserializationStrategy(){}

  DeserializationStrategy(String directoryPath) {
    this.directoryPath = directoryPath;
  }

  /**
   * Retruns a LocationManager object with it's contents deserialized
   *
   * @return deserialized LocationManager
   */
  public LocationManager executeLocationManager() throws ClassNotFoundException, IOException {
    if (directoryPath != null) {
      return new LocationManager(getFullPath(locationManagerPath));
    } else {
      return new LocationManager(locationManagerPath);
    }
  }

  /**
   * Returns a PriceManager object with it's contents deserialized
   *
   * @return deserialized PriceManager
   */
  public PriceManager executePriceManager() throws ClassNotFoundException, IOException {
    if(directoryPath != null) {
      return new PriceManager(getFullPath(priceManagerPath));
    }
    else {
      return new PriceManager(priceManagerPath);
    }
  }

  /**
   * Returns a StockManager object with it's contents deserialized
   *
   * @param orderManager the orderManager to initialize the stockManager with
   * @return deserialized StockManager
   */
  public StockManager executeStockManager(OrderManager orderManager)
      throws ClassNotFoundException, IOException {
    if(directoryPath != null) {
      return new StockManager(getFullPath(stockManagerPath), orderManager);
    }
    else {
      return new StockManager(stockManagerPath, orderManager);
    }
  }

  /**
   * Returns a InventoryManager object with it's contents deserialized
   *
   * @param stockManager the stockManager to initialize the InventoryManager with
   * @param priceManager the priceManager to initialize the InventoryManager with
   * @return deserialized InventoryManager
   */
  public InventoryManager executeInventoryManager(StockManager stockManager,
      PriceManager priceManager)
      throws ClassNotFoundException, IOException {
    if (directoryPath != null) {
      return new InventoryManager(getFullPath(inventoryManagerPath), stockManager, priceManager);
    } else {
      return new InventoryManager(inventoryManagerPath, stockManager, priceManager);
    }
  }

  /**
   * Returns a DailySalesManager object with it's contents deserialized
   *
   * @return deserialized DailySalesManager
   */
  public DailySalesManager executeDailySalesManager() throws ClassNotFoundException, IOException {
    if(directoryPath != null) {
      return new DailySalesManager(getFullPath(dailySalesManagerPath));
    }
    else {
      return new DailySalesManager(dailySalesManagerPath);
    }
  }



  /**
   * Returns a UserManager object with it's contents deserialized
   *
   * @return deserialized UserManager
   */
  public UserManager executeUserManager()
      throws ClassNotFoundException, IOException {
    if(directoryPath != null) {
      return new UserManager(getFullPath(userManagerPath));
    }
    else {
      return new UserManager(userManagerPath);
    }
  }

  /**
   * Returns a UserManager object with it's contents deserialized
   *
   * @param kbd the buffered reader to initialize the UserManager with
   * @return deserialized UserManager
   */
  public UserManager executeUserManager(BufferedReader kbd)
      throws ClassNotFoundException, IOException {
    if(directoryPath != null) {
      return new UserManager(getFullPath(userManagerPath), kbd);
    }
    else {
      return new UserManager(userManagerPath, kbd);
    }
  }

  /**
   * Returns an OrderManager object with its contents deserialized
   *
   * @return deserialized OrderManager
   */
  public OrderManager executeOrderManager() throws ClassNotFoundException, IOException {
    if(directoryPath != null) {
      return new OrderManager(getFullPath(orderManagerPath));
    }
    else {
      return new OrderManager(orderManagerPath);
    }
  }

  private String getFullPath(String fileName) {
    File directory = new File(directoryPath);
    File file = new File(directory, fileName);
    return file.getPath();
  }
}
