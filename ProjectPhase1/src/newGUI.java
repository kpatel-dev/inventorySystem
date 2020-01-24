import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

class newGUI extends JFrame{
  Logger logger;
  final static int maxGap = 20;
  private JButton groceryButton;
  private JButton pharmacyButton;
  private JButton shutDownButton;
  private JFrame output;
  private JTextArea outputText;

  newGUI() {
    super("Store Manager");
    initComponents();
    logger = new Logger(this);
  }

  private void initComponents() {

    int fontSize = 30;    // Buttons' font size

    // Setup output JFrame
    output = new JFrame("Output");
    output.setResizable(true);
    output.setMinimumSize(new Dimension(800, 800));
    // Output window should always stay open until program closes fully
    output.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    outputText = new JTextArea();
    setButtonFontSize(outputText, 15);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(1, 0));

    Dimension preferredSize = new Dimension(output.getSize());
    panel.setPreferredSize(preferredSize);

    JScrollPane areaScrollPane = new JScrollPane(outputText);
    areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    areaScrollPane.setPreferredSize(output.getMinimumSize());

    panel.add(areaScrollPane);
    output.getContentPane().add(panel, BorderLayout.NORTH);
    output.add(new JSeparator(), BorderLayout.CENTER);
    output.setVisible(true);

    // Setup this JFrame
    setResizable(false);

    // Executes when window is closed
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        System.exit(0);
      }
    });

    // Set up grocery button
    groceryButton = new JButton("Grocery");
    setButtonFontSize(groceryButton, fontSize);
    groceryButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        groceryButtonActionPerformed(evt);
      }
    });

    // Set up new pharmacy button
    pharmacyButton = new JButton("Pharmacy");
    setButtonFontSize(pharmacyButton, fontSize);
    pharmacyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        pharmacyButtonActionPerformed(evt);
      }
    });

    // Set up shut down button
    shutDownButton = new JButton("Shut Down");
    setButtonFontSize(shutDownButton, fontSize);
    shutDownButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        shutDownButtonActionPerformed(evt);
      }
    });

    addComponentsToPane(getContentPane());
  }

  private void addComponentsToPane(Container pane) {

    GridLayout mainLayout = new GridLayout(0,3);
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(mainLayout);

    // Setting up button sizes
    JButton b = new JButton("Filler button");
    Dimension buttonSize = b.getPreferredSize();
    Dimension preferredSize = new Dimension(
        (int)(buttonSize.getWidth() * 7.5) + maxGap,
        (int)(buttonSize.getHeight() * 10.5) + maxGap * 2);
    mainPanel.setPreferredSize(preferredSize);

    // Add the main panel buttons
    mainPanel.add(groceryButton);
    mainPanel.add(pharmacyButton);
    mainPanel.add(shutDownButton);

    pane.add(mainPanel, BorderLayout.NORTH);
    pane.add(new JSeparator(), BorderLayout.CENTER);
    pack();
  }

  private void groceryButtonActionPerformed(ActionEvent evt) {
    // Execute grocery store process
    loadFrame grocery = new loadFrame(this, "Grocery");
    setVisible(false);
    grocery.run();
  }

  private void pharmacyButtonActionPerformed(ActionEvent evt) {
    // Execute pharmacy process
    loadFrame pharmacy = new loadFrame(this, "Pharmacy");
    setVisible(false);
    pharmacy.run();
  }

  private void shutDownButtonActionPerformed(ActionEvent evt) {
    System.exit(0);
  }

  void print(String s) {
    outputText.append(s + "\n");
  }

  static void setButtonFontSize(JComponent component, int fontSize) {
    component.setFont(new Font("Arial", Font.PLAIN, fontSize));
  }

}
