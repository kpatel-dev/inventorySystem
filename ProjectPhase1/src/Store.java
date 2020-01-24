import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JTextField;

class Store {
  final String type;    // Type of store (grocery, pharmacy, etc)
  String directoryPath;   // Directory path of the location of this Store's .ser files
  newGUI newGUIFrame;   // Reference to initial GUI
  SerializationStrategy serialize;
  DeserializationStrategy deserialize;
  LocationManager locationManager;
  OrderManager orderManager;
  PriceManager priceManager;
  StockManager stockManager;
  InventoryManager inventoryManager;
  DailySalesManager dailySalesManager;
  UserManager userManager;
  ReceiptManager receiptManager;
  User currentUser;

  // Creates an empty Store.
  Store(String type, newGUI newGUIFrame) {
    this.type = type;
    this.newGUIFrame = newGUIFrame;
    initialSetup();
  }

  // Creates a store and loads it with initial inventory items
  Store(String type, newGUI newGUIFrame, BufferedReader fileInput) {
    this.type = type;
    this.newGUIFrame = newGUIFrame;
    initialSetup();

    // Load inventory items
    InventoryLoader inventoryLoader = new InventoryLoader(fileInput, locationManager,
        inventoryManager);
    try {
      ArrayList<Product> products = inventoryLoader.loadItems();
      inventoryLoader.sendProducts(products);
    }
    catch(IOException ex) {
      ex.printStackTrace();
    }
  }

  // Loads a serialized store
  Store(String type, newGUI newGUIFrame, String directoryPath) {
    this.type = type;
    this.newGUIFrame = newGUIFrame;
    this.directoryPath = directoryPath;
    deserialize();
    this.receiptManager = new ReceiptManager(this.dailySalesManager);
  }

  private void initialSetup() {
    //kbd = new JTextField();   // User input is now from the text field instead of command line

    //Create a serializer for serializing all the managers
    serialize = new SerializationStrategy();
    deserialize = new DeserializationStrategy();

    // Setup store managers.
    locationManager = new LocationManager();
    orderManager = new OrderManager();   // Ordermanager is never read from a .ser file
    priceManager = new PriceManager();
    stockManager = new StockManager(orderManager);
    inventoryManager = new InventoryManager(stockManager, priceManager);
    dailySalesManager = new DailySalesManager();
    userManager = new UserManager();
    receiptManager = new ReceiptManager(dailySalesManager);
  }

  private void deserialize() {
    try {
      serialize = new SerializationStrategy();
      // Deserialize from files in directoryPath
      deserialize = new DeserializationStrategy(directoryPath);

      locationManager = deserialize.executeLocationManager();
      priceManager = deserialize.executePriceManager();
      stockManager = deserialize.executeStockManager(orderManager);
      inventoryManager = deserialize.executeInventoryManager(stockManager, priceManager);
      dailySalesManager = deserialize.executeDailySalesManager();
      userManager = deserialize.executeUserManager();
    }
    catch(ClassNotFoundException | IOException ex) {
      ex.printStackTrace();
    }
  }

  void serialize() {
    try{
      serialize.execute(locationManager);
      serialize.execute(priceManager);
      serialize.execute(stockManager);
      serialize.execute(inventoryManager);
      serialize.execute(dailySalesManager);
      serialize.execute(userManager);
      serialize.execute(orderManager);
    }
    catch(IOException ex) {
      ex.printStackTrace();
    }
  }

  void shutDown() {
    this.serialize();
    dailySalesManager.printProfitToHistory();
    dailySalesManager.printDailySales();
  }

  void run() {
    currentUser = null;     // Reset current user to null

    // GUI for registering or logging in an user
    LogInGUI logInGUI = new LogInGUI(this);
    logInGUI.run();
  }

  void run(User user) {
    // User has been registered or logged in via LogInGUI
    currentUser = user;

    StoreGUI storeGUI = new StoreGUI(this);
    storeGUI.run();
  }
}
