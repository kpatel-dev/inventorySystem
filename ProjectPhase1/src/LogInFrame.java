import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;

class LogInFrame extends JFrame {

  private Store store;
  private JLabel IDLabel;
  private JTextField IDTextField;
  private JLabel passwordLabel;
  private JPasswordField passwordField;
  private JButton logInButton;

  LogInFrame(Store store) {
    super("Log In Existing User");
    setResizable(false);
    this.store = store;
    initComponents();
  }

  private void initComponents() {
    // Executes when window is closed
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        store.run();
        setVisible(false);
      }
    });

    int fontSize = 20;    // Buttons' font size

    // Set up id label
    IDLabel = new JLabel("Enter ID: ");
    newGUI.setButtonFontSize(IDLabel, fontSize);

    // Set up id textfield
    IDTextField = new JTextField(20);

    // Set up password label
    passwordLabel = new JLabel("Enter password: ");
    newGUI.setButtonFontSize(passwordLabel, fontSize);

    // Set up password field
    passwordField = new JPasswordField(20);

    // Set up log in button
    logInButton = new JButton("Log in");
    newGUI.setButtonFontSize(logInButton, fontSize);
    logInButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        System.out.println(evt == null);
        logInButtonActionPerformed(evt);
      }
    });

    addComponents(getContentPane());
  }

  private void addComponents(Container pane) {
    GridLayout mainLayout = new GridLayout(4,3);
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(mainLayout);

    // Setting up button sizes
    JButton b = new JButton("Filler button");
    Dimension buttonSize = b.getPreferredSize();
    Dimension preferredSize = new Dimension(
        (int)(buttonSize.getWidth() * 7.5) + newGUI.maxGap,
        (int)(buttonSize.getHeight() * 3.5) + newGUI.maxGap * 2);
    mainPanel.setPreferredSize(preferredSize);

    // Add the main panel buttons
    mainPanel.add(IDLabel);
    mainPanel.add(IDTextField);
    mainPanel.add(passwordLabel);
    mainPanel.add(passwordField);
    mainPanel.add(new JLabel());
    mainPanel.add(logInButton);

    pane.add(mainPanel, BorderLayout.NORTH);
    pane.add(new JSeparator(), BorderLayout.CENTER);
  }

  private void logInButtonActionPerformed(ActionEvent evt) {
    String userID = IDTextField.getText();
    String password = passwordField.getText();
    UserManager userManager = store.userManager;
    System.out.println(userManager);

    boolean existingUser = userManager.existingUser(userID);

    if(existingUser){
      if(userManager.checkUserCredentials(userID, password)) {
        setVisible(false);
        store.run(store.userManager.getUser(userID));
      }
      else {
        System.out.println("Invalid user credentials.");
      }
    }
    else {
      System.out.println("Invalid user ID. Please enter a different user ID.");
    }
  }

  void run() {
    pack();
    setVisible(true);
  }
}
