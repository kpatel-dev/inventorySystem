import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ProfitManager keeps track of the daily sales, daily profit and daily revenue.
 */
public class DailySalesManager implements Serializable {

  /**
   * an ArrayList of today's sales
   */
  private ArrayList<Transaction> dailyTransactions;

  /**
   * a HashMap of transaction numbers to the transactions up to date
   */
  private HashMap<Integer, Transaction> transactionHashMap;

  /**
   * the total profit today
   */
  private double dailyProfit;
  /**
   * the total revenue today
   */
  private double dailyRevenue;
  /**
   * the total number of transactions
   */
  private static int totalTransactions = 0;

  /**
   * the total reciepts
   */
  private static int receiptNumber = 0;

  /**
   * Create a profitManager with an empty dailySales list
   */
  DailySalesManager() {
    dailyTransactions = new ArrayList<>();
    transactionHashMap = new HashMap<>();
  }

  /**
   * Create a DailySalesManager from a file
   *
   * @param filePath file which the class was serialized to
   */
  DailySalesManager(String filePath) throws ClassNotFoundException, IOException {
    this();
    //  Taken from StudentManager.java in CSC207 lecture example
    // Reads serializable objects from filePath.
    File file = new File(filePath);

    if (file.exists()) {
      readFromFile(filePath);
    } else {
      file.createNewFile();
      Logger.addEvent("DailySalesManager", "DailySalesManager",
          "created a new file for serialization", false);
    }
  }

  /**
   * Helper method when reading from a file. Sets the current ArrayList of dailyTransactions
   *
   * @param dailyTransactions the ArrayList of daily sales up to the current point
   */
  private void setDailyTransactions(ArrayList<Transaction> dailyTransactions) {
    for (Transaction transaction : dailyTransactions) {
      //only add it to the dailySales list if the sale happened today
      if (transaction.transactionDate.getDayOfMonth() == LocalDateTime.now().getDayOfMonth()) {
        this.dailyTransactions.add(transaction);
        //if it was a sale from today, add it to the profit and revenue
        dailyProfit = dailyProfit + transaction.profit;
        dailyRevenue = dailyRevenue + transaction.revenue;

      }
      Logger.addEvent("DailySalesManager", "setDailyTransactions",
          "the list of dailySales was set", false);
    }
  }

  /**
   * Returns the daily profit for today
   *
   * @return the daily profit
   */
  public double getDailyProfit() {
    return dailyProfit;
  }

  /**
   * Returns the daily revenue for today
   *
   * @return the daily revenue
   */
  public double getDailyRevenue() {
    return dailyRevenue;
  }

  /**
   * Returns the current ArrayList of dailySales
   */
  public ArrayList<Transaction> getDailySales() {
    return dailyTransactions;
  }

  /**
   * Returns the current HashMap of all transactions
   */
  public HashMap<Integer, Transaction> getTransactionHashMap() {
    return transactionHashMap;
  }

  /**
   * Return the total transactions
   */
  public int getTotalTransactions() {
    return totalTransactions;
  }

  /**
   * Handles a sale transaction by adjusting the dailySales list, the dailyProfit and the
   * dailyRevenue.
   *
   * @param product the product that was sold
   */
  public void saleTransaction(Product product) {
    //create a new Sale object
    Transaction sale = new Transaction(product);
    //update the totals with the new sale
    updateDailyTotals(sale);
    Logger.addEvent("DailySalesManager", "saleTransaction",
        "a new Sale was made for " + product.getName(), false);
  }

  /**
   * Handles a sale transaction of more than 1 quanitity, by adjusting the dailySales list, the
   * dailyProfit and the dailyRevenue.
   *
   * @param product the product that was sold
   */
  public void saleTransaction(Product product, int quantity) {
    //create new sale transactions
    for (int i = 0; i < quantity; i++) {
      saleTransaction(product);
    }
  }

  /**
   * Handles a sale transaction by adjusting the dailySales list, the dailyProfit and the
   * dailyRevenue.
   *
   * @param transactionNumber the transaction number that needs to be reversed
   */
  public void refundTransaction(int transactionNumber) {
    try {
      //create a refund transaction for a previous sale transaction
      Transaction refund = new Transaction(transactionHashMap.get(transactionNumber));
      //update the daily totals
      updateDailyTotals(refund);

      Logger.addEvent("DailySalesManager", "refundTransaction",
          "a new Refund was made for " + refund.product.getName() + "transaction number: "
              + transactionNumber, true);
    } catch (NullPointerException e) {
      Logger.addEvent("DailySalesManager", "refundTransaction",
          "refund wasn't made - transaction number doesn't exist:" + transactionNumber, true);
    }
  }

  /**
   * Helper method for transactions. Updates the daily totals when a transaction is made.
   */
  private void updateDailyTotals(Transaction transaction) {
    dailyTransactions.add(transaction);
    transactionHashMap.put(transaction.getTransactionNumber(), transaction);
    dailyProfit = dailyProfit + transaction.profit;
    dailyRevenue = dailyRevenue + transaction.revenue;
  }


  /**
   * Display the daily totals
   */
  public String displayTotals() {
    String s = String.format("\n\nDaily Profit: $ %.2f\nDaily Revenue: $ %.2f\n\n", dailyRevenue, dailyProfit);
    //log the event
    Logger.addEvent("DailySalesManager", "displayTotals",
        "the daily totals were displayed:", false);

    return s;
  }

  /**
   * Helper method to make a file writer for all the print services in this class.
   *
   * @param directory the name of the directory
   * @param fileName the name of the file
   * @param append true if the existing file should be appended
   * @return the writer for the required file
   */
  private FileWriter writer(String directory, String fileName, boolean append) {
    try {
      //create the directory for the file
      File file = new File(directory);
      file.mkdirs();
      //create the file
      FileWriter writer;
      if (append) {
        writer = new FileWriter(
            directory + "/" + fileName + ".txt", true);
      } else {
        writer = new FileWriter(
            directory + "/" + fileName + ".txt");
      }
      return writer;
    } catch (IOException e) {

      Logger.addEvent("DailySalesManager", "writer",
          "daily sales file not made", false);
      return null;

    }
  }

