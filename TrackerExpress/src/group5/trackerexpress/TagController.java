package group5.trackerexpress;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;

public class TagController implements TController {

	
	private TagMap tags;
	private static TagController instance;
	
	private TagController(Context context){
		tags = new TagMap(context);
	}


	public static TagController getInstance(Context context){
		if (instance == null){		
			instance = new TagController(context);
		}
		return instance;
	}


	public TagMap getTagMap(){
		return tags;
	}
}
