package group5.trackerexpress;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * The Class SectionsPagerAdapter. Allows for tabbed interfaces.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public abstract class SectionsPagerAdapter extends FragmentPagerAdapter {

	/**
	 * Instantiates a new sections pager adapter.
	 *
	 * @param fm the fm
	 */
	public SectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	public abstract Fragment getItem(int arg0);

	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	public abstract int getCount();
}
