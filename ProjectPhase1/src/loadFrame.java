import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

public class loadFrame extends JFrame implements ActionListener {
  private final String type;
  private final newGUI newGUIFrame;
  private JButton loadStoreButton, newStoreButton, loadInventoryButton;
  private JTextArea log;
  private JScrollPane logScrollPane;
  private JFileChooser fileChooser;
  private int fontSize = 20;  // Buttons' font size
  private static final String newline = "\n";

  loadFrame(newGUI newGUIFrame, String type) {
    super(type);
    setResizable(false);
    this.newGUIFrame = newGUIFrame;
    this.type = type;

    // Create log
    log = new JTextArea(5,20);
    log.setMargin(new Insets(5,5,5,5));
    log.setEditable(false);
    logScrollPane = new JScrollPane(log);
    newGUI.setButtonFontSize(logScrollPane, fontSize);

    initComponents();
  }

  private void initComponents() {
    setResizable(false);

    // Executes when window is closed
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        setVisible(false);
        newGUIFrame.setVisible(true);
      }
    });

    // Set up file chooser
    fileChooser = new JFileChooser();

    // Set up load existing store button
    loadStoreButton = new JButton("Load Existing " + type);
    newGUI.setButtonFontSize(loadStoreButton, fontSize);
    loadStoreButton.addActionListener(this);

    // Set up new store button
    newStoreButton = new JButton("New " + type);
    newGUI.setButtonFontSize(newStoreButton, fontSize);
    newStoreButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        newStoreButtonActionPerformed(evt);
      }
    });

    // Set up load from inventory button
    loadInventoryButton = new JButton("Load From Inventory");
    newGUI.setButtonFontSize(loadInventoryButton, fontSize);
    loadInventoryButton.addActionListener(this);

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
        (int)(buttonSize.getWidth() * 7.5) + newGUI.maxGap,
        (int)(buttonSize.getHeight() * 10.5) + newGUI.maxGap * 2);
    mainPanel.setPreferredSize(preferredSize);

    // Add the main panel buttons
    mainPanel.add(newStoreButton);
    mainPanel.add(loadStoreButton);
    mainPanel.add(loadInventoryButton);

    pane.add(mainPanel, BorderLayout.NORTH);
    pane.add(new JSeparator(), BorderLayout.CENTER);
    pane.add(logScrollPane, BorderLayout.CENTER);
  }

  private void newStoreButtonActionPerformed(ActionEvent evt) {
    Store store = new Store(type, newGUIFrame);

    Logger.addEvent("LoadFrame", "newStoreButtonActionPerformed",
        "New "+ type + " created.", true);

    setVisible(false);
    store.run();
  }

  // From Oracle tutorial FileChooserDemo.java
  public void actionPerformed(ActionEvent e) {

    // Set appropriate fileChooser file selection
    if (e.getSource() == loadStoreButton) {
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }
    else if (e.getSource() == loadInventoryButton) {
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }

    int returnVal = fileChooser.showOpenDialog(this);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();

      if(file.isFile()) {
        try {
          BufferedReader fileInput = new BufferedReader(new FileReader(file.getName()));
          Store store = new Store(type, newGUIFrame, fileInput);

          log.append("Opening: " + file.getName() + "." + newline);

          setVisible(false);
          store.run();
        }
        // Note that this exception should never occur since filePath should always exist
        catch (FileNotFoundException ex) {
          System.out.println("File not found. Loading empty inventory system.");
          Store store = new Store(type, newGUIFrame);

          Logger.addEvent("StoreSimulator", "main",
              "Initial inventory could not be loaded.", true);

          setVisible(false);
          store.run();
        }
      }
      else if(file.isDirectory()) {
        Store store = new Store(type, newGUIFrame, file.getName());

        log.append("Opening: " + file.getName() + "." + newline);

        setVisible(false);
        store.run();
      }
      else {
        System.out.println("File not found. Loading empty store system.");
        Store store = new Store(type, newGUIFrame);

        Logger.addEvent("StoreSimulator", "main",
            "Store could not be loaded from files.", true);
        log.append(file.getName() + " does not exist.");

        setVisible(false);
        store.run();
      }
    }
    else {
      log.append("Open command cancelled by user." + newline);
    }
    log.setCaretPosition(log.getDocument().getLength());
  }

  void run() {
    pack();
    setVisible(true);
  }
}
