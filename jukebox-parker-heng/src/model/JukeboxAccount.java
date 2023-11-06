/**
 * Jukebox Account Model : for the users
 * @authors: Hengsocheat Pok, Parker Hines
 */


package model;
import java.io.File;

import java.io.Serializable;
import java.net.URI;
// Parker Hines
import java.time.LocalDate;
import java.util.ArrayList;

import controller_view.PaneWithTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

// This class name is just a suggestion. 
// The account will need to use the type LocalDate
public class JukeboxAccount implements Serializable {
  
    private int songsPlayed; // number of songs played
    private LocalDate today; // day song was last played
    private String username;
    private String password;
    public transient ObservableList<Song> currentTable;

    // constructor
    public JukeboxAccount(String username, String password) {
        this.username = username;
        this.password = password;
        songsPlayed = 0;
        today = LocalDate.now();
        System.out.println("\nNew Account created for user: " + username);
        System.out.println("----------------------");
    }
    
    
    // inc song for playSong in GUI
    public void incSong() {
    	songsPlayed++;
    	today = LocalDate.now();
    }
    
    // function determines if user is eligible to play a song
    public boolean isEligible() {
    	// can replace this loop with checkIfNewDay() after Tuesday.
        LocalDate now = LocalDate.now();
        // checks if the last song was played today
        if (!today.equals(now)) { // if not, resets vars
            today = LocalDate.now();
            songsPlayed = 0;
        }
        if (songsPlayed < 100) {
            return true;
        }
        return false;
    }
        
    // function purely for testing, that pretends the date is tomorrow's
    // and then checks eligibility again. should always return true.
    public void pretendItIsTomorrow() {
        today = today.plusDays(1); // increments today's date by 1
    }
    
    // getter for number of songs played
    public int getNumSongsPlayed() {
    	// needs logic to check if a new day has occured before returning songs played
    	checkIfNewDay();
        return songsPlayed;
    }
    
    public void checkIfNewDay() {
    	LocalDate now = LocalDate.now();
    	if (!today.equals(now)) { // if not, resets vars
            today = LocalDate.now();
            songsPlayed = 0;
        }
    }
    
    // getter for name on the account
    public String getUsername() {
    	return this.username;
    }
    
    // getter for password on the account
    public String getPassword() {
    	return this.password;
    }
    
    public void print (){
        System.out.println("Username: " + this.username + "\nPassword: " + this.password);
    }
    

    
}