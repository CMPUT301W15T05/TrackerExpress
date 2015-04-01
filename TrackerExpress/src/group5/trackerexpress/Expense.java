package group5.trackerexpress;

import java.util.UUID;


import android.content.Context;
import android.graphics.Bitmap;

/**
 * An expense item hold data for a signle purchase, as well as a photo and location.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class Expense extends TModel{
	
	private static final long serialVersionUID = 1L;

	/** The title of the expense. */
	private String title;
	
	/** The date of the expense. */
	private Date date;
	
	/** The id of the expense. */
	private UUID uuid;
	
	/** The currency of the expense. */
	private String currency;
	
	/** The category of the expense. */
	private String category;
	
	/** The amount of the expense. */
	private Double amount;
	
	/** The image of the receipt for the expense. */
	private Bitmap bitmap;

	/** The status of the expense (incompleteness). */
	private boolean complete; 
	
	/**
	 * Instantiates a new expense.
	 */
	public Expense() {
		uuid = UUID.randomUUID();
	}

	/**
	 * Instantiates a new expense.
	 *
	 * @param string the string
	 */
	public Expense(String string) {
		this.title = string;
		uuid = UUID.randomUUID();
	}
    
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public boolean isComplete() {
		return complete;
	}

	/**
	 * Sets the status.
	 *
	 * @param context Needed for file IO
	 * @param status the status
	 */
	public void setComplete(Context context, boolean complete) {
		this.complete = complete;
		notifyViews(context);
	}

	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * Sets the title.
	 *
	 * @param context Needed for file IO
	 * @param title the title
	 */
	public void setTitle(Context context, String title) {
		this.title = title;
		notifyViews(context);
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the date.
	 *
	 * @param context Needed for file IO
	 * @param d1 the Date
	 */
	public void setDate(Context context, Date convertedDateSelection) {
		this.date = convertedDateSelection;
		notifyViews(context);
	}

	/**
	 * Gets the year.
	 *
	 * @return the year
	 */
	public Date getDate() {
		// TODO Auto-generated method stub
		return date;
	}


	/**
	 * Gets the currency.
	 *
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * Sets the currency.
	 *
	 * @param context Needed for file IO
	 * @param currency the currency
	 */
	public void setCurrency(Context context, String currency) {
		this.currency = currency;
		notifyViews(context);
	}

	/**
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * Sets the amount.
	 *
	 * @param context Needed for file IO
	 * @param amount the amount
	 */
	public void setAmount(Context context, Double amount) {
		this.amount = amount;
		notifyViews(context);
	}
	
	/**
	 * Gets the Category
	 * 
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category
	 * 
	 * @param context
	 * @param category
	 */
	public void setCategory(Context context, String category){
		// TODO Auto-generated method stub
		this.category = category;
		notifyViews(context);
		
	}
	
	/**
	 * Gets the receipt image
	 * @return bitmap the receipt image
	 */
	public Bitmap getBitmap(){
		return bitmap;
	}
	
	/**
	 * Sets the receipt image
	 * @param context
	 * @param bitmap
	 */

	public void setBitmap(Context context, Bitmap receipt) {
		// TODO Auto-generated method stub
		this.bitmap = bitmap;
		notifyViews(context);
	}
	
}