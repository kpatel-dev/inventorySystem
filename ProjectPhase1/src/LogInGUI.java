import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;

class LogInGUI extends JFrame {
  private Store store;
  private JButton newUserButton;
  private JButton logInButton;
  private JButton closeStoreButton;
  private final String[] userTypes = {"Manager", "Cashier", "Reshelver", "Receiver"};
  private String[] tempUserInfo = new String[2];

  LogInGUI(Store store) {
    this.store = store;
    initComponents();
  }

  private void initComponents() {
    setResizable(false);

    // Executes when window is closed
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        store.shutDown();
        setVisible(false);
        store.newGUIFrame.setVisible(true);
      }
    });

    int fontSize = 30;    // Buttons' font size

    // Set up new user button
    newUserButton = new JButton("New User");
    newGUI.setButtonFontSize(newUserButton, fontSize);
    newUserButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        newUserButtonActionPerformed(evt);
      }
    });

    // Set up log in button
    logInButton = new JButton("Log In");
    newGUI.setButtonFontSize(logInButton, fontSize);
    logInButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        logInButtonActionPerformed(evt);
      }
    });

    // Set up close store button
    closeStoreButton = new JButton("Close Store");
    newGUI.setButtonFontSize(closeStoreButton, fontSize);
    closeStoreButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        closeStoreButtonActionPerformed(evt);
      }
    });

    addComponents(getContentPane());
  }

  private void addComponents(Container pane) {
    GridLayout mainLayout = new GridLayout(0,3);
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(mainLayout);

    // Setting up button sizes
    JButton b = new JButton("Filler button");
    Dimension buttonSize = b.getPreferredSize();
    Dimension preferredSize = new Dimension(
        (int)(buttonSize.getWidth() * 7.5) + newGUI.maxGap,
        (int)(buttonSize.getHeight() * 10.5) + newGUI.maxGap * 2);
    mainPanel.setPreferredSize(preferredSize);

    // Add the main panel buttons
    mainPanel.add(newUserButton);
    mainPanel.add(logInButton);
    mainPanel.add(closeStoreButton);

    pane.add(mainPanel, BorderLayout.NORTH);
    pane.add(new JSeparator(), BorderLayout.CENTER);
  }

  private void newUserButtonActionPerformed(ActionEvent evt) {
    RegisterUserFrame registerFrame = new RegisterUserFrame(store);
    setVisible(false);
    registerFrame.run();
  }

  private void logInButtonActionPerformed(ActionEvent evt) {
    LogInFrame logInFrame = new LogInFrame(store);
    setVisible(false);
    logInFrame.run();
  }

  private void closeStoreButtonActionPerformed(ActionEvent evt) {
    setVisible(false);
    store.newGUIFrame.setVisible(true);
  }

  void run() {
    pack();
    setVisible(true);
  }
}
