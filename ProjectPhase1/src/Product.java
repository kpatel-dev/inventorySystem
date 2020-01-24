import java.io.Serializable;
import java.util.ArrayList;

/**
 * A product inside a grocery store. Products are identified by their unique UPC code, and are
 * initialized with their upc, name, quantity, Price, Section, and a threshold quantity for
 * reordering.
 */
public class Product implements Serializable{

  private String upc;
  private String name;
  private int quantity;
  private Price price;
  private Section section;
  private int threshold;
  private ArrayList<Price> priceHistory;
  private String distributor;
  private double cost;
  private int aisle;

  /**
   * Creates a new Product.
   * Product are created once the very first time it's registered
   * to a store, and modified every time after. A product is equal to another product if they have
   * the same upc.
   *
   * @param upc Unique identifier of the Product
   * @param name Common name of the Product
   * @param quantity Integer quantity of the Product
   * @param price Selling price of the Product
   * @param section Section of the store the Product is found in
   * @param threshold Threshold quantity, below which an order is automatically made for the Product
   * @param distributor Where the Product came from
   * @param cost Cost of buying the Product from the distributor
   */
  public Product(String upc, String name, int quantity, Price price, Section section, int threshold,
      String distributor, double cost, int aisle) {
    this.upc = upc;
    this.name = name;
    this.quantity = quantity;
    this.price = price;
    this.section = section;
    this.threshold = threshold;
    this.distributor = distributor;
    priceHistory = new ArrayList<>();
    priceHistory.add(price);
    this.cost = cost;
    this.aisle = aisle;
  }

  /**
   * Returns upc.
   */
  String getUpc() {
    return upc;
  }

  /**
   * Returns name.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets name.
   *
   * @param name New name to set.
   */
  void setName(String name) {
    this.name = name;
  }

  /**
   * Returns quantity.
   */
  int getQuantity() {
    return quantity;
  }

  /**
   * Adds quantity to current quantity.
   *
   * @param quantity Quantity to add.
   */
  void addQuantity(int quantity) {
    this.quantity += quantity;
  }

  /**
   * Removes quantity from current quantity. Throws an InvalidAmountException if the quantity to
   * remove is greater than the current quantity.
   *
   * @param quantity Quantity to remove.
   */
  void removeQuantity(int quantity) throws InvalidAmountException {
    if (quantity > this.quantity) {
      throw new InvalidAmountException();
    }
    this.quantity -= quantity;
  }

  /**
   * Returns price.
   */
  Price getPrice() {
    return price;
  }

  /**
   * Sets price.
   *
   * @param price New Price to set.
   */
  void setPrice(Price price) {
    this.price = price;
    priceHistory.add(price);
  }

  /**
   * Returns priceHistory.
   */
  ArrayList<Price> getPriceHistory() {
    return priceHistory;
  }

  /**
   * Returns Section.
   */
  Section getSection() {
    return section;
  }

  /**
   * Returns a threshold.
   */
  int getThreshold() {
    return threshold;
  }

  /**
   * Returns distributor.
   */
  String getDistributor() {
    return distributor;
  }

  /**
   * Returns cost.
   */
  double getCost() {
    return cost;
  }

  int getAisle() {
    return aisle;
  }

  void setAisle(int a){
    aisle = a;
  }

  /**
   * Returns a string representation of Product, consisting of the name and upc.
   */
  @Override
  public String toString() {
    return name + "\n" + upc;
  }

//  public String toString() {
//    return "Product{" +
//        "upc=" + upc +
//        ", name='" + name + '\'' +
//        ", quantity=" + quantity +
//        ", price=" + price +
//        ", priceHistory=" + priceHistory +
//        ", section=" + section +
//        ", threshold=" + threshold +
//        '}';
//  }

  /**
   * Returns true if a Product has the same upc as another Product.
   *
   * @param o Object to compare
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Product product = (Product) o;

    return upc.equals(product.upc);
  }

}
