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
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

class ChangeRegularPriceFrame extends JFrame implements PropertyChangeListener {
  private StoreGUI storeGUI;
  private JLabel priceLabel;
  private JFormattedTextField priceField;
  private JButton changeButton;
  private Product currentProduct;
  private Double newPrice;

  ChangeRegularPriceFrame(StoreGUI storeGUI, Product product) {
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

    // Set up price label
    priceLabel = new JLabel("Enter the new price: ");
    newGUI.setButtonFontSize(priceLabel, fontSize);

    // Set up new price textfield
    priceField = new JFormattedTextField();
    priceField.setValue(0000.00);
    priceField.setColumns(7);
    priceField.addPropertyChangeListener("value", this);

    // Set up change button
    changeButton = new JButton("Change");
    newGUI.setButtonFontSize(changeButton, fontSize);
    changeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        changeButtonActionPerformed(evt);
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
    mainPanel.add(priceLabel);
    mainPanel.add(priceField);
    mainPanel.add(new JLabel());
    mainPanel.add(changeButton);

    pane.add(mainPanel, BorderLayout.NORTH);
    pane.add(new JSeparator(), BorderLayout.CENTER);
  }

  private void changeButtonActionPerformed(ActionEvent evt) {
    storeGUI.store.priceManager.changePrice(currentProduct, new Price(newPrice));
    setVisible(false);
    storeGUI.setVisible(true);
  }

  public void propertyChange(PropertyChangeEvent evt) {
    newPrice = Double.parseDouble(priceField.getText());
  }

  void run(){
    pack();
    setVisible(true);
  }
}
