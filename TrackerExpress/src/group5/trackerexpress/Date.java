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
	
	public int getMM(){
		return mm;
	}
	
	public void setYYYY( int yyyy ){
		this.yyyy = yyyy;
	}
	
	public int getYYYY(){
		return yyyy;
	}
	
	@Override
	public int compareTo(Date d) {
		// TODO Auto-generated method stub
		if ( ! (d instanceof Date) ){
			// Just return they are not equal. 
			// Should not be used in practice.
			// This is just a safeguard.
			return 1;
		}
		
		Integer int1 = (Integer) this.getDate();
		Integer int2 = (Integer) d.getDate();
		
		return int1.compareTo(int2);
	}
	
	public boolean beforeAfterCheck(Date before, Date after){
		return (after.getDate() > before.getDate());
	}
	
	public boolean validDate(Date d){
		boolean valid = true;
		if (d.yyyy > 9999 || d.yyyy < 0)
			return false;
		if (d.mm > 12 || d.mm < 1)
			return false;
		if (d.dd > 31 || d.dd < 1)
			return false;
		if ((d.mm < 8 && d.mm%2 == 0) || (d.mm > 7 && d.mm%2 == 1)){
			//February
			if (d.mm == 2){
				//Leap Years
				if (d.yyyy%4 == 0){
					if (d.yyyy%100 != 0 || d.yyyy%400 == 0){
						if (d.dd > 29)
							return false;
					}
				}
				if (d.dd > 28)
					return false;
			}
			if (d.dd > 30)
				return false;
		}
		return valid;
	}
}
