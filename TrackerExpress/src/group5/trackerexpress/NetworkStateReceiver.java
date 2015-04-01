package group5.trackerexpress;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

/**
 * NetworkStateReceiver's sole responsibility is to act as a 
 * network change listener. Views may add themselves to this
 * class so that they will be notified when to update.
 * 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 5
 */
public class NetworkStateReceiver extends BroadcastReceiver {	
    private static ArrayList<TView> views;
    
	@Override
    public void onReceive(final Context context, final Intent intent) {
    	
        if (intent.getExtras() != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

            if (ni != null && ni.isConnectedOrConnecting()) {
            	Toast.makeText(context, "network connected",
            			   Toast.LENGTH_LONG).show();
            	updateViews();
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            	Toast.makeText(context, "no network connectivity",
            			   Toast.LENGTH_LONG).show();
            	updateViews();
            }
        }
    }
	
	public static void addView(TView v){
		if ( views == null ){
			views = new ArrayList<TView>();
		}
		
		views.add(v);
	}
	
	private void updateViews(){
		for ( TView v : views ){
			v.update(null);
		}
	}
}
