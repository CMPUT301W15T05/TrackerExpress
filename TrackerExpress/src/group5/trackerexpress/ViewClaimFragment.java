package group5.trackerexpress;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ViewClaimFragment extends Fragment implements TView {

	private Claim claim;
	
	public ViewClaimFragment(Claim claim) {
		this.claim = claim;
	}

	public ViewClaimFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ScrollView sv = new ScrollView(ClaimInfoActivity.getThis());
		LinearLayout ll = new LinearLayout(ClaimInfoActivity.getThis());
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);
		
		TextView tv = new TextView(ClaimInfoActivity.getThis());
		tv.setText("Dynamic layouts ftw!");
		ll.addView(tv);
		
		EditText et = new EditText(ClaimInfoActivity.getThis());
		et.setText("weeeeeeeeeee~!");
		ll.addView(et);
		
		Button b = new Button(ClaimInfoActivity.getThis());
		b.setText("I don't do anything, but I was added dynamically. :)");
		ll.addView(b);
		
		for(int i = 0; i < 20; i++) {
			CheckBox cb = new CheckBox(ClaimInfoActivity.getThis());
			cb.setText("I'm dynamic!");
			ll.addView(cb);
		}
		((Activity) ClaimInfoActivity.getThis()).setContentView(sv);
		
		View rootView = inflater.inflate(R.layout.fragment_view_claim,
				container, false);
		
		return rootView;
	}

	@Override
	public void update(TModel model) {
		// TODO Auto-generated method stub
		
	}
}