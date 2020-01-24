import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.management.openmbean.KeyAlreadyExistsException;

/**
 * UserManager keeps track of all registered Users of the system. It adds and deletes users from
 * the system, and also checks their credentials when attempting to log-in.
 */
public class UserManager {

  private HashMap<String, User> users;  // Key: String userID, Value: User associated with userID
  private HashMap<String, ArrayList<User>> userType;  // Key: String userType, Value: ArrayList of users of that type
  private BufferedReader kbd;

  UserManager() {
    this.users = new HashMap<>();
    this.userType = new HashMap<>();
    userType.put("Manager", new ArrayList<>());
    userType.put("Cashier", new ArrayList<>());
    userType.put("Receiver", new ArrayList<>());
    userType.put("Reshelver", new ArrayList<>());
  }

  /**
   * Create a new UserManager.
   */
  UserManager(BufferedReader kbd) {
    this.users = new HashMap<>();
    this.userType = new HashMap<>();
    userType.put("Manager", new ArrayList<>());
    userType.put("Cashier", new ArrayList<>());
    userType.put("Receiver", new ArrayList<>());
    userType.put("Reshelver", new ArrayList<>());
    this.kbd = kbd;
  }

  UserManager(String filePath) throws ClassNotFoundException, IOException {
    //  Taken from StudentManager.java in CSC207 lecture example
    // Reads serializable objects from filePath.
    File file = new File(filePath);

    if (file.exists()) {
      readFromFile(filePath);
    } else {
      file.createNewFile();
    }
  }

  /**
   * Create a new UserManager using the serialized information stored in file with path filePath.
   */
  UserManager(String filePath, BufferedReader kbd) throws ClassNotFoundException, IOException {
    //  Taken from StudentManager.java in CSC207 lecture example
    // Reads serializable objects from filePath.
    File file = new File(filePath);

    if (file.exists()) {
      readFromFile(filePath);
    } else {
      file.createNewFile();
    }
    this.kbd = kbd;
  }

  /**
   * Returns the User object with id userID.
   */
  User getUser(String userID) {
    if (!existingUser(userID)) {
      throw new IllegalArgumentException();
    } else {
      return users.get(userID);
    }
  }

  /**
   * Returns the hashmap of User Types
   */
  HashMap<String, ArrayList<User>> getUserType()

  {
    return userType;
  }

  /**
   * Return the hashmap of users
   */
  HashMap<String, User> getUsers() {
    return users;
  }

  /**
   * Return true if a User with id id is already registered in this UserManager.
   */
  boolean existingUser(String id) {
    return users.containsKey(id);
  }

  /**
   * Return true if type is matches the name of any of the user subclasses.
   */
  boolean validType(String type) {
    return userType.containsKey(type) || type.equals("New User");
  }


  /**
   * Register a new User with id userID, type type, and password userPassword.
   */
  private void addUser(String userID, String type, String userPassword) {
    if (users.containsKey(userID)) {
      throw new KeyAlreadyExistsException();
    } else {
      if (existingUser(userID)) {
        throw new KeyAlreadyExistsException();
      } else if (!validType(type)) {
        throw new IllegalArgumentException();
      } else {
        switch (type) {
          case "Manager":
            Manager manager = new Manager(userID, userPassword);
            users.put(userID, manager);
            userType.get(type).add(manager);
            break;

          case "Cashier":
            Cashier cashier = new Cashier(userID, userPassword);
            users.put(userID, cashier);
            userType.get(type).add(cashier);
            break;

          case "Receiver":
            Receiver receiver = new Receiver(userID, userPassword);
            users.put(userID, receiver);
            userType.get(type).add(receiver);
            break;

          case "Reshelver":
            Reshelver reshelver = new Reshelver(userID, userPassword);
            users.put(userID, reshelver);
            userType.get(type).add(reshelver);
            break;
        }
      }
    }
  }

  /**
   * Delete a registered User from this UserManager.
   */
  public void deleteUser(String userID) {
    User user = users.get(userID);
    User removed = users.remove(userID);

    if (removed == null) {
      Logger.addEvent("UserManager", "deleteUser",
          "User " + userID + " does not exist and could not be removed from user manager.",
          true);
    } else {
      userType.get(user.getType()).remove(user);
      Logger.addEvent("UserManager", "deleteUser",
          "User " + userID + " was removed from user manager.", true);
    }
  }

