package group5.trackerexpress;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
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

	/** The return Value of the Input Box. */
	private Editable value = null;

	/** The button Add Tag. */
	private Button b_add_tag;
	
	/** The button that Selects All tags **/
	private Button b_select_all;

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

		this.update(null);

		final TagMap mapOfTags = getTagMap(getActivity());
		ArrayList<Tag> listOfTags = mapOfTags.toList();

		mapOfTags.addView(this);
		getTagMap(getActivity()).addView(this);

		b_add_tag = (Button) rootView.findViewById(R.id.b_add_tag);
		b_select_all = (Button) rootView.findViewById(R.id.b_select_all);
		
		b_add_tag.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Tag t = new Tag("");
				getName(t, false);
			}
		});
		
		b_select_all.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<Tag> tm = getTagMap(getActivity()).toList();
				for ( Tag t : tm ){
					t.setSelected(getActivity(), true);
				}
			}
			
		});

		// Item click listener
		lv_tag_list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {

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
							mapOfTags.deleteTag(getActivity(), t.getUuid());
							break;
						case R.id.op_edit_tag:
							getName(t, true);
							value = null;
							break;
						default:
							break;
						}

						return true;
					}
				});

				popup.show();
				return false;
			}
		});

		return rootView;
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
	private void getName(final Tag t, final boolean edit) {
		String message = "Enter a new name";
		final AutoCompleteTextView input = new AutoCompleteTextView(
				getActivity());

		ArrayList<String> tags = new ArrayList<String>();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		if (edit)
			builder.setTitle("Edit Tag");
		else
			builder.setTitle("Create Tag");
		builder.setMessage(message);
		builder.setView(input);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				value = input.getText();
				if (input.getText() != null) {
					if (!edit) {
						Tag newTag = new Tag(input.getText().toString());
						getTagMap(getActivity()).addTag(getActivity(), newTag);
						// listOfTags.add(newTag);
					} else
						t.rename(getActivity(), input.getText().toString());

				}

				value = null;
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Do nothing.
			}
		});
		AlertDialog build = builder.create();
		build.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see group5.trackerexpress.TView#update(group5.trackerexpress.TModel)
	 */
	@Override
	public void update(TModel model) {
		ArrayList<Tag> listOfTags = getTagMap(getActivity()).toList();
		if (listOfTags == null) {
			listOfTags = new ArrayList<Tag>();
		}
		MainTagListAdapter a = new MainTagListAdapter(getActivity(), listOfTags);
		lv_tag_list.setAdapter(a);
	}
}