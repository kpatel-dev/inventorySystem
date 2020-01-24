import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;

class RegisterUserFrame extends JFrame implements ActionListener {
  private Store store;
  private JLabel newIDLabel;
  private JTextField newIDTextField;
  private JLabel newPasswordLabel;
  private JPasswordField newPasswordField;
  private JLabel userType;
  private JComboBox<String> types;
  private final String[] userTypes = {"Manager", "Cashier", "Reshelver", "Receiver"};
  private JButton registerButton;
  private String newUserType;

  RegisterUserFrame(Store store) {
    super("Register New User");
    setResizable(false);
    this.store = store;
    initComponents();
    newUserType = types.getItemAt(0);
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

    // Set up new id label
    newIDLabel = new JLabel("Enter an ID: ");
    newGUI.setButtonFontSize(newIDLabel, fontSize);

    // Set up new id textfield
    newIDTextField = new JTextField(20);

    // Set up new password label
    newPasswordLabel = new JLabel("Enter a password: ");
    newGUI.setButtonFontSize(newPasswordLabel, fontSize);

    // Set up new password field
    newPasswordField = new JPasswordField(20);

    // Set up user type label
    userType = new JLabel("User type: ");
    newGUI.setButtonFontSize(userType, fontSize);

    // Set up user type Combo Box
    types = new JComboBox<>(userTypes);
    newGUI.setButtonFontSize(types, fontSize);
    types.setEditable(false);
    types.addActionListener(this);

    // Set up register button
    registerButton = new JButton("Register");
    newGUI.setButtonFontSize(registerButton, fontSize);
    registerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        registerButtonActionPerformed(evt);
      }
    });

    addComponents(getContentPane());
  }

  private void addComponents(Container pane){
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
    mainPanel.add(newIDLabel);
    mainPanel.add(newIDTextField);
    mainPanel.add(newPasswordLabel);
    mainPanel.add(newPasswordField);
    mainPanel.add(userType);
    mainPanel.add(types);
    mainPanel.add(new JLabel());
    mainPanel.add(registerButton);

    pane.add(mainPanel, BorderLayout.NORTH);
    pane.add(new JSeparator(), BorderLayout.CENTER);
  }

  private void registerButtonActionPerformed(ActionEvent evt) {

    String userID = newIDTextField.getText();
    String password = newPasswordField.getText();
    UserManager userManager = store.userManager;

    boolean isRegistered = userManager.registerUser(userID, this.newUserType, password);

    if(isRegistered){
      setVisible(false);
      store.run(store.userManager.getUser(userID));
    }
    else {
      System.out.println("Invalid user ID. Please enter a different user ID.");
    }
  }

  public void actionPerformed(ActionEvent evt) {
    JComboBox comboBox = (JComboBox)evt.getSource();
    newUserType = (String)comboBox.getSelectedItem();
  }

  void run() {
    pack();
    setVisible(true);
  }
}
