import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.Border;

import HW4.JDBC_CSC;

/**
 * @author michaelpermyashkin
 *
 */
public class MessageGUI extends JFrame implements ActionListener {

  static JFrame frame = new JFrame("Messaging");
  static JPanel mainPanel = new JPanel(); // main panel which is split into right(menu) and left(messages)

  static JPanel menuPanel = new JPanel(); // divides into top and bottom to house menu search(top) and scroll
                                          // display(bottom)
  static JPanel messagePanel = new JPanel(); // divides into top and bottom - top(messages can be read) bottom(write
                                             // message)

  static JPanel messagesDisplayArea = new JPanel(); // top half of message window which will display all messages
  static JPanel writeMessageDisplayArea = new JPanel(); // bottom half where new messages are composed

  static JPanel menuSearchPanel = new JPanel(); // top half of menu panel where user can search by name of ID
  static JPanel menuDisplayPlayersPanel = new JPanel(); // bottom of menu panel which displays a scroll menu of players
  static JPanel selectButtonPanel = new JPanel(); // panel holds select button under player list menu

  static JLabel messageAreaLabel = new JLabel("Messages"); // label where all sent/received messages will be displayed
  static JTextArea messageDisplayBox = new JTextArea(); // area where all sent/received messages are displayed

  static JLabel writeMessageAreaLabel = new JLabel("Write Messages Here"); // labels text area where messages are
                                                                           // composed
  static JTextArea writeMessageBox = new JTextArea(); // text area where new messages are composed by users
  static JButton sendButton = new JButton("SEND"); // button will send messages user wrote in text box

  static JTextField searchName = new JTextField(15); // text field where user can search for player by name
  static JButton searchButton = new JButton("SEARCH"); // button will search DB for player name user entered

  static JLabel menuDisplayPlayersLabel = new JLabel("Select Player"); // label for player list menu
  static JPanel displayPlayerMenuPanel = new JPanel(); // displays list menu of players
  static JButton selectButton = new JButton("SELECT");
  static JButton messageNewPlayerButton = new JButton("Leave Conversation"); // allows user to message new player
                                                                             // without closing app
  static JPanel messageNewPlayerButtonPanel = new JPanel();
  static JPanel menuButtonPanel = new JPanel(); // panel where buttons on menu display will appear

  static JPanel loginPanel = new JPanel();
  static JLabel loginLabel = new JLabel("Enter Your Username: ");
  static JTextField loginName = new JTextField(12); // text field where user can search for player by name
  static JButton loginButton = new JButton("Login");
  
  static String Username = "";
  static String sentMessagePrompt = "";
  static String toPlayer = "";

  static String[] playerList = new String[10];
  JList<String> list = new JList<String>();

  /**
   * Designs the basic skeleton layout of the application. -size -Title -Menu
   * section which allows user to select or search a player to message -Message
   * window which will display all sent/received messages as well as providing an
   * area for the user to compose new messages
   * 
   */
  public static void main(String[] args) throws SQLException {

    MessageGUI gui = new MessageGUI();
    gui.createApplication();
  }

