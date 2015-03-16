package group5.trackerexpress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class MainClaimListAdapter.
 */
public class MainClaimListAdapter extends ArrayAdapter<Claim> {
	
	/** The claim list. */
	private Claim[] claimList;
	
	/** The context. */
	private Context context;
	
	/**
	 * Instantiates a new main claim list adapter.
	 *
	 * @param context the context
	 * @param claims the claims
	 */
	public MainClaimListAdapter(Context context, Claim[] claims){
		super(context, R.layout.fragment_my_claims_item, claims);
		this.claimList = claims;
		this.context = context;
	}
	
	/**
	 * The Class ClaimHolder.
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
	}
	
	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
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

			v.setTag(holder);
		} else {
			holder = (ClaimHolder) v.getTag();
		}
		
		Claim c = claimList[position];
		holder.claimName.setText(c.getClaimName());
		holder.destinations.setText(c.toStringDestinations());
		holder.amounts.setText(c.getExpenseList().toStringTotalCurrencies());
		//holder.tags.setText(text);
		
		return v;
	}
}
