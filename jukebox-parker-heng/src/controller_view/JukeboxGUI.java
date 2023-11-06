/**
 * Main GUI: GUI controller
 * @authors: Hengsocheat Pok, Parker Hines
 */


package controller_view;
 
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.AccountCollection;
import model.JukeboxAccount;
import model.PlayList;
import model.Song;
import javafx.scene.layout.BorderPane;
import java.io.*;
import java.net.URI;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
 
public class JukeboxGUI extends Application {
 
    LoginCreateAccountPane logInPane;
    BorderPane everything;
    LoginCreateAccountPane signUpPane;
    Scene currentScene;
    AnchorPane logInButtonsArea;
    AnchorPane signUpButtonsArea;
    Button signUpButton;
    Button logInButton;
    Button logOutButton;
    Button backToMainPaneButton;
    Button createAccountButton;
    Button selectSong1;
    Button selectSong2;
    JukeboxAccount workingAccount;
 
    HBox topButtonsArea;
    VBox betweenTablesArea;
    BorderPane areaBelowTheTables;
    String darkTheme = getClass().getResource("DarkTheme.css").toExternalForm();
 
    private Label textInstruction;
    private TextFlow textInstructionArea;
    private Label signUpStatusText = new Label("");
    private Label logInStatusText = new Label("");
    private static AccountCollection listOfAccounts = new AccountCollection();
    private static AccountCollection listOfTheActiveAccount= new AccountCollection();
    private PaneWithTableView mainSongsPane;
    private PaneWithTableView queueTablePane;
    private Button playButton;
    //private TableView<BankAccount> allSongsTable;
    //private TableView<BankAccount> queueTable;
    private HBox tablesArea;
    private TableView<Song> allSongsTable;
    private static PlayList globalQueue = new PlayList();
    private transient static MediaPlayer currentMediaPlayer = null; //
    private volatile transient MediaPlayer mediaPlayer; // google said to use volatile for threading concerns
    PlayList queue = new PlayList(); //
    private Media media;
    private Button pauseButton;
    private Button skipButton;
    public static void main(String[] args) {
	
	//saveState(globalQueue, "PlayList.ser");
	
       //saveState(listOfAccounts, "Accounts.ser");
 
	//saveState(listOfTheActiveAccount, "ActiveAccount.ser");
 
        loadState(listOfAccounts, "Accounts.ser");												// load the list of all accounts available
        loadState(globalQueue, "PlayList.ser");
        System.out.println(globalQueue.getQueue());
        /*listOfAccounts.getAccountArrayList().clear();
        saveState(listOfAccounts, "Accounts.ser");*/
        System.out.println();
        System.out.printf("Existing Accounts (%d): ", listOfAccounts.getAccountArrayList().size());
        System.out.println();
        System.out.println();
        listOfAccounts.print();
        System.out.println("-----------------------------------------");
        launch(args);
    }
 
