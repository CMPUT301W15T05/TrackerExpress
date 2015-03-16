package group5.trackerexpress;


import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * The Class TagController.
 */
public class TagController implements TController {

	
	/** The tags. */
	private TagMap tags;
	
	/** The instance. */
	private static TagController instance;
	
	/**
	 * Instantiates a new tag controller.
	 *
	 * @param context the context
	 */
	private TagController(Context context){
		tags = new TagMap(context);
	}


	/**
	 * Gets the single instance of TagController.
	 *
	 * @param context the context
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
