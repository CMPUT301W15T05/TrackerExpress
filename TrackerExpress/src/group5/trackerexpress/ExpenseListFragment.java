package group5.trackerexpress;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ExpenseListFragment extends Fragment implements TView {

	private Claim claim;
	
	public ExpenseListFragment(Claim claim) {
		this.claim = claim;
	}

	public ExpenseListFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_expense_list,
				container, false);
		
		return rootView;
	}

	@Override
	public void update(TModel model) {
		// TODO Auto-generated method stub
		
	}

}
