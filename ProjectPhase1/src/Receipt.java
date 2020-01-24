import java.util.ArrayList;
import java.util.HashMap;

/**
 * The receipt object holds the products being checked out for a specific transaction
 */
public class Receipt {

  /**
   * the list of products with its quantity being sold
   */
  private HashMap<Product, Integer> receiptProducts;
  /**
   * the total price in dollars
   */
  private double total;
  /**
   * the DailySalesManager associated with the receipt
   */
  private DailySalesManager dailySalesManager;

  /**
   * Creates a Receipt object initialized with an empty HashMap, the total set to 0 and the
   * associated DailySalesManager
   *
   * @param dailySalesManager the DailySalesManager associated with the receipt
   */
  Receipt(DailySalesManager dailySalesManager) {
    this.dailySalesManager = dailySalesManager;
    receiptProducts = new HashMap<>();
    total = 0;
  }

  /**
   * add a single product to the current receipt
   *
   * @param product the product being added to the receipt
   */
  public void addItem(Product product) {
    addItem(product, 1);
  }

  /**
   * add a list of products to the receipt
   *
   * @param products the products to be added to the receipt
   */
  public void addItem(ArrayList<Product> products) {
    for (Product product : products) {
      addItem(product, 1);
    }
  }

  /**
   * remove a quantity of a product from the receipt
   *
   * @param product the product to be removed from the receipt
   */
  public boolean removeItem(Product product, int quantity) {
    if (receiptProducts.get(product) >= quantity && receiptProducts.get(product) != null) {
      receiptProducts.put(product, receiptProducts.get(product) - quantity);
      Logger.addEvent("Receipt", "removeItem",
          "removed " + product.getName() + " x" + quantity + " from the receipt", true);

      return true;
    } else {
      receiptProducts.put(product, receiptProducts.get(product) - quantity);
      Logger.addEvent("Receipt", "removeItem",
          product.getName() + " x" + quantity
              + " exceed quantity on the receipt - could not be removed", true);
      return false;
    }
  }

  /**
   * the total cost of all the products
   *
   * @return the total in dollars
   */
  public double getTotal() {
    return total;
  }

  /**
   * add a product with a quantity greater than 1 to the receipt
   *
   * @param product the product being added
   * @param quantity the quantity of the product being added
   */
  public boolean addItem(Product product, int quantity) {
    if (!receiptProducts.containsKey(product)) {
      /*if this is the first time adding the product, add it the HashMap with its corresponding quantity*/
      receiptProducts.put(product, quantity);
      return true;
    } else {
      /*if the product as been added before, add the quantity to the existing quantity of the product on the receipt*/
      receiptProducts.put(product, receiptProducts.get(product) + quantity);
    }
    Logger.addEvent("Receipt", "addItem",
        "added " + product.getName() + " x" + quantity + " to the receipt", true);
    return false;
  }

  /**
   * cashes out all the items on the receipt
   */
  public void cashOut() {
    //record the transactions to dailySalesManager and total all the items
    for (Product product : receiptProducts.keySet()) {
      int quantity = receiptProducts.get(product);
      dailySalesManager.saleTransaction(product, quantity);
      //update the total
      double price = product.getPrice().getCurrentPrice();
      total = total + price * quantity;
    }
  }

  /**
   * Helper method for the toString method. Creates a String with all the items, their quantities
   * and their subtotal
   *
   * @return String with the information of the products on the receipt
   */
  private String receiptContents() {
    String receiptItems = "";
    for (Product product : receiptProducts.keySet()) {
      //calculate all the values for the product
      int productQuantity = receiptProducts.get(product);
      double productPrice = product.getPrice().getCurrentPrice();
      double productSubTotal = productPrice * productQuantity;
      //record the information of the product to the receiptItems
      receiptItems = receiptItems
          + "\n\n" + product
          + " x" + productQuantity
          + "\nprice/qty: " + productPrice
          + "\nsub total: $" + productSubTotal + "\n";
    }
    //return the information inside receiptItems
    return receiptItems;
  }

  /**
   * Returns a string with the all the information on the receipt
   *
   * @return the information on the receipt
   */
  @Override
  public String toString() {
    return String.format(receiptContents() + "\n\nTotal = .%2f", total);

  }


}