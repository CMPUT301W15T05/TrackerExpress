package group5.trackerexpress;

import group5.trackerexpress.MainTagListArrayAdapter.TagHolder;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainClaimListArrayAdapter extends ArrayAdapter<Claim> {
	private ArrayList<Claim> claimList;
	private Context context;
	
	public MainClaimListArrayAdapter(Context context, ArrayList<Claim> claims){
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
		
		Claim c = claimList.get(position);
		holder.claimName.setText(c.getClaimName());
		
		// Get the destinations in a list format
		String str_destinations = "";
		ArrayList<String[]> destinations = c.getDestination();
		for ( int i = 0; i < destinations.size() - 1; i++ ){
			str_destinations += destinations.get(i)[0] + ", ";
		}
		if ( destinations.size() > 1 ){
			str_destinations += destinations.get(destinations.size() - 1);
		}
		holder.destinations.setText(str_destinations);
		
		
		holder.chkBox.setChecked(checkBoxState[position]);

		holder.chkBox.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (((CheckBox)v).isChecked()){
					checkBoxState[position] = true;
					tagList.get(position).setSelected(context, true);
				} else {
					checkBoxState[position] = false;
					tagList.get(position).setSelected(context, false);
				}
			}	
		});
		
		return v;
	}
}
