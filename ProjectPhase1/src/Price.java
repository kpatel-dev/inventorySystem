import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Price holds information regarding the regular price, sale price, sale start date and sale end
 * date. It is able to check if an item is currently on sale or has a future sale price.
 */
public class Price implements Serializable{

  /**
   * the regular price of the product
   */
  private double regularPrice;
  /**
   * the sale price of the product
   */
  private double salePrice;
  /**
   * if the price is currently on sale
   */
  private boolean onSale = false;
  /**
   * if the price has a future sale price
   */
  private boolean futureSale = false;
  /**
   * the date the sale starts
   */
  private LocalDate saleStartDate;
  /**
   * the date the sale ends
   */
  private LocalDate saleEndDate;
  /**
   * the date the price was changed
   */
  private LocalDate modifiedDate;

  /**
   * Create a Price that only has the regular price.
   *
   * @param regularPrice the regular price
   */
  Price(double regularPrice) {
    this.regularPrice = regularPrice;
    modifiedDate = LocalDate.now();
  }

  /**
   * Create a Price that has a regular price, sale price, sale start date and sale end date.
   *
   * @param regularPrice the regular price
   * @param salePrice the sale price
   * @param saleStartDate the date the sale starts
   * @param saleEndDate the date the sale ends
   */
  Price(double regularPrice, double salePrice, LocalDate saleStartDate, LocalDate saleEndDate) {
    this.regularPrice = regularPrice;
    this.salePrice = salePrice;
    this.saleStartDate = saleStartDate;
    this.saleEndDate = saleEndDate;
    modifiedDate = LocalDate.now();
    //set the current sale status (whether it is currently on sale or has a future sale date)
    setSaleStatus();
  }

  /**
   * Helper method to set the current status for the sale based on if it is currently on sale or has
   * a future sale.
   */
  private void setSaleStatus() {
    LocalDate currentDate = LocalDate.now();
    try {
      //checks if today's date falls within the sale time period
      if ((currentDate.isAfter(saleStartDate) && currentDate.isBefore(saleEndDate)) || currentDate
          .equals(saleStartDate) || currentDate.equals(saleEndDate)) {
        onSale = true;
        futureSale = false;
        //check if today's date is before the sale starts
      } else if (currentDate.isBefore(saleStartDate)) {
        onSale = false;
        futureSale = true;
      }
    } catch (NullPointerException e) {
      onSale = false;
      futureSale = false;
    }
  }

  /**
   * Returns true only if there is no longer a sale and there isn't an upcoming sale
   */
  public boolean checkEndOfSale() {
    try {
      LocalDate currentDate = LocalDate.now();
      return currentDate.isAfter(saleEndDate);
      //if it doesn't have a sale end date, it is on regular price and there is no sale
    } catch (NullPointerException e) {
      return true;
    }
  }

  /**
   * Returns the current price in dollars.
   */
  public double getCurrentPrice() {
    if (onSale) {
      return salePrice;
    } else {
      return regularPrice;
    }
  }

  /**
   * Returns the start date of the sale.
   */
  public LocalDate getSaleStartDate() {
    return saleStartDate;
  }

  /**
   * Returns true only if the product is currently on sale.
   */
  public boolean getOnSale() {
    return onSale;
  }

  /**
   * Returns true only if the price contains a future sale
   */
  public boolean getFutureSale() {
    return futureSale;
  }

  /**
   * Returns the regular price.
   */
  public double getRegularPrice() {
    return regularPrice;
  }


  /**
   * Formats a string that can print the current price information. If it is on sale, this will
   * include the regular price, the sale price, the sale start date and the sale end date. If it is
   * not on sale, it will only show the regular price.
   *
   * @return String object with formatted price information.
   */
  @Override
  public String toString() {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd uuuu");
    //if it contains a sale start date, show the sale information
    if (saleStartDate != null) {
      return String.format(
          "\nDate Modified: " + modifiedDate.format(formatter)
              + "\nRegular Price: $ %.2f \nSale Price: $ %.2f \nSale Start Date: " + saleStartDate
              .format(formatter)
              + "\nSale End Date: " + saleEndDate.format(formatter) + "\n", regularPrice,
          salePrice);
      //if it is on regular, only show the regular price
    } else {
      return String
          .format("\nDate Modified: " + modifiedDate.format(formatter) + "\nRegular Price: $ %.2f\n",
              regularPrice);
    }
  }

}
