package group5.trackerexpress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.content.Context;

public class TModel {
    private List<TView> views;

    public TModel() {
        views = new ArrayList<TView>();
    	}

    public void addView(TView view) {
        if (!views.contains(view)) {
            views.add(view);
        }
    }

    public void deleteView(TView view) {
        views.remove(view);
    }

    public void notifyViews(Context context) {
        for (TView view : views) {
        	view.update(this);
        }
        
        TagController.getInstance(context).getTagMap().saveData(context);
        ClaimController.getInstance(context).getClaimList().saveData(context);		
        UserController.getInstance(context).getUser().saveData(context);		
    }
    
    

}