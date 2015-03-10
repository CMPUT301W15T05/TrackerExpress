package group5.trackerexpress;

// Simple date object that consists of an integer
// in the form of DDMMYYYY
public class Date implements Comparable<Date>{
	private Integer dd;
	private Integer mm;
	private Integer yyyy;
	
	public Date ( int yyyy, int mm, int dd ){
		this.dd = dd;
		this.mm = mm;
		this.yyyy = yyyy;
	}

	public Date() {
	}

	public Integer getDate(){
		return yyyy * 10000 + mm * 100 + dd;
	}
	
	public void setDD( int dd ){
		this.dd = dd;
	}
	
	public int getDD(){
		return dd;
	}
	
	public void setMM( int mm ){
		this.mm = mm;
	}
	
	public int MM(){
		return mm;
	}
	
	public void setYYYY( int yyyy ){
		this.yyyy = yyyy;
	}
	
	public int YYYY(){
		return yyyy;
	}
	
	@Override
	public int compareTo(Date d) {
		// TODO Auto-generated method stub
		if ( ! (d instanceof Date) ){
			// Just return they are not equal. 
			// Should not be used in practice.
			// This is just a safeguard.
			return -1;
		}
		
		Integer int1 = (Integer) this.getDate();
		Integer int2 = (Integer) d.getDate();
		
		return int1.compareTo(int2);
	}
	
}
