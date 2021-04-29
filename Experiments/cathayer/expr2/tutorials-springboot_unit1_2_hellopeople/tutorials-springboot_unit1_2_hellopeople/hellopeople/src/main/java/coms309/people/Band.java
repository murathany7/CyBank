package coms309.people;

/**
 * 
 * Class used to test and see if I can control two seperate lists
 * 
 * @author Cutler Thayer
 *
 */
public class Band {

	public String name;
	
	public String artist;
	
	public String song;
	
	public Band() {
		
	}
	
	public Band(String name, String artist, String song) {
		this.name = name;
		this.artist = artist;
		this.song = song;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getSong() {
		return song;
	}
	
	public void setSong(String song) {
		this.song = song;
	}
	
	public String concertString() {
		return "Watch " + artist + " of " + name + " play " + song + " very soon!";
	}
}
