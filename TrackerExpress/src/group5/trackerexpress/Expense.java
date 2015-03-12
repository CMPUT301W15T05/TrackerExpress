package group5.trackerexpress;

import java.util.UUID;

import android.content.Context;

public class Expense extends TModel{
	
	private String title;
	private int status; 
	private Date date;
	private UUID uuid;
	private String currency;
	private Double amount;
	
	public Expense() {
		uuid = UUID.randomUUID();
	}

	public Expense(String string) {
		this.title = string;
		uuid = UUID.randomUUID();
	}
    
	public int getStatus() {
		return status;
	}

	public void setStatus(Context context, int status) {
		this.status = status;
		notifyViews(context);
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setTitle(Context context, String title) {
		this.title = title;
		notifyViews(context);
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}

	public void setDate(Context context, Date d1) {
		this.date = d1;
		notifyViews(context);
	}

	public Date getDate() {
		// TODO Auto-generated method stub
		return date;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(Context context, String currency) {
		this.currency = currency;
		notifyViews(context);
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Context context, Double amount) {
		this.amount = amount;
		notifyViews(context);
	}
	
}