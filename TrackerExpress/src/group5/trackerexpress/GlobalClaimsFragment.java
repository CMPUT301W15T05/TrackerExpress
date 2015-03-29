package group5.trackerexpress;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

/**
 * Fragment that deals with the global claim list, claims submitted by other users.
 * 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen,
 *         Rishi Barnwal
 * @version Part 4
 */
public class GlobalClaimsFragment extends Fragment implements TView {

	/** The list xml item. */
	private ListView lv_global_list;

	/** The adapter. */
	private MainClaimListAdapter adapter;

	private ClaimList globalClaims;

	// Menu items to hide when selecting an option on a claim
	/** The Constant submittedOrApprovedHiddenItems. */
	private static final int[] submittedOrApprovedHiddenItems = {
			R.id.op_edit_claim, R.id.op_submit_claim };

	/**
	 * Instantiates a new global claims fragment.
	 */
	public GlobalClaimsFragment() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_global_claims,
				container, false);

		lv_global_list = (ListView) rootView
				.findViewById(R.id.lv_global_claims);
		lv_global_list.setItemsCanFocus(true);

		globalClaims = GetGlobalClaims(); // Placeholder until elastic search implemented
		final Claim[] arrayGlobalClaims = globalClaims.toList();

		adapter = new MainClaimListAdapter(getActivity(), arrayGlobalClaims);
		lv_global_list.setAdapter(adapter);

		
		lv_global_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v,
					final int position, long arg3) {
				
				final Claim c = (Claim)lv_global_list.getAdapter().getItem(position);
				
				PopupMenu popup = new PopupMenu(getActivity(), v);
				popup.getMenuInflater().inflate(R.menu.global_claims_popup, popup.getMenu());
			
				popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
                    	Intent intent;
                    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    	String message = "Enter comments";
                    	final EditText input = new EditText(getActivity());
                    	builder.setMessage(message);
                    	builder.setView(input);
                    	builder.setNegativeButton("Cancel", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// Do Nothing
							}
						});
                    	
                        switch(item.getItemId()){
                        case R.id.op_view_global:
                        	intent = new Intent( getActivity(), ClaimInfoActivity.class );
                        	intent.putExtra( "claimUUID",  c.getUuid() );
                        	startActivity(intent);
                        	break;
                        case R.id.op_approve:
                        	builder.setTitle("Approve Claim");
                        	builder.setPositiveButton("Approve", new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									c.setComments(input.getText().toString());
									c.setApprover(Controller.getUser(getActivity()));
									c.setStatus(getActivity(), Claim.APPROVED);
									globalClaims.notifyViews(getActivity());
								}
							});
                        	break;
                        case R.id.op_return:
                        	builder.setTitle("Return Claim");
                        	builder.setPositiveButton("Return", new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									c.setComments(input.getText().toString());
									c.setApprover(Controller.getUser(getActivity()));
									c.setStatus(getActivity(), Claim.RETURNED);
									globalClaims.notifyViews(getActivity());
								}
							});
                        	break;
                        }
                        
                        AlertDialog build = builder.create();
                        build.show();
						return false;
					}
				});
				popup.show();
			}
		});
		return rootView;
	}

	// Creates Dummy ClaimList for testing
	private ClaimList GetGlobalClaims() {
		// TODO Auto-generated method stub
		final ClaimList listOfClaims = Controller.getClaimList(getActivity());
		return listOfClaims;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see group5.trackerexpress.TView#update(group5.trackerexpress.TModel)
	 */

	@Override
	public void update(TModel model) {
		// TODO Auto-generated method stub

	}
	
}