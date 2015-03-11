package group5.trackerexpress;

import java.util.UUID;

import android.content.Context;

public class Expense extends TModel{
	
	private String title;
	private int status; 
	private int order;
	private Date date;
	
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

	public void setOrder(Context context, int order) {
		this.order = order;
		notifyViews(context);
	}

	public static final int NO_FLAG = 0;
	public static final int FLAG = 1;

	private UUID uuid;
	

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

	public int getOrder() { // returns order from within it's list
		return order;
	}
}