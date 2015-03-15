package group5.trackerexpress;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GlobalClaimsFragment extends Fragment implements TView {

	public GlobalClaimsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_global_claims,
				container, false);
		return rootView;
	}

	@Override
	public void update(TModel model) {
		// TODO Auto-generated method stub
		
	}
}