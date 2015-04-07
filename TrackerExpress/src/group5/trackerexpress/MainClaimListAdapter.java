package group5.trackerexpress;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * List object for the claim list. Allows lots of information to be displayed in the list.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class MainClaimListAdapter extends ArrayAdapter<Claim> {
	public static final String incompleteString = "INCOMPLETE";
	public static final String approvedStatus = "APPROVED";
	public static final String inProgressStatus = "IN PROGRESS";
	public static final String returnedStatus = "RETURNED";
	public static final String submittedStatus = "SUBMITTED";
	
	/** The claim list. */
	private Claim[] claimList;
	
	/** The context. */
	private Context context;
	
	/** Claim's distances from home ordered **/
	ArrayList<Claim> distanceOrderedClaims;
	
	final String myFormat = "MM/dd/yyyy"; //In which you need put here
	final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
	
	/**
	 * Instantiates a new main claim list adapter.
	 *
	 * @param context Needed for file IO
	 * @param claims the claims
	 */
	public MainClaimListAdapter(Context context, Claim[] claims){
		super(context, R.layout.fragment_my_claims_item, claims);
		this.claimList = claims;
		this.context = context;
		Claim[] claimList = ClaimList.sortClaimsByLocation(context, claims);
		
		if (claimList != null) {
			this.distanceOrderedClaims = new ArrayList<Claim>( 
					Arrays.asList( claimList) );
		} else {
			this.distanceOrderedClaims = null;
		}
	}
	
	/**
	 * The Class ClaimHolder holds the views of the custom listview
	 */
	private static class ClaimHolder{
		
		/** The claim name. */
		public TextView claimName;
		
		/** The destinations. */
		public TextView destinations;
		
		/** The amounts. */
		public TextView amounts;
		
		/** The tags. */
		public TextView tags;
		
		/** incompleteness of claim **/
		public TextView isIncompleteStatus;
		
		/** incompleteness indicators on expenses indicater */
		public TextView incompleteExpenses;
		
		/** official status of claim **/
		public TextView status;
		
		/** start date  of claim **/
		public TextView startDate;
		
		/** "to" between dates **/
		public TextView toDate;
		
		/** end date of claim **/
		public TextView endDate;
		
		/** color box indicating distance **/
		public ImageView distanceView;
		
	}
	
	/** Updates the respective listview item in the custom listview
	 * 
	 * @param position: index of item
	 * @param convertView: view of item
	 * @param parent: parent of item ( the listView )
	**/
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		View v = convertView;
		ClaimHolder holder = new ClaimHolder();
		
		if ( convertView == null ){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.fragment_my_claims_item, null);
			
			holder.claimName = (TextView) v.findViewById(R.id.tv_main_claim_list_name);
			holder.destinations = (TextView) v.findViewById(R.id.tv_main_claim_list_destinations);
			holder.amounts = (TextView) v.findViewById(R.id.tv_main_claim_list_amounts);
			holder.tags = (TextView) v.findViewById(R.id.tv_main_claim_list_tags);
			holder.isIncompleteStatus = (TextView ) v.findViewById(R.id.tv_main_claim_list_isIncomplete);
			holder.incompleteExpenses = (TextView) v.findViewById(R.id.tv_main_claim_list_incomplete_expenses);
			holder.status = (TextView) v.findViewById(R.id.tv_main_claim_list_status);
			holder.startDate = (TextView) v.findViewById(R.id.tv_main_claim_list_start);
			holder.toDate = (TextView) v.findViewById(R.id.tv_main_claim_list_to);
			holder.endDate = (TextView) v.findViewById(R.id.tv_main_claim_list_end);
			holder.distanceView = (ImageView) v.findViewById(R.id.iv_distance);
			
			v.setTag(holder);
		} else {
			holder = (ClaimHolder) v.getTag();
		}
		
		Claim c = claimList[position];
		
		// Print claim name
		holder.claimName.setText(c.getClaimName());
		
		// Print destinations
		holder.destinations.setText("Destinations: " + c.toStringDestinations());
		
		// Print currency totals
		holder.amounts.setText("Currency Totals: " + c.getExpenseList().toStringTotalCurrencies());
		
		ArrayList<Expense> e = c.getExpenseList().toList();
		if ( e.size() > 0 ){
			Log.i("myMessage", "Jiya re " + e.get(0).getAmount() + " " + e.get(0).getCurrency() );
		}
		
		// Print incompleteness indicator
		if ( c.isIncomplete() ){
			holder.isIncompleteStatus.setText(incompleteString);
		} else {
			holder.isIncompleteStatus.setText("");
		}
		
		// Display if there are any incomplete expense
		if ( c.hasIncompleteExpense() ){
			holder.incompleteExpenses.setVisibility(View.VISIBLE);
		} else {
			holder.incompleteExpenses.setVisibility(View.GONE);
		}
		
		// Print status
		switch(c.getStatus()){
		case Claim.APPROVED:
			holder.status.setText(approvedStatus + " BY: " + c.getApproverName());
			break;
		case Claim.IN_PROGRESS:
			holder.status.setText(inProgressStatus);
			break;
		case Claim.RETURNED:
			holder.status.setText(returnedStatus + " BY: " + c.getApproverName());
			break;
		case Claim.SUBMITTED:
			holder.status.setText(submittedStatus);
			break;
		}
		
		// Print tags
		holder.tags.setText("Tags: " + c.toStringTags(context));
		
		// Print date(s)
		if ( c.getStartDate() != null ){
			holder.startDate.setText("Date(s): " + sdf.format(c.getStartDate().getTime()));
			
			if ( c.getEndDate() != null ){
				holder.toDate.setVisibility(View.VISIBLE);
				holder.endDate.setText(sdf.format(c.getEndDate().getTime()));
			} else {
				holder.toDate.setVisibility(View.INVISIBLE);
			}
		} else {
			holder.toDate.setVisibility(View.INVISIBLE);
		}
				
		// Print distance color indicator
		int destinationSize = c.getDestinationList().size();
		if ( destinationSize == 0 ){
			holder.distanceView.setVisibility( View.GONE );
		} else if (distanceOrderedClaims != null) {
			
			holder.distanceView.setVisibility( View.VISIBLE );

			DisplayMetrics metrics = context.getResources().getDisplayMetrics();
			int screenWidth = metrics.widthPixels;
			
			int posOfClaim = distanceOrderedClaims.indexOf(c) + 1;
			
			double percentile = ((double)posOfClaim)/distanceOrderedClaims.size();			
			holder.distanceView.getLayoutParams().width = (int)(screenWidth*percentile);
			
			if ( percentile <= 0.25 ){
				holder.distanceView.setBackgroundColor(context.getResources().getColor(R.color.closest));
			} else if ( percentile <= 0.5 ){
				holder.distanceView.setBackgroundColor(context.getResources().getColor(R.color.close));
			} else if ( percentile <= 0.75 ){
				holder.distanceView.setBackgroundColor(context.getResources().getColor(R.color.far));
			} else {
				holder.distanceView.setBackgroundColor(context.getResources().getColor(R.color.furthest));
			}
		}
		
		return v;
	}
	
}
