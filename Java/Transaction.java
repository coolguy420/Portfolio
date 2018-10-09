import java.util.Date;

/**
 * Transaction: class to record a credit or debit transaction on an account
 */
public class Transaction {
  private String accountID;
  private int amount; //negative for debit, positive for credit
  private long dateTimeStamp;

  // Constructors
  public Transaction(String acc, int amt, long dts) {
    accountID = acc;
    amount = amt; 
    dateTimeStamp = dts;
  }

  //Another constructor: usually, we create a transaction timestamped 'now'
  public Transaction(String acc, int amt) {
    this (acc, amt, System.currentTimeMillis());
  }

  //Accessors
  public String getAccountID()    { return accountID;     }
  public int getAmount()          { return amount;        }
  public long getDateTimeStamp()  { return dateTimeStamp; }

  public void setAmount(int amt)  { amount = amt; }
  public void updateDateTimeStamp() {
    dateTimeStamp = System.currentTimeMillis();
  }

  //NB new Date(timeStamp).toString() converts timeStamp to a date/time string
  public String toString() {
    return String.format("Transaction on account %s: amount %d, dated %s",
      accountID, amount, new Date(dateTimeStamp));
  }

} //end class

