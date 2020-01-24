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
import java.text.ParseException;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.text.MaskFormatter;

class ChangeSalePriceFrame extends JFrame implements PropertyChangeListener {
  private StoreGUI storeGUI;
  private JLabel priceLabel;
  private JFormattedTextField priceField;
  private JLabel startDateLabel;
  private JFormattedTextField startDateField;
  private JLabel endDateLabel;
  private JFormattedTextField endDateField;
  private JButton addButton;
  private Product currentProduct;
  private LocalDate startDate;
  private LocalDate endDate;

  ChangeSalePriceFrame(StoreGUI storeGUI, Product product) {
    super("Change Regular Price");
    setResizable(false);
    this.storeGUI = storeGUI;
    this.currentProduct = product;
    initComponents();
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
    MaskFormatter dateFormatter = createFormatter("####-##-##");

    // Set up sale price label
    priceLabel = new JLabel("Enter the new price: ");
    newGUI.setButtonFontSize(priceLabel, fontSize);

    // Set up sale price textfield
    priceField = new JFormattedTextField();
    priceField.setValue(0000.00);
    priceField.setColumns(7);
    priceField.addPropertyChangeListener("value", this);

    // Set up sale start date label
    startDateLabel = new JLabel("Enter sale start date (YYYY-MM-DD): ");
    newGUI.setButtonFontSize(startDateLabel, fontSize);

    // Set up sale start date textfield
    startDateField = new JFormattedTextField(dateFormatter);
    startDateField.setValue("0000-00-00");
    startDateField.addPropertyChangeListener("value", this);

    // Set up end date label
    endDateLabel = new JLabel("Enter sale end date (YYYY-MM-DD): ");
    newGUI.setButtonFontSize(endDateLabel, fontSize);

    // Set up sale end date textfield
    endDateField = new JFormattedTextField(dateFormatter);
    endDateField.setValue("0000-00-00");
    endDateField.addPropertyChangeListener("value", this);

    // Set up add button
    addButton = new JButton("Add sale");
    newGUI.setButtonFontSize(addButton, fontSize);
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        addButtonActionPerformed(evt);
      }
    });

    addComponents(getContentPane());
  }

  private void addComponents(Container pane) {
    GridLayout mainLayout = new GridLayout(5,3);
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
    mainPanel.add(priceLabel);
    mainPanel.add(priceField);
    mainPanel.add(startDateLabel);
    mainPanel.add(startDateField);
    mainPanel.add(endDateLabel);
    mainPanel.add(endDateField);
    mainPanel.add(new JLabel());
    mainPanel.add(addButton);

    pane.add(mainPanel, BorderLayout.NORTH);
    pane.add(new JSeparator(), BorderLayout.CENTER);
  }

  private void addButtonActionPerformed(ActionEvent evt) {
    Double salePrice = Double.parseDouble(priceField.getText());
    Price price = new Price(currentProduct.getPrice().getRegularPrice(), salePrice, startDate, endDate);
    storeGUI.store.priceManager.changePrice(currentProduct, price);
    setVisible(false);
    storeGUI.run();
  }

  public void propertyChange(PropertyChangeEvent evt) {
    Object source = evt.getSource();

    if(source == startDateField) {
      startDate = LocalDate.parse(startDateField.getText());
    }
    else if(source == endDateField) {
      endDate = LocalDate.parse(endDateField.getText());
    }
  }

  private MaskFormatter createFormatter(String s) {
    MaskFormatter formatter = null;
    try {
      formatter = new MaskFormatter(s);
    } catch (ParseException exc) {
      System.err.println("Formatter is bad: " + exc.getMessage());
      System.exit(-1);
    }
    return formatter;
  }

  void run(){
    pack();
    setVisible(true);
  }
}
