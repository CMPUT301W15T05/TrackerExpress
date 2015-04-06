package group5.trackerexpress;

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
		
		lv_global_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v,
					final int position, long arg3) {

				final Claim c = (Claim) lv_global_list.getAdapter().getItem(
						position);

				final PopupMenu popup = new PopupMenu(getActivity(), v);
				popup.getMenuInflater().inflate(R.menu.global_claims_popup,
						popup.getMenu());
				
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

						switch (item.getItemId()) {
						case R.id.op_view_global:
							Intent intent;
							intent = new Intent(getActivity(),
									ClaimInfoActivity.class);
							intent.putExtra("claimUUID", c.getUuid());
							startActivity(intent);
							break;
							
						case R.id.op_approve:
							builder.setTitle("Approve Claim");
							builder.setPositiveButton("Approve",
									new OnClickListener() {
								
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											if (input.getText().toString()
													.equals("")) {
												Toast.makeText(getActivity(),
														"Comments Required",
														Toast.LENGTH_LONG)
														.show();
											} else {
												new ElasticSearchEngine().approveClaim(getActivity(), c.getUuid(), input.getText().toString());
											}
										}
									});
							break;
						case R.id.op_return:
							builder.setTitle("Return Claim");
							builder.setPositiveButton("Return",
									new OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											if (input.getText().toString()
													.equals("")) {
												Toast.makeText(getActivity(),
														"Comments Required",
														Toast.LENGTH_LONG)
														.show();
											} else {
												new ElasticSearchEngine().returnClaim(getActivity(), c.getUuid(), input.getText().toString());
											}
										}
									});
							break;
						}
						update(null);
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


	/*
	 * (non-Javadoc)
	 * 
	 */
	@Override
	public void update(TModel model) {
		Claim[] listOfClaims = new ElasticSearchEngine().getClaimsForGlobalClaimList(getActivity());
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