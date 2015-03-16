package group5.trackerexpress;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * The Class TModel.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class TModel implements Serializable{
    
    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The views. */
	protected transient List<TView> views;

    /**
     * Instantiates a new t model.
     */
    public TModel() {
    	views = new ArrayList<TView>();
    }
    
    /**
     * Make sure views isnt null.
     */
    protected void makeSureViewsIsntNull(){
    	if (views == null)
    		views = new ArrayList<TView>();
    }

    /**
     * Adds the view.
     *
     * @param view the view
     */
    public void addView(TView view) {
    	makeSureViewsIsntNull();
        if (!views.contains(view)) {
            views.add(view);	
        }
    }
    
    /**
     * Adds the views.
     *
     * @param views the views
     */
    public void addViews(List<TView> views){
    	makeSureViewsIsntNull();
    	for (TView view : views){
    		addView(view);
    	}
    }

    /**
     * Delete view.
     *
     * @param view the view
     */
    public void deleteView(TView view) {
    	makeSureViewsIsntNull();
        views.remove(view);
    }
    
    /**
     * Notify views.
     *
     * @param context the context
     */
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