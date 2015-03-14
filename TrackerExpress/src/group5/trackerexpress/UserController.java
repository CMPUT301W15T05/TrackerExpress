package group5.trackerexpress;


import android.content.Context;

public class UserController implements TController {

	
	private User user;
	private static UserController instance = null;
	
	private UserController(Context context) {
		System.out.println ("Making user");
		user = new User(context);
		System.out.println ("User making DONE");
	}


	public static UserController getInstance(Context context){
		if (instance == null){		
			System.out.println ("Making instance");
			instance = new UserController(context);
			System.out.println ("Instance making done!");
		}
		System.out.println ("Returning Predefined Instance");
		return instance;
	}


	public User getUser(){
		return user;
	}
	

}
