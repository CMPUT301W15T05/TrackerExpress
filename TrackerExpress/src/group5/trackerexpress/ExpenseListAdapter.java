package group5.trackerexpress;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The Class ExpenseListAdapter.
 */
public class ExpenseListAdapter extends ArrayAdapter<Expense> {
	
	/** The expense list. */
	private ArrayList<Expense> expenseList;
	
	/** The context. */
	private Context context;
	
	/**
	 * Instantiates a new expense list adapter.
	 *
	 * @param context the context
	 * @param arrayListExpense the expenses
	 */
	public ExpenseListAdapter(Context context, ArrayList<Expense> arrayListExpense){
		super(context, R.layout.fragment_expense_list_item, arrayListExpense);
		this.expenseList = arrayListExpense;
		this.context = context;
	}
	
	/**
	 * The Class ExpenseHolder.
	 */
	private static class ExpenseHolder{
		
		/** The expense name. */
		public TextView expenseTitle;
		
		/** The date. */
		public TextView date;
		
		/** The category. */
		public TextView category;
		
		/** The amount. */
		public TextView amount;
		
		/** The status. */
		public TextView status;
		
		/** The receipt. */
		public ImageView receipt;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		View v = convertView;
		ExpenseHolder holder = new ExpenseHolder();
		
		if ( convertView == null ){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.fragment_expense_list_item, null);
			
			holder.expenseTitle = (TextView) v.findViewById(R.id.tv_expense_list_title);
			holder.date = (TextView) v.findViewById(R.id.tv_expense_date);
			holder.category = (TextView) v.findViewById(R.id.tv_expense_list_category);
			holder.amount = (TextView) v.findViewById(R.id.tv_expense_list_amount);
			holder.receipt = (ImageView) v.findViewById(R.id.iv_expense_receipt);
			holder.status = (TextView) v.findViewById(R.id.tv_expense_list_status);

			v.setTag(holder);
		} else {
			holder = (ExpenseHolder) v.getTag();
		}
		
		Expense e = expenseList.get(position);
		holder.expenseTitle.setText(String.valueOf(e.getTitle()));
		
		if(e.isComplete()){
			holder.status.setText("");
		}else{
			holder.status.setText("INCOMPLETE");
		}
		
		if (e.getDate() != null) {
			holder.date.setText(e.getDate().getShortString());
		}
		
		if ( e.getAmount() != null ){
			String amountSpent = String.valueOf(e.getAmount());
			holder.amount.setText(amountSpent + " " + e.getCurrency());
		} 
		
		holder.category.setText(e.getCategory());
		holder.receipt.setImageBitmap(e.getBitmap());
		
		return v;
	}
}
