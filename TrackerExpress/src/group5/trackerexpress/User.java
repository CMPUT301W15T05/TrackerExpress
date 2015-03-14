package group5.trackerexpress;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;

public class User extends TModel{
	private String email;
	private String password;
	private String name;
	
	private static final String FILENAME = "user.sav";
	
	public User(Context context) {
		System.out.println ("New User Start GOOD");
		loadData(context);
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(Context context, String email) {
		this.email = email;
		notifyViews(context);
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(Context context, String password) {
		this.password = password;
		notifyViews(context);
	}
	public String getName() {
		return name;
	}
	public void setName(Context context, String name) {
		System.out.println ("Setting name");
		this.name = name;
		System.out.println ("Notifying views about name");
		notifyViews(context);
		System.out.println ("Views notified about name");
	}
	
	public void saveData(Context context) {
		try {
			new FileCourrier<User>(this).saveFile(context, FILENAME, this);
		} catch (IOException e) {
			System.err.println ("Could not save claims.");
			throw new RuntimeException();
		}
	}

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

}
