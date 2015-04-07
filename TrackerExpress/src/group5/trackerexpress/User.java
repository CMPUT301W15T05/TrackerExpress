package group5.trackerexpress;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.location.Location;

/**
 * Stores data about the current user. Only a single user may exist on a phone. 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class User extends TModel{
	
	/**
	 * generated serial number for Serializable type
	 */
	private static final long serialVersionUID = -2978019878882065454L;

	/** The email of the user. */
	private String email;
	
	/** The password of the user. */
	private String password;
	
	/** The name of the user. */
	private String name;
	
	/** The SIGNED_IN boolean to check if user is signed in. */
	private Boolean SIGNED_IN; // true if the user was signed in
	
	/** The Constant FILENAME for saving. */
	private static final String FILENAME = "user.sav";
	
	/** The location of the user */
	private Location location;
	
	/**
	 * Instantiates a new user.
	 *
	 * @param context Needed for file IO
	 */
	public User(Context context) {
		loadData(context);
		System.out.println("Getting the new user");
	}
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the email.
	 *
	 * @param context Needed for file IO
	 * @param email the email
	 */
	public void setEmail(Context context, String email) {
		this.email = email;
		notifyViews(context);
	}
	
	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Sets the password.
	 *
	 * @param context Needed for file IO
	 * @param password the password
	 */
	public void setPassword(Context context, String password) {
		this.password = password;
		notifyViews(context);
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param context Needed for file IO
	 * @param name the name
	 */
	public void setName(Context context, String name) {
		System.out.println ("Setting name");
		this.name = name;
		System.out.println ("Notifying views about name");
		notifyViews(context);
		System.out.println ("Views notified about name");
	}
	
	/**
	 * Save data.
	 *
	 * @param context Needed for file IO
	 */
	public void saveData(Context context) {
		try {
			new FileCourrier<User>(this).saveFile(context, FILENAME, this);
		} catch (IOException e) {
			System.err.println ("Could not save user.");
			throw new RuntimeException();
		}
	}

	/**
	 * Load data.
	 *
	 * @param context Needed for file IO
	 */
	public void loadData(Context context) {
		User user;
		try {
			System.out.println ("FileCourrier start");
			user = new FileCourrier<User>(this).loadFile(context, FILENAME);
			
			System.out.println ("FileCourrier end");
			this.email = user.getEmail();
			this.password = user.getPassword();
			this.name = user.getName();
			this.SIGNED_IN = user.isSignedIn();
		} catch (FileNotFoundException e) {
			System.err.println ("File doesnt exist.");
			this.email = "em2@example.com";
			this.password = "password";
			this.name = "Em2";
			this.SIGNED_IN = true;
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * Sets the signed in boolean.
	 *
	 * @param signedIn the new signed in
	 */
	public void setSignedIn(Context context, boolean signedIn) {
		this.SIGNED_IN = signedIn;
		notifyViews(context);
	}
	
	/**
	 * Checks if user is signed in.
	 *
	 * @return true, if is signed in
	 */
	public boolean isSignedIn() {
		return SIGNED_IN;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
