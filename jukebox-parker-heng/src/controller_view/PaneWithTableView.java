/**
 * Implement table views to our GUI
 * @authors: Hengsocheat Pok, Parker Hines
 */


package controller_view; /**
 * This pane holds a TableView, adds two columns to it and
 * shows a List of BankAccount data, the ID and the balance.
 * When you click on a row in the TableView, 111.11 is deposited.
 * The TableView is refreshed and the before and after state are printed.
 */
 
import model.PlayList;
import model.Song;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
 
public class PaneWithTableView extends BorderPane implements Serializable {
   
  private TableView<Song> tableView;
  private ObservableList<Song> songsTableToDisplay;
  private ArrayList<Song> allSongsList;
 
  // we have to setItems later like this:
  // table.setItems(bankAccounts);
  private boolean type; // type = true means it's a list of all songs, false means it's a queue.
private ArrayList<File> songsScanned;
private File folder;
private File[] mp3Files;
 
  @SuppressWarnings("unchecked")
  public PaneWithTableView(Boolean type) {
	  // PH
	  tableView = new TableView<Song>();
	  songsTableToDisplay = FXCollections.observableArrayList();
      this.type = type;
      
      final Label label = type ? new Label("\nAll Songs") : new Label("\nQueue");
      label.setFont(new Font("Arial", 20));
      this.setTop(label);
      
      TableColumn<Song, String> title = new TableColumn<>("Title");  // Heading
      TableColumn<Song, String> artist = new TableColumn<>("Artist");
      TableColumn<Song, String> time = new TableColumn<>("Time");
      
      title.setCellValueFactory(new PropertyValueFactory<Song, String>("Title"));
      artist.setCellValueFactory(new PropertyValueFactory<Song, String>("Artist"));
      time.setCellValueFactory(new PropertyValueFactory<Song, String>("Time"));
      
      tableView.setItems(songsTableToDisplay);
      
      title.setMinWidth(148);
      artist.setMinWidth(150);
      time.setMinWidth(148);

      tableView.getColumns().addAll(title, artist, time);
      
      if (type) {
	  allSongsList = new ArrayList<>();
    	  addAllSongs();
    	  updateList();
    	//scanSongs();
      }
      
      if (!type) {
	  allSongsList = new ArrayList<>();
	  updateList();
      }
      this.setCenter(tableView);
    
  }
 
  private	void	  scanSongs() {
      songsScanned	= new ArrayList<File>();
      folder = new File("songfiles");
      mp3Files = folder.listFiles();
      if (mp3Files != null) {
	  for (File file : mp3Files) {	
	      allSongsList.add(new Song(file.toString()));
	      //System.out.println(file.toString());
	  }
      }
      
      
  }
  
  private void addAllSongs() {
      	scanSongs();
      	if (allSongsList.isEmpty()) {			// demo songs
        	  allSongsList.add(new Song("Pokemon Capture", "Pikachu", "0:05", "songfiles/Capture.mp3"));
        	  allSongsList.add(new Song("Danse Macabre", "Kevin MacLeod", "0:34", "songfiles/DanseMacabreViolinHook.mp3"));
        	  allSongsList.add(new Song("Determined Tumbao", "FreePlay Music", "0:20", "songfiles/DeterminedTumbao.mp3"));
        	  allSongsList.add(new Song("LopingSting", "Kevin MacLeod", "0:05", "songfiles/LopingSting.mp3"));
        	  allSongsList.add(new Song("Swing Cheese", "FreePlay Music", "0:15", "songfiles/SwingCheese.mp3"));
        	  allSongsList.add(new Song("The Curtain Rises", "Kevin MacLeod", "0:28", "songfiles/TheCurtainRises.mp3"));
        	  allSongsList.add(new Song("UntameableFire", "Pierre Langer", "4:42", "songfiles/UntameableFire.mp3"));
        	  allSongsList.add(new Song("Running Out Of Reasons", "The Wanted", "songfiles/Running Out Of Reasons.mp3"));
        	  allSongsList.add(new Song("Wide Awake", "Alan Wake II", "songfiles/Wide Awake.mp3"));}
  }
  
  
  public ObservableList<Song> getList() {
	  return songsTableToDisplay;
  }
  
  public TableView<Song> getTable() {
	  return tableView;
  }
  
  public void addToQueue(Song selectedSong) {
      	allSongsList.add(selectedSong);
	updateList();
  }
  
  public void updateList () {
      if (!allSongsList.isEmpty())
	  songsTableToDisplay.setAll(allSongsList);
      else {
	  songsTableToDisplay.clear();
      }
      
  }
  
  public void update(PlayList newQueue) {
      songsTableToDisplay.clear();
      allSongsList.clear();
      for (int i = 0; i < newQueue.getQueue().size(); i++) {
          while (newQueue.getQueue().peek() != null) {
              String nextSong = newQueue.getFirst();
              newQueue.removeFirst();
              Song nextSongObj = new Song(nextSong);
              allSongsList.add(nextSongObj);
          }
      }
      updateList();
  }
  
  public void removeFirst() {
      if (!allSongsList.isEmpty()) {
	  allSongsList.remove(0);}
	updateList();
  }
 
}


 