package group5.trackerexpress;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	public static final int INDEX_OF_MY_CLAIMS_TAB = 1;
	public static final int INDEX_OF_TAGS_TAB = 1;
	public static final int INDEX_OF_APPROVE_CLAIMS_TAB = 1;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	private ArrayList<Tag> tagList;
	private Context mainContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			Bundle args = new Bundle();
			args.putInt(FragmentMyClaims.ARG_SECTION_NUMBER, position + 1 );
			
			switch( position ){
				case 0: 
					fragment = new FragmentMyClaims();
					break;
				case 1:
					fragment = new FragmentTagList();
					break;
				case 2:
					fragment = new FragmentGlobalClaims();
					break;
				default: Log.i("myMessage", "This should never happen");
			}
			
			fragment.setArguments(args);

			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	public static class FragmentMyClaims extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		private ListView lv_claim_list;
		private MainClaimListAdapter adapter;
		private Button b_add_claim;

		// Menu items to hide when selecting an option on a claim
		private static final int[] submittedOrApprovedHiddenItems = {R.id.op_edit_claim, R.id.op_submit_claim};
		
		public FragmentMyClaims() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_my_claims,
					container, false);
			
			// Fragment's views
			lv_claim_list = (ListView) rootView.findViewById(R.id.lv_my_claims);
			lv_claim_list.setItemsCanFocus(true);
			b_add_claim = (Button) rootView.findViewById(R.id.b_add_claim);
			
			final ClaimList listOfClaims = ClaimController.getInstance(getActivity()).getClaimList();
			final Claim[] arrayClaims = listOfClaims.getAllClaims();
			
			adapter = new MainClaimListAdapter(getActivity(), arrayClaims);
			lv_claim_list.setAdapter(adapter);
			
			b_add_claim.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent( getActivity(), EditClaimActivity.class );
					intent.putExtra("isNewClaim", true);
					startActivity(intent);
				}
				
			});
			
			lv_claim_list.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> a, View v,
						final int position, long arg3) {
					// TODO Auto-generated method stub
					
					final Claim c = (Claim) lv_claim_list.getAdapter().getItem(position);
					
					PopupMenu popup = new PopupMenu(getActivity(), v);
					popup.getMenuInflater().inflate(R.menu.tag_list_popup, popup.getMenu());
					
					onPrepareOptionsMenu(popup, c);
					
					// Popup menu item click listener
					popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
						
						@Override
	                    public boolean onMenuItemClick(MenuItem item) {
                        	Claim claimAnalyzed = (Claim) lv_claim_list.getAdapter().getItem(position);
                        	Intent intent;
	                        switch(item.getItemId()){
	                        case R.id.op_delete_claim: 
	                        	// Delete tag off of Claim ArrayList for listview
	                        	listOfClaims.deleteClaim(getActivity(), claimAnalyzed.getUuid());
	                        	Claim[] arrayClaims = listOfClaims.getAllClaims();
	                        	MainClaimListAdapter a = new MainClaimListAdapter( getActivity().getBaseContext(), arrayClaims );
	                			lv_claim_list.setAdapter(a);
	                			// Delete it off the model
	                        	listOfClaims.deleteClaim(getActivity(), claimAnalyzed.getUuid());
	                        	break;
	                        case R.id.op_edit_claim:
	                        	intent = new Intent( getActivity(), EditClaimActivity.class );
	                        	intent.putExtra( "isNewClaim", false );
	                        	intent.putExtra("claimUUID", c.getUuid());
	                        	startActivity(intent);
	                        	break;
	                        case R.id.op_view_claim:
	                        	intent = new Intent( getActivity(), ViewClaimActivity.class );
	                        	intent.putExtra( "claimUUID",  c.getUuid() );
	                        	startActivity(intent);
	                        	break;
	                        case R.id.op_submit_claim:
	                        	// TODO:
	                        	// Submit the claim to server
	                        	// using controller
	                        	break;
	                        default: break;
	                        }
	                    	
	                        return true;
	                    }
	                });
					
		            popup.show();					
				}
				
			});
			
			return rootView;
		}
		
		public void onPrepareOptionsMenu( PopupMenu popup, Claim c ){
			switch(c.getStatus()){
			case Claim.APPROVED:
			case Claim.SUBMITTED:
				for( int id : submittedOrApprovedHiddenItems ){
					popup.getMenu().findItem(id).setVisible(false);
				}
				break;
			default:
				break;
			}
		}
	}

	public static class FragmentTagList extends Fragment implements TView {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		private ListView lv_tag_list;
		private MainTagListAdapter adapter;
		private Editable value = null;
		private Button b_add_tag;
		
		public FragmentTagList() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tags_list,
					container, false);

			lv_tag_list = (ListView) rootView.findViewById(R.id.lv_tags);
			lv_tag_list.setItemsCanFocus(true);
			
			final TagMap mapOfTags = TagController.getInstance(getActivity()).getTagMap();
			final ArrayList<Tag> listOfTags = mapOfTags.getTags();
			final Context myContext = getActivity().getApplicationContext();
			
			b_add_tag = (Button) rootView.findViewById(R.id.b_add_tag);
			
			b_add_tag.setOnClickListener(new Button.OnClickListener(){
			    public void onClick(View v) {
			    	getName();
			    	if ( value != null ){
			    		String name = value.toString();
			    		Tag newTag = new Tag(name);
			    		mapOfTags.addTag(getActivity(), newTag);
			    		listOfTags.add(newTag);
			    		MainTagListAdapter a = new MainTagListAdapter( myContext, listOfTags );
            			lv_tag_list.setAdapter(a);
			    	}
			    	
			    	value = null;
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
	                        	listOfTags.remove(t);
	                        	MainTagListAdapter a = new MainTagListAdapter( getActivity().getBaseContext(), listOfTags );
	                			lv_tag_list.setAdapter(a);
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
	        /*
	         * Causes error since TagListArrayAdapter uses listOfTags.get() which apparently is 
	         * an Hashmap cast as an ArrayList<Tag> 
	        listOfTags.get(0); <- Something is up with get()
			adapter = new MainTagListAdapter( myContext, listOfTags );
			*/
			lv_tag_list.setAdapter(adapter);

			return rootView;
		}
		private void getName(){
			String message = "Enter a new name";
			final EditText input = new EditText(getActivity());
			
			new AlertDialog.Builder(getActivity())
		    .setTitle("Rename Tag")
		    .setMessage(message)
		    .setView(input)
		    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {       		
		        	value = input.getText();
		        }
		    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		            // Do nothing.
		        }
		    }).show();
			
		}

		@Override
		public void update(TModel model) {
			// TODO Auto-generated method stub
			
		}
	}

	public static class FragmentGlobalClaims extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public FragmentGlobalClaims() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	public Context getThis() {
		return this;
	}

}
