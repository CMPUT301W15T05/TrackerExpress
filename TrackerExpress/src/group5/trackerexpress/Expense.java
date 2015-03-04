package group5.trackerexpress;

import java.util.ArrayList;
import java.util.UUID;

public class Expense {
	
	private String title;
	private ArrayList<Expense> expenseList;
	private int status; 
	private Date date;
    
	public static final int NO_FLAG = 0;
	public static final int FLAG = 1;

	
	
	private UUID uuid = UUID.randomUUID();

	public Expense(String title) {
		// TODO Auto-generated constructor stub
		this.title = title;
	}
	
	public Expense() {
		// TODO Auto-generated constructor stub
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
		return 0;
	}

	public UUID getId() {
		// TODO Auto-generated method stub
		return uuid;
	}
}
