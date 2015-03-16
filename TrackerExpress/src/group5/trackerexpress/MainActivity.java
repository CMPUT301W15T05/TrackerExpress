package group5.trackerexpress;

import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.Menu;

// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class MainActivity extends ActionBarActivity {

	/** The Constant INDEX_OF_MY_CLAIMS_TAB. */
	private static final int INDEX_OF_MY_CLAIMS_TAB = 0;
	
	/** The Constant INDEX_OF_TAGS_TAB. */
	private static final int INDEX_OF_TAGS_TAB = 1;
	
	/** The Constant INDEX_OF_GLOBAL_CLAIMS_TAB. */
	private static final int INDEX_OF_GLOBAL_CLAIMS_TAB = 2;

	/* (non-Javadoc)
	 * @see group5.trackerexpress.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		SectionsPagerAdapter mSectionsPagerAdapter = new MainPagerAdapter(
				getSupportFragmentManager());
		
		setUpActionBar(mSectionsPagerAdapter, R.id.pager_activity_main);
	}

	/* (non-Javadoc)
	 * @see group5.trackerexpress.ActionBarActivity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	private class MainPagerAdapter extends SectionsPagerAdapter {

		/**
		 * Instantiates a new main pager adapter.
		 *
		 * @param fm the fm
		 */
		public MainPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		/* (non-Javadoc)
		 * @see group5.trackerexpress.SectionsPagerAdapter#getItem(int)
		 */
		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			Bundle args = new Bundle();
			
			switch( position ){
				case INDEX_OF_MY_CLAIMS_TAB: 
					fragment = new MyClaimsFragment();
					break;
				case INDEX_OF_TAGS_TAB:
					fragment = new TagListFragment();
					break;
				case INDEX_OF_GLOBAL_CLAIMS_TAB:
					fragment = new GlobalClaimsFragment();
					break;
				default: Log.i("myMessage", "This should never happen");
			}
			
			fragment.setArguments(args);

			return fragment;
		}

		/* (non-Javadoc)
		 * @see group5.trackerexpress.SectionsPagerAdapter#getCount()
		 */
		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case INDEX_OF_MY_CLAIMS_TAB:
				return getString(R.string.title_section1_my_claims).toUpperCase(l);
			case INDEX_OF_TAGS_TAB:
				return getString(R.string.title_section2_tag_list).toUpperCase(l);
			case INDEX_OF_GLOBAL_CLAIMS_TAB:
				return getString(R.string.title_section3_global_claims).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * Gets the this.
	 *
	 * @return the this
	 */
	public Context getThis() {
		return this;
	}

}
		