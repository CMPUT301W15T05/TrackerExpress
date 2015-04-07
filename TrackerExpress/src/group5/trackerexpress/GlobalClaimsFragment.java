package group5.trackerexpress;

import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fragment that deals with the global claim list, claims submitted by other
 * users.
 * 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen,
 *         Rishi Barnwal
 * @version Part 4
 */
public class GlobalClaimsFragment extends Fragment implements TView {

	/** The list xml item. */
	private ListView lv_global_list;

	/** The error notice xml item */
	private TextView tv_global_error;
	
	/** Updates global claims once in a while */
	PeriodicTViewUpdater updater;
	
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

		lv_global_list = (ListView) rootView.findViewById(R.id.lv_global_claims);
		lv_global_list.setItemsCanFocus(true);
		
		tv_global_error = (TextView) rootView.findViewById(R.id.tv_global_claims_error);
		
		update(null);
		
		// UPDATER TESTED HERE ( Updater not practical for GlobalClaimsList atm
		// 		b/c GlobalClaimsList is rescrolled to the top )
		//updater = new PeriodicTViewUpdater();
		//updater.addView(GlobalClaimsFragment.this);
		//updater.startRepeatingTask();
		
		lv_global_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v,
					final int position, long arg3) {

				final Claim c = (Claim) lv_global_list.getAdapter().getItem(
						position);

				final PopupMenu popup = new PopupMenu(getActivity(), v);
				popup.getMenuInflater().inflate(R.menu.global_claims_popup,
						popup.getMenu());
				
				onPrepareOptionsMenu(popup, c);
				
				
				popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						final AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						
						String message = "Enter comments";
						
						final EditText input = new EditText(getActivity());
						builder.setMessage(message);
						builder.setView(input);
						builder.setNegativeButton("Cancel",
								new OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// Do Nothing
									}
								});
						
						// Following 3 variables are to set up
						// dialog box for if a claim is approved 
						// or returned. 
						String title_part = null;
						String toast_part = null;
						int claim_status = 0;
						
						final int itemId = item.getItemId();
						
						if ( itemId == R.id.op_view_global ){
							Intent intent;
							intent = new Intent(getActivity(),
									ClaimInfoActivity.class);
							intent.putExtra("claimUUID", c.getUuid());
							startActivity(intent);
						} else if ( itemId == R.id.op_approve ){
							title_part = "Approve";
							toast_part = "Approving";
							claim_status = Claim.APPROVED;
						} else {
							title_part = "Return";
							toast_part = "Returning";
							claim_status = Claim.RETURNED;
						}
						
						

						// Following builds the dialog box for return/approving a claim
						if ( itemId == R.id.op_approve || itemId == R.id.op_return ){
							// toast_part and claim_status must be constants to be used
							// by the dialog box 
							final String toast_part_use = toast_part;
							final int claim_status_use = claim_status;
							
							builder.setTitle(title_part + " Claim");
							builder.setPositiveButton(title_part, new OnClickListener() {
								@Override
								public void onClick( DialogInterface dialog, int which) {
									if (input.getText().toString().equals("")) {
										Toast.makeText(getActivity(), "Comments Required", Toast.LENGTH_LONG).show();
									} else {
										Toast.makeText(getActivity(), toast_part_use + " Claim", Toast.LENGTH_LONG).show();
										c.setComments(c.getComments() + '\n' + input.getText().toString());
										//int old_status = c.getStatus();
										try {
											//c.setStatus(getActivity(), claim_status_use );
											//if ( itemId == R.id.op_approve ){
											Log.e("PRE", "PREREV");
											c.setStatus(getActivity(), claim_status_use);
											Log.e("prestatus", c.getStatus() + "");
											new ElasticSearchEngine().reviewClaim(getActivity(), c.getUuid(), c.getComments(), claim_status_use);
											Log.e("poststatus", c.getStatus() + "");
											Log.e("POST", "POSTREV");
											//} else {
												//new ElasticSearchEngine().returnClaim(getActivity(), c.getUuid(), c.getComments());
											//}
										} catch(IOException e) {
											//FIXME: Do something about elastic search fail
											//c.setStatus(getActivity(), old_status);
											Log.e("EXCPET", "EXCEPT");
										}
										
										try {
											Thread.sleep(3000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										update(null);
									}
								}
							});
						}
						//update(null);
						AlertDialog build = builder.create();
						if (item.getItemId() != R.id.op_view_global)
							build.show();
						return false;
					}
				});
				popup.show();
			}

			private void onPrepareOptionsMenu(PopupMenu popup, Claim c) {
				if (c.getStatus() != Claim.SUBMITTED) {
					popup.getMenu().findItem(R.id.op_approve).setVisible(false);
					popup.getMenu().findItem(R.id.op_return).setVisible(false);
				}	
			}
		}
		);
		return rootView;
	}


	/*
	 * (non-Javadoc)
	 * 
	 */
	@Override
	public void update(TModel model) {
		Claim[] listOfClaims;
		try {
			listOfClaims = new ElasticSearchEngine().getClaimsForGlobalClaimList(getActivity());
		} catch (IOException e) {
			//FIXME: Do something about elastic search fail
			e.printStackTrace();
			listOfClaims = null;
		}
		if (listOfClaims == null ){
			tv_global_error.setVisibility(View.VISIBLE);
			return;
		} else {
			tv_global_error.setVisibility(View.GONE);
		}
		GlobalClaimsListAdapter a = new GlobalClaimsListAdapter(getActivity(), listOfClaims);
		lv_global_list.setAdapter(a);
	}

}