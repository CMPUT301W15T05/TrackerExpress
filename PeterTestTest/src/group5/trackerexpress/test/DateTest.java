package group5.trackerexpress.test;

import group5.trackerexpress.Date;
import group5.trackerexpress.MainActivity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

public class DateTest extends ActivityInstrumentationTestCase2<MainActivity> {
	Context context;
	
	public DateTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		context = getActivity();
	}
	

	public void testDateCompareToEqual() {
		Date d1 = new Date( 1, 1, 1995 );
		Date d2 = new Date( 1, 1, 1995 );
		
		assertTrue("Dates should be equal", d1.compareTo(d2) == 0 );
	}
	
	public void testDateCompareToLater() {
		Date d1 = new Date( 2, 1, 1995 );
		Date d2 = new Date( 1, 1, 1995 );
		
		assertTrue ( "Date 1 should be later", d1.compareTo(d2) > 0 );
	}
	
	public void testDateCompareToEarlier() {
		Date d1 = new Date( 2, 1, 1995 );
		Date d2 = new Date( 1, 1, 1995 );
		
		assertTrue ( "Date 1 should be later", d2.compareTo(d1) < 0 );
	}
	
	public void testDateNull() {
		Date d1 = null;
		Date d2 = new Date( 1, 1, 1995 );
		
		assertTrue ( "Date 1 should not equal Date 2", d2.compareTo(d1) > 0 );
	}

}