    @Override
    public void start(Stage primaryStage) throws Exception {
 
        LayoutGUI();
        registerHandlers(primaryStage);
        currentScene = new Scene(everything, 500, 270);
        primaryStage.setScene(currentScene);
        primaryStage.setTitle("Log In or Sign Up");
        primaryStage.setResizable(false);
        loadState(listOfTheActiveAccount, "ActiveAccount.ser");									// load the list of the active account
        
        
        checkUpAtStart(primaryStage);
        
        currentScene.getStylesheets().clear();
        currentScene.getStylesheets().add(darkTheme);

        primaryStage.show();
        
        primaryStage.setOnCloseRequest(e -> {
          
                    //if (globalQueue.getQueue().isEmpty())
                	
            
                    if (workingAccount != null) {
                	//saveState(listOfTheActiveAccount, "ActiveAccount.ser");
                	Queue<String> tempQueue = new LinkedList<>(queue.getQueue());
                	globalQueue.getQueue().clear();
                	globalQueue.getQueue().addAll(tempQueue);
                	workingAccount.currentTable = null;
		/*
		 * for (int i = 0; i < workingAccount.getPlayList().getQueue().size(); i++) {
		 * String extractedPath = workingAccount.getPlayList().getQueue().;
		 * System.out.println(extractedPath);
		 * globalQueue.queueUpNextSong(extractedPath); }
		 */                
                     }
                    else {
                	listOfTheActiveAccount.getAccountArrayList().clear();
                    }
                    saveState(listOfTheActiveAccount, "ActiveAccount.ser");
                    //System.out.println(globalQueue.getQueue().size() ==0 ? "you are saving an empty list!!!!": "You are saving some elements!!!");
                    //System.out.println(globalQueue.getFirst());
                    saveState(globalQueue, "PlayList.ser");
                    saveState(listOfAccounts, "Accounts.ser");
                    });
        
     // Create a Timeline to update the TableView every 1 second
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            // Update the data in the TableView
            mainSongsPane.updateList();
            queueTablePane.updateList();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); // Run the timeline indefinitely
        timeline.play(); // Start the timeline
        
    }
 
    public void checkUpAtStart(Stage primaryStage) {
	
	if (globalQueue.getQueue().isEmpty() && listOfTheActiveAccount.getAccountArrayList().size() == 0)
        {
            //System.out.println("starting with new data...");
            if (listOfTheActiveAccount.getAccountArrayList().size() > 0) {
		listOfTheActiveAccount.getAccountArrayList().remove(0);}			// remove the working account from the list of the active account
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.setHeaderText("Each user can play up to 100 songs per day!");
            //alert.setContentText("Click 'Yes' to start where you left off or 'No' to start over (logging in with another account)");
            Optional<ButtonType> result = alert.showAndWait();
            changeView(mainSongsPane, primaryStage);
            return;
        }
	
	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("You did not log out the last time. Do you wish to continue where you left off?");
        alert.setContentText("Click 'Yes' to start where you left off or 'No' to start over (logging in with another account)");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.YES){
            
            System.out.println("Yes Clicked: continuing from where we left off...");
            if (!listOfTheActiveAccount.getAccountArrayList().isEmpty()) {
        	workingAccount = listOfTheActiveAccount.getAccountArrayList().get(0);
            } else {
            	workingAccount = new JukeboxAccount("E X", "0");
            }
            
            Queue<String> tempQueue = new LinkedList<>(globalQueue.getQueue());
            if (!tempQueue.isEmpty()) {
        	//System.out.println("first in queue: " + tempQueue.peek() + "\n");
            	Song song = new Song(tempQueue.peek());
                tempQueue.poll();
                //System.out.printf("Startup playing: " + song.getPath(), "\n");
                playSong(song, false, workingAccount);
                System.out.println("\n\nTo be added to the table and played\n\n" + song.getTitle());
                queueTablePane.addToQueue(song);
            }
            while (!tempQueue.isEmpty()) {
            	Song song = new Song(tempQueue.peek());
                tempQueue.poll();
                queue.queueUpNextSong(song.getPath());
                System.out.println("\n\nTo be added to the table and queued\n\n" + song.getTitle());
                queueTablePane.addToQueue(song);
            }
            
            if (workingAccount.getUsername().equals("E X")) {
            	workingAccount = null;
            }
            
            changeView(mainSongsPane, primaryStage);
            
        }
        else{
            System.out.println("No Clicked: starting with new data...");
            if (listOfTheActiveAccount.getAccountArrayList().size() > 0) {
        	workingAccount = null;
		listOfTheActiveAccount.getAccountArrayList().remove(0);}			// remove the working account from the list of the active account
		//saveState(listOfTheActiveAccount, "ActiveAccount.ser");				// remember the active account (0 in this case)
            globalQueue.getQueue().clear();
            changeView(mainSongsPane, primaryStage);
            }
    }
 
    private void LayoutGUI() {
	
	// init the main elements of the tableview pane
    	mainSongsPane = new PaneWithTableView(true);
        allSongsTable = mainSongsPane.getTable();
        queueTablePane = new PaneWithTableView(false);
        queueTablePane.getTable();
        playButton = new Button("Play");
        new Button("Resume");
        pauseButton = new Button("Pause");
        skipButton = new Button("Skip");
        logOutButton = new Button("Log Out");                
        betweenTablesArea = new VBox(playButton, pauseButton, skipButton, logOutButton);
        
        betweenTablesArea.setPadding(new Insets(150, 5, 0, 5));
        betweenTablesArea.setSpacing(15.00);
        tablesArea = new HBox(mainSongsPane, betweenTablesArea, queueTablePane);
        tablesArea.setSpacing(20.00);
        
        
        // init the elements of the startup panes
        textInstruction = new Label("\n");
        textInstructionArea = new TextFlow(textInstruction);                                 // init the top label
        textInstructionArea.setTextAlignment(TextAlignment.CENTER);              // set text alignment to center
        everything = new BorderPane(); 									// init the pane storing the login pane and
 
 
	logInPane = new LoginCreateAccountPane(1); 						// init the login
	signUpPane = new LoginCreateAccountPane(2); 					// init the signup pane.
 
        logInButton = new Button("Log In");                                                 		// init the login button
        signUpButton = new Button("Sign Up");                                                	// init the signup button
        backToMainPaneButton = new Button("Back to Main Pane");
 
        setUpLogInButtonsArea();
        setUpSignUpButtonsArea();
 
	/*
	 * //set the top label (instruction) (will be used later in the course)
	 * textInstruction = new Label("\nLogin or Create Account");
	 * 
	 * // init the temporary song playing buttons and their area selectSong1= new
	 * Button("Select Song 1"); selectSong2 = new Button("Select Song 2");
	 * topButtonsArea = new HBox(selectSong1, selectSong2);
	 * topButtonsArea.setPadding(new Insets(10,0,10,150));
	 * topButtonsArea.setSpacing(20); everything.setTop(topButtonsArea);
	 */
        
    }
 
    private void setUpLogInButtonsArea() {
 
        logInButtonsArea = new AnchorPane(logInButton, signUpButton, logInStatusText);    // init the startup buttons area
        logInButtonsArea.setPrefHeight(140);
        everything.setBottom(logInButtonsArea);                                                                 				   // add it to the bottom area of the borderpane
        // adjust the login button
        AnchorPane.setTopAnchor(logInButton, 10.00);
        AnchorPane.setLeftAnchor(logInButton, 250 + 170.00);
        // adjust the signup button
        AnchorPane.setTopAnchor(signUpButton, 10.00);
        AnchorPane.setLeftAnchor(signUpButton, 250 + 240.00);
        // adjust the status text
        AnchorPane.setTopAnchor(logInStatusText, 90.00);
        AnchorPane.setLeftAnchor(logInStatusText, 250 + 190.00);
 
    }
 
    private void setUpSignUpButtonsArea() {
 
        createAccountButton = new Button("Create Account");                                                 							// init the create account button
        signUpButtonsArea = new AnchorPane(createAccountButton, backToMainPaneButton, signUpStatusText);         // init the startup buttons area
        signUpButtonsArea.setPrefHeight(140);
 
        // adjust the buttons
        AnchorPane.setTopAnchor(createAccountButton, 10.00);
        AnchorPane.setLeftAnchor(createAccountButton, 170.00);
        AnchorPane.setTopAnchor(backToMainPaneButton, 10.00);
        AnchorPane.setLeftAnchor(backToMainPaneButton, 280.00);
        // adjust the prompt text
        AnchorPane.setTopAnchor(signUpStatusText, 50.00);
        AnchorPane.setLeftAnchor(signUpStatusText, 180.00);
 
    }
 
    private void registerHandlers(Stage primaryStage){
 
        signUpButton.setOnAction( e -> {
            changeView(signUpPane, primaryStage);
        });
 
        backToMainPaneButton.setOnAction( e -> {
            changeView(mainSongsPane, primaryStage);
        });
 
        createAccountButton.setOnAction(e -> {
            String enteredUsername = signUpPane.getUsernameTextField().getText();
            String enteredPassword = signUpPane.getPasswordField().getText();
 
            if ((enteredPassword.isEmpty() || enteredPassword.contains(" ") ||
                    enteredUsername.isEmpty() || enteredUsername.contains(" ") || listOfAccounts.contains(enteredUsername))){
 
                signUpStatusText.setText("The username is taken/empty or \nthe password field is empty or\nany of them contains a space\ntry again!");
                System.out.println();
                System.out.println("The username is taken/empty or the password field is empty, try again!\n");
                System.out.println("\n------------------------------------------------\n");
            }
            else {
                JukeboxAccount newAccount = new JukeboxAccount(enteredUsername, enteredPassword);
                listOfAccounts.add(newAccount);
                //saveState(listOfAccounts, "Accounts.ser");
                signUpStatusText.setText("Account Created!\nYour Credentials are on the Console!\nReturn to the main page and log in.");
                /*
                 * System.out.printf("Latest List of Existing Accounts (%d): ",
                 * listOfAccounts.getAccountArrayList().size()); System.out.println();
                 * System.out.println(); listOfAccounts.print();
                 * System.out.println("------------------------------------");
                 */
            }
        });
 
        logInButton.setOnAction( e -> {
            String enteredUsername = logInPane.getUsernameTextField().getText();
            String enteredPassword = logInPane.getPasswordField().getText();
            if (workingAccount == null) {
                if (!(enteredPassword.isEmpty() && enteredUsername.isEmpty())){
                    boolean found = false;
                    if (listOfAccounts.contains(enteredUsername) && listOfAccounts.correctPassword(enteredUsername, enteredPassword)){
                        logInStatusText.setText("Logged In!\nWelcome, " + enteredUsername + "!");
                        // PH - set working account
                        // TODO: initialize account from .ser file, to get songsPlayed in next iteration
                        for (JukeboxAccount currentAccount: listOfAccounts.getAccountArrayList()) {
                    		if (currentAccount.getUsername().equals(enteredUsername)) {
                    			found = true;
                    			workingAccount = currentAccount;
                    			workingAccount.currentTable = queueTablePane.getList();
                    			listOfTheActiveAccount.add(workingAccount);						// the working account (the active account) is now added to the list
                    			//saveState(listOfTheActiveAccount, "ActiveAccount.ser");				// remember that we have one active account
     
                    		}
                    	}
     
                        System.out.print("User has played " + workingAccount.getNumSongsPlayed() + " today.");
                        System.out.println("\nLogged In!");
                        System.out.println("Welcome, " + workingAccount.getUsername() + "!\n");
                        System.out.println("-------------------------------------\n");
     
                        changeView(mainSongsPane, primaryStage);
                    }
     
                    if (!found){
                        logInStatusText.setText("Wrong Username or Password!\nTry Again!");
                        System.out.println("\nWrong Username or Password!\nTry Again!\n\n-----------------------------------\n");}
                }}
        });
 
        logOutButton.setOnAction( e -> {
        	if (workingAccount != null) {
        		System.out.println("New songsPlayed for " + workingAccount.getUsername() + ": " + workingAccount.getNumSongsPlayed());
        		listOfAccounts.replace(workingAccount.getUsername(), workingAccount);
        		System.out.println("Logged out of " + workingAccount.getUsername() + "'s account."); 
        		logInStatusText.setText("Logged out of user account: " + workingAccount.getUsername()+ "!");
        		workingAccount = null; // working account is null again
        		changeView(mainSongsPane, primaryStage);
        		listOfTheActiveAccount.getAccountArrayList().removeAll(listOfTheActiveAccount.getAccountArrayList());			// remove the working account from the list of the active account
        		//saveState(listOfTheActiveAccount, "ActiveAccount.ser");				// remember the active account (0 in this case)
        	}
 
        	else {
        		System.out.println("Button failed, no user logged in.");
        	}
        	//workingAccount = null; // working account is null again
        	logInPane.getPasswordField().clear();
        	logInPane.getUsernameTextField().clear();
        });
 
        playButton.setOnAction( e -> {
            if (workingAccount != null) { // check if there's a working account
                if (!workingAccount.isEligible()) {
                    Alert songsExceeded = new Alert(Alert.AlertType.ERROR);
                    songsExceeded.setHeaderText("You've exceeded your 100-song limit for today.");
                    songsExceeded.showAndWait();
                }
                else {
                    Song songSelection = (Song) allSongsTable.getSelectionModel().getSelectedItem();
                    if (songSelection != null) {
                        queueTablePane.addToQueue(songSelection);
                        playSong(songSelection, true, workingAccount);
                    }
                }
            }
            else {
                System.out.println("Button failed, no user logged in.");
            }
            //Song songSelection = (Song) allSongsTable.getSelectionModel().getSelectedItem();
            //if (songSelection != null) {
            //    queueTablePane.addToQueue(songSelection);
            //}
        });
        
        pauseButton.setOnAction( e -> {
            if (currentMediaPlayer != null && pauseButton.getText().equals("Pause")) {
        	currentMediaPlayer.pause();
            	pauseButton.setText("Resume");}
            else if (currentMediaPlayer != null && pauseButton.getText().equals("Resume")) {
        	currentMediaPlayer.play();
            	pauseButton.setText("Pause");}
        });
        
        skipButton.setOnAction( e -> {
            queueTablePane.removeFirst();
            if (currentMediaPlayer != null ) {
        	pauseButton.setText("Pause");
        	currentMediaPlayer.stop();			// stop the player
        	
        	// moving on to the next song and remove the current one from the queue:
        	 //queueTablePane.removeFirst();
                 //queue.removeFirst();
                 System.out.println("Removed First Element of PlayList array");
                 
                 // saves the next song path before removing it from the list (if exists)
                 String nextSongPath = queue.getFirst();
                 queue.removeFirst();
                 
                 // if there's more stuff in the queue
                 if (nextSongPath != null) {
               	  Song nextSong = new Song(nextSongPath);
               	  System.out.println("Next song path: " + nextSongPath);
                     //queue.removeFirst(); // removes the first node (updates head node in MyList)
                     
                     currentMediaPlayer = null;
                     System.out.println("Removing current media player");

                     playSong(nextSong, false, workingAccount); // plays associated song, resets waiter
               	  System.out.println("Attempting to play next song");
                 } else {
                     System.out.println("no more songs to play!");
               	  currentMediaPlayer = null;
                 }
            }
        });
        
    }
 
    private void changeView (LoginCreateAccountPane newStartUpPane, Stage primaryStage){
 
        everything = new BorderPane();
        everything.setCenter(newStartUpPane);                                              // add the new pane to the center
 
        if (newStartUpPane.getType() == 2){                                                 // if changing to sign up
            everything.setBottom(signUpButtonsArea);
            textInstruction.setText("\nCreate an Account");
            // add the text to the top area of the new startup pane
            textInstructionArea = new TextFlow(textInstruction);
            textInstructionArea.setTextAlignment(TextAlignment.CENTER);
            everything.setTop(textInstructionArea);
            
            //coloring
            currentScene.getStylesheets().add(darkTheme);
            //signUpPane.switchToDark();
        }
 
        else if (newStartUpPane.getType() == 1){						// if changing to log in
            everything.setBottom(logInButtonsArea);
            textInstruction.setText("\nLogin or Create Account");
            everything.setTop(topButtonsArea);
            
            // coloring
            currentScene.getStylesheets().add(darkTheme);
            logInPane.switchToDark();
        }
 
 
 
        currentScene = new Scene(everything, 530, 270);           // create a new scene
        primaryStage.setScene(currentScene);                            // set it
 
        signUpStatusText.setText("");
        logInStatusText.setText("");
        signUpPane.getUsernameTextField().clear();
        signUpPane.getPasswordField().clear();
 
    }
 
    private void changeView (PaneWithTableView tablePane, Stage primaryStage){
 
        everything = new BorderPane();
        areaBelowTheTables = new BorderPane();
 
        everything.setCenter(tablesArea);                                           
        areaBelowTheTables.setCenter(logInPane);
        areaBelowTheTables.setBottom(logInButtonsArea);
        everything.setBottom(areaBelowTheTables);
        everything.setPadding(new Insets(10, 10, 10, 10));
 
        if (workingAccount != null)
            textInstruction.setText("\n" + workingAccount.getUsername() + "'s Playlist");
        else
            textInstruction.setText("\n");
       textInstruction.setFont(new Font(18));
        textInstructionArea = new TextFlow(textInstruction);
        textInstructionArea.setTextAlignment(TextAlignment.CENTER);
        everything.setTop(textInstructionArea);
 
        currentScene = new Scene(everything, 320*3 + 70, 320*2);           // create a new scene
        primaryStage.setScene(currentScene);                            			// set it
 
        signUpStatusText.setText("");
        logInStatusText.setText("");
        signUpPane.getUsernameTextField().clear();
        signUpPane.getPasswordField().clear();
        
        //coloring
        currentScene.getStylesheets().add(darkTheme);
        logInPane.switchToDark();
  
 
    }
 
 
    private static void saveState(AccountCollection listOfAccounts, String filename){
        /**
         * serialize the list of accounts
         */
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(listOfAccounts);
            System.out.println("serialized: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void saveState(PlayList listOfSongPathsSaved, String filename){
        /**
         * serialize the list of accounts
         */
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(listOfSongPathsSaved);
            System.out.println("serialized: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    private static void loadState(AccountCollection listOfAccounts, String filename){
        /**
         * deserialize the items in a list of accounts
         */
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            AccountCollection loaded = (AccountCollection) in.readObject();
            listOfAccounts.getAccountArrayList().addAll(loaded.getAccountArrayList());
            System.out.println("deserialized: " + filename);
 
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static void loadState(PlayList listOfSongPathsSaved, String filename){
        /**
         * deserialize the items in a list of accounts
         */
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            PlayList loaded = (PlayList) in.readObject();
            System.out.println(loaded.getFirst() + "first element in the load function");
           
            listOfSongPathsSaved.getQueue().clear();
            listOfSongPathsSaved.getQueue().addAll(loaded.getQueue());
            
	    /*
	     * if (listOfSongPathsSaved.getQueue().size() == 0) System.out.println(
	     * "00000000000000000000000000000000000000000000000000000000000000000000000");
	     * System.out.println("deserialized: " + filename);
	     */
 
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // PH - function to play a song
    public void playSong(Song nextSong, boolean checkEligibility, JukeboxAccount working) {
        if (!checkEligibility || working.isEligible()) { 
            // copied from PlayAnMP3.java 
            String path = nextSong.getPath();
            System.out.println("Current song path: " + path);
            // Need a File and URI object so the path works on all OSs
            File file = new File(path);
            URI uri = file.toURI();
            System.out.println(uri);
            media = new Media(uri.toString());
            
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
            
            if (checkEligibility && working.isEligible()) { // will only increment song counter if called from button
                working.incSong();
            }
        }
        else {
            System.out.println("User is no longer eligible to queue songs ( limit 100/day )");
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
          queueTablePane.removeFirst();
          queue.removeFirst();
          System.out.println("Removed First Element of PlayList array");
          
          // saves the next song path before removing it from the list (if exists)
          String nextSongPath = queue.getFirst();
          
          // if there's more stuff in the queue
          if (nextSongPath != null) {
        	  Song nextSong = new Song(nextSongPath);
        	  System.out.println("Next song path: " + nextSongPath);
              //queue.removeFirst(); // removes the first node (updates head node in MyList)
              
              currentMediaPlayer = null;
              System.out.println("Removing current media player");

              playSong(nextSong, false, workingAccount); // plays associated song, resets waiter
        	  System.out.println("Attempting to play next song");
          } else {
              	//queueTablePane.removeFirst();
              	//queueTablePane.updateList();
        	  currentMediaPlayer = null;
          }
        }
      }
 
}