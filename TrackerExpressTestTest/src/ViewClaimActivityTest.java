import java.util.ArrayList;

import group5.trackerexpress.Claim;
import group5.trackerexpress.R;
import group5.trackerexpress.ViewClaimActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ViewClaimActivityTest extends
		ActivityInstrumentationTestCase2<ViewClaimActivity> {
	
	public ViewClaimActivityTest() {
		super(ViewClaimActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void deleteExpenseTest (){
		ListView lv_before = (ListView) findViewById(R.id.lv_claims);
		ArrayAdapter<Claim> before = (ArrayAdapter<Claim>) lv_before.getAdapter();
	}
	
	public void editExpenseTest() {
		
	}
}
