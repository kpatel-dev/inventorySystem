/**
 * Cashier is a subclass of User that is most likely to sell items.
 */

public class Cashier extends User {

  /**
   * Create a Cashier with userID ID and and password password.
   *
   * @param ID: login ID of this Cashier
   * @param password: password of this Cashier
   */
  Cashier(String ID, String password) {
    super(ID, "Cashier", password);
    String[] selectCommands = {"Sell item", "Change quantity of items in stock",
        "Check sale price and dates", "All commands"};
    setSelectCommands(selectCommands);
  }
}
