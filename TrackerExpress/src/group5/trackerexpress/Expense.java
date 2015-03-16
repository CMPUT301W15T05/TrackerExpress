package group5.trackerexpress;

import java.util.UUID;

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * The Class Expense.
 */
public class Expense extends TModel{
	
	/** The title. */
	private String title;
	
	/** The status. */
	private int status; 
	
	/** The date. */
	private Date date;
	
	/** The uuid. */
	private UUID uuid;
	
	/** The currency. */
	private String currency;
	
	/** The amount. */
	private Double amount;
	
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
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param context the context
	 * @param status the status
	 */
	public void setStatus(Context context, int status) {
		this.status = status;
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
	 * @param context the context
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
		// TODO Auto-generated method stub
		return title;
	}

	/**
	 * Sets the date.
	 *
	 * @param context the context
	 * @param d1 the d1
	 */
	public void setDate(Context context, Date d1) {
		this.date = d1;
		notifyViews(context);
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
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
	 * @param context the context
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
	 * @param context the context
	 * @param amount the amount
	 */
	public void setAmount(Context context, Double amount) {
		this.amount = amount;
		notifyViews(context);
	}
	
}