package group5.trackerexpress;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class TModel {
    protected List<TView> views;

    public TModel() {
        views = new ArrayList<TView>();
    	}

    public void addView(TView view) {
        if (!views.contains(view)) {
            views.add(view);
        }
    }
    
    public void addViews(List<TView> views){
    	for (TView view : views){
    		addView(view);
    	}
    }

    public void deleteView(TView view) {
        views.remove(view);
    }
    
    public void notifyViews(Context context) {
		System.out.println ("Notifying views within notifyViews");
		System.out.println ("Starting view looping");
        for (TView view : views) {
			System.out.println ("Looping dem views");
        	view.update(this);
        }
		System.out.println ("View Looping finished");

        TagController.getInstance(context).getTagMap().saveData(context);
		System.out.println ("Tag controller good");
        ClaimController.getInstance(context).getClaimList().saveData(context);	
		System.out.println ("Claim controller good");	
        UserController.getInstance(context).getUser().saveData(context);	
		System.out.println ("User controller good");	
    }
    
    

}