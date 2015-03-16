package group5.trackerexpress;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class TModel implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected transient List<TView> views;

    public TModel() {
    	views = new ArrayList<TView>();
    }
    
    protected void makeSureViewsIsntNull(){
    	if (views == null)
    		views = new ArrayList<TView>();
    }

    public void addView(TView view) {
    	makeSureViewsIsntNull();
        if (!views.contains(view)) {
            views.add(view);	
        }
    }
    
    public void addViews(List<TView> views){
    	makeSureViewsIsntNull();
    	for (TView view : views){
    		addView(view);
    	}
    }

    public void deleteView(TView view) {
    	makeSureViewsIsntNull();
        views.remove(view);
    }
    
    public void notifyViews(Context context) {
    	makeSureViewsIsntNull();

        for (TView view : views) {
        	view.update(this);
        }

        System.out.println ("Saving...");
        TagController.getInstance(context).getTagMap().saveData(context);
        ClaimController.getInstance(context).getClaimList().saveData(context);	
        UserController.getInstance(context).getUser().saveData(context);	
        System.out.println ("Saved.");

    }
    
    /*
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.views = new ArrayList<TView>();
    }
    */
}