package group5.trackerexpress;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
		
		View rootView = inflater.inflate(R.layout.fragment_view_claim,
				container, false);
		
		return rootView;
	}

	@Override
	public void update(TModel model) {
		// TODO Auto-generated method stub
		
	}
}