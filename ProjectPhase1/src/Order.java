import java.io.Serializable;

/**
 * An Order of a Product. Quantity to order can be specified upon initialization. If not, the
 * default order amount will be 3x the threshold quantity of the Product.
 */
public class Order implements Serializable{

  // Product and quantity to order
  private Product product;
  private int quantity;

  /**
   * Creates a new Order, with 3x the threshold quantity of the Product.
   *
   * @param product Product of this Order
   */
  public Order(Product product) {
    this.product = product;
    this.quantity = 3 * product.getThreshold();
  }

  /**
   * Creates a new Order, with a specific quantity of the Product.
   *
   * @param product Product of this Order
   * @param quantity Specific quantity to order
   */
  public Order(Product product, int quantity) {
    this.product = product;
    this.quantity = quantity;
  }

  /**
   * Returns Product of Order.
   */
  public Product getProduct() {
    return product;
  }

  /**
   * Sets Product of Order.
   *
   * @param product Product of Order.
   */
  public void setProduct(Product product) {
    this.product = product;
  }

  /**
   * Returns quantity of Order.
   */
  public int getQuantity() {
    return quantity;
  }

  /**
   * Sets quantity of Order.
   *
   * @param quantity Quantity of Order.
   */
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  /**
   * Returns String representation of an Order. This contains the upc, name, distributor, and
   * quantity to order of the product.
   */
  @Override
  public String toString() {
    return product.getUpc() + ", " +
        product.getName() + ", " +
        product.getDistributor() + ", " +
        String.valueOf(quantity);
  }

  /**
   * Returns true if an Order is the same as another Order. Two Orders are the same if they are from
   * the same Product and are ordering the same Quantity.
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

    Order order = (Order) o;

    return quantity == order.quantity && (product != null ? product.equals(order.product)
        : order.product == null);
  }

}