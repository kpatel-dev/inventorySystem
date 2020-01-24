import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class ProductFrame extends JFrame implements ActionListener{
  private StoreGUI storeGUI;
  private JLabel productLabel;
  private JComboBox<Product> products;
  private JLabel commands;
  private JComboBox<String> availableCommands;
  private JButton enterButton;
  private String currentCommand;
  private Product currentProduct;

  ProductFrame(StoreGUI storeGUI) {
    super("Product");
    this.storeGUI = storeGUI;
    initComponents();

    // Set initial current command and product
    currentProduct = products.getItemAt(0);
    currentCommand = availableCommands.getItemAt(0);
  }

  private void initComponents() {
    // Executes when window is closed
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        setVisible(false);
        storeGUI.setVisible(true);
      }
    });

    int fontSize = 20;    // Buttons' font size

    // Set up product label
    productLabel = new JLabel("Product: ");
    newGUI.setButtonFontSize(productLabel, fontSize);

    ArrayList<Product> productsArray = storeGUI.store.inventoryManager.getStoreProducts();
    Product[] productsList = new Product[productsArray.size()];

    for(int i = 0; i < productsList.length; i++) {
      productsList[i] = productsArray.get(i);
    }

    // Set up products Combo Box
    products = new JComboBox<>(productsList);
    products.setEditable(false);
    newGUI.setButtonFontSize(products, fontSize);
    products.addActionListener(this);

    // Set up commands label
    commands = new JLabel("Commands: ");
    newGUI.setButtonFontSize(commands, fontSize);

    String[] productCommands = {"Order history of specific product by upc", "Product cost",
        "Product current price", "Product current quantity", "Product location",
        "Product price history", "Scan existing item", "Sell item", "Refund product",
        "Change product price (regular)", "Change product price (sale)", "Scan in one"
        , "Change product location", "Print aisle number of product"};
    // Set up commands Combo Box
    availableCommands = new JComboBox<>(productCommands);
    availableCommands.setEditable(false);
    newGUI.setButtonFontSize(availableCommands, fontSize);
    availableCommands.addActionListener(this);

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
    mainPanel.add(productLabel);
    mainPanel.add(products);
    mainPanel.add(commands);
    mainPanel.add(availableCommands);
    mainPanel.add(new JLabel());
    mainPanel.add(enterButton);

    pane.add(mainPanel, BorderLayout.NORTH);
    pane.add(new JSeparator(), BorderLayout.CENTER);
  }

  private void enterButtonActionPerformed(ActionEvent evt) {
    switch(currentCommand) {
      case "Order history of specific product by upc":
        storeGUI.store.orderManager.printProductOrderHistory(currentProduct.getUpc());
        break;
      case "Product cost":
        Double cost = currentProduct.getCost();
        storeGUI.store.newGUIFrame.print(cost.toString());
        break;
      case "Product current price":
        Price price = currentProduct.getPrice();
        storeGUI.store.newGUIFrame.print(price.toString());
        break;
      case "Product current quantity":
        Integer quantity = currentProduct.getQuantity();
        storeGUI.store.newGUIFrame.print(quantity.toString());
        break;
      case "Product location":
        Section location = currentProduct.getSection();
        storeGUI.store.newGUIFrame.print(String.format("Section: " + location.getSectionName()
        + "\n"));
        break;
      case "Product price history":
        storeGUI.store.priceManager.printPriceHistory(currentProduct);
        break;
      case "Scan existing item":
        storeGUI.store.orderManager.processIncomingOrder(currentProduct.getUpc());
        break;
      case "Sell item":
        storeGUI.store.stockManager.remove(currentProduct);
        storeGUI.store.newGUIFrame.print("One " + currentProduct.getName() + " sold.");
        storeGUI.store.dailySalesManager.saleTransaction(currentProduct, 1);
        break;
      case "Scan in one":
        storeGUI.store.stockManager.add(currentProduct);
        break;
      case "Print aisle number of product":
        storeGUI.store.locationManager.printAisleNumByProduct(currentProduct);
        break;
      case "Change product location":
        ChangeLocationFrame changeLocationFrame = new ChangeLocationFrame(storeGUI, currentProduct);
        setVisible(false);
        changeLocationFrame.run();
        break;
      case "Change product price (regular)":
        ChangeRegularPriceFrame changeRegularPriceFrame = new ChangeRegularPriceFrame(
            storeGUI, currentProduct);
        setVisible(false);
        changeRegularPriceFrame.run();
        break;
      case "Change product price (sale)":
        ChangeSalePriceFrame changeSalePriceFrame = new ChangeSalePriceFrame(storeGUI, currentProduct);
        setVisible(false);
        changeSalePriceFrame.run();
        break;
      case "Refund product":
        RefundTransactionFrame refundTransactionFrame = new RefundTransactionFrame(
            storeGUI, currentProduct);
        setVisible(false);
        refundTransactionFrame.run();
        break;
    }
  }

  // Occurs when a product or quantity is selected
  public void actionPerformed(ActionEvent evt) {
    JComboBox comboBox = (JComboBox)evt.getSource();

    if(evt.getSource() == products) {
      currentProduct = (Product)comboBox.getSelectedItem();
    }
    else {
      currentCommand = (String)comboBox.getSelectedItem();
    }
  }

  void run() {
    pack();
    setVisible(true);
  }
}
