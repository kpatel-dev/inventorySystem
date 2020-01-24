/**
 * Exception for Product, where more quantity than what exists is trying to be removed.
 */
public class InvalidAmountException extends Exception {

  public InvalidAmountException() {
    super();
  }

  public InvalidAmountException(String s) {
    super(s);
  }
}
