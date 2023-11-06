/**
 * Manages the queue
 * @authors: Hengsocheat Pok, Parker Hines
 */



package model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

/*
 * PlayList class, uses a Queue to maintain and access elements of the current song play list.
 * Serializable for persistence.
 */
public class PlayList implements Serializable {
	Queue<String> playList;

	/**
	 * constructor
	 */
	public PlayList() {
		this.playList = new LinkedList<>();
	}

	/**
	 * adds a song to the end of the queue
	 * 
	 * @param element: path to the next song to be queued
	 */
	public void queueUpNextSong(String element) {
	    	System.out.println(element);
		playList.add(element);
	}

	/**
	 * removes the first element of the list (next song).
	 */
	public void removeFirst() {
		playList.poll();
	}

	/**
	 * gets the first element in the list (next song)
	 * 
	 * @return next song in queue
	 */
	public String getFirst() {
		return playList.peek();

	}
	
	public Queue<String> getQueue() {
	    return playList;
	}
}
