package group5.trackerexpress;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Displays the list of claims made by the current user.
 * 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class MyClaimsFragment extends Fragment implements TView {

	/** The ListView of claims */
	private ListView lv_claim_list;
	
	/** The button to Add Claim */
	private Button b_add_claim;
	
	/** The textview that shows if the internet is disconnected **/
	private TextView tv_has_internet;
	
	/** The menu items to hide in the options menu for clicked claims. */
	private static final int[] submittedOrApprovedHiddenItems = {R.id.op_edit_claim, R.id.op_submit_claim, R.id.op_delete_claim, R.id.op_add_expense};
	
	/** Empty fragment constructor. */
	public MyClaimsFragment() {
	}
	
	/** onCreateView
	 ** @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 **/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		View rootView = inflater.inflate(R.layout.fragment_my_claims,
				container, false);
		
		// Fragment's views
		lv_claim_list = (ListView) rootView.findViewById(R.id.lv_my_claims);
		lv_claim_list.setItemsCanFocus(true);
		b_add_claim = (Button) rootView.findViewById(R.id.b_add_claim);
		tv_has_internet = (TextView) rootView.findViewById(R.id.tv_internet_status);
		
		Controller.updateClaimsFromInternet(getActivity());
		update(null);
		Controller.getClaimList(getActivity()).addView(this);
		NetworkStateReceiver.addView(this);
		
		b_add_claim.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent( getActivity(), EditClaimActivity.class );
				intent.putExtra("isNewClaim", true);
				startActivity(intent);
			}
		});
		
		lv_claim_list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> a, View v,
					final int position, long arg3) {
				// TODO Auto-generated method stub
				
				final Claim clickedOnClaim = (Claim) lv_claim_list.getAdapter().getItem(position);
				
				PopupMenu popup = new PopupMenu(getActivity(), v);
				popup.getMenuInflater().inflate(R.menu.my_claims_popup, popup.getMenu());
				
				onPrepareOptionsMenu(popup, clickedOnClaim);
				
				// Popup menu item click listener
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					
					@Override
                    public boolean onMenuItemClick(MenuItem item) {
                    	Intent intent;
                        switch(item.getItemId()){
                        case R.id.op_delete_claim: 
                        	Controller.getClaimList(getActivity()).deleteClaim(getActivity(), clickedOnClaim.getUuid());
                        	break;
                        case R.id.op_edit_tags:
                        case R.id.op_edit_claim:
                        	intent = new Intent( getActivity(), EditClaimActivity.class );
                        	intent.putExtra( "isNewClaim", false );
                        	intent.putExtra("claimUUID", clickedOnClaim.getUuid());
                        	startActivity(intent);
                        	break;
                        case R.id.op_view_claim:
                        	intent = new Intent( getActivity(), ClaimInfoActivity.class );
                        	intent.putExtra( "claimUUID",  clickedOnClaim.getUuid() );
                        	intent.putExtra( "fromMyClaims", true);
                        	startActivity(intent);
                        	break;
                        case R.id.op_submit_claim:
                        	if ( clickedOnClaim.isIncomplete() ){
            					Toast.makeText(getActivity(), "Can't submit, claim is incomplete.", Toast.LENGTH_SHORT). show();
                        	} else {
                        		//FIXME: Handle connectivity error
                        		Toast.makeText(getActivity(), "Submitting", Toast.LENGTH_LONG).show();
                        		new ElasticSearchEngine().submitClaim(getActivity(), clickedOnClaim);
                        		
                        	}
                        	break;
                        case R.id.op_add_expense:
                        	Expense exp = new Expense();
            				
            				clickedOnClaim.getExpenseList().addExpense(getActivity(), exp);
            				intent = new Intent( getActivity(), EditExpenseActivity.class );
            				intent.putExtra("claimUUID", clickedOnClaim.getUuid());
            				intent.putExtra("expenseUUID", exp.getUuid());
            				
            				startActivity(intent);
                        default: break;
                        }
                        
                        return true;
                    }
                });
				
	            popup.show();					
			}
			
		});

		return rootView;
	}
	
	/**
	 * Prepares option menu for when a claim is selected.
	 *
	 * @param popup the Popup Menu in question
	 * @param c the Claim in question
	 */
	public void onPrepareOptionsMenu( PopupMenu popup, Claim c ){
		switch(c.getStatus()){
		case Claim.APPROVED:
		case Claim.SUBMITTED:
			for( int id : submittedOrApprovedHiddenItems ){
				popup.getMenu().findItem(id).setVisible(false);
			}
			popup.getMenu().findItem(R.id.op_edit_tags).setVisible(true);
			break;
		default:
			popup.getMenu().findItem(R.id.op_edit_tags).setVisible(false);
			break;
		}
	}

	/** 
	 * Update method updates the listview
	 * 
	 * @param model for when a model calls it
	 **/
	@Override
	public void update(TModel model) {
		// TODO Auto-generated method stub

		MainClaimListAdapter adapter;
		Claim[] listOfClaims = ClaimList.getFilteredClaims(getActivity());

		adapter= new MainClaimListAdapter(getActivity(), listOfClaims);
		
		lv_claim_list.setAdapter(adapter);
		
		// Update internet status
		if ( Controller.isInternetConnected(getActivity()) ){
			tv_has_internet.setVisibility(View.GONE);
		} else {
			tv_has_internet.setVisibility(View.VISIBLE);
		}
		
	}
	
}
