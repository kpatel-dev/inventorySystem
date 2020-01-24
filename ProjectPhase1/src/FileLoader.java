import java.io.IOException;
import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * Abstract class FileLoader parses information from .txt files and creates the relevant objects.
 */
abstract class FileLoader {

  BufferedReader fileInput;
  LocationManager locationManager;

  /**
   * Create a new FileLoader object.
   *
   * @param fileInput: Open file to read information from
   * @param locationManager: LocationManager object that holds all Section objects
   */
  FileLoader(BufferedReader fileInput, LocationManager locationManager) {

    this.fileInput = fileInput;
    this.locationManager = locationManager;
  }

  abstract ArrayList loadItems() throws IOException;
}
