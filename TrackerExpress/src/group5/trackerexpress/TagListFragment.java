package group5.trackerexpress;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * Fragment activity that deals with the tag list tab in the main activity.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class TagListFragment extends Fragment implements TView {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	/** The ListView of tags */
	private ListView lv_tag_list;

	/** The button Add Tag. */
	private Button b_add_tag;
	
	/** The button that Selects All tags **/
	private Button b_select_all;

	/** Checks if all tags are selected **/
	private Boolean all_selected;

	/**
	 * Instantiates a new tag list fragment.
	 */
	public TagListFragment() {
	}

	/**
	 * 
	 * Sets up all the OnClickListeners
	 * 
	 **/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_tags_list,
				container, false);

		lv_tag_list = (ListView) rootView.findViewById(R.id.lv_tags);
		lv_tag_list.setItemsCanFocus(true);

		b_add_tag = (Button) rootView.findViewById(R.id.b_add_tag);
		b_select_all = (Button) rootView.findViewById(R.id.b_select_all);
		
		this.update(null);

		final TagMap mapOfTags = getTagMap(getActivity());

		//mapOfTags.addView(this);
		getTagMap(getActivity()).addView(this);

		
		b_add_tag.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				getName(new Tag(""));
			}
		});
		
		b_select_all.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				getTagMap(getActivity()).selectAll(getActivity(), !all_selected);
				
			}
			
		});
		
		
		lv_tag_list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Perform same actions as regular click listener
				OnTagClick(view, mapOfTags, position);
				return true;
			}
		});

		// Item click listener
		lv_tag_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				OnTagClick(view, mapOfTags, position);
			}
		});

		return rootView;
	}

	private void OnTagClick(View view, final TagMap tagMap, final int position) {
		PopupMenu popup = new PopupMenu(getActivity(), view);
		popup.getMenuInflater().inflate(R.menu.tag_list_popup,
				popup.getMenu());

		// Popup menu item click listener
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				Tag t = (Tag) lv_tag_list.getAdapter()
						.getItem(position);

				switch (item.getItemId()) {
				case R.id.op_delete_tag:
					tagMap.deleteTag(getActivity(), t.getUuid());
					break;
				case R.id.op_edit_tag:
					getName(t);
					break;
				default:
					break;
				}

				return true;
			}
		});

		popup.show();
	}
	
	/**
	 * Gets the tag map.
	 *
	 * @param context
	 *            the context
	 * @return the tag map
	 */
	private TagMap getTagMap(Context context) {
		return Controller.getTagMap(context);
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	private void getName(final Tag t) {
		final Boolean edit = !t.toString().isEmpty();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Enter a new name");
		
		if (edit)
			builder.setTitle("Edit Tag");
		else
			builder.setTitle("Create Tag");

		final EditText input = new EditText(getActivity());
		input.setMaxLines(1);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		input.setText(t.toString());
		
		builder.setView(input);
		
		builder.setPositiveButton("Ok", EditableActivity.doNothingClicker);
		builder.setNegativeButton("Cancel", EditableActivity.doNothingClicker);
		final AlertDialog aDialog = builder.create();
		
		aDialog.setOnShowListener(new DialogInterface.OnShowListener() {

	        @Override
	        public void onShow(DialogInterface dialog) {

	            Button b = aDialog.getButton(AlertDialog.BUTTON_POSITIVE);
	            b.setOnClickListener(new View.OnClickListener() {

	                @Override
	                public void onClick(View view) {
	                	String sInput = input.getText().toString().trim();

	                	if (sInput.isEmpty()) {
	                		input.setError("Enter a tag name");
	                		input.setText(sInput);
	                		return;
	                	}
	                	
	                	TagMap tagMap = getTagMap(getActivity());
	                	
	    	        	try {
	    	        		tagMap.searchForTagByString(sInput);
	                		input.setError("Tag already exists");
	                		input.setText(sInput);
	    	        		return;
	    	        	} catch (IllegalAccessException e) {
	    	        		Toast.makeText(getActivity(), "New Tag", Toast.LENGTH_SHORT).show();

		    	        	if (edit) {
		    	        		t.rename(getActivity(), sInput);
		    	        	} else {
		    	        		tagMap.addTag(getActivity(), new Tag(sInput));
		    	        	}
	    	        	}
	                	
	                    aDialog.dismiss();
	                }
	            });
	        }
	    });
		
		aDialog.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see group5.trackerexpress.TView#update(group5.trackerexpress.TModel)
	 */
	@Override
	public void update(TModel model) {
		
		TagMap tagMap = getTagMap(getActivity());
		
		ArrayList<Tag> listOfTags = tagMap.toList();
		if (listOfTags == null) {
			listOfTags = new ArrayList<Tag>();
		}
		
		int checked = tagMap.numChecked();
		if (checked == listOfTags.size() ) {
			b_select_all.setText(R.string.button_deselect_all);
			all_selected = true;
		} else {
			b_select_all.setText(R.string.button_select_all);
			all_selected = false;
		}
		
		MainTagListAdapter a = new MainTagListAdapter(getActivity(), listOfTags);
		lv_tag_list.setAdapter(a);
	}
}