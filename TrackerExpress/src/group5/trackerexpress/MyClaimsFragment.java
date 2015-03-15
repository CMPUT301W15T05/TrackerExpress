package group5.trackerexpress;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MyClaimsFragment extends Fragment implements TView {

	private ListView lv_claim_list;
	private MainClaimListAdapter adapter;
	private Button b_add_claim;

	// Menu items to hide when selecting an option on a claim
	private static final int[] submittedOrApprovedHiddenItems = {R.id.op_edit_claim, R.id.op_submit_claim};
	
	public MyClaimsFragment() {
	}

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
					int position, long arg3) {
				// TODO Auto-generated method stub
				
				Claim c = (Claim) lv_claim_list.getAdapter().getItem(position);
				
				
				
				if ( c.getStatus() == Claim.SUBMITTED || c.getStatus() == Claim.APPROVED ){
					
				}
			}
			
		});
		
		return rootView;
	}

	@Override
	public void update(TModel model) {
		// TODO Auto-generated method stub
		
	}
}
