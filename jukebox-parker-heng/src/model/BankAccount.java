package model;
 
/**
* This class models a minimal bank account to be used with textbook
*
*  Computing Fundamentals with Java
*            Rick Mercer
*    Franklin, Beedle & Associates
*/
import java.text.NumberFormat;
 
public class BankAccount {
  // Instance variables that every BankAccount object will maintain
  private String ID;
  private Double balance;
 
  /**
   * Initialize private data fields with arguments during construction.
   * 
   * @param initID      A String meant to uniquely identify this BankAccount.
   * @param initBalance This BankAccount's starting balance.
   */
  public BankAccount(String initID, double initBalance) {
    ID = initID;
    balance = initBalance;
  }
 
  // Need this getter method for TableView. Need the upper case 'I' after get.
  public String getID() {
    return ID;
  }
 
  // Need this getter method for TableView. Need the upper case 'B' after get.
  public String getBalanceAsString() {
    NumberFormat nf = NumberFormat.getCurrencyInstance();
    return nf.format(balance);
  }
 
  public String getTime() {
      String time = "0:00";
      return time;
  }
 
 
 
  /**
   * Credit this account by depositAmount. Precondition: depositAmount >= 0.0. If
   * negative, balance is DEBITed.
   * 
   * @param depositAmount amount of money to credit to this BankAccount.
   */
  public void deposit(double depositAmount) {
    balance = balance + depositAmount;
  }
 
  /**
   * Debit this account by withdrawalAmount if it is positive and also no greater
   * than this account's current balance.
   * 
   * @param withdrawalAmount The requested amount of money to withdraw
   * @return true if 0 < withdrawalAmount <= the balance.
   */
  public boolean withdraw(double withdrawalAmount) {
    boolean result = true;
    if ((withdrawalAmount <= balance) && (withdrawalAmount > 0.0))
      balance = balance - withdrawalAmount;
    else
      result = false;
    return result;
  }
 
  /**
   * Access this account's current balance.
   * 
   * @return the BankAccount's current balance.
   */
  public double getBalance() {
    return balance;
  }
 
  /**
   * Return the state of this object as a String.
   */
  public String toString() {
    return getID() + ": " + getBalanceAsString();
  }
 
 
} // end class BankAccount