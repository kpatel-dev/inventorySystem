import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Creates the main GUI for the store with all the commands
 */
class StoreGUI extends JFrame implements ActionListener {
  Store store;
  private JLabel commands;
  private JComboBox<String> availableCommands;
  private JButton enterButton;
  private String currentCommand;
  private int fontSize = 20;    // Buttons' font size

  /**
   * Create a frame for the store gui
   * @param store the store the GUI is being made for
   */
  StoreGUI(Store store) {
    super(store.type);
    setResizable(false);
    this.store = store;
    initComponents();
  }

  /**
   * Initialize the components for the frame
   */
  private void initComponents() {

    // Executes when window is closed
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        store.run();
        setVisible(false);
      }
    });

    // Set up commands label
    commands = new JLabel("Commands: ");
    newGUI.setButtonFontSize(commands, fontSize);

    String[] userCommands = store.currentUser.returnSelectCommandsList();
    // Set up commands Combo Box
    availableCommands = new JComboBox<>(userCommands);
    availableCommands.setEditable(false);
    newGUI.setButtonFontSize(availableCommands, fontSize);
    availableCommands.addActionListener(this);

    currentCommand = availableCommands.getItemAt(0);  // Set first command as default

    // Set up enter button
    enterButton = new JButton("Enter");
    newGUI.setButtonFontSize(enterButton, fontSize);
    enterButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        enterButtonActionPerformed(evt);
      }
    });

    addComponents(getContentPane());
  }

  /**
   * add all the components to the panel
   * @param pane the container containing the panel
   */
  private void addComponents(Container pane) {
    // Reset pane
    pane.removeAll();

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
    mainPanel.add(commands);
    mainPanel.add(availableCommands);
    mainPanel.add(new JLabel());
    mainPanel.add(enterButton);

    pane.add(mainPanel, BorderLayout.NORTH);
    pane.add(new JSeparator(), BorderLayout.CENTER);
  }

  /**
   * Action performed when the enter button is clicked
   * @param evt the event that occured
   */
  private void enterButtonActionPerformed(ActionEvent evt) {
    switch(currentCommand) {
      case "Check out customer":
        CheckOutFrame checkOutFrame = new CheckOutFrame(this);
        setVisible(false);
        checkOutFrame.run();
        break;
      case "Change quantity of items in stock":
        ChangeQuantityFrame changeQuantityFrame = new ChangeQuantityFrame(this);
        setVisible(false);
        changeQuantityFrame.run();
        break;
      case "Daily totals":
        store.newGUIFrame.print(store.dailySalesManager.displayTotals());
        break;
      case "Daily sales":
        store.dailySalesManager.printDailySales();
        break;
      case "Pending orders":
        store.orderManager.printPendingOrders();
        break;
      case "Order history":
        store.orderManager.printOrderHistory();
        break;
      case "Order history of specific product by upc": case "Product cost":
        case "Product current price": case "Product current quantity": case "Product location":
          case "Product price history": case "Check sale price and dates": case "Refund product":
            case "Scan existing item": case "Sell item": case "Change product price (regular)":
              case "Change product price (sale)": case "Scan in one": case "Change product location":
        ProductFrame productFrame = new ProductFrame(this);
        setVisible(false);
        productFrame.run();
        break;
      case "Print on sale items":
        store.priceManager.printOnSaleItems();
        break;
      case "Print upcoming sale items":
        store.priceManager.printUpcomingSaleItems();
        break;
      case "Print in stock items":
        store.stockManager.printInStockItems();
        break;
      case "Print out of stock items":
        store.stockManager.printOutOfStockItems();
        break;
      case "Scan new item":
        NewItemFrame newItemFrame = new NewItemFrame(this);
        setVisible(false);
        newItemFrame.run();
        break;
      case "All commands":
        ArrayList<String> allCommands = store.currentUser.returnAllCommandsList();
        String[] allCommandsList = new String[allCommands.size()];

        for(int i = 0; i < allCommandsList.length; i++) {
          allCommandsList[i] = allCommands.get(i);
        }
        availableCommands = new JComboBox<>(allCommandsList);
        availableCommands.setEditable(false);
        newGUI.setButtonFontSize(availableCommands, fontSize);
        availableCommands.addActionListener(this);
        currentCommand = availableCommands.getItemAt(0);
        addComponents(getContentPane());
        setVisible(true);
        break;
      case "Log out":
        store.run();
        setVisible(false);
        break;
    }
  }

  /**
   * Set the current command when a command is selected
   * @param evt the JComboBox event that occurred
   */
  // Occurs when a command is selected
  public void actionPerformed(ActionEvent evt) {
    JComboBox comboBox = (JComboBox)evt.getSource();
    currentCommand = (String)comboBox.getSelectedItem();
  }

  /**
   * Make the gui visible
   */
  void run() {
    pack();
    setVisible(true);
  }
}
