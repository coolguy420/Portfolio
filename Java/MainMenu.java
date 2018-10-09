/**
 *  MainMenu class for Bigg City Bank system
 */
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*; //ArrayList; HashMap; LinkedList;


public class MainMenu extends JFrame implements ActionListener 
{
  // data collections
  private Map<Integer, Client> clients;
  private Map<String, CurrentAccount> accounts;
  private List<Transaction> pendingTransactions, completedTransactions;
  //private CurrentAccount currAcc;
  
  //GUI
  private TransactionDlg transactionDlg;
  private JButton btnLoadData, btnCredit, btnDebit, btnProcessTransactions, btnListClients, btnListAccounts;
  private JButton btnPendingTransactions, btnCompletedTransactions, btnSave;

  //To launch the application
  public static void main(String[] args) 
  {
    MainMenu app = new MainMenu();
    app.setVisible(true);
  }

  // Constructor
  public MainMenu() 
  {
    // Database
    clients = new HashMap<Integer, Client>();
    accounts = new HashMap<String, CurrentAccount>();
    pendingTransactions = new LinkedList<Transaction>();
    completedTransactions = new LinkedList<Transaction>();

    // GUI - create custom dialog instances
    transactionDlg = new TransactionDlg(this);

    // GUI - set window properties
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(200, 100, 250, 300);

    //GUI - main menu buttons    
    JPanel mainPnl = new JPanel();
    mainPnl.setLayout(new GridLayout(9,1));

    btnLoadData = new JButton("Load Data");
    btnLoadData.addActionListener(this);
    mainPnl.add(btnLoadData);
    
    btnCredit = new JButton("Credit");
    btnCredit.addActionListener(this);
    mainPnl.add(btnCredit);
    
    btnDebit = new JButton("Debit");
    btnDebit.addActionListener(this);
    mainPnl.add(btnDebit);
    
    btnProcessTransactions = new JButton("Process Transactions");
    btnProcessTransactions.addActionListener(this);
    mainPnl.add(btnProcessTransactions);
 
    btnListClients = new JButton("List Clients");
    btnListClients.addActionListener(this);
    mainPnl.add(btnListClients);
    
    btnListAccounts = new JButton("List Accounts");
    btnListAccounts.addActionListener(this);
    mainPnl.add(btnListAccounts);
    
    btnPendingTransactions = new JButton("List Pending Transactions");
    btnPendingTransactions.addActionListener(this);
    mainPnl.add(btnPendingTransactions);
    
    btnCompletedTransactions = new JButton("List Completed Transactions");
    btnCompletedTransactions.addActionListener(this);
    mainPnl.add(btnCompletedTransactions);
    
    btnSave = new JButton("Save");
    btnSave.addActionListener(this);
    mainPnl.add(btnSave);
    
    //TODO: More button setup
   
    add(mainPnl, BorderLayout.CENTER);
  } //end constructor

  //Accessors for data structures
  public Map<Integer, Client>  getClients()         
  { 
      return clients;      
  }   
  
  public Map<String, CurrentAccount> getAccounts()  
  { 
      return accounts;     
  }
  
  public List<Transaction> getPendingTransactions() 
  {
    return pendingTransactions;
  }
  
  public List<Transaction> getCompletedTransactions() 
  {
    return completedTransactions;
  }

  /**
   * Actions in response to button clicks
   */
  public void actionPerformed(ActionEvent evt) 
  {
    Object src = evt.getSource();
    //read customers, items, orders JUST ONCE to initialise the system data.
    if (src == btnLoadData) 
    { 
      loadClientData();
      loadAccountData();
      loadPendingTransactions();
      loadCompletedTransactions();

      btnLoadData.setEnabled(false);      
    }
    else if (src == btnCredit) // dialog will do multiple credit transactions
    {  
          transactionDlg.setCreditMode();
          transactionDlg.setVisible(true);
    }
    else if (src == btnDebit) 
    { // dialog will do multiple debit transactions
          transactionDlg.setDebitMode();
          transactionDlg.setVisible(true);
    }
    else if (src == btnProcessTransactions) 
    { // iterate through orders
      processTransactions();
    }
    else if (src == btnListClients) 
    { 
      listClients();
    }
    else if (src == btnListAccounts) 
    { 
      listAccounts();
    }
    else if(src == btnPendingTransactions)
    {
        listPendingTransactions();
    }
    else if(src == btnCompletedTransactions)
    {
        listCompletedTransactions();
    }
    else if(src == btnSave)
    {
        saveAccounts();
        savePendingTransactions();
        saveCompletedTransactions();
    }
    //TODO: three more cases: list pending transactions,
    //      list completed transactions save data (accounts, pending transactions)
  } // end actionPerformed()
  
