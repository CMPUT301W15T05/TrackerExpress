package group5.trackerexpress;


import android.content.Context;

/**
 * Singleton that provides access to User model.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 * @see User
 */
public class UserController implements TController {

	
	/** The user. */
	private User user;
	
	/** The instance. */
	private static UserController instance = null;
	
	/**
	 * Instantiates a new user controller.
	 *
	 * @param context Needed for file IO
	 */
	private UserController(Context context) {
		System.out.println ("Making user");
		user = new User(context);
		System.out.println ("User making DONE");
	}


	/**
	 * Gets the single instance of UserController.
	 *
	 * @param context Needed for file IO
	 * @return single instance of UserController
	 */
	public static UserController getInstance(Context context){
		if (instance == null){		
			System.out.println ("Making instance");
			instance = new UserController(context);
			System.out.println ("Instance making done!");
		}
		System.out.println ("Returning Predefined Instance");
		return instance;
	}


	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser(){
		return user;
	}
	

}
