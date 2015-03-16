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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

// TODO: Auto-generated Javadoc
/**
 * The Class MyClaimsFragment.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class MyClaimsFragment extends Fragment implements TView {

	/** The lv_claim_list. */
	private ListView lv_claim_list;
	
	/** The adapter. */
	private MainClaimListAdapter adapter;
	
	/** The b_add_claim. */
	private Button b_add_claim;

	// Menu items to hide when selecting an option on a claim
	/** The Constant submittedOrApprovedHiddenItems. */
	private static final int[] submittedOrApprovedHiddenItems = {R.id.op_edit_claim, R.id.op_submit_claim};
	
	/**
	 * Instantiates a new my claims fragment.
	 */
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
		
		final ClaimList listOfClaims = ClaimController.getInstance(getActivity()).getClaimList();
		final Claim[] arrayClaims = listOfClaims.getAllClaims();
		
		adapter = new MainClaimListAdapter(getActivity(), arrayClaims);
		lv_claim_list.setAdapter(adapter);
		
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
				
				final Claim c = (Claim) lv_claim_list.getAdapter().getItem(position);
				
				PopupMenu popup = new PopupMenu(getActivity(), v);
				popup.getMenuInflater().inflate(R.menu.my_claims_popup, popup.getMenu());
				
				onPrepareOptionsMenu(popup, c);
				
				// Popup menu item click listener
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					
					@Override
                    public boolean onMenuItemClick(MenuItem item) {
                    	Claim claimAnalyzed = (Claim) lv_claim_list.getAdapter().getItem(position);
                    	Intent intent;
                        switch(item.getItemId()){
                        case R.id.op_delete_claim: 
                        	// Delete tag off of Claim ArrayList for listview
                        	listOfClaims.deleteClaim(getActivity(), claimAnalyzed.getUuid());
                        	Claim[] arrayClaims = listOfClaims.getAllClaims();
                        	MainClaimListAdapter a = new MainClaimListAdapter( getActivity().getBaseContext(), arrayClaims );
                			lv_claim_list.setAdapter(a);
                			// Delete it off the model
                        	listOfClaims.deleteClaim(getActivity(), claimAnalyzed.getUuid());
                        	break;
                        case R.id.op_edit_claim:
                        	intent = new Intent( getActivity(), EditClaimActivity.class );
                        	intent.putExtra( "isNewClaim", false );
                        	intent.putExtra("claimUUID", c.getUuid());
                        	Log.i("myMessage", "hi");
                        	startActivity(intent);
                        	break;
                        case R.id.op_view_claim:
                        	intent = new Intent( getActivity(), ClaimInfoActivity.class );
                        	intent.putExtra( "claimUUID",  c.getUuid() );
                        	startActivity(intent);
                        	break;
                        case R.id.op_submit_claim:
                        	// TODO:
                        	// Submit the claim to server
                        	// using controller
                        	if ( c.isIncomplete() ){
            					Toast.makeText(getActivity(), "Updating", Toast.LENGTH_SHORT). show();
                        	} else {
                        		c.setStatus(getActivity(), Claim.SUBMITTED);
                        	}
                        	break;
                        default: break;
                        }

                        update(null);
                        
                        return true;
                    }
                });
				
	            popup.show();					
			}
			
		});
		
		return rootView;
	}
	
	/**
	 * On prepare options menu.
	 *
	 * @param popup the popup
	 * @param c the c
	 */
	public void onPrepareOptionsMenu( PopupMenu popup, Claim c ){
		switch(c.getStatus()){
		case Claim.APPROVED:
		case Claim.SUBMITTED:
			for( int id : submittedOrApprovedHiddenItems ){
				popup.getMenu().findItem(id).setVisible(false);
			}
			break;
		default:
			break;
		}
	}

	/* (non-Javadoc)
	 * @see group5.trackerexpress.TView#update(group5.trackerexpress.TModel)
	 */
	@Override
	public void update(TModel model) {
		// TODO Auto-generated method stub
		
	}
}
