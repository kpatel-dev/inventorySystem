/**
 * Exception used in StockManager; it is thrown when a Product product with the same upc as
 * another product already registered in StockManager is attempted to be registered.
 */
class DuplicateProductException extends RuntimeException{

  DuplicateProductException(String s) {
    super(s);
  }
}
