package group5.trackerexpress;

import java.util.ArrayList;
import java.util.UUID;

public class Expense {
	
	private String title;
	private int status; 
	private int order;
	private Date date;
    
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public static final int NO_FLAG = 0;
	public static final int FLAG = 1;

	private UUID uuid;
	
	public Expense() {
		// TODO Auto-generated constructor stub
		uuid = UUID.randomUUID();
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}

	public void setDate(Date d1) {
		// TODO Auto-generated method stub
		this.date = d1;
	}

	public Date getDate() {
		// TODO Auto-generated method stub
		return date;
	}

	public int getOrder() { // returns order from within it's list
		return order;
	}

	public UUID getId() {
		// TODO Auto-generated method stub
		return uuid;
	}
}
