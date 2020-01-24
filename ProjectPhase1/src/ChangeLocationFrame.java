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
import javax.swing.JSeparator;

class ChangeLocationFrame extends JFrame implements PropertyChangeListener {
  private StoreGUI storeGUI;
  private Product currentProduct;
  private JLabel aisleLabel;
  private JFormattedTextField aisleF;
  private JButton changeLocationButton;
  private int desiredAisle;

  ChangeLocationFrame(StoreGUI storeGUI, Product currentProduct) {
    super("Change Location");
    setResizable(false);
    this.storeGUI = storeGUI;
    this.currentProduct = currentProduct;
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

    // Set up aisle label
    aisleLabel = new JLabel("Aisle: ");
    newGUI.setButtonFontSize(aisleLabel, fontSize);

    // Set up aisle textfield
    aisleF = new JFormattedTextField();
    newGUI.setButtonFontSize(aisleF, fontSize);
    aisleF.setValue("00");
    aisleF.setColumns(2);
    aisleF.addPropertyChangeListener("value", this);

    // Set up change location button
    changeLocationButton = new JButton("Change Location");
    newGUI.setButtonFontSize(changeLocationButton, fontSize);
    changeLocationButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        changeLocationButtonActionPerformed(evt);
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
    mainPanel.add(aisleLabel);
    mainPanel.add(aisleF);
    mainPanel.add(new JLabel());
    mainPanel.add(changeLocationButton);

    pane.add(mainPanel, BorderLayout.NORTH);
    pane.add(new JSeparator(), BorderLayout.CENTER);
  }

  private void changeLocationButtonActionPerformed(ActionEvent evt) {
    storeGUI.store.locationManager.changeLocation(currentProduct, desiredAisle);
    setVisible(false);
    storeGUI.run();
  }

  public void propertyChange(PropertyChangeEvent evt) {
    desiredAisle = Integer.parseInt(aisleF.getText());
  }

  void run() {
    pack();
    setVisible(true);
  }
}
