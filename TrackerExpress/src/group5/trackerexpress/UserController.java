package group5.trackerexpress;


import android.content.Context;

public class UserController implements TController {

	
	private User user;
	private static UserController instance;
	
	private UserController(Context context){
		user = new User(context);
	}


	public static UserController getInstance(Context context){
		if (instance == null){		
			instance = new UserController(context);
		}
		return instance;
	}


	public User getUser(){
		return user;
	}
	

}