  public void createApplication() throws SQLException {
    JDBC_CSC db = new JDBC_CSC();

    setSize(800, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    setLocationRelativeTo(null);

    /*
     * divides main panel into 2 columns
     * 
     * left - menu to select player right - where messages will be read and sent
     */
    JPanel outerPanel = new JPanel(new BorderLayout());
    Border outerBorder = BorderFactory.createEmptyBorder(15, 15, 15, 15);

    outerPanel.add(mainPanel, BorderLayout.CENTER);
    mainPanel.setBorder(outerBorder);
    mainPanel.setLayout(new GridLayout(0, 2));
    Border border = BorderFactory.createLineBorder(Color.BLACK);

    /*
     * Left Side - Menu Display
     * 
     * top - where user can search for a player by name or ID bottom - where scroll
     * menu will display all players user can message
     */
    menuPanel.setLayout(new GridLayout(3, 0));

    // top of menu display used for login(entering your own username) and searching
    // player by name
    JPanel loginSearchPanel = new JPanel();
    loginSearchPanel.setLayout(new GridLayout(2, 0));

    loginButton.addActionListener(this);
    loginPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    loginPanel.add(loginLabel, c);
    c.gridx++;
    loginPanel.add(loginName, c);
    c.gridx++;
    loginPanel.add(loginButton, c);
    ;

    loginSearchPanel.add(loginPanel);

    Border searchBorder = BorderFactory.createEmptyBorder(20, 0, 0, 0);
    menuSearchPanel.setBorder(searchBorder);
    menuSearchPanel.setLayout(new GridBagLayout());
    c.gridx = 0;
    c.gridy = 0;
    menuSearchPanel.add(new JLabel("Search Player: "), c);
    c.gridx = 1;
    c.gridy = 0;
    menuSearchPanel.add(searchName, c);
    c.gridx = 2;
    searchButton.addActionListener(this);
    menuSearchPanel.add(searchButton, c);

    loginSearchPanel.add(menuSearchPanel);

    menuPanel.add(loginSearchPanel);
    Dimension dim = new Dimension();
    dim = menuSearchPanel.getPreferredSize();
    dim.width = 40;
    menuSearchPanel.setPreferredSize(dim);

    // bottom of menu section which will display a list menu of all players
    menuDisplayPlayersPanel.setLayout(new GridLayout(0, 1));

    displayPlayerMenuPanel.setLayout(new BorderLayout());
    displayPlayerMenuPanel.setBorder(border);
    menuDisplayPlayersLabel.setHorizontalAlignment(SwingConstants.CENTER);
    menuDisplayPlayersLabel.setForeground(Color.BLACK);
    menuDisplayPlayersLabel.setBorder(border);
    displayPlayerMenuPanel.add(menuDisplayPlayersLabel, BorderLayout.NORTH);
    Border menuListBorder = BorderFactory.createEmptyBorder(0, 100, 0, 100);
    displayPlayerMenuPanel.setBorder(menuListBorder);

    /**
     * get player names from table PLAYER to display in menu
     */
    db.getPlayerNames();
    playerList = db.nameList;
    list.setListData(playerList);

    list.setBorder(border);
    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setViewportView(list);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    displayPlayerMenuPanel.add(scrollPane, BorderLayout.CENTER);

    Border menuDisplayBorder = BorderFactory.createEmptyBorder(10, 15, 10, 10);
    menuDisplayPlayersPanel.setBorder(menuDisplayBorder);
    menuDisplayPlayersPanel.add(displayPlayerMenuPanel);

    menuPanel.add(menuDisplayPlayersPanel);

    menuButtonPanel.setLayout(new GridLayout(2, 0));

    Border selectButtonBorder = BorderFactory.createEmptyBorder(10, 30, 30, 30);
    selectButtonPanel.setBorder(selectButtonBorder);
    selectButtonPanel.add(selectButton);
    selectButton.addActionListener(this);
    menuButtonPanel.add(selectButtonPanel);

    Border newPlayerButtonBorder = BorderFactory.createEmptyBorder(10, 40, 30, 30);
    messageNewPlayerButtonPanel.setBorder(newPlayerButtonBorder);
    messageNewPlayerButtonPanel.add(messageNewPlayerButton);
    messageNewPlayerButton.addActionListener(this);
    menuButtonPanel.add(messageNewPlayerButtonPanel);

    menuPanel.add(menuButtonPanel);

    /*
     * Right Side - Messaging Window divides panel into top and bottom
     * 
     * top = messages will be displayed bottom = where new messages will be composed
     * by user
     */
    messagePanel.setLayout(new GridLayout(2, 0));

    // top half of messagePanel to display messages
    messagesDisplayArea.setLayout(new BorderLayout());
    messageAreaLabel.setHorizontalAlignment(SwingConstants.CENTER);
    messageAreaLabel.setForeground(Color.BLACK);
    messageAreaLabel.setBorder(border);
    messagesDisplayArea.add(messageAreaLabel, BorderLayout.NORTH);

    messageDisplayBox.setBorder(border);
    messagesDisplayArea.add(messageDisplayBox, BorderLayout.CENTER);

    // bottom of messaging window where user can compose new messages
    Border writeMessageBorder = BorderFactory.createEmptyBorder(10, 0, 0, 0);
    writeMessageDisplayArea.setLayout(new BorderLayout());
    writeMessageDisplayArea.setBorder(writeMessageBorder);
    writeMessageAreaLabel.setBorder(border);
    writeMessageAreaLabel.setHorizontalAlignment(SwingConstants.CENTER);
    writeMessageAreaLabel.setForeground(Color.BLACK);
    writeMessageDisplayArea.add(writeMessageAreaLabel, BorderLayout.NORTH);
    writeMessageBox.setBorder(border);
    writeMessageDisplayArea.add(writeMessageBox, BorderLayout.CENTER);

    sendButton.addActionListener(this);
    writeMessageDisplayArea.add(sendButton, BorderLayout.SOUTH);

    messagePanel.add(messagesDisplayArea);
    messagePanel.add(writeMessageDisplayArea);

    /**
     * Once both menu and messaging panels are complete both are added to the main
     * panel
     */
    mainPanel.add(menuPanel);
    mainPanel.add(messagePanel);
    add(mainPanel);
    setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    JDBC_CSC db = null;
    try {
      db = new JDBC_CSC();
    } catch (SQLException e2) {
      // TODO Auto-generated catch block
      e2.printStackTrace();
    } 
    
    /**
     *      LOGIN BUTTON
     */
    if (e.getSource() == loginButton) {
      Username = loginName.getText();
    }
    
    /**
     *      SEND BUTTON
     * 
     * everytime message is sent, 
     * update MESSAGES table
     */
    if (e.getSource() == sendButton) {
      messageDisplayBox.setForeground(Color.RED);
      messageDisplayBox.append(writeMessageBox.getText());
      writeMessageBox.setText("");
      
      //when messages are sent Update Messages tables
      String messages = messageDisplayBox.getText();
      try {
        db.appendMessagesToTable(Username, toPlayer, messages);
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
    }

    /**
     *      SEARCH BUTTON
     */
    if (e.getSource() == searchButton) {
      toPlayer = searchName.getText();
    }

    /**
     *      SELECT BUTTON
     */
    if (e.getSource() == selectButton) {
      toPlayer = list.getSelectedValue();
    }

    /**
     *      LEAVE CONVERSATION BUTTON
     */
    if (e.getSource() == messageNewPlayerButton) {
      writeMessageBox.setText("");
      messageDisplayBox.setText("");
      Username = "";
      toPlayer = "";
    }
  }

}
