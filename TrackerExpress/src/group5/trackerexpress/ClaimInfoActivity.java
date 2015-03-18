package group5.trackerexpress;

import java.util.Locale;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.Menu;

// TODO: Auto-generated Javadoc
/**
 * The Class ClaimInfoActivity.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class ClaimInfoActivity extends ActionBarActivity {

	/** The Constant INDEX_OF_VIEW_CLAIM_TAB. */
	private static final int INDEX_OF_VIEW_CLAIM_TAB = 0;
	
	/** The Constant INDEX_OF_EXPENSE_LIST_TAB. */
	private static final int INDEX_OF_EXPENSE_LIST_TAB = 1;

	private static Context instance;
	private Claim claim;
	
	/* (non-Javadoc)
	 * @see group5.trackerexpress.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim_info);

		instance = this;

		final Intent intent = this.getIntent();
    	UUID serializedId = (UUID) intent.getSerializableExtra("claimUUID");

	    claim = Controller.getClaimList(this).getClaim(serializedId);
	    
	    
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		SectionsPagerAdapter mSectionsPagerAdapter = new ClaimInfoPagerAdapter(
				getSupportFragmentManager());
		
		setUpActionBar(mSectionsPagerAdapter, R.id.pager_activity_claim_info);
	}

	/* (non-Javadoc)
	 * @see group5.trackerexpress.ActionBarActivity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.claim_info, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	private class ClaimInfoPagerAdapter extends SectionsPagerAdapter {

		/**
		 * Instantiates a new claim info pager adapter.
		 *
		 * @param fm the fm
		 */
		public ClaimInfoPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		/* (non-Javadoc)
		 * @see group5.trackerexpress.SectionsPagerAdapter#getItem(int)
		 */
		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			Fragment fragment = null;
			Bundle args = new Bundle();
			
			switch( position ){
				case INDEX_OF_VIEW_CLAIM_TAB: 
					fragment = new ViewClaimFragment(claim);
					break;
				case INDEX_OF_EXPENSE_LIST_TAB:
					fragment = new ExpenseListFragment(claim);
					break;
				default: Log.i("myMessage", "This should never happen");
			}
		
			fragment.setArguments(args);
			
			return fragment;//PlaceholderFragment.newInstance(position + 1);
		}

		/* (non-Javadoc)
		 * @see group5.trackerexpress.SectionsPagerAdapter#getCount()
		 */
		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case INDEX_OF_VIEW_CLAIM_TAB:
				return getString(R.string.title_section1_claim_info).toUpperCase(l);
			case INDEX_OF_EXPENSE_LIST_TAB:
				return getString(R.string.title_section2_claim_expenses).toUpperCase(l);
			}
			return null;
		}
	}
	

	public static Context getThis() {
		return instance;
	}

}
