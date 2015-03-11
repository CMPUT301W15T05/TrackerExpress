package group5.trackerexpress;

import java.io.IOException;
import android.content.Context;

public class User extends TModel{
	private String email;
	private String password;
	private String name;
	
	private static final String FILENAME = "user.sav";
	
	public User(Context context) {
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
		this.name = name;
		notifyViews(context);
	}
	
	
	
	public void saveData(Context context) {
		try {
			new FileCourrier<User>().saveFile(context, FILENAME, this);
		} catch (IOException e) {
			System.err.println ("Could not save claims.");
			throw new RuntimeException();
		}
	}

	public void loadData(Context context) {
		User user;
		try {
			user = new FileCourrier<User>().loadFile(context, FILENAME);
			this.email = user.getEmail();
			this.password = user.getPassword();
			this.name = user.getName();
		} catch (IOException e) {
			System.err.println ("No user data found.");
		}
	}

}
