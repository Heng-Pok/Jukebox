/**
 * Song class: acts as a song
 * @authors: Hengsocheat Pok, Parker Hines
 */

package model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class Song {
	private String title;
	private String artist;
	private String time;
	private String path;
	private int[] duration = new int[2];
	private boolean stillPlaying = false;
	//private ObservableList<Song> songsTableToDisplay;
	
	/**
	 * constructor
	 * @param title: title of song
	 * @param artist: artist of song
	 * @param time: length of song in format 0:00
	 * @param path: file path to song for program use
	 */
	public Song(String title, String artist, String time, String path) {
		this.title = title;
		this.artist = artist;
		this.time = time;
		this.path = path;
		
	}

	
	public interface DurationCallback {
	        void onDurationReceived(int minutes, int seconds);
	    }

	public static void getSongLength(String filePath, DurationCallback callback) {
	        Media media = new Media(new File(filePath).toURI().toString());
	        MediaPlayer mediaPlayer = new MediaPlayer(media);

	        // Add a listener to wait until the metadata is loaded
	        media.setOnError(() -> {
	            System.out.println("Error occurred while loading media.");
	        });

	        mediaPlayer.setOnReady(() -> {
	            // Get the duration of the song in seconds
	            double durationInSeconds = media.getDuration().toSeconds();
	            int minutes = (int) durationInSeconds / 60;
	            int seconds = (int) durationInSeconds % 60;

	            // Release resources after getting the duration
	            mediaPlayer.dispose();

	            // Call the callback with the duration in minutes and seconds
	            callback.onDurationReceived(minutes, seconds);
	        });
	        
	    }
	
	public Song(String title, String artist, String path) {
		this.title = title;
		this.artist = artist;
		this.path = path;
		getSongLength(path, (minutes, seconds) -> {
			   //System.out.printf("time: %d:%02d\n", minutes, seconds);
			    this.time = String.format("%d:%02d", minutes, seconds);
		});
	}
	
	public Song (String path) {
	        if (path.equals("songfiles/Capture.mp3")) {
	            this.title = "Pokemon Capture";
	            this.artist = "Pikachu";
	            this.time = "0:05";
	            this.path = path;
	        }
	        if (path.equals("songfiles/DanseMacabreViolinHook.mp3")) {
	            this.title = "Danse Macabre";
	            this.artist = "Kevin MacLeod";
	            this.time = "0:34";
	            this.path = path;
	        }
	        if (path.equals("songfiles/DeterminedTumbao.mp3")) {
	            this.title = "Determined Tumbao";
	            this.artist = "FreePlay Music";
	            this.time = "0:20";
	            this.path = path;
	        }
	        if (path.equals("songfiles/LopingSting.mp3")) {
	            this.title = "LopingSting";
	            this.artist = "Kevin MacLeod";
	            this.time = "0:05";
	            this.path = path;
	        }
	        if (path.equals("songfiles/SwingCheese.mp3")) {
	            this.title = "Swing Cheese";
	            this.artist = "FreePlay Music";
	            this.time = "0:15";
	            this.path = path;
	        }
	        if (path.equals("songfiles/TheCurtainRises.mp3")) {
	            this.title = "The Curtain Rises";
	            this.artist = "Kevin MacLeod";
	            this.time = "0:28";
	            this.path = path;
	        }
	        if (path.equals("songfiles/UntameableFire.mp3")) {
	            this.title = "UntameableFire";
	            this.artist = "Pierre Langer";
	            this.time = "4:42";
	            this.path = path;
	        }
	        else {
	            	this.path = path;
	            	this.title = path.replace("songfiles\\", "");
	            	this.title = this.title.replace(".mp3", "");
			this.artist = "unknown";
			getSongLength(path, (minutes, seconds) -> {
				   //System.out.printf("time: %d:%02d\n", minutes, seconds);
				    this.time = String.format("%d:%02d", minutes, seconds);
			});
	          
	        }
	}
	
	private class CalculateSongLength implements Runnable {
	    	public String path;
	    	public String time;

	        public CalculateSongLength(String path) {
	            this.path = path;
	        }
	    	@Override
	    	public void run() {
	    	getSongLength(path, (minutes, seconds) -> {
			   System.out.printf("time: %d:%02d\n", minutes, seconds);
			    this.time = String.format("%d:%02d", minutes, seconds);
		});
	    	}
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getArtist() {
		return this.artist;
	}

	public String getTime() {
		return this.time;
	}
	
	public String getPath() {
		return this.path;
	}
}
