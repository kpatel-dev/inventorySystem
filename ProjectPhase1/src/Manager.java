/**
 * Manager is a subclass of User that is most likely to look up store details.
 */

public class Manager extends User{

  /**
   * Create a Manager with userID ID and and password password.
   *
   * @param ID: login ID of this Manager
   * @param password: password of this Manager
   */
  Manager(String ID, String password) {
    super(ID, "Manager", password);
    String[] selectCommands = {"Pending orders", "Daily sales", "Daily totals", "All commands"};
    setSelectCommands(selectCommands);
  }
}
