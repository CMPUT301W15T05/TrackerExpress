package group5.trackerexpress;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * The Class User.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class User extends TModel{
	
	/** The email of the user. */
	private String email;
	
	/** The password of the user. */
	private String password;
	
	/** The name of the user. */
	private String name;
	
	/** The SIGNED_IN boolean to check if user is signed in. */
	private boolean SIGNED_IN; // true if the user was signed in
	
	/** The Constant FILENAME for saving. */
	private static final String FILENAME = "user.sav";
	
	/**
	 * Instantiates a new user.
	 *
	 * @param context the context
	 */
	public User(Context context) {
		System.out.println ("New User Start GOOD");
		loadData(context);
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
	 * @param context the context
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
	 * @param context the context
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
	 * @param context the context
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
	 * @param context the context
	 */
	public void saveData(Context context) {
		try {
			new FileCourrier<User>(this).saveFile(context, FILENAME, this);
		} catch (IOException e) {
			System.err.println ("Could not save claims.");
			throw new RuntimeException();
		}
	}

	/**
	 * Load data.
	 *
	 * @param context the context
	 */
	public void loadData(Context context) {
		User user;
		try {
			System.out.println ("FileCourrier start");
			user = new FileCourrier<User>(this).loadFile(context, FILENAME);
			//user = new FileCourrier<User>().loadFile(context, FILENAME);
			
			
			System.out.println ("FileCourrier end");
			this.email = user.getEmail();
			this.password = user.getPassword();
			this.name = user.getName();
		} catch (FileNotFoundException e) {
			System.err.println ("File doesnt exist.");
		} catch (NullPointerException e) {
			System.err.println ("Null pointer exception.");
		} catch (IOException e) {
			System.err.println ("No user data found.");
		} catch (Exception e) {
			System.out.println (e.getClass().toString());
		}
	}
	
	/**
	 * Sets the signed in boolean.
	 *
	 * @param signedIn the new signed in
	 */
	public void setSignedIn(boolean signedIn) {
		this.SIGNED_IN = signedIn;
	}
	
	/**
	 * Checks if user is signed in.
	 *
	 * @return true, if is signed in
	 */
	public boolean isSignedIn() {
		return SIGNED_IN;
	}

}
