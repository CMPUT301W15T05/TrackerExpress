package group5.trackerexpress;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.Menu;

public class ClaimInfoActivity extends ActionBarActivity {

	private static final int INDEX_OF_VIEW_CLAIM_TAB = 0;
	private static final int INDEX_OF_EXPENSE_LIST_TAB = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim_info);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		SectionsPagerAdapter mSectionsPagerAdapter = new ClaimInfoPagerAdapter(
				getSupportFragmentManager());
		
		setUpActionBar(mSectionsPagerAdapter, R.id.pager_activity_claim_info);
	}

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

		public ClaimInfoPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			Fragment fragment = null;
			Bundle args = new Bundle();
			
			switch( position ){
				case INDEX_OF_VIEW_CLAIM_TAB: 
					fragment = new ViewClaimFragment();
					break;
				case INDEX_OF_EXPENSE_LIST_TAB:
					fragment = new ExpenseListFragment();
					break;
				default: Log.i("myMessage", "This should never happen");
			}
		
			fragment.setArguments(args);
			
			return fragment;//PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

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
