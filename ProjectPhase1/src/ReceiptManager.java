import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ReceiptManager manages the receipts. It keeps track of the total receipts, makes new receipts and
 * prints receipts.
 */
public class ReceiptManager {

  /**
   * The current receipt number for today
   */
  private static int receiptNumber = 0;
  /**
   * The dailySalesManager that the ReceiptManager is associated with
   */
  private DailySalesManager dailySalesManager;

  /**
   * Creates a new ReceiptManager object by initializing the associated dailySalesManager
   *
   * @param dailySalesManager the dailySalesManager associated with the RecieptManager
   */
  ReceiptManager(DailySalesManager dailySalesManager) {
    this.dailySalesManager = dailySalesManager;
  }

  /**
   * Create a new receipt for this ReceiptManager
   *
   * @return the new Receipt made
   */
  public Receipt makeReceipt() {
    return new Receipt(dailySalesManager);
  }

  /**
   * Prints the receipt to the corresponding month folder inside the Receipt directory and completes
   * the transactions.
   *
   * @param receipt the receipt that needs to be printed
   */
  public void printReceipt(Receipt receipt) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-uuuu h:mm:ssa");
      DateTimeFormatter date = DateTimeFormatter.ofPattern("MMM-dd-uuuu");

      File file = new File("Receipts/" + LocalDateTime.now().format(date));
      file.mkdirs();
      //create a file for this receipt number
      FileWriter writer = new FileWriter(
          "Receipts/" + LocalDateTime.now().format(date) + "/" + receiptNumber++ + ".txt");
      //write a title to the receipt file
      writer.write("Receipt" + "\nReceipt Number: " + receiptNumber
          + "\nTime Printed: " + LocalDateTime.now().format(formatter) + "\n");
      //write the receipt contents to the file
      writer.write(receipt.toString());
      Logger.addEvent("Receipt", "printReceipt",
          "printed receipt", true);

      writer.close();

      //create transactions for all the items on the receipt
      receipt.cashOut();
    } catch (IOException e) {
      //log the event
      Logger.addEvent("Receipt", "printReceipt",
          "Couldn't print receipt", true);

    }
  }


}



