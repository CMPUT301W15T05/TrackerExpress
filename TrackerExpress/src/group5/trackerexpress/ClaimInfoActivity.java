package group5.trackerexpress;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Menu;

/**
 * Displays information about claims. 
 * Contains a fragment for the expense list and the claim property view.
 * 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class ClaimInfoActivity extends ActionBarActivity {

	/** The Constant INDEX_OF_VIEW_CLAIM_TAB. */
	private static final int INDEX_OF_VIEW_CLAIM_TAB = 0;
	
	/** The Constant INDEX_OF_EXPENSE_LIST_TAB. */
	private static final int INDEX_OF_EXPENSE_LIST_TAB = 1;
	
	/** the claim to be viewed */
	private Claim claim;
	
	/** 
	 * 	tells the expense list fragment if it should
	 *  be able to add expenses (the version of claim list)
	 */
	private boolean myClaimListVersion;
	
	/**
	 * Sets up the tabs and their titles
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim_info);

		final Intent intent = this.getIntent();
		
		/** gets the claim in question's id to retrieve the claim from the controller */
		UUID serializedId = (UUID) intent.getSerializableExtra("claimUUID");
		
		/** Runs activity like MyClaims tab would like if there is a special intent extra */
		myClaimListVersion = intent.getBooleanExtra("fromMyClaims", false);
				
		// Determines where to get the claim from
		if ( myClaimListVersion ){
			// Get claim from Controller if MyClaims tab called
	    	claim = Controller.getClaim(this, serializedId);
		} else {
			// Get claim from Elastic Search if GlobalClaims tab called
			try {
				claim = (new ElasticSearchEngineClaims()).getClaim(ClaimInfoActivity.this, serializedId);
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	    
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		SectionsPagerAdapter mSectionsPagerAdapter = new ClaimInfoPagerAdapter(
				getSupportFragmentManager());
		
		setUpActionBar(mSectionsPagerAdapter, R.id.pager_activity_claim_info);
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

		/**
		 * Display fragment depending on index of fragment called
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
					fragment = new ExpenseListFragment(claim, myClaimListVersion);
					break;
				default: break;
			}
		
			fragment.setArguments(args);
			
			return fragment;
		}

		/**
		 * Simply returns the number of tabs of this activity
		 * 
		 * @return number of tabs of this activity
		 */
		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		/**
		 * Sets the proper page title based on index of current tab
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

}
