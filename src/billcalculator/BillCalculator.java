package billcalculator;
//Name Zafar Iqbal
//Student ID 1671637
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BillCalculator extends JFrame {

    // JLabel for Restaurant  
    private JLabel restaurantJLabel;
    private JPanel waiterJPanel;
// JLabel and JTextField for table number  
    private JLabel tableNumberJLabel;
    private JTextField tableNumberJTextField;
    // JLabel and JTextField for waiter name  
    private JLabel waiterNameJLabel;
    private JTextField waiterNameJTextField;
    // JPanel to display menu items  
    private JPanel menuItemsJPanel;
    // JLabel and JComboBox for beverage  
    private JLabel beverageJLabel;
    private JComboBox beverageJComboBox;
// JLabel and JComboBox for appetizer  
    private JLabel appetizerJLabel;
    private JComboBox appetizerJComboBox;
    // JLabel and JComboBox for main course  
    private JLabel mainCourseJLabel;
    private JComboBox mainCourseJComboBox;
    // JLabel and JComboBox for dessert  
    private JLabel dessertJLabel;
    private JComboBox dessertJComboBox;
    // JButton for calculate bill  
    private JButton calculateBillJButton;
    // JLabel and JTextField for subtotal  
    private JLabel subtotalJLabel;
    private JTextField subtotalJTextField;
// JLabel and JTextField for tax  
    private JLabel taxJLabel;
    private JTextField taxJTextField;
    // JLabel and JTextField for total  
    private JLabel totalJLabel;
    private JTextField totalJTextField;
    //Declare double variables for each Catgories prices.
    private static double BeveragePrice = 0, AppetizerPrice = 0, MainCoursePrice = 0, DessertPrice = 0;
    //Declare Double variable as subtotal for each item price. 
    private static double subtotal = 0;
    // Declare double variable for Tax value.
    private static double TAX_RATE = 0.05;
    private static double BeverageTotal = 0, AppetizerTotal = 0, MainCourseTotal = 0, DessertTotal = 0;

    // declare instance variables for database processing  
    private Connection myConnection;
    private Statement myStatement;
    private ResultSet myResultSet;
    private String URL = "jdbc:mysql://localhost/restaurant?useSSL=false";
    // declare instance variable ArrayList to hold bill items  
    private static ArrayList billItems = new ArrayList();
    private double count = 0;

    public BillCalculator(String databaseUserName, String databasePassword) throws SQLException, ClassNotFoundException {
        try {
            myConnection = DriverManager.getConnection(URL, databaseUserName, databasePassword);
            myStatement = (Statement) myConnection.createStatement();
            createUserInterface();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.exit(1);
        } // end catch
        finally {
            // end catch block
        } // end of finally
    } // end constructor

    private void createUserInterface() {
        Container contentPane = getContentPane();
        // enable explicit positioning of GUI components   
        contentPane.setLayout(null);
        // set up restaurantJLabel  
        restaurantJLabel = new JLabel();
        restaurantJLabel.setBounds(80, 8, 128, 24);
        restaurantJLabel.setText("Restaurant");
        restaurantJLabel.setFont(
                new Font("SansSerif", Font.BOLD, 16));
        contentPane.add(restaurantJLabel);
        // set up waiterJPanel  
        createWaiterJPanel();
        contentPane.add(waiterJPanel);
        createMenuItemsJPanel();
        contentPane.add(menuItemsJPanel);
        // set up calculateBillJButton  
        calculateBillJButton = new JButton();
        calculateBillJButton.setBounds(92, 320, 90, 24);
        calculateBillJButton.setText("Calculate Bill");
        calculateBillJButton.setBorder(
                BorderFactory.createRaisedBevelBorder());
        contentPane.add(calculateBillJButton);
        calculateBillJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    calculateBillJButtonActionPerformed(event);
                } catch (SQLException ex) {
                    Logger.getLogger(BillCalculator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        // set up subtotalJLabel  
        subtotalJLabel = new JLabel();
        subtotalJLabel.setBounds(28, 360, 56, 16);
        subtotalJLabel.setText("Subtotal:");
        contentPane.add(subtotalJLabel);
        // set up subtotalJTextField  
        subtotalJTextField = new JTextField();
        subtotalJTextField.setBounds(92, 360, 90, 20);
        subtotalJTextField.setEditable(true);
        subtotalJTextField.setBorder(
                BorderFactory.createLoweredBevelBorder());
        subtotalJTextField.setHorizontalAlignment(JTextField.RIGHT);
        contentPane.add(subtotalJTextField);
        // set up taxJLabel  
        taxJLabel = new JLabel();

        taxJLabel.setBounds(28, 392, 56, 16);
        taxJLabel.setText("Tax:");
        contentPane.add(taxJLabel);
        // set up taxJTextField  
        taxJTextField = new JTextField();
        taxJTextField.setBounds(92, 392, 90, 20);
        taxJTextField.setEditable(false);
        taxJTextField.setBorder(
                BorderFactory.createLoweredBevelBorder());
        taxJTextField.setHorizontalAlignment(JTextField.RIGHT);
        contentPane.add(taxJTextField);
        // set up totalJLabel  
        totalJLabel = new JLabel();
        totalJLabel.setBounds(28, 424, 56, 16);
        totalJLabel.setText("Total:");
        contentPane.add(totalJLabel);
        // set up totalJTextField  
        totalJTextField = new JTextField();
        totalJTextField.setBounds(92, 424, 90, 20);
        totalJTextField.setEditable(false);
        totalJTextField.setBorder(
                BorderFactory.createLoweredBevelBorder());
        totalJTextField.setHorizontalAlignment(JTextField.RIGHT);
        contentPane.add(totalJTextField);
        // set properties of application's window  
        setTitle("Restaurant Bill Calculator"); // set window title  
        setSize(280, 500); // set window size  
        setVisible(true);  // display window  
        // ensure database connection is closed   
        // set properties of application's window  
        setTitle("Restaurant Bill Calculator"); // set window title  
        setSize(280, 500); // set window size  
        setVisible(true);  // display window  
        addWindowListener(
                new WindowAdapter() // anonymous inner class  
                {
                    // event handler called when close button is clicked  
                    @Override
                    public void windowClosing(WindowEvent event) {
                        frameWindowClosing(event);
                    }
                } // end anonymous inner class  
        ); // end addWindowListener  
    }

    private void createWaiterJPanel() {
        // set up waiterJPanel  
        waiterJPanel = new JPanel();
        waiterJPanel.setBounds(20, 48, 232, 88);
        waiterJPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Waiter Information"));
        waiterJPanel.setLayout(null);
        // set up tableNumberJLabel  
        tableNumberJLabel = new JLabel();
        tableNumberJLabel.setBounds(35, 24, 90, 16);
        tableNumberJLabel.setText("Table number:");
        waiterJPanel.add(tableNumberJLabel);
        // set up tableNumberJTextField  
        tableNumberJTextField = new JTextField();
        tableNumberJTextField.setBounds(128, 24, 88, 21);
        waiterJPanel.add(tableNumberJTextField);
// set up waiterNameJLabel  
        waiterNameJLabel = new JLabel();
        waiterNameJLabel.setBounds(35, 56, 90, 16);
        waiterNameJLabel.setText("Waiter name:");
        waiterJPanel.add(waiterNameJLabel);
        // set up waiterNameJTextField  
        waiterNameJTextField = new JTextField();
        waiterNameJTextField.setBounds(128, 56, 88, 21);
        waiterJPanel.add(waiterNameJTextField);
    } // end method createWaiterJPanel  
// create menuItemsJPanel  

    private void createMenuItemsJPanel() {
        // set up menuItemsJPanel  
        menuItemsJPanel = new JPanel();
        menuItemsJPanel.setBounds(20, 152, 232, 152);
        menuItemsJPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Menu Items"));
        menuItemsJPanel.setLayout(null);
// set up beverageJLabel  
        beverageJLabel = new JLabel();
        beverageJLabel.setBounds(8, 24, 80, 24);
        beverageJLabel.setText("Beverage:");
        menuItemsJPanel.add(beverageJLabel);
// set up beverageJComboBox  
        beverageJComboBox = new JComboBox();
        beverageJComboBox.setBounds(88, 24, 128, 25);
        menuItemsJPanel.add(beverageJComboBox);
        beverageJComboBox.addItemListener(
                new ItemListener() // anonymous inner class  
                {
                    // event handler called when item in beverageJComboBox  
                    // is selected  
                    @Override
                    public void itemStateChanged(ItemEvent event) {
                        try {
                            beverageJComboBoxItemStateChanged(event);

                        } catch (SQLException ex) {
                            Logger.getLogger(BillCalculator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } // end anonymous inner class  
        ); // end addItemListener  
// add items to beverageJComboBox  
        beverageJComboBox.addItem("");
        beverageJComboBox.addItem("Minerals");
        beverageJComboBox.addItem("Tea");
        beverageJComboBox.addItem("Coffee");
        beverageJComboBox.addItem("Mineral Water");
        beverageJComboBox.addItem("Fruit Juice");
        beverageJComboBox.addItem("Milk");
        loadCategory("Beverage", beverageJComboBox);
// set up appetizerJLabel  
        appetizerJLabel = new JLabel();
        appetizerJLabel.setBounds(8, 56, 80, 24);
        appetizerJLabel.setText("Appetizer:");
        menuItemsJPanel.add(appetizerJLabel);
        // set up appetizerJComboBox  
        appetizerJComboBox = new JComboBox();
        appetizerJComboBox.setBounds(88, 56, 128, 25);
        menuItemsJPanel.add(appetizerJComboBox);
        appetizerJComboBox.addItemListener(
                new ItemListener() // anonymous inner class  
                {
                    // event handler called when item in appetizerJComboBox  
                    // is selected  
                    @Override
                    public void itemStateChanged(ItemEvent event) {
                        appetizerJComboBoxItemStateChanged(event);
                    }
                } // end anonymous inner class  
        ); // end addItemListener  
// add items to appetizerJComboBox  
        appetizerJComboBox.addItem("");
        appetizerJComboBox.addItem("Chicken Wings");
        appetizerJComboBox.addItem("Pate and Toast");
        appetizerJComboBox.addItem("Potato Skins");
        appetizerJComboBox.addItem("Nachos");
        appetizerJComboBox.addItem("Garlic Mushrooms");
        appetizerJComboBox.addItem("Seafood Cocktail");
        appetizerJComboBox.addItem("Brie Cheese");
        loadCategory("Appetizer", appetizerJComboBox);
        mainCourseJLabel = new JLabel();
        mainCourseJLabel.setText("Main Course:");
        mainCourseJLabel.setBounds(8, 88, 80, 24);
        menuItemsJPanel.add(mainCourseJLabel);
// set up mainCourseJComboBox  
        mainCourseJComboBox = new JComboBox();
        mainCourseJComboBox.setBounds(88, 88, 128, 25);

        mainCourseJComboBox.addItemListener(
                new ItemListener() // anonymous inner class  
                {
// is selected  
                    @Override
                    public void itemStateChanged(ItemEvent event) {
                        mainCourseJComboBoxItemStateChanged(event);
                    }
                } // end anonymous inner class  
        ); // end addItemListener  
        menuItemsJPanel.add(mainCourseJComboBox);
// add items to mainCourseJComboBox  
        mainCourseJComboBox.addItem("");
        mainCourseJComboBox.addItem("Seafood Alfredo");
        mainCourseJComboBox.addItem("Chicken Alfredo");
        mainCourseJComboBox.addItem("Lasagne");
        mainCourseJComboBox.addItem("Turkey Club");
        mainCourseJComboBox.addItem("Lobster Pie");
        mainCourseJComboBox.addItem("Rib Steak");
        mainCourseJComboBox.addItem("Scampi");
        mainCourseJComboBox.addItem("Turkey & Ham");
        mainCourseJComboBox.addItem("Chicken Kiev");
        loadCategory("Main Course", mainCourseJComboBox);
        // set up dessertJLabel  
        dessertJLabel = new JLabel();
        dessertJLabel.setBounds(8, 120, 80, 24);
        dessertJLabel.setText("Dessert:");
        menuItemsJPanel.add(dessertJLabel);
// set up dessertJComboBox  
        dessertJComboBox = new JComboBox();
        dessertJComboBox.setBounds(88, 120, 128, 25);
        menuItemsJPanel.add(dessertJComboBox);
        dessertJComboBox.addItemListener(
                new ItemListener() // anonymous inner class  
                {
                    // event handler called when item in dessertJComboBox  
                    // is selected  
                    @Override
                    public void itemStateChanged(ItemEvent event) {
                        try {
                            dessertJComboBoxItemStateChanged(event);
                        } catch (SQLException ex) {
                            Logger.getLogger(BillCalculator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } // end anonymous inner class  
        ); // end addItemListener  
        // add items to dessertJComboBox  
        dessertJComboBox.addItem("");
        dessertJComboBox.addItem("Apple Pie");
        dessertJComboBox.addItem("Sundae");
        dessertJComboBox.addItem("Carrot Cake");
        dessertJComboBox.addItem("Mud Pie");
        dessertJComboBox.addItem("Pavlova");
        loadCategory("Dessert", dessertJComboBox);
    } // end method createMenuItemsJPanel  
// add items to JComboBox  

    private void loadCategory(
            String category, JComboBox categoryJComboBox) {
    } // end method loadCategory  
    // user select beverage  

    private void beverageJComboBoxItemStateChanged(ItemEvent event) throws SQLException {
        // add items to beverageJComboBox
        beverageJComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (event.getSource() == beverageJComboBox) {
                    //Beverage selection if any of the item is selected by user. 
                    if (beverageJComboBox.getSelectedItem().equals("Minerals") || beverageJComboBox.getSelectedItem().equals("Tea")
                            || beverageJComboBox.getSelectedItem().equals("Coffee") || beverageJComboBox.getSelectedItem().equals("Mineral Water")
                            || beverageJComboBox.getSelectedItem().equals("Fruit Juice") || beverageJComboBox.getSelectedItem().equals("Milk")) {
                        String BeverageQuery = "SELECT * from menu  where name ='" + beverageJComboBox.getSelectedItem() + "'";
                        try {
                            myResultSet = myStatement.executeQuery(BeverageQuery);
                            if (myResultSet.next()) {
                                BeveragePrice = myResultSet.getDouble("price");
                                //Show price in Subtotal Field by calling calculateSubtotal method.
                                subtotalJTextField.setText(String.valueOf(calculateSubtotal(subtotal)));
                                //Show Tax in Tax Field which is going to be added.
                                //To Show price Including Tax in Total TextField.
                                taxJTextField.setText(String.valueOf(TAX_RATE));
                                //Assign AppetizerTotal to a price and tax rate.
                                BeverageTotal = calculateSubtotal(subtotal) + TAX_RATE;
                                // Add price inculding tax to billItems ArrayList.
                                billItems.add(BeverageTotal);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(BillCalculator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            }
        });
    } // end method beverageJComboBoxItemStateChanged  

    // user select appetizer  
    private void appetizerJComboBoxItemStateChanged(ItemEvent event) {
        appetizerJComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Change of event.
                if (event.getSource() == appetizerJComboBox) {
                    //If any item name is selected from combobox.
                    if (appetizerJComboBox.getSelectedItem().equals("Chicken Wings") || appetizerJComboBox.getSelectedItem().equals("Pate and Toast")
                            || appetizerJComboBox.getSelectedItem().equals("Potato Skins") || appetizerJComboBox.getSelectedItem().equals("Nachos")
                            || appetizerJComboBox.getSelectedItem().equals("Garlic Mushrooms") || appetizerJComboBox.getSelectedItem().equals("Seafood Cocktail")
                            || appetizerJComboBox.getSelectedItem().equals("Brie Cheese")) {
                        //After user selection execute query.
                        String AppetizerQuery = "SELECT * from menu  where name ='" + appetizerJComboBox.getSelectedItem() + "'";
                        try {
                            myResultSet = myStatement.executeQuery(AppetizerQuery);
                            // Get the price value from the database.
                            if (myResultSet.next()) {
                                AppetizerPrice = myResultSet.getDouble("price");
                                //Show price in Subtotal Field by calling calculateSubtotal method.
                                subtotalJTextField.setText(String.valueOf(calculateSubtotal(subtotal)));
                                //Show Tax in Tax Field which is going to be added.
                                //To Show price Including Tax in Total TextField.
                                taxJTextField.setText(String.valueOf(TAX_RATE));
                                //Assign AppetizerTotal to a price and tax rate.
                                AppetizerTotal = calculateSubtotal(subtotal) + TAX_RATE;
                                // Add price inculding tax to billItems ArrayList.
                                billItems.add(AppetizerTotal);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(BillCalculator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
    } // end method appetizerJComboBoxItemStateChanged  

    // user select main course  
    private void mainCourseJComboBoxItemStateChanged(
            ItemEvent event) {
        mainCourseJComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == mainCourseJComboBox) {
                    //Main Course selection if any of the item is selected by user. 
                    if (mainCourseJComboBox.getSelectedItem().equals("Seafood Alfredo")
                            || mainCourseJComboBox.getSelectedItem().equals("Chicken Alfredo")
                            || mainCourseJComboBox.getSelectedItem().equals("Lasagne")
                            || mainCourseJComboBox.getSelectedItem().equals("Turkey Club")
                            || mainCourseJComboBox.getSelectedItem().equals("Lobster Pie")
                            || mainCourseJComboBox.getSelectedItem().equals("Rib Steak")
                            || mainCourseJComboBox.getSelectedItem().equals("Scampi")
                            || mainCourseJComboBox.getSelectedItem().equals("Turkey & Ham")
                            || mainCourseJComboBox.getSelectedItem().equals("Chicken Kiev")) {
                        //Prepare sql query if any item is selected also to retrive the price of selected item.
                        String MainCourseQuery = "SELECT * from menu  where name ='" + mainCourseJComboBox.getSelectedItem() + "'";
                        try {
                            myResultSet = myStatement.executeQuery(MainCourseQuery);
                            if (myResultSet.next()) {
                                MainCoursePrice = myResultSet.getDouble("price");
                                //Show price in Subtotal Field by calling calculateSubtotal method.
                                subtotalJTextField.setText(String.valueOf(calculateSubtotal(subtotal)));
                                //Show Tax in Tax Field which is going to be added.
                                taxJTextField.setText(String.valueOf(TAX_RATE));
                                //After adding Tax to SubToatal.
                                MainCourseTotal = calculateSubtotal(subtotal) + TAX_RATE;
                                //To Show price Including Tax in Total TextField.
                                // Add price inculding tax to billItems.
                                billItems.add(MainCourseTotal);

                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(BillCalculator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
    } // end method mainCourseJComboBoxItemStateChanged  

    // user select dessert  
    private void dessertJComboBoxItemStateChanged(ItemEvent event) throws SQLException {
        dessertJComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (event.getSource() == dessertJComboBox) {
                    //Dessert selection if any item is selected by user. 
                    if (dessertJComboBox.getSelectedItem().equals("Apple Pie")
                            || dessertJComboBox.getSelectedItem().equals("Sundae")
                            || dessertJComboBox.getSelectedItem().equals("Carrot Cake")
                            || dessertJComboBox.getSelectedItem().equals("Mud Pie")
                            || dessertJComboBox.getSelectedItem().equals("Pavlova")) {
                        //Prepare sql query if any item is selected also to retrive the price of selected item.
                        String DessertQuery = "SELECT * from menu  where name ='" + dessertJComboBox.getSelectedItem() + "'";
                        try {
                            myResultSet = myStatement.executeQuery(DessertQuery);
                            if (myResultSet.next()) {
                                DessertPrice = myResultSet.getDouble("price");
                                //Show price in Subtotal Field by calling calculateSubtotal method.
                                subtotalJTextField.setText(String.valueOf(calculateSubtotal(subtotal)));
                                //Show Tax in Tax Field which is going to be added.
                                taxJTextField.setText(String.valueOf(TAX_RATE));
                                //To Show price Including Tax in Total TextField.
                                // Add Tax to subtotal and add to billItems.
                                DessertTotal = calculateSubtotal(subtotal) + TAX_RATE;
                                billItems.add(DessertTotal);

                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(BillCalculator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
    } // end method mainCourseJComboBoxItemStateChanged  

    // end method dessertJComboBoxItemStateChanged  
    // user click Calculate Bill JButton  
    private void calculateBillJButtonActionPerformed(
            ActionEvent event) throws SQLException {
        //Show price Including Tax in Total TextField.
        //Display the sum of all when Bill Calculator button is pressed.
        //For loop to sum all items price and tax.
        int counts;
        for (counts = 0; counts < billItems.size(); counts++) {
            //Display the sum values in the total text field.
            totalJTextField.setText(String.valueOf(billItems.get(counts)));

        }
        //Insert query to save the total values into the database by getting the values from all text fields. 
        String sql = "insert into restauranttables(tableNumber,subTotal,waitername)values('" + tableNumberJTextField.getText() + "','" + totalJTextField.getText() + "','" + waiterNameJTextField.getText() + "')";

        myStatement.executeUpdate(sql);

    } // end method calculateBillJButtonActionPerformed  
// calculate subtotal  

    private static double calculateSubtotal(double subtotal) {
// Assign total to all Catgorey's total prices and tax. 
        subtotal = BeveragePrice + AppetizerPrice + MainCoursePrice + DessertPrice;
        return subtotal;
    } // end method calculateSubtotal  

    private void frameWindowClosing(WindowEvent event) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }  // end method frameWindowClosing  
// calculate subtotal 

    public static void main(String args[]) throws ClassNotFoundException, SQLException {

        String databaseUserName = "root";
        String databasePassword = "123456";
        BillCalculator application = new BillCalculator(databaseUserName, databasePassword
        );
//        if (args.length == 2) {
//         
//         String databaseUserName = args[0];
//           String databasePassword = args[1];
//          
//
//            // **** TODO ****** create new RestaurantBillCalculator
//            BillCalculator application
//                    = new BillCalculator(
//                            databaseUserName, databasePassword);
//            } else {
//            System.out.println("Usage: java "
//                    + "RestaurantBillCalculator databaseUserName databasePassword");
//   } // end method main
    }
} // end class RestaurantBillCalculator
