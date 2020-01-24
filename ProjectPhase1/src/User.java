import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * User is any registered user of the system that is identified by ID.
 */
abstract class User implements Serializable{
  private String ID;
  private String type;  // Subclass type of User
  private String password;
  private ArrayList<String> validCommands;  // Commands that a User can type in as valid input
  private String[] selectCommands;

  /**
   * Create a Reshelver with userID ID and and password password.
   *
   * @param ID: login ID of this User
   * @param type: String of the subclass name of this User
   * @param password: password of this User
   */
  User(String ID, String type, String password) {
    this.ID = ID;
    this.type = type;
    this.password = password;
    this.validCommands = new ArrayList<>();
    Collections.addAll(validCommands, "Check out customer", "Change quantity of items in stock",
        "Change product price (regular)", "Change product price (sale)", "Daily totals",
        "Daily sales",  "Order history of specific product by upc", "Refund product",
        "Pending orders", "Product cost", "Product current price", "Product current quantity",
        "Product location", "Order history", "Product price history", "Scan existing item",
        "Scan new item", "Sell item", "Print on sale items", "Print upcoming sale items",
        "Scan in one", "Print aisle number of product", "Print in stock items",
        "Print out of stock items", "Change product location",
        "All commands", "Log out", "Shut down");
  }

  /**
   * returns the users id
   */
  String getID() {return this.ID;}

  /**
   * returns the users type
   */
  String getType() {return this.type;}

  /**
   * returns the user's password
   */
  String getPassword() {return this.password;}

  void setSelectCommands(String[] commands) {
    selectCommands = commands;
  }

  /**
   * Returns true if command is one of the commands available to a User.
   */
  boolean checkCommand(String command) {
    return validCommands.contains(command);
  }


  ArrayList<String> returnAllCommandsList() {
    return validCommands;
  }

  /**
   * Return a printable, user-friendly String of all commands available to all Users, each
   * separated with a newline character.
   */
  String returnAllCommands() {
    String s = "";

    for(String command : validCommands) {
      s += command + "\n";
    }

    return s;
  }

  /**
   * Return a printable, user-friendly String of subclass-specific commands, each separated with a
   * newline character.
   */
  String returnSelectCommands() {
    String s = "";

    for(String command : selectCommands) {
      s += command + "\n";
    }

    return s;
  }

  /**
   * Returns the list of commands for all users
   */
  String[] returnSelectCommandsList() {
    return selectCommands;
  }
}