package group5.trackerexpress;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

// http://androidcocktail.blogspot.it/2012/04/adding-checkboxes-to-custom-listview-in.html
public class TagListArrayAdapter extends ArrayAdapter<Tag> {
	
	private ArrayList<Tag> tagList;
	private Context context;
	boolean[] checkBoxState;
	
	public TagListArrayAdapter(Context context, ArrayList<Tag> tags){
		super(context, R.layout.fragment_tags_list_item, tags);
		this.tagList = tags;
		this.context = context;
		
		checkBoxState = new boolean[tags.size()];
		for ( int i = 0; i < tags.size(); i++ ){
			checkBoxState[i] = tags.get(i).isSelected();
		}
	}
	
	
	private static class TagHolder{
		public TextView tagName;
		public CheckBox chkBox;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
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
