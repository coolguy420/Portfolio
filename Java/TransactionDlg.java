/**
 * OrderDlg
 * Custom dialog class with methods to input details of an order,
 *  and to create an order record.
 *  NAME: MATIN SALALI 
 *  STUDENT ID: 15033021
 *  
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*; //Date, collection classes

public class TransactionDlg extends JDialog implements ActionListener 
{
  private MainMenu parent;

  //GUI
  private String creditLegend = "Create Credits", debitLegend = "Create Debits";
  private JTextField txtAccountID;
  private JTextField txtAmount;
  private JButton btnSubmit, btnHide;

  // Constructor
  public TransactionDlg(MainMenu p) 
  {
    setTitle(creditLegend);
    parent = p; //data structures are here

    //Components -
    txtAccountID = new JTextField(10); //input field, 10 columns wide
    txtAmount = new JTextField(6); 
    btnSubmit = new JButton("Submit");
    btnHide   = new JButton("Hide");

    //Layout -
    JPanel pnl = new JPanel(), cpnl = new JPanel();
    pnl.add(new JLabel("Account ID:"));
    pnl.add(txtAccountID);
    cpnl.add(pnl);
    pnl = new JPanel();
    pnl.add(new JLabel("Amount:"));
    pnl.add(txtAmount);
    cpnl.add(pnl);
    add(cpnl, BorderLayout.CENTER);

    pnl = new JPanel();
    pnl.add(btnSubmit);
    pnl.add(btnHide);
    add(pnl, BorderLayout.SOUTH);

    setBounds(100, 100, 300, 200);

    //Action
    btnSubmit.addActionListener(this);
    btnHide.addActionListener(this);
  } //end constructor

  /**
   * Credit/debit logic methods
   */ 
  public void setCreditMode() 
  { 
      setTitle(creditLegend); 
  }
    
  public void setDebitMode()  
  { 
      setTitle(debitLegend); 
  }

  public boolean isInCreditMode() 
  { 
      return getTitle().equals(creditLegend); 
  }
    
  public boolean isInDebitMode()  
  { 
      return getTitle().equals(debitLegend); 
  }

  /**
   * Actions: on click of 'Submit', make a transaction record and add to database;
   *          on click of 'Hide', hide the dialogue window.
   */
  public void actionPerformed(ActionEvent evt) 
  {
    Object src = evt.getSource();
    if (src == btnHide) 
    {
      setVisible(false);
      txtAccountID.setText("");
      txtAmount.setText("");
    }
    else if (src == btnSubmit) 
    {
      if (createRcdOk()) 
      {
        txtAccountID.setText("");
        txtAmount.setText("");
      }
    }
  } //end actionPerformed

  /**
   * createRcdOk() - Does the actual work of getting data from the two
   *   TextFields, validating it, creating a transaction record and adding
   *   it to the parent's pendingTransactions list.
   *
   * Returns true if data valid and record added, 
   *        false if problems with data (account ID or amount).
   *
   * Validation: 
   *   Account ID must be an ID that actually occurs in the parent's 
   *      collection of accounts.
   *   Amount string should be numeric and positive.
   *
   * If amount string contains a decimal point, read it as Float, multiply it
   *   by 100, and cast to int; if there is no decimal point, parse it
   *   directly as int. 
   *
   * Creating the Transaction:
   *   If it is a debit Transaction (query isInDebitMode()) make the
   *     amount negative: flip the sign; otherwise (credit Transaction)
   *     leave it positive.
   *   Construct the Transaction the account ID and this amount value
   *   Add it to the pendingTransactions list.
   *   Display a confirmation message on the console.
   */
    public boolean createRcdOk() {
    String accountID = txtAccountID.getText();  
    String amount = txtAmount.getText();
    int amountInt = 0;
    float amountFloat = 0;
    
    if(parent.validateAccountID(accountID)){
        System.out.println("Account ID validated.");
        if(amount.contains(".")) { // looks for decimal point, If found
            amountFloat = Float.parseFloat(txtAmount.getText());// parse it as float, get text 
                                                                //@ param txtAmount
            amountFloat *= 100;// times it by 100
            
            amountInt = (int)(Math.round(amountFloat)); // cast it as an int and round if needed to
        }else{
            amountInt = Integer.parseInt(txtAmount.getText()); // else, parse it as Int, get text 
                                                               // @ param txtAmount
        }
        if(isInDebitMode()){
            amountInt = -amountInt;// if amount is in debit mode, reverse the sign to minus
        }
        //add to pending transactions @ param accountID, amountInt, System.currentTimeMillis()
        parent.addToPendingTransactions(new Transaction(accountID, amountInt, System.currentTimeMillis()));
        //simple print statement if successful
        System.out.println("Transaction added.");
        return true;// retunrs true
    }

    return false;// if errors, return as false, dont do anything
  } //end createRcdOk()
 
}


