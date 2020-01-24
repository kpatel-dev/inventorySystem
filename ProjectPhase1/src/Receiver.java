/**
 * Receiver is a subclass of User that is most likely to scan in products and look up their history.
 */

class Receiver extends User {

  private String[] selectCommands = {"Scan new item", "Scan existing item", "Product location",
      "Product cost", "Product price history", "Product current price", "All commands"};

  /**
   * Create a Receiver with userID ID and and password password.
   *
   * @param ID: login ID of this Receiver
   * @param password: password of this Receiver
   */
  Receiver(String ID, String password) {
    super(ID, "Receiver", password);
    String[] selectCommands = {"Scan new item", "Scan existing item", "Product location",
        "Product cost", "Product price history", "Product current price", "All commands"};
    setSelectCommands(selectCommands);
  }
}
