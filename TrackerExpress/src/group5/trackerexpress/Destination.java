package group5.trackerexpress;

import java.io.Serializable;

import android.location.Location;

/**
 * The Destination class holds all the necessary information
 * for a complete destination
 * 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class Destination {
	
	private static final long serialVersionUID = 1L;
	
	/** The name of the destination */
	private String name;
	
	/** The coordinates of the destination */
	private Location location;
	
	/** The description of the destination */
	private String description;
	
	/** Constructor to make an empty destination */
	public Destination(){
		location = new Location("");
	}
	
	/** 
	 * gets the name of the destination
	 * 
	 * @return the name of the destination
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * sets the name of the destination
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * gets the description of the destination
	 * 
	 * @return description of destination
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * sets the description of the destination
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * sets the longitude of destination
	 * 
	 * @param lon: longitude
	 */
	public void setLongitude(double lon){
		location.setLongitude(lon);
	}
	
	/**
	 * sets the latitude of destination
	 * 
	 * @param lat: latitude
	 */
	public void setLatitude(double lat){
		location.setLatitude(lat);
	}
	
	/**
	 * gets the longitude of destination
	 * 
	 * @return longitude of destination
	 */
	public double getLongitude(){
		return location.getLongitude();
	}
	
	/**
	 * gets the latitude of destination
	 * 
	 * @return latitude of destination
	 */
	public double getLatitude(){
		return location.getLatitude();
	}

	/**
	 * gets the location object of the destination
	 * 
	 * @return location object
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * sets the location object of the destination
	 * 
	 * @param location
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	
	/**
	 * string information about the destination
	 * 
	 * @return name and description info
	 */
	public String toString(){
		return name + " - " + description;
	}
	
	/**
	 * gets the distance between two destinations
	 * 
	 * @param d1: first destination
	 * @param d2: second destination
	 * @return distance between d1 and d2
	 */
	public static double getDistanceBetween( Destination d1, Destination d2 ){
		return d1.getLocation().distanceTo(d2.getLocation());
	}
	
}
