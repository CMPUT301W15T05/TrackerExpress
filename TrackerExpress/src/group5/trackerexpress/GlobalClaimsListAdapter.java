package group5.trackerexpress;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GlobalClaimsListAdapter extends ArrayAdapter<Claim> {
	public static final String approvedStatus = "APPROVED";
	public static final String returnedStatus = "RETURNED";
	public static final String submittedStatus = "SUBMITTED";
	
	/** The claim list. */
	private Claim[] claimList;
	
	/** The context. */
	private Context context;
	
	/**
	 * Instantiates a new main claim list adapter.
	 *
	 * @param context Needed for file IO
	 * @param claims the claims
	 */
	public GlobalClaimsListAdapter(Context context, Claim[] claims){
		super(context, R.layout.fragment_my_claims_item, claims);
		this.claimList = claims;
		this.context = context;
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
		public TextView user;
		
		/** incompleteness of claim **/
		public TextView isApprovedStatus;
		
		/** official status of claim **/
		public TextView status;
		
		/** start date  of claim **/
		public TextView startDate;
		
		/** "to" between dates **/
		public TextView toDate;
		
		/** end date of claim **/
		public TextView endDate;
		
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
			holder.user = (TextView) v.findViewById(R.id.tv_main_claim_list_destinations);
			holder.destinations = (TextView) v.findViewById(R.id.tv_main_claim_list_amounts);
			holder.amounts = (TextView) v.findViewById(R.id.tv_main_claim_list_tags);
			holder.isApprovedStatus = (TextView ) v.findViewById(R.id.tv_main_claim_list_isIncomplete);
			holder.status = (TextView) v.findViewById(R.id.tv_main_claim_list_status);
			holder.startDate = (TextView) v.findViewById(R.id.tv_main_claim_list_start);
			holder.toDate = (TextView) v.findViewById(R.id.tv_main_claim_list_to);
			holder.endDate = (TextView) v.findViewById(R.id.tv_main_claim_list_end);
			
			v.setTag(holder);
		} else {
			holder = (ClaimHolder) v.getTag();
		}
		
		Claim c = claimList[position];
		holder.claimName.setText(c.getClaimName());
		holder.destinations.setText("Destinations: " + c.toStringDestinations());
		holder.amounts.setText("Currency Totals: " + c.getExpenseList().toStringTotalCurrencies());
		switch(c.getStatus()){
		case Claim.APPROVED:
			holder.status.setText(approvedStatus);
			break;
		case Claim.RETURNED:
			holder.status.setText(returnedStatus);
			break;
		case Claim.SUBMITTED:
			holder.status.setText(submittedStatus);
			break;
		}
		
		c.setuserName(getContext(), "Sean"); // For testing
		holder.user.setText("User: " + c.getuserName());
		
		if ( c.getStartDate() != null ){
			holder.startDate.setText("Date(s): " + c.getStartDate().getShortString());
			
			if ( c.getEndDate() != null ){
				holder.toDate.setVisibility(View.VISIBLE);
				holder.endDate.setText(c.getEndDate().getShortString());
			} else {
				holder.toDate.setVisibility(View.INVISIBLE);
			}
		} else {
			holder.toDate.setVisibility(View.INVISIBLE);
		}
		
		return v;
	}
}
