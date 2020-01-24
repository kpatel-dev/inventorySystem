import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class NewItemFrame extends JFrame implements ActionListener, PropertyChangeListener {
  private StoreGUI storeGUI;
  private JLabel upcLabel, nameLabel, quantityLabel, priceLabel, sectionLabel;
  private JLabel thresholdLabel, distributorLabel, costLabel, aisleLabel;
  private JFormattedTextField upcF, quantityF, priceF, thresholdF, costF, aisleF;
  private JTextField nameF, distributorF;
  private JComboBox<String> sectionF;
  private JButton registerItemButton;
  private String upc;
  private int quantity, threshold, aisle;
  private Price price;
  private Section section;
  private double cost;

  NewItemFrame(StoreGUI storeGUI) {
    super("Register New Item");
    setResizable(false);
    this.storeGUI = storeGUI;
    initComponents();
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

    // Set up upc components
    upcLabel = new JLabel("Enter the UPC: ");
    newGUI.setButtonFontSize(upcLabel, fontSize);
    upcF = new JFormattedTextField();
    upcF.setValue(000000000000);
    upcF.setColumns(12);
    upcF.addPropertyChangeListener("value", this);

    // Set up name components
    nameLabel = new JLabel("Enter product name: ");
    newGUI.setButtonFontSize(nameLabel, fontSize);
    nameF = new JTextField(20);

    // Set up quantity components
    quantityLabel = new JLabel("Enter initial quantity: ");
    newGUI.setButtonFontSize(quantityLabel, fontSize);
    quantityF = new JFormattedTextField();
    quantityF.setValue(0000);
    quantityF.setColumns(4);
    quantityF.addPropertyChangeListener("value", this);

    // Set up price components
    priceLabel = new JLabel("Enter regular price: ");
    newGUI.setButtonFontSize(priceLabel, fontSize);
    priceF = new JFormattedTextField();
    priceF.setValue(0000.00);
    priceF.setColumns(7);
    priceF.addPropertyChangeListener("value", this);

    // Set up section components
    sectionLabel = new JLabel("Section: ");
    newGUI.setButtonFontSize(sectionLabel, fontSize);

    // Get valid section names and convert into String[]
    ArrayList<Section> sectionsArray = storeGUI.store.locationManager.getSectionList();
    String[] sectionsList = new String[sectionsArray.size()];

    for(int i = 0; i < sectionsList.length; i++) {
      sectionsList[i] = sectionsArray.get(i).getSectionName();
    }

    sectionF = new JComboBox<>(sectionsList);
    newGUI.setButtonFontSize(sectionF, fontSize);
    sectionF.setEditable(false);
    sectionF.addActionListener(this);

    // Set up threshold components
    thresholdLabel = new JLabel("Enter threshold: ");
    newGUI.setButtonFontSize(thresholdLabel, fontSize);
    thresholdF = new JFormattedTextField();
    thresholdF.setValue(0000);
    thresholdF.setColumns(4);
    thresholdF.addPropertyChangeListener("value", this);

    // Set up distributor components
    distributorLabel = new JLabel("Enter distributor: ");
    newGUI.setButtonFontSize(distributorLabel, fontSize);
    distributorF = new JTextField(20);

    // Set up cost components
    costLabel = new JLabel("Enter cost: ");
    newGUI.setButtonFontSize(costLabel, fontSize);
    costF = new JFormattedTextField();
    costF.setValue(0000.00);
    costF.setColumns(7);
    costF.addPropertyChangeListener("value", this);

    // Set up aisle components
    aisleLabel = new JLabel("Enter aisle: ");
    newGUI.setButtonFontSize(aisleLabel, fontSize);
    aisleF = new JFormattedTextField();
    aisleF.setValue(00);
    aisleF.setColumns(2);
    aisleF.addPropertyChangeListener("value", this);

    // Set up register item button
    registerItemButton = new JButton("Register item");
    newGUI.setButtonFontSize(registerItemButton, fontSize);
    registerItemButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        registerItemButtonActionPerformed(evt);
      }
    });

    addComponents(getContentPane());
  }

  private void addComponents(Container pane) {
    GridLayout mainLayout = new GridLayout(10,3);
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
    mainPanel.add(upcLabel);
    mainPanel.add(upcF);
    mainPanel.add(nameLabel);
    mainPanel.add(nameF);
    mainPanel.add(quantityLabel);
    mainPanel.add(quantityF);
    mainPanel.add(priceLabel);
    mainPanel.add(priceF);
    mainPanel.add(sectionLabel);
    mainPanel.add(sectionF);
    mainPanel.add(aisleLabel);
    mainPanel.add(aisleF);
    mainPanel.add(thresholdLabel);
    mainPanel.add(thresholdF);
    mainPanel.add(distributorLabel);
    mainPanel.add(distributorF);
    mainPanel.add(costLabel);
    mainPanel.add(costF);
    mainPanel.add(new JLabel());
    mainPanel.add(registerItemButton);

    pane.add(mainPanel, BorderLayout.NORTH);
    pane.add(new JSeparator(), BorderLayout.CENTER);
  }

  private void registerItemButtonActionPerformed(ActionEvent evt) {
    String name = nameF.getText();
    String distributor = distributorF.getText();

    Product newProduct = new Product(upc, name, quantity, price, section, threshold, distributor,
        cost, aisle);
    storeGUI.store.inventoryManager.addProduct(newProduct);
    storeGUI.store.locationManager.addProduct(section, newProduct, aisle, "");
    setVisible(false);
    storeGUI.run();
  }

  public void actionPerformed(ActionEvent evt) {
    JComboBox comboBox = (JComboBox)evt.getSource();
    section = new Section((String)comboBox.getSelectedItem());
  }

  public void propertyChange(PropertyChangeEvent evt) {
    if(evt.getSource() == upcF) {
      upc = upcF.getText();
    }
    else if(evt.getSource() == quantityF) {
      quantity = Integer.parseInt(quantityF.getText());
    }
    else if(evt.getSource() == priceF) {
      price = new Price(Double.parseDouble(priceF.getText()));
    }
    else if(evt.getSource() == thresholdF) {
      threshold = Integer.parseInt(thresholdF.getText());
    }
    else if(evt.getSource() == costF) {
      cost = Double.parseDouble(costF.getText());
    }
    else if(evt.getSource() == aisleF) {
      aisle = Integer.parseInt(aisleF.getText());
    }
  }

  void run() {
    pack();
    setVisible(true);
  }
}
