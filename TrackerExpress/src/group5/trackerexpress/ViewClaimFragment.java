package group5.trackerexpress;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Fragment that handles the tab that displays claim properties in the view claim activity.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
@SuppressLint("ValidFragment")
public class ViewClaimFragment extends Fragment implements TView {
	
	Button viewComments;
	
	final String myFormat = "MM/dd/yyyy"; //In which you need put here
	final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
	
	private View rootView;
	private Claim claim;

	
	public ViewClaimFragment(Claim claim) {
		this.claim = claim;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_view_claim,
				container, false);

		// Claim title
		TextView title = (TextView) rootView.findViewById(R.id.viewClaimTitle);

		title.setText(claim.getClaimName());
		
		claim.addView(ViewClaimFragment.this);
		
		// User name
		TextView nameInfo = (TextView) rootView.findViewById(R.id.viewClaimNameInfo);
		nameInfo.setText(" " + claim.getSubmitterName());
		
		// Inserting duration row
		String datePrefix = null;
		String duration = null;
		Calendar startDate = claim.getStartDate();
		Calendar endDate = claim.getEndDate();
		
		if (startDate != null && endDate != null) {
			datePrefix = getString(R.string.view_claim_duration);
			duration = sdf.format(startDate.getTime()) + " - " + sdf.format(endDate.getTime());
		} else if (startDate != null) {
			datePrefix = getString(R.string.view_claim_start_date);
			duration = sdf.format(startDate.getTime());
		} else if (endDate != null ) {
			datePrefix = getString(R.string.view_claim_end_date);
			duration = " - " + sdf.format(endDate.getTime());
		}
		

        RowTableLayout rtl = new RowTableLayout(rootView, getActivity());
        rtl.setPadding(2, 12);
		
		if (datePrefix != null) {
			rtl.insertRow(R.id.viewClaimDateTable, datePrefix + duration, true);
		}
		
		// Inserting description
		String claimDescription = claim.getDescription();
		
		if (claimDescription != null && !claimDescription.isEmpty()) {
			rtl.insertRow(R.id.viewClaimDescriptionTable, getString(R.string.view_claim_description), true);
			rtl.insertRow(R.id.viewClaimDescriptionTable, claimDescription, false);
		}
		
		// Inserting destinations
		ArrayList<Destination> destinations = claim.getDestinationList();
		
		if (destinations.size() > 0) {
			String destination;
			
			rtl.insertRow(R.id.viewClaimDestinationTable, getString(R.string.view_claim_destinations), true);
			
			for (int i = 0; i < destinations.size(); i++){
				destination = destinations.get(i).getName() + " - " + destinations.get(i).getDescription();
				rtl.insertRow(R.id.viewClaimDestinationTable, destination, false);
			}
		}
		
		// Inserting amounts
		ExpenseList expenseList = claim.getExpenseList();
		
		if (expenseList.toList().size() > 0) {
			String[] expenses = expenseList.toStringTotalCurrencies().split(", ");

			rtl.insertRow(R.id.viewClaimAmountSpentTable, getString(R.string.view_claim_ammount_spent), true);
			
			for (int i = 0; i < expenses.length; i++) {
				rtl.insertRow(R.id.viewClaimAmountSpentTable, expenses[i], false);
			}
			
		}
		
		// TODO: Inserting tags
		ArrayList<UUID> tagList = claim.getTagsIds(getActivity());
		
		
		if (tagList != null && tagList.size() > 0) {
			rtl.insertRow(R.id.viewClaimTagsTable, getString(R.string.view_claim_tags), true);
			TagMap tagMap = Controller.getTagMap(getActivity());
			
			for (int i = 0; i < tagList.size(); i++) {
				Tag tag = tagMap.getTag(tagList.get(i));
				rtl.insertRow(R.id.viewClaimTagsTable, tag.toString(), false);
			}
			
		}
		viewComments = (Button) rootView.findViewById(R.id.viewComments);
		if (claim.getComments() == "" || claim.getComments() == null)
			viewComments.setVisibility(View.GONE);
		
		viewComments.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
    			alertDialog.setTitle("Comments from: " + claim.getApproverName());
    			alertDialog.setMessage(claim.getComments());
    			alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog, int which) {
    					
    				}
    			});
    			alertDialog.show();
			}
		});
		
		
		return rootView;
	}

	/* (non-Javadoc)
	 * @see group5.trackerexpress.TView#update(group5.trackerexpress.TModel)
	 */
	@Override
	public void update(TModel model) {
		// TODO Auto-generated method stub
		
	}	
}