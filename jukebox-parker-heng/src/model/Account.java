package model;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.time.LocalDate;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Account implements Serializable {
    private String username;
    private String password;
    private int songsPlayed; // PH - var for songs played on account today
    private LocalDate today; // PH - var to hold the date

    public Account (String username, String password){
        this.username = username;
        this.password = password;
        this.songsPlayed = 0; // PH - init at 0 for Thursday
        today = LocalDate.now(); // PH - set the date to today
        System.out.print("\nA new account has been created: \n");
        print();
        System.out.println("\n---------------------------------");
        System.out.println();
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setUsername(String newUsername){
        this.username = newUsername;
    }

    public void setPassword(String newPassword){
        this.password = newPassword;
    }

    public void print (){
        System.out.println("Username: " + this.username + "\nPassword: " + this.password);
    }
    
    // PARKER HINES -- added functions below for Thursday deliverable
    private MediaPlayer currentMediaPlayer = null;
    private volatile MediaPlayer mediaPlayer; // google said to use volatile for threading concerns
    PlayList queue = new PlayList();
    // PH - function to play a song
    public void playSong(String songPath, boolean checkEligibility) {
        if (!checkEligibility || isEligible()) { 
        	// copied from PlayAnMP3.java 
        	String path = songPath;
        	System.out.println("Current song path: " + songPath);
        	// Need a File and URI object so the path works on all OSs
            File file = new File(path);
            URI uri = file.toURI();
            System.out.println(uri);
            Media media = new Media(uri.toString());
            
            if (currentMediaPlayer == null || currentMediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            	mediaPlayer = new MediaPlayer(media);
            	currentMediaPlayer = mediaPlayer;
                //mediaPlayer.setOnEndOfMedia(new Waiter()); // sets waiter to check queue after song plays
                Thread mediaThread = new Thread(new PlayingSong(mediaPlayer)); // new thread to play media
                mediaThread.start(); // start thread
            } else { // if song is already playing
                queue.queueUpNextSong(path); // new song added to end of queue
                System.out.println(path + " queued at the end of the playlist.");
            }
        	
            if (checkEligibility && isEligible()) { // will only increment song counter if called from button
            	songsPlayed++;
            }
            today = LocalDate.now(); // change last played date
        }
        else {
        	System.out.println("User is no longer eligible to queue songs ( limit 3/day )");
        }
    }
    
    // runnable implementation to multi-thread the media-player
    private class PlayingSong implements Runnable {
    	private MediaPlayer mediaPlayer;

        public PlayingSong(MediaPlayer mediaPlayer) {
            this.mediaPlayer = mediaPlayer;
        }
    	@Override
    	public void run() {
    		System.out.println("---------------------------------");
    		System.out.println("Playing a song.");
    		mediaPlayer.play(); // plays the media player
    		mediaPlayer.setOnEndOfMedia(() -> {
                System.out.println("Song finished playing.");
                System.out.println("Starting a pause.");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Pause over.");
                System.out.println("---------------------------------");
                
                new Thread(new Waiter()).start();
            });
    	}
    }
    
    private class Waiter implements Runnable {
        @Override
        public void run() {
          System.out.println("Song ended.");
          
          // saves the next song path before removing it from the list (if exists)
          String nextSongPath = queue.getFirst();
          
          // if there's more stuff in the queue
          if (nextSongPath != null) {
        	  System.out.println("Next song path: " + nextSongPath);
              queue.removeFirst(); // removes the first node (updates head node in MyList)
              System.out.println("Removed First Element of PlayList array");
              
              currentMediaPlayer = null;
              System.out.println("Removing current media player");

              playSong(nextSongPath, false); // plays associated song, resets waiter
        	  System.out.println("Attempting to play next song");
          }
        }
      }
    
    // PH - function determines if user is eligible to play a song
    public boolean isEligible() {
    	// can replace this loop with checkIfNewDay() after Tuesday.
        LocalDate now = LocalDate.now();
        // checks if the last song was played today
        if (!today.equals(now)) { // if not, resets vars
            today = LocalDate.now();
            songsPlayed = 0;
        }
        if (songsPlayed < 3) {
            return true;
        }
        return false;
    }
    
    // PH - getter for number of songs played
    public int getNumSongsPlayed() {
    	// needs logic to check if a new day has occured before returning songs played
    	checkIfNewDay();
        return songsPlayed;
    }
    
    // PH - checks if a new day has occurred (to reset songsPlayed)
    public void checkIfNewDay() {
    	LocalDate now = LocalDate.now();
    	if (!today.equals(now)) { // if not, resets vars
            today = LocalDate.now();
            songsPlayed = 0;
        }
    }
}
