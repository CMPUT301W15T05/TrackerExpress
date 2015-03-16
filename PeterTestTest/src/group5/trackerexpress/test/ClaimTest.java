package group5.trackerexpress.test;

import group5.trackerexpress.Claim;
import group5.trackerexpress.MainActivity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

public class ClaimTest extends ActivityInstrumentationTestCase2<MainActivity> {
	Context context;
	
	public ClaimTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		context = getActivity();
	}

	public void testToStringDestinations(){
		Claim testClaim = new Claim( "Claim1" );
		for ( int i = 0;  i < 6; i++ ){
			testClaim.addDestination(context, Integer.toString(i), Integer.toString(i+1));
		}
		String shouldLookLike = "0, 1, 2, 3, 4, 5";
		assertEquals( "Should be equal", shouldLookLike, testClaim.toStringDestinations() );
		
		testClaim = new Claim( "Claim1" );
		testClaim.addDestination(context, "1", "Reason");
		shouldLookLike = "1";
		assertEquals( "Should be equal", shouldLookLike, testClaim.toStringDestinations() );
	}
	
}
