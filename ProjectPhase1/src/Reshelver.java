/**
 * Reshelver is a subclass of User that is most likely to look up product location and quantities.
 */
class Reshelver extends User{

  /**
   * Create a Reshelver with userID ID and and password password.
   *
   * @param ID: login ID of this Reshelver
   * @param password: password of this Reshelver
   */
  Reshelver(String ID, String password) {
    super(ID, "Reshelver", password);
    String[] selectCommands = {"Product location", "Order history", "Product current quantity"
        , "All commands"};
    setSelectCommands(selectCommands);
  }
}
