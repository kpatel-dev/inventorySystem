import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger class logs the events into a new text file according to the date.
 */
public class Logger {

  private static newGUI GUI;

  /**
   * file that the logs are stores to
   */
  private static File file;

  /**
   * Create a new text file for the logs according to the date. The filename would be in the format
   * of log-MMM-dd-uuuu.txt, where MMM-dd-uuu represents the date.
   */
  Logger() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-uuuu");
    File logDir = new File(
        "ProjectPhase1", "Logs");

    // attempt to create the directory here
    boolean successful = logDir.mkdirs();
    if (successful) {
      // creating the directory succeeded
      System.out.println("Log directory was created successfully");
    } else {
      // creating the directory failed
      System.out.println("Log directory couldn't be made or already exists");
    }

    DateTimeFormatter date = DateTimeFormatter.ofPattern("MMM-dd-uuuu");
    //make a new file inside the Log directory
    file = new File(logDir.getPath(), "log-" + LocalDate.now().format(date) + ".txt");
    try {
      file.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  Logger(newGUI GUI) {
    this();
    this.GUI = GUI;
  }

  /**
   * Add an event that occurred to the log file.
   *
   * @param description the description of the event
   * @param sourceClass the name of the source class
   * @param sourceMethod the name of the source method
   * @param outputToUser true if the description should be output to the user
   */
  public static void addEvent(String sourceClass, String sourceMethod, String description,
      boolean outputToUser) {
    try {
      DateTimeFormatter date = DateTimeFormatter.ofPattern("MMM-dd-uuuu");
      //append to the existing file
      FileWriter fw = new FileWriter(file.getPath(), true);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-uuuu h:mm:ssa");
      //log the event with the time, class name, method and a description
      fw.write(LocalDateTime.now().format(formatter) +
          "     " + sourceClass + "     " + sourceMethod
          + "\nEvent: " + description + "\n\n");
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    //output the description to the terminal
    if (outputToUser) {
      GUI.print(description);
    }
  }
}
