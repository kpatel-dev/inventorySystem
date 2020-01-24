import static java.lang.Integer.parseInt;

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
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

class CheckOutFrame extends JFrame implements ActionListener {

  private StoreGUI storeGUI;
  private JLabel productLabel, quantityLabel;
  private JComboBox<Product> products;
  private JFormattedTextField quantityF;
  private JButton addItem, removeItem, checkOut;
  private Receipt currentReceipt;
  private Product currentProduct;


  CheckOutFrame(StoreGUI storeGUI) {
    super("Checkout");
    setResizable(false);
    this.storeGUI = storeGUI;
    initComponents();

    // Set initial current command and product
    currentProduct = products.getItemAt(0);
    currentReceipt = storeGUI.store.receiptManager.makeReceipt();
  }

  private void initComponents() {
// Executes when window is closed
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        storeGUI.run();
        setVisible(false);
      }
    });

    int fontSize = 20;    // Buttons' font size

    // Set up quantity components
    quantityLabel = new JLabel("Enter initial quantity: ");
    newGUI.setButtonFontSize(quantityLabel, fontSize);
    quantityF = new JFormattedTextField();
    quantityF.setValue(0000);
    quantityF.setColumns(4);
    ;

    // Set up section components
    productLabel = new JLabel("Product: ");
    newGUI.setButtonFontSize(productLabel, fontSize);

    // Get valid section names and convert into String[]
    ArrayList<Product> productsArray = storeGUI.store.inventoryManager.getStoreProducts();
    Product[] productsList = new Product[productsArray.size()];

    for (int i = 0; i < productsList.length; i++) {
      productsList[i] = productsArray.get(i);
    }

    // Set up products Combo Box
    products = new JComboBox<>(productsList);
    products.setEditable(false);
    newGUI.setButtonFontSize(products, fontSize);
    products.addActionListener(this);

    // Set up quantity components
    quantityLabel = new JLabel("Enter quantity: ");
    newGUI.setButtonFontSize(quantityLabel, fontSize);
    quantityF = new JFormattedTextField();
    quantityF.setValue(0000);
    quantityF.setColumns(4);

    //set up add item button
    addItem = new JButton("Add Item");
    newGUI.setButtonFontSize(addItem, fontSize);
    addItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        addItemActionPerformed(e);
      }
    });

    //set up remove item button
    removeItem = new JButton("Remove Item");
    newGUI.setButtonFontSize(removeItem, fontSize);
    removeItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        removeItemActionPerformed(e);
      }
    });

    //set up checkout button
    checkOut = new JButton("Check out");
    newGUI.setButtonFontSize(removeItem, fontSize);
    checkOut.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        checkOutActionPerformed(e);
      }
    });

    addComponents(getContentPane());
  }


  private void addItemActionPerformed(ActionEvent evt) {
    int quantity = parseInt(quantityF.getText());
    Product product = currentProduct;
    if (product.getQuantity() >= quantity) {
      currentReceipt.addItem(product, quantity);
      storeGUI.store.stockManager.remove(product, quantity);
    } else {
      Logger.addEvent("CheckOutFrame", "addItemActionPerformed",
          "Not enough " + product.getName() + "in stock. Only " + product.getQuantity() + " left.",
          true);
    }
  }


  private void removeItemActionPerformed(ActionEvent evt) {
    int quantity = parseInt(quantityF.getText());
    Product product = currentProduct;
    if(currentReceipt.removeItem(product, quantity)) {
      storeGUI.store.stockManager.add(product, quantity);
    }
  }

  private void checkOutActionPerformed(ActionEvent evt) {
    storeGUI.store.receiptManager.printReceipt(currentReceipt);
    setVisible(false);
    storeGUI.run();
  }


  private void addComponents(Container pane) {
    GridLayout mainLayout = new GridLayout(5, 3);
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(mainLayout);

    // Setting up button sizes
    JButton b = new JButton("Filler button");
    Dimension buttonSize = b.getPreferredSize();
    Dimension preferredSize = new Dimension(
        (int) (buttonSize.getWidth() * 7.5) + newGUI.maxGap,
        (int) (buttonSize.getHeight() * 3.5) + newGUI.maxGap * 2);
    mainPanel.setPreferredSize(preferredSize);

    // Add the main panel buttons
    mainPanel.add(productLabel);
    mainPanel.add(products);
    mainPanel.add(quantityLabel);
    mainPanel.add(quantityF);
    mainPanel.add(addItem);
    mainPanel.add(removeItem);
    mainPanel.add(new JLabel());
    mainPanel.add(checkOut);

    pane.add(mainPanel, BorderLayout.NORTH);
    pane.add(new JSeparator(), BorderLayout.CENTER);
  }


  // Occurs when a product is selected
  public void actionPerformed(ActionEvent evt) {
    JComboBox comboBox = (JComboBox) evt.getSource();

    currentProduct = (Product) comboBox.getSelectedItem();

  }

  void run() {
    pack();
    setVisible(true);
  }
}