  /**
   * Print service for printing the daily profit to a monthly profit history file for easy access.
   */
  public void printProfitToHistory() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-uuuu h:mm:ssa");
    FileWriter writer = writer("ProfitHistory", LocalDate.now().getMonth().toString(), true);
    try {
      writer.write("\nTime Printed: " + LocalDateTime.now()
          .format(formatter));

      writer
          .write(String.format("\nDaily Profit: $ %.2f\nDaily Revenue: $ %.2f\n\n", dailyRevenue,
              dailyProfit));
      writer.close();
      Logger.addEvent("PriceManager", "printProfitToHistory",
          "the daily total was added to the profit history",false);
    } catch (IOException e) {
      Logger.addEvent("PriceManager", "printProfitToHistory",
          "IOException - the daily total was not added to the profit history", false);
    }

  }

  /**
   * Prints the daily sales to a text document  called "Daily-Sales.txt" with details of each
   * transaction to a file.
   */
  public void printDailySales() {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-uuuu h:mm:ssa");
      DateTimeFormatter date = DateTimeFormatter.ofPattern("MMM-dd-uuuu");
      FileWriter writer = writer("DailySales/Transactions", LocalDate.now().format(date), false);
      writer.write("Daily Sales" + "\nTime Printed: " + LocalDateTime.now()
          .format(formatter));

      //print the daily totals
      writer
          .write(String.format("\n\nDaily Profit: $ %.2f\nDaily Revenue: $ %.2f\n\n", dailyRevenue,
              dailyProfit));
      //print out each transaction in the daily Transactions list
      for (Transaction transaction : dailyTransactions) {
        writer.write(transaction + "\n");
      }

      //also print out a summary of the daily sales
      printDailySalesSummary();
      Logger.addEvent("DailySalesManager", "printDailySales",
          "the list of dailySales was print to a file", true);
      writer.close();
    } catch (IOException e) {
      Logger.addEvent("DailySalesManager", "printDailySales",
          "IOException - the list of dailySales could not be printed to a file", true);
    }

  }

  /**
   * Prints the daily sales to a text document  called "Sales-Summary.txt" in the output directory
   */
  public void printDailySalesSummary() {

    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-uuuu h:mm:ssa");
      DateTimeFormatter date = DateTimeFormatter.ofPattern("MMM-dd-uuuu");
      FileWriter writer = writer("DailySales/SalesSummary", LocalDate.now().format(date), false);

      writer.write("Daily Sales" + "\nTime Printed: " + LocalDateTime.now()
          .format(formatter));
      //show the total daily profit and revenue
      writer
          .write(String.format("\n\nDaily Profit: $ %.2f\nDaily Revenue: $ %.2f\n\n", dailyRevenue,
              dailyProfit));

      //get a HashMap of the products sold with the quantity sold
      HashMap<Product, Integer> productQuantitySold = getProductQuantitySold();

      //print out the daily sales details of each product
      for (Product product : productQuantitySold.keySet()) {
        int quantitySold = productQuantitySold.get(product);
        double currentPrice = product.getPrice().getCurrentPrice();
        double totalRevenue = currentPrice * quantitySold;
        double totalProfit = (currentPrice - product.getCost()) * quantitySold;
        writer.write(
            product + " x" + quantitySold
                + "\nProduct Daily Revenue = " + totalRevenue
                + "\nProduct Daily Profit = " + totalProfit + "\n\n");
      }
      Logger.addEvent("DailySalesManager", "printDailySales",
          "the list of dailySales was print to a file", false);
      writer.close();
    } catch (IOException e) {
      Logger.addEvent("DailySalesManager", "printDailySales",
          "IOException - the list of dailySales could not be printed to a file", false);
    }


  }

  /**
   * Helper method to find how many of each product was sold in today's transactions
   *
   * @return the hashmap of product to the amount of the product sold
   */
  private HashMap<Product, Integer> getProductQuantitySold() {
    //HashMap of the product to the amount that was sold
    HashMap<Product, Integer> productQuantitySold = new HashMap<>();
    for (Transaction transaction : dailyTransactions) {
      //if this product doesn't exist in the HashMap, put it in the HashMap with quantity of 1
      if (!productQuantitySold.containsKey(transaction.product)) {
        productQuantitySold.put(transaction.product, 1);
      } else {
        //if this product is in the HashMap, add 1 to the existing quantity
        productQuantitySold
            .put(transaction.product, productQuantitySold.get(transaction.product) + 1);
      }
    }
    return productQuantitySold;
  }


  /**
   * Used for de-serializing DailySalesManager
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
      setDailyTransactions((ArrayList<Transaction>) input.readObject());
      transactionHashMap = (HashMap<Integer, Transaction>) input.readObject();
      totalTransactions = (Integer) input.readObject();

      Logger.addEvent("DailySalesManager", "readFromFile",
          "Loaded Daily Sales Manager from " + filePath, false);
    } catch (IOException e) {
      Logger.addEvent("DailySalesManager", "readFromFile",
          "IOException-unable to load Daily Sales Manager from " + filePath, false);
    }
  }

  /**
   * Transaction holds the information regarding a transaction. This includes the product that was
   * sold/returned, the cost of the product, the revenue earned from the product, the profit earned
   * from the product, the transaction number and the date the sale occurred.
   */
  private class Transaction implements Serializable {

    private Product product;
    private double profit;
    private double cost;
    private double revenue;
    private LocalDateTime transactionDate;
    private String transactionType;
    private int transactionNumber;

    /**
     * Create a Transaction event for a product that was sold.
     *
     * @param product the product that was sold
     */
    Transaction(Product product) {
      this.product = product;
      transactionType = "SALE";
      revenue = this.product.getPrice().getCurrentPrice();
      cost = this.product.getCost();
      profit = revenue - cost;
      transactionDate = LocalDateTime.now();
      transactionNumber = totalTransactions++;
    }

    /**
     * Creates a transaction event to reverse a previous transaction (refund).
     *
     * @param transaction the transaction that needs to be reversed
     */
    Transaction(Transaction transaction) {
      this(transaction.product);
      transactionType = "REFUND FOR TRANSACTION " + transaction.transactionNumber;
      profit = -profit;
      revenue = -revenue;
    }

    /**
     * Returns the transaction number.
     */
    public int getTransactionNumber() {
      return transactionNumber;
    }

    /**
     * Formats a string that holds the Transaction information with the product, the date, the
     * revenue, the cost, the profit and the transaction number.
     *
     * @return String object with formatted sale information.
     */
    @Override
    public String toString() {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-uuuu h:mm:ssa");
      return String
          .format(
              transactionType + "\n" + product + "\nDate: " + transactionDate.format(formatter)
                  + "\nRevenue: $ %.2f\nCost: $ %.2f\nProfit: $ %.2f\nTransaction Number: %s\n",
              revenue,
              cost, profit, transactionNumber);
    }


  }


}