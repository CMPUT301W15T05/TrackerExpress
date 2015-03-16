package group5.trackerexpress;


import android.content.Context;

/**
 * Singleton that provides access to Tags.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 * @see Tag
 */
public class TagController implements TController {

	
	/** The tags. */
	private TagMap tags;
	
	/** The instance. */
	private static TagController instance;
	
	/**
	 * Instantiates a new tag controller.
	 *
	 * @param context Needed for file IO
	 */
	private TagController(Context context){
		tags = new TagMap(context);
	}


	/**
	 * Gets the single instance of TagController.
	 *
	 * @param context Needed for file IO
	 * @return single instance of TagController
	 */
	public static TagController getInstance(Context context){
		if (instance == null){		
			instance = new TagController(context);
		}
		return instance;
	}


	/**
	 * Gets the tag map.
	 *
	 * @return the tag map
	 */
	public TagMap getTagMap(){
		return tags;
	}
}
