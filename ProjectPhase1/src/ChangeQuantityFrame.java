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

class ChangeQuantityFrame extends JFrame implements ActionListener{
  private StoreGUI storeGUI;
  private JLabel productLabel;
  private JComboBox<Product> products;
  private JLabel quantityLabel;
  private JComboBox<String> quantity;
  private JButton enterButton;
  private Product currentProduct;
  private int currentQuantityToChange;

  ChangeQuantityFrame(StoreGUI storeGUI) {
    super("Change Quantity");
    setResizable(false);
    this.storeGUI = storeGUI;
    initComponents();

    // Initial current product and quantity to change
    currentProduct = products.getItemAt(0);
    currentQuantityToChange = Integer.parseInt(quantity.getItemAt(0));
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

    // Set up user type Combo Box
    products = new JComboBox<>(productsList);
    products.setEditable(false);
    newGUI.setButtonFontSize(products, fontSize);
    products.addActionListener(this);

    // Set up quantity label
    quantityLabel = new JLabel("Quantity: ");
    newGUI.setButtonFontSize(quantityLabel, fontSize);

    String[] quantityList = new String[11];

    for(int i = -5; i < 6; i++) {
      quantityList[i + 5] = (new Integer(i)).toString();
    }

    // Set up quantity Combo Box
    quantity = new JComboBox<>(quantityList);
    quantity.setEditable(false);
    newGUI.setButtonFontSize(quantity, fontSize);
    quantity.addActionListener(this);

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
    mainPanel.add(quantityLabel);
    mainPanel.add(quantity);
    mainPanel.add(new JLabel());
    mainPanel.add(enterButton);

    pane.add(mainPanel, BorderLayout.NORTH);
    pane.add(new JSeparator(), BorderLayout.CENTER);
  }

  private void enterButtonActionPerformed(ActionEvent evt) {
    if(currentQuantityToChange < 0) {
      storeGUI.store.stockManager.remove(currentProduct, 0 - currentQuantityToChange);
      setVisible(false);
      storeGUI.run();
    }
    else if(currentQuantityToChange > 0) {
      storeGUI.store.stockManager.add(currentProduct, currentQuantityToChange);
      setVisible(false);
      storeGUI.run();
    }
  }

  // Occurs when a product or quantity is selected
  public void actionPerformed(ActionEvent evt) {
    JComboBox comboBox = (JComboBox)evt.getSource();

    if(evt.getSource() == products) {
      currentProduct = (Product)comboBox.getSelectedItem();
    }
    else if(evt.getSource() == quantity) {
      currentQuantityToChange = Integer.parseInt((String)comboBox.getSelectedItem());
    }
  }

  void run() {
    pack();
    setVisible(true);
  }
}
