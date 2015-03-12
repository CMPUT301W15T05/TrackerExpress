package group5.trackerexpress;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainClaimListAdapter extends ArrayAdapter<Claim> {
	private Claim[] claimList;
	private Context context;
	
	public MainClaimListAdapter(Context context, Claim[] claims){
		super(context, R.layout.fragment_my_claims_item, claims);
		this.claimList = claims;
		this.context = context;
	}
	
	private static class ClaimHolder{
		public TextView claimName;
		public TextView destinations;
		public TextView amounts;
		public TextView tags;
	}
	
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
