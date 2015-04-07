package group5.trackerexpress;

import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;

/**
 * The Class MainActivity. Has three tabs: A list of all claims made by user, a list of 
 * all Tags, and a list of other people's claims for approval.
 * 
 * 
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

		final ViewPager mViewPager = (ViewPager) findViewById(R.id.pager_activity_main);
		
		// Keeps the fragments updated
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

		     @Override
		     public void onPageSelected(int position){
		    	 Fragment visible = getVisibleFragment();
		    	 getActionBar().setSelectedNavigationItem(position);
		    	 ((TView)visible).update(null);
		     }

		     @Override
		     public void onPageScrolled(int arg0, float arg1, int arg2) {}
		     @Override
		     public void onPageScrollStateChanged(int arg0) {}
		  });
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
	 * Returns the fragment currently on screen
	 * 
	 * @return Fragment currently visible
	 */
	public Fragment getVisibleFragment(){
	    FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
	    List<Fragment> fragments = fragmentManager.getFragments();
	    for(Fragment fragment : fragments){
	        if(fragment != null && fragment.isVisible())
	            return fragment;
	    }
	    return null;
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

}
		