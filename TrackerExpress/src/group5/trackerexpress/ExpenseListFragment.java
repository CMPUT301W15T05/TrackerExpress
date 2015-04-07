package group5.trackerexpress;

import com.google.android.gms.maps.model.LatLng;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Fragment that displays the expense list.
 * @authors Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
@SuppressLint("ValidFragment")
public class ExpenseListFragment extends Fragment implements TView {

	/** The claim. */
	private Claim claim;

	/** Tells whether the Add Expense button should show */
	private boolean myClaimListVersion;
	
	private ListView lv_expense_list;
	
	//private ExpenseListAdapter adapter;

	private Button b_add_expense;
	
	/**
	 * Instantiates a new expense list fragment.
	 *
	 * @param claim the claim
	 * @param myClaimListVersion 
	 */
	@SuppressLint("ValidFragment")
	public ExpenseListFragment(Claim claim, boolean myClaimListVersion) {
		this.claim = claim;
		this.myClaimListVersion = myClaimListVersion;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_expense_list,
				container, false);
		
		// Fragment's views
		lv_expense_list = (ListView) rootView.findViewById(R.id.lv_my_expenses);
		lv_expense_list.setItemsCanFocus(true);
		
		update(null);
		claim.getExpenseList().addView(this);

		b_add_expense = (Button) rootView.findViewById(R.id.b_add_expense);
		if (claim.getStatus() == Claim.SUBMITTED || claim.getStatus() == Claim.APPROVED)
			b_add_expense.setVisibility(View.GONE);

		if ( ! myClaimListVersion ){
			b_add_expense.setVisibility(View.GONE);
		}
		
		b_add_expense.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent( getActivity(), EditExpenseActivity.class );
				intent.putExtra("claimUUID", claim.getUuid());
            	intent.putExtra("isNewExpense", true);
				
				startActivity(intent);
			}	
		});
		
		lv_expense_list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> a, View v,
					final int position, long arg3) {
				// TODO Auto-generated method stub
				
				final Expense clickedOnExpense = (Expense) lv_expense_list.getAdapter().getItem(position);
				
				PopupMenu popup = new PopupMenu(getActivity(), v);
				popup.getMenuInflater().inflate(R.menu.expense_list_popup, popup.getMenu());
				
				onPrepareOptionsMenu(popup);
				if (!clickedOnExpense.isHasLocation() || myClaimListVersion)
					popup.getMenu().findItem(R.id.op_view_location).setVisible(false);
				// Popup menu item click listener
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					
					@Override
                    public boolean onMenuItemClick(MenuItem item) {
                    	Intent intent;
                        switch(item.getItemId()){
                        case R.id.op_delete_expense: 
                        	claim.getExpenseList().removeExpense(getActivity(), clickedOnExpense.getUuid());
                        	break;
                        case R.id.op_edit_expense:
                        	intent = new Intent( getActivity(), EditExpenseActivity.class );
            				intent.putExtra("claimUUID", claim.getUuid());
                        	intent.putExtra("expenseUUID", clickedOnExpense.getUuid());
                        	startActivity(intent);
                        	break;

                        case R.id.op_view_location:
                        	LatLng latlng = new LatLng(clickedOnExpense.getLatitude(), clickedOnExpense.getLongitude());
                        	intent = new Intent (getActivity(), MapActivity.class);
                        	intent.putExtra("latlng", latlng);
<<<<<<< HEAD
                        	break;
                        	
                        case R.id.op_view_image:                      	
                        	intent = new Intent(getActivity(), ViewImageDialog.class);
                        	Receipt receipt = clickedOnExpense.getReceipt();
                        	if(receipt != null){
                        		intent.putExtra("filePath", receipt.getPath());
                        		startActivity(intent);
                        		
                        	}else{
                        		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    			alertDialog.setTitle("Warning");
                    			alertDialog.setMessage("No picture was taken.");
                    			alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    				public void onClick(DialogInterface dialog, int which) {			
                    				}
                    			});
                    			alertDialog.show();
                        	}
              
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

	public void onPrepareOptionsMenu( PopupMenu popup ){
		if ( ! myClaimListVersion ||
				claim.getStatus() == Claim.APPROVED || 
				claim.getStatus() == Claim.SUBMITTED ){
			popup.getMenu().findItem(R.id.op_edit_expense).setVisible(false);
			popup.getMenu().findItem(R.id.op_delete_expense).setVisible(false);
		}
	}
	
	/* 
	 * @see group5.trackerexpress.TView#update(group5.trackerexpress.TModel)
	 */
	@Override
	public void update(TModel model) {
		if (claim.getExpenseList().size() > 0)
			lv_expense_list.setAdapter( new ExpenseListAdapter(getActivity(), 
						claim.getExpenseList().toList()));
	}
	
	@Override
	public void onDestroy(){
		claim.getExpenseList().deleteView(this);
		super.onDestroy();
	}

}
