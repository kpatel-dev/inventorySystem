import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

class RefundTransactionFrame extends JFrame implements ActionListener {
  private StoreGUI storeGUI;
  private JLabel transactionLabel;
  private JComboBox<String> transactionNumbers;
  private JButton refundButton;
  private Product currentProduct;
  private int currentTransactionNumber;

  RefundTransactionFrame(StoreGUI storeGUI, Product product) {
    super("Refund product");
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

    // Set up transaction label
    transactionLabel = new JLabel("Pick transaction number: ");
    newGUI.setButtonFontSize(transactionLabel, fontSize);

    int totalTransactions = storeGUI.store.dailySalesManager.getTotalTransactions();
    String[] transactionList; //= new String[storeGUI.store.dailySalesManager.getTotalTransactions() - 1];
    transactionList = new String[storeGUI.store.dailySalesManager.getTotalTransactions()];

    for(int i = 1; i <= storeGUI.store.dailySalesManager.getTotalTransactions(); i++) {
      transactionList[i - 1] = (new Integer(i)).toString();
    }

    // Set up quantity Combo Box
    transactionNumbers = new JComboBox<>(transactionList);
    transactionNumbers.setEditable(false);
    newGUI.setButtonFontSize(transactionNumbers, fontSize);
    transactionNumbers.addActionListener(this);

    currentTransactionNumber = Integer.parseInt(transactionNumbers.getItemAt(0));

    // Set up refund button
    refundButton = new JButton("Refund");
    newGUI.setButtonFontSize(refundButton, fontSize);
    refundButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        refundButtonActionPerformed(evt);
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
    mainPanel.add(transactionLabel);
    mainPanel.add(transactionNumbers);
    mainPanel.add(new JLabel());
    mainPanel.add(refundButton);

    pane.add(mainPanel, BorderLayout.NORTH);
    pane.add(new JSeparator(), BorderLayout.CENTER);
  }

  private void refundButtonActionPerformed(ActionEvent evt) {
    storeGUI.store.dailySalesManager.refundTransaction(currentTransactionNumber);
    setVisible(false);
    storeGUI.run();
  }

  public void actionPerformed(ActionEvent evt) {
    JComboBox comboBox = (JComboBox)evt.getSource();
    currentTransactionNumber = Integer.parseInt((String)(comboBox.getSelectedItem()));
  }

  void run(){
    pack();
    setVisible(true);
  }
}
