package group5.trackerexpress;

import java.util.ArrayList;

import android.os.Handler;

/**
 * To be use with caution (Multithreading errors may ensue.
 * Main purpose of this class is to update views 
 * such as the Global Claim Fragment
 * which are not updated by models.
 * 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen,
 *         Rishi Barnwal
 *
 */
public class PeriodicTViewUpdater {
	private static final int CHECKING_INTERVAL = 3000;
	
	private Runnable mStatusChecker;
	private Handler mHandler;
	private int mInterval;
	
	private ArrayList<TView> views;
	
	public PeriodicTViewUpdater(){
		mStatusChecker = new Runnable() {
			@Override 
		    public void run() {
				for ( TView v : views ){
					v.update(null);
				}
				mHandler.postDelayed(mStatusChecker, mInterval);
		    }
		};
		mHandler = new Handler();
		mInterval = CHECKING_INTERVAL;
		views = new ArrayList<TView>();
	}

	public void addView(TView view){
		views.add(view);
	}
	
	public void startRepeatingTask() {
		mStatusChecker.run(); 
	}

	public void stopRepeatingTask() {
		mHandler.removeCallbacks(mStatusChecker);
	}
}
