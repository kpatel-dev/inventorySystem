import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An OrderManager manages everything related to orders. OrderManager automatically makes orders for
 * Products that are below threshold, and has a history of all the orders that have been made.
 */
public class OrderManager {

  // There is only ONE OrderManager at all times.
  private static OrderManager orderManager = null;
  private ArrayList<Order> pendingOrders;

  /**
   * Creates OrderManager with a special case list. This also creates an OrderHistory.txt file in
   * OrderList folder, and a pendingOrders ArrayList.
   */
  OrderManager() {
    pendingOrders = new ArrayList<>();
    File dir = new File("OrderList");

    boolean successful = dir.mkdir();
    if (successful)
    {
      System.out.println("Directory created.");
    }
    else
    {
      System.out.println("Directory not created or already exists.");
    }

    // Create OrderHistory.txt when OrderManager is made if it doesn't already exist.
    File file = new File("OrderList/OrderHistory.txt");
    try {
      file.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Create a InventoryManager from a file.
   *
   * @param filePath file which the class was serialized to
   */
  OrderManager(String filePath) throws ClassNotFoundException, IOException {

    File file = new File(filePath);

    if (file.exists()) {
      readFromFile(filePath);
    } else {
      file.createNewFile();
      //log the event
      Logger.addEvent("OrderManager", "OrderManager",
          "created a new file for serialization", false);
    }
  }

  /**
   * Returns this OrderManager.
   */
  static OrderManager getInstance() {
    if (orderManager == null) {
      orderManager = new OrderManager();
    }
    return orderManager;
  }

  /**
   * Makes an order of Product. Sends out an order (in the form of a txt file with the name of the
   * file being the upc), and adds it to pendingOrders and OrderHistory.txt. The information
   * included in an order is the upc, name, distributor, and quantity of the Product to be ordered.
   *
   * @param product Product to order
   * @param quantity quantity of Product to order
   */
  void makeOrder(Product product, int quantity) {
    String textToWrite;
    Order order;
    // checks to see if the quantity to order is 3x the threshold
    if (!(quantity == 3 * product.getThreshold())) {
      order = new Order(product, quantity);
      textToWrite = order.toString();
    } else {
      order = new Order(product);
      textToWrite = order.toString();
    }

    // checks to see if there is a pending order of the same Product
    if (!pendingOrders.contains(order)) {
      try {

        File file = new File("OrderList/" + product.getUpc() + ".txt");
        try {
          file.createNewFile();
        } catch (IOException e) {
          e.printStackTrace();
        }

        FileWriter fw = new FileWriter("OrderList/" + product.getUpc() + ".txt");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(textToWrite);
        bw.close();
        fw.close();

        // update the pending orders list
        pendingOrders.add(order);

        // update the master order history
        updateOrderHistory(textToWrite);

        // update the product's individual order history
        updateProductOrderHistory(product.getUpc(), textToWrite);

        // update log
        Logger.addEvent("OrderManager", "makeOrder",
            "Order made for " + product.getName(),false);

      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println("Successfully placed order for " + product.getName());
    } else {
      System.out.println("Order already exists for " + product.getName());
    }
  }

  private void updateProductOrderHistory(String upc, String textToWrite) {
    try {
      File file = new File("OrderList/" + upc + "-OrderHistory.txt");
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }

      FileWriter fw = new FileWriter("OrderList/" + upc + "-OrderHistory.txt", true);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-uuuu h:mm:ssa");

      fw.write(LocalDateTime.now().format(formatter) + " ordered: " + textToWrite + "\n");
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Updates OrderHistory.txt. The history should only be modified within OrderManager. The history
   * will include the current date of the order, and the upc, name, distributor, and quantity of the
   * ordered Product.
   *
   * @param textToWrite Text entry to add to OrderHistory.txt.
   */
  private void updateOrderHistory(String textToWrite) {
    try {
      FileWriter fw = new FileWriter("OrderList/OrderHistory.txt", true);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-uuuu h:mm:ssa");

      fw.write(LocalDateTime.now().format(formatter) + " ordered: " + textToWrite + "\n");
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Prints out order history, i.e. all entries in OrderHistory.txt.
   */
  void printOrderHistory() {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader("OrderList/OrderHistory.txt"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    String line;
    try {
      assert br != null;
      while ((line = br.readLine()) != null) {
        System.out.println(line);
        Logger.addEvent("OrderManager", "printOrderHistory", "Order history printed.", false);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Prints out the order history of the Product with the given upc.
   */
  void printProductOrderHistory(String upc) {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader("OrderList/" + upc + "-OrderHistory.txt"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    String line;
    try {
      assert br != null;
      while ((line = br.readLine()) != null) {
        System.out.println(line);
        Logger.addEvent("OrderManager", "printProductOrderHistory",
            "Order history for " + upc + " printed.", true);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Prints the recent orders made.
   */
  void printPendingOrders() {
    try{
    for (Order order : pendingOrders) {
      System.out.println(order);
      Logger.addEvent("OrderManager", "printPendingOrders",
          "Recent orders list printed.", true);
    }}
    catch (NullPointerException e){
      Logger.addEvent("OrderManager", "printPendingOrders",
          "Recent orders list doesn't exit and could not be printed.", true);
    }
  }

  /**
   * Process an incoming order, identified by upc. Will remove the corresponding order file from
   * OrderList directory and update quantity and recent orders.
   *
   * @param upc Upc of incoming order
   */
  void processIncomingOrder(String upc) {
    // Remove the order file
    File[] files = new File("OrderList").listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.getName().split("\\.")[0].equals(upc)) {
          file.delete();
        }
      }
    }
    // Update the pendingOrders list
    ArrayList<Order> temp = new ArrayList<>();
    for (Order order : pendingOrders) {
      if (order.getProduct().getUpc().equals(upc)) {
        // Add the ordered quantity to the Product's current quantity
        order.getProduct().addQuantity(order.getQuantity());
        temp.add(order);
        System.out.println("Incoming order for " + upc + " processed.");
      }
    }
    pendingOrders = new ArrayList<>();
    pendingOrders.addAll(temp);

    // update the product's specific order history
    try {
      FileWriter fw = new FileWriter("OrderList/" + upc + "-OrderHistory.txt", true);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-uuuu h:mm:ssa");

      fw.write(LocalDateTime.now().format(formatter) + " order received: " + upc + "\n");
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // update logger
    Logger.addEvent("OrderManager", "processIncomingOrder",
        "Incoming order for" + upc + "processed.", true);
  }


  /**
   * Returns pendingOrders ArrayList.
   *
   * @return pendingOrders
   */
  public ArrayList<Order> getPendingOrders() {
    return pendingOrders;
  }


  /**
   * Used for de-serializing OrderManager.
   *
   * @param filePath the path where the file is saved
   */
  private void readFromFile(String filePath) throws ClassNotFoundException {
    try {
      InputStream file = new FileInputStream(filePath);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);

      // Deserialize
      pendingOrders = ((ArrayList<Order>) input.readObject());
      //log the event
      Logger.addEvent("OrderManager", "readFromFile",
          "loaded OrderManager from " + filePath, false);
    } catch (IOException e) {
      //log the event
      Logger.addEvent("OrderManager", "readFromFile",
          "IOException-unable to load Inventory Manager from " + filePath, false);
    }
  }

}
