package group5.trackerexpress;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupMenu;
import android.widget.AdapterView.OnItemLongClickListener;

// TODO: Auto-generated Javadoc
/**
 * The Class TagListFragment.
 */
public class TagListFragment extends Fragment implements TView {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	/** The lv_tag_list. */
	private ListView lv_tag_list;
	
	/** The value. */
	private Editable value = null;
	
	/** The b_add_tag. */
	private Button b_add_tag;
	
	/**
	 * Instantiates a new tag list fragment.
	 */
	public TagListFragment() {
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_tags_list,
				container, false);

		lv_tag_list = (ListView) rootView.findViewById(R.id.lv_tags);
		lv_tag_list.setItemsCanFocus(true);
		
		this.update(null);
		
		final TagMap mapOfTags = getTagMap(getActivity());
		ArrayList<Tag> listOfTags = mapOfTags.getTags();
		
		mapOfTags.addView(this);
		getTagMap(getActivity()).addView(this);
		
		b_add_tag = (Button) rootView.findViewById(R.id.b_add_tag);
		
		b_add_tag.setOnClickListener(new Button.OnClickListener(){
		    public void onClick(View v) {
		    	getName();
		    	/*
		    	if ( value != null ){
		    		String name = value.toString();
		    		Tag newTag = new Tag(name);
		    		mapOfTags.addTag(getActivity(), newTag);
		    		//listOfTags.add(newTag);
		    		//MainTagListAdapter a = new MainTagListAdapter( myContext, listOfTags );
        			lv_tag_list.setAdapter(a);
		    	}
		    	
		    	value = null;
		    	*/
		    }
		});
		
		// Item click listener
        lv_tag_list.setOnItemLongClickListener( new OnItemLongClickListener() {
        	@Override
			public boolean onItemLongClick(AdapterView<?> parent,
					View view, final int position, long id) {
				
				PopupMenu popup = new PopupMenu(getActivity(), view);
				popup.getMenuInflater().inflate(R.menu.tag_list_popup, popup.getMenu());
				
				// Popup menu item click listener
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                    	Tag t = (Tag) lv_tag_list.getAdapter().getItem(position);
                    	
                        switch(item.getItemId()){
                        case R.id.op_delete_tag: 
                        	// Delete tag off of Tag ArrayList for listview
                        	//listOfTags.remove(t);
                        	//MainTagListAdapter a = new MainTagListAdapter( getActivity().getBaseContext(), listOfTags );
                			//lv_tag_list.setAdapter(a);
                			// Delete it off the model
                        	mapOfTags.deleteTag(getActivity(), t.getUuid());
                        	break;
                        case R.id.op_edit_tag:
                        	getName();
                        	
                        	if ( value != null ){
                        		String name = value.toString();
                        		t.rename(getActivity(), name);
                        	}
                        	
                        	value = null;
                        	break;
                        default: break;
                        }
                    	
                        return true;
                    }
                });
				
	            popup.show();
				return false;
			}
        });
        Log.i("myMessage", Integer.toString(listOfTags.size()));

		return rootView;
	}
	
	/**
	 * Gets the tag map.
	 *
	 * @param context the context
	 * @return the tag map
	 */
	private TagMap getTagMap(Context context) {
		return TagController.getInstance(context).getTagMap();
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	private void getName(){
		String message = "Enter a new name";
		final AutoCompleteTextView input = new AutoCompleteTextView(getActivity());

		ArrayList<String> tags = new ArrayList<String>();
		
		new AlertDialog.Builder(getActivity())
	    .setTitle("Create Tag")
	    .setMessage(message)
	    .setView(input)
	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {       		
	        	value = input.getText();
		    	if ( input.getText() != null ){
		    		Tag newTag = new Tag(input.getText().toString());
		    		getTagMap(getActivity()).addTag(getActivity(), newTag);
		    		//listOfTags.add(newTag);

		    	}
		    	
		    	value = null;
	        }
	    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            // Do nothing.
	        }
	    }).show();
		
	}

	/* (non-Javadoc)
	 * @see group5.trackerexpress.TView#update(group5.trackerexpress.TModel)
	 */
	@Override
	public void update(TModel model) {
		ArrayList<Tag> listOfTags = getTagMap(getActivity()).getTags();			
		MainTagListAdapter a = new MainTagListAdapter( getActivity(), listOfTags );
		lv_tag_list.setAdapter(a);
	}
}