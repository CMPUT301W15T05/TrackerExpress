package group5.trackerexpress;

import java.util.ArrayList;
import java.util.List;

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

    public void notifyViews() {
        for (TView view : views) {
        	view.update(this);
        }
    }
}