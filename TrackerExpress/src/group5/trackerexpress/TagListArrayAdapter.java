package group5.trackerexpress;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class TagListArrayAdapter extends ArrayAdapter<Tag> 
	implements CompoundButton.OnCheckedChangeListener{
	private ArrayList<Tag> tagList;
	private Context context;
	
	public TagListArrayAdapter(Context context, ArrayList<Tag> tags){
		super(context, R.layout.fragment_tags_list_item, tags);
		this.tagList = tags;
		this.context = context;
	}
	
	
	private static class TagHolder{
		public TextView tagName;
		public CheckBox chkBox;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View v = convertView;
		TagHolder holder = new TagHolder();
		
		if ( convertView == null ){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.fragment_tags_list_item, null);
			
			holder.tagName = (TextView) v.findViewById(R.id.tv_tags_list_item);
			holder.chkBox = (CheckBox) v.findViewById(R.id.cb_tags_list_item);
		
			v.setTag(holder);
		} else {
			holder = (TagHolder) v.getTag();
		}
		
		Tag t = tagList.get(position);
		holder.tagName.setText(t.toString());
		holder.chkBox.setChecked(t.isSelected());
		holder.chkBox.setTag(t);
		
		return v;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView,
			boolean isChecked) {
		// TODO Auto-generated method stub
	    
	}
}
