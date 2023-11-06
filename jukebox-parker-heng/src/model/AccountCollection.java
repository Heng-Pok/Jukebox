/**
 * A class that stores all accounts ever created by users
 * @authors: Hengsocheat Pok, Parker Hines
 */



package model;

import java.io.Serializable;
import java.util.ArrayList;

public class AccountCollection implements Serializable {

    private ArrayList<JukeboxAccount> accountArrayList;

    public AccountCollection(){
        accountArrayList = new ArrayList<>();
    }

    public void add (JukeboxAccount account) {
        accountArrayList.add(account);
    }

    public ArrayList<JukeboxAccount> getAccountArrayList(){
        return accountArrayList;
    }
    
    public void replace (String username, JukeboxAccount replacement) {
    	System.out.println("Replacing object data in linked list for user: " + username);
    	System.out.println("New songsPlayed should be: " + replacement.getNumSongsPlayed());
    	int i;
    	for (i = 0; i < accountArrayList.size(); i++) {
    		JukeboxAccount currentAccount = accountArrayList.get(i);
    		if (currentAccount.getUsername().equals(username)) {
    			System.out.println("Found account for user: " + username);
    			accountArrayList.set(i, replacement);
    			System.out.println("Account updated for user: " + username);
    		}
    	}
    	return;
    }
    
    public boolean contains (String username) {
    	boolean found = false;
    	for (JukeboxAccount currentAccount: accountArrayList) {
    		if (currentAccount.getUsername().equals(username)) {
    			found = true;
    		}
    	}
		return found;
    }
    
    public boolean correctPassword (String username, String password) {
    	boolean match = false;
    	for (JukeboxAccount currentAccount: accountArrayList) {
    		if (currentAccount.getUsername().equals(username) && currentAccount.getPassword().equals(password)) {
    			match = true;
    		}
    	}
		return match;
    }

    public void print(){
        for (JukeboxAccount account: accountArrayList) {
            account.print();
            System.out.println();
        }
    }
}

