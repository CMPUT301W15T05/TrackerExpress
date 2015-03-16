package group5.trackerexpress;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class ExpenseListFragment.
 */
public class ExpenseListFragment extends Fragment implements TView {

	/** The claim. */
	private Claim claim;
	
	/**
	 * Instantiates a new expense list fragment.
	 *
	 * @param claim the claim
	 */
	public ExpenseListFragment(Claim claim) {
		this.claim = claim;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_expense_list,
				container, false);
		
		TextView title = (TextView) rootView.findViewById(R.id.tv_expense_list_title);
		

		
		return rootView;
	}

	/* 
	 * @see group5.trackerexpress.TView#update(group5.trackerexpress.TModel)
	 */
	@Override
	public void update(TModel model) {
		// TODO Auto-generated method stub
		
	}

}