  /**
   * Load data from clients.txt using a Scanner; unpack and populate
   *   clients Map.
   */
  public void loadClientData() 
  {
    String fnm="", snm="", pcd="";
    int num=0, id=1;
    try 
    {
      Scanner scnr = new Scanner(new File("clients.txt"));
      scnr.useDelimiter("\\s*#\\s*");
        //fields delimited by '#' with optional leading and trailing spaces
      while (scnr.hasNextInt()) 
      {
        id  = scnr.nextInt();
        snm = scnr.next();
        fnm = scnr.next();
        num = scnr.nextInt();
        pcd = scnr.next();
        clients.put(new Integer(id), new Client(id, snm, fnm, num, pcd));
      }
      scnr.close();
    }
    catch (NoSuchElementException e) 
    {
      System.out.printf("%d %s %s %d %s\n", id, snm, fnm, num, pcd);
      JOptionPane.showMessageDialog(this, e.getMessage(),
          "fetch of next token failed ", JOptionPane.ERROR_MESSAGE);
    } 
    catch (NumberFormatException e) 
    {
        JOptionPane.showMessageDialog(this, e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    } 
    catch (IllegalArgumentException e) 
    {
        JOptionPane.showMessageDialog(this, e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    } 
    catch (IOException e) 
    {
      JOptionPane.showMessageDialog(this, "File Not Found",
          "Unable to open data file", JOptionPane.ERROR_MESSAGE);
    }
    System.out.println("Clients loaded");
  } //end readCustomerData()
  
  // List clients on console
  public void listClients() 
  {
    System.out.println("Clients:");
    for (Client c: clients.values()) 
    {
      System.out.println(c);
    }
    System.out.println();
  } //listCustomers()

  /**
   * Read data from currentAccounts.txt using a Scanner; unpack and populate
   *   accounts Map.
   */
  public void loadAccountData() 
  {
    String id="", srtCd="";
    int onr=0, bal=0, crLm=0;
    try 
    {
      Scanner scnr = new Scanner(new File("currentAccounts.txt"));
      scnr.useDelimiter("\\s*#\\s*");
      while (scnr.hasNext()) 
      {
        id  = scnr.next();
        onr = scnr.nextInt();
        srtCd = scnr.next();
        bal = scnr.nextInt();
        crLm = scnr.nextInt();
        accounts.put(id, new CurrentAccount(id, onr, srtCd, bal, crLm));
      }
      scnr.close();
    } 
    catch (NoSuchElementException e) 
    {
      System.out.printf("%s %d %s, %d, %d\n", id, onr, srtCd, bal, crLm);
      JOptionPane.showMessageDialog(this, e.getMessage(),
          "fetch of next token failed ", JOptionPane.ERROR_MESSAGE);
    } 
    catch (NumberFormatException e) 
    {
        JOptionPane.showMessageDialog(this, e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    catch (IllegalArgumentException e) 
    {
        JOptionPane.showMessageDialog(this, e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    catch (IOException e)
    {
      JOptionPane.showMessageDialog(this, "File Not Found",
          "Unable to open data file", JOptionPane.ERROR_MESSAGE);
    }
    System.out.println("Account data loaded");
  } //end readItemData()

  // List Accounts on console
  public void listAccounts() // this method is used to list the accounts
  {
    System.out.println("Current Accounts:");
    for (CurrentAccount a: accounts.values()) 
    {
      System.out.println(a);
    }
    System.out.println();
  } 
  
  public boolean validateAccountID(String accountID) // a boolean method that takes accountID as a parameter to check againts the key for validation
  {
     String accID;
     boolean matches = false;
     accID = accountID;
      
     for(Map.Entry<String, CurrentAccount> entry: accounts.entrySet())
     {
         if(accID.equals(entry.getKey()))
         {
             return true;
         }
     }
     return false;
  }
  
  /*
   * this method is used to add transactions that have
   * not yet been processed to the pending transactions
   * list.
   */
  public void addToPendingTransactions(Transaction trans)
  {
      pendingTransactions.add(trans);
  }
  
  /*
   * this method is used to add transactions that have
   * been processed to the completed transactions
   * list.
   */
  public void addToCompletedTransactions(Transaction trans)
  {
      completedTransactions.add(trans);
  }
  
  /*
   * this method is used to list all the
   * completed transactions
   * 
   */
  public void listPendingTransactions()
  {
      for(Transaction transactions : pendingTransactions)
      {
          System.out.println("");
          System.out.println(transactions.toString());
      }
  }
  
  /*
   * this method uses the Scanner to scan the Text file
   * that contains all the pending transactions and 
   * loads it into the program. 
   * exceptions are being caught incase of errors.
   */
  public void loadPendingTransactions() {
    String account = "";
    int amount = 0;
    long dateTime = 0;
    try {
        Scanner scnr = new Scanner(new File("PendingTransactions.txt"));
        scnr.useDelimiter("\\s*#\\s*");
        while (scnr.hasNext()) {
            account = scnr.next();
            amount = scnr.nextInt();
            dateTime = scnr.nextLong();
            pendingTransactions.add(new Transaction(account, amount, dateTime));
        }
        scnr.close();
    } catch (NoSuchElementException e) {
        System.out.printf("%s %d %l\n", account, amount, dateTime);
        JOptionPane.showMessageDialog(this, e.getMessage(),
        "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(),
        "Error", JOptionPane.ERROR_MESSAGE);
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(),
        "Error", JOptionPane.ERROR_MESSAGE);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "File Not Found",
        "Unable to open data file", JOptionPane.ERROR_MESSAGE);
    }
    System.out.println("Transactions loaded");
  }
  
   /*
   * this method is used to list all the
   * completed transactions 
   */
  public void listCompletedTransactions()
  {
      for(Transaction transactions : completedTransactions)
      {
          System.out.println("");
          System.out.println(transactions.toString());
      }
  }
  
  /*
   * Makes button load completed transactions
   * 
   * @param account, amount, date & time
   * @return loads completed transactions
   */
  public void loadCompletedTransactions() {
    String account = "";
    int amount = 0;
    long dateTime = 0;
    try {
        Scanner scnr = new Scanner(new File("completedTransactions.txt"));
        scnr.useDelimiter("\\s*#\\s*");
        while (scnr.hasNext()) {
            account = scnr.next();
            amount = scnr.nextInt();
            dateTime = scnr.nextLong();
            completedTransactions.add(new Transaction(account, amount, dateTime));
        }
        scnr.close();
    } catch (NoSuchElementException e) {
        System.out.printf("%s %d %l\n", account, amount, dateTime);
        JOptionPane.showMessageDialog(this, e.getMessage(),
        "fetch of next token failed ", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(),
        "Error", JOptionPane.ERROR_MESSAGE);
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(),
        "Error", JOptionPane.ERROR_MESSAGE);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "File Not Found",
        "Unable to open data file", JOptionPane.ERROR_MESSAGE);
    }
    }
  
  /*
   * this method write to a new file
   * called currentAccounts.txt which is a text file
   * it writes all the attributes assosiated with the
   * account class.
   * @param AccountID, owner, sortcode, balance, overDraftLimit
   */
  public void saveAccounts(){
      File currentAccount = new File ("currentAccounts.txt");
      try{
          PrintWriter output = new PrintWriter (currentAccount);
          for ( CurrentAccount a: accounts.values()){
              output.print(a.getAccountID() + "# " + a.getOwner() + "# " + a.getSortCode() + "# "+ a.getBalance() + "# " + a.getOverdraftLimit() +"#" +"\n");
            }
            output.close();       
        }catch (FileNotFoundException e){
            JOptionPane.showMessageDialog (this, "FILE DOES NOT EXIST ", 
            "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    
       /*
   * Makes button save pending
   * 
   * @param AccountID, Amount, Date & Time
   * @return save pending
   */
  public void savePendingTransactions() {
    File transactions = new File("PendingTransactions.txt");
    try{
      PrintWriter output=new PrintWriter(transactions);
      for (Transaction t: pendingTransactions) {
        output.print(t.getAccountID()+"# "+t.getAmount()+"# "+t.getDateTimeStamp()+"#\n ");
      }
      output.close();
    } catch (FileNotFoundException e){
       JOptionPane.showMessageDialog(this, "File not found.",
       "Error", JOptionPane.ERROR_MESSAGE);
    }
    }
    
    
  /*
   * Makes button save completed transactions
   * 
   * @param AccountID, Amount, Date & Time
   * @return saves complete transactions
   */  
  public void saveCompletedTransactions() {
    File complete = new File("completedTransactions.txt");
    try{
      PrintWriter output=new PrintWriter(complete);
      for (Transaction t: completedTransactions) {
          output.print(t.getAccountID()+"# "+t.getAmount()+"# "+t.getDateTimeStamp()+"#\n");
        }
        output.close();
    } catch (FileNotFoundException e){
        JOptionPane.showMessageDialog(this, "File not found.",
        "Error", JOptionPane.ERROR_MESSAGE);
    }  
    }
  
    /*
   * Makes button process transactions
   * 
   * @param AccountID, Amount
   * @return processes transactions
   */
  public void processTransactions() {
      String id="";
      int amt = 0;
      for (Transaction t: pendingTransactions) {
      id = t.getAccountID();
      amt = t.getAmount();
      Account account = accounts.get(id);
      if(account==null){
          System.out.println("Account with ID: "+id+" doesn't exist.");
        } else {
          if(amt<0){
              amt = -amt;
              account.debit(amt);
          } else if(amt>0){
              account.credit(amt);
          }
          completedTransactions.add(t);
          pendingTransactions.remove(t);
        }
    }
    System.out.println("Transactions processed.");
    }
} 
  