  boolean registerUser(String userID, String type, String userPassword) {
    if(!existingUser(userID)) {
      addUser(userID, type, userPassword);

      Logger.addEvent("UserManager", "registerUser",
          "User " + userID + " was registered to user manager.", true);
      return true;
    }
    else {
      Logger.addEvent("UserManager", "registerUser",
          "User " + userID + " is already registered to user manager.", true);
      return false;
    }
  }

  /**
   * Obtain new user's ID, type, and password and use addUser to register this user into this
   * UserManager, and return the new User object.
   */
  User registerUser() throws IOException {

    User addedUser = null;

    while (addedUser == null) {
      System.out.println("Please enter a new ID: ");
      String id = kbd.readLine().trim();

      while (existingUser(id)) {
        System.out.println("User ID already exists. Please enter a different ID: ");
        id = kbd.readLine().trim();
      }

      System.out.println("Please enter a user type (One of Manager/Cashier/Receiver/Reshelver): ");
      String type = kbd.readLine().trim();

      while (!validType(type)) {
        System.out.println("Invalid User-type. Please enter one of the following: "
            + "Manager/Cashier/Receiver/Reshelver");
        type = kbd.readLine().trim();
      }

      System.out.println("Please enter a password: ");
      String pw = kbd.readLine().trim();

      try {
        addUser(id, type, pw);
      } catch (KeyAlreadyExistsException e) {
        Logger.addEvent("UserManager", "registerUser",
            "User " + id + " already exists and could not be registered to user manager.",
            true);

      } catch (IllegalArgumentException e) {
        Logger.addEvent("UserManager", "registerUser",
            "User type " + type + " does not exist and user could not be registered to "
                + "user manager.", true);
      }

      addedUser = getUser(id);
    }

    Logger.addEvent("UserManager", "registerUser",
        "User " + addedUser.getID() + " was registered to user manager.", true);

    return addedUser;
  }

  /**
   * Returns true if the User object with id userId has password as its password. If userID is
   * not registered in this UserManager, return false.
   */
  boolean checkUserCredentials(String userID, String password) {

    if (!existingUser(userID)) {
      Logger.addEvent("UserManager", "checkUserCredentials",
          "User " + userID + " log in failed.", false);
      return false;
    } else {
      User user = users.get(userID);
      boolean logIn = user.getPassword().equals(password);

      if (logIn) {
        Logger.addEvent("UserManager", "checkUserCredentials",
            "User " + userID + " logged in successfully.", false);
      } else {
        Logger.addEvent("UserManager", "checkUserCredentials",
            "User " + userID + " log in failed.", false);
      }

      return logIn;
    }
  }

  /**
   * Returns a User if an existing user ID and corresponding password are inputted via kbd.
   * Returns null if log-in was unsuccessful. Currently not possible but may be useful in the
   * future in order to limit the number of times someone can attempt to log-in.
   */
  User logIn() throws IOException {

    User currentUser = null;

    while (currentUser == null) {
      System.out.println("Enter user ID: ");
      String id = kbd.readLine().trim();
      System.out.println("Enter password: ");
      String pw = kbd.readLine().trim();
      while (!checkUserCredentials(id, pw)) {
        System.out.println("Invalid credentials. Please try again. ");
        System.out.println("Enter user ID: ");
        id = kbd.readLine().trim();
        System.out.println("Enter password: ");
        pw = kbd.readLine().trim();
      }
      System.out.println("Log-in successful.");
      currentUser = getUser(id);
    }

    return currentUser;
  }

  /**
   * Read an .ser file with file path filePath and update the contents of this UserManager.
   */
  private void readFromFile(String filePath) throws ClassNotFoundException {
    try {
      // Taken from StudentManager.java in CSC207 lecture example
      InputStream file = new FileInputStream(filePath);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);

      // Deserialize
      // kbd is not serializable and is never read.
      users = (HashMap<String, User>) input.readObject();
      userType = (HashMap<String, ArrayList<User>>) input.readObject();

      Logger.addEvent("UserManager", "readFromFile",
          "User manager was loaded from " + filePath + ".", false);
    } catch (IOException e) {
      System.out.println("Unable to load User Manager from file.");
      Logger.addEvent("UserManager", "readFromFile",
          "Unable to load user manager from " + filePath + ".", true);
    }
  }
}