package group5.trackerexpress;

// TODO: Auto-generated Javadoc
// Simple date object that consists of an integer
// in the form of DDMMYYYY
/**
 * The Class Date.
 * 
 * The Date object that consists of an integer
 * in the form of DDMMYYYY.
 * 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class Date implements Comparable<Date>{
	
	/** The day of the Date. */
	private Integer dd;
	
	/** The month of the Date. */
	private Integer mm;
	
	/** The year of the Date. */
	private Integer yyyy;
	
	/**
	 * Instantiates a new date.
	 *
	 * @param yyyy the yyyy
	 * @param mm the mm
	 * @param dd the dd
	 */
	public Date ( int yyyy, int mm, int dd ){
		this.dd = dd;
		this.mm = mm;
		this.yyyy = yyyy;
	}
	
	/**
	 * Instantiates a new date.
	 */
	public Date() {
	}

	/**
	 * Gets the date in a single integer.
	 *
	 * @return the date
	 */
	public Integer getDate(){
		return yyyy * 10000 + mm * 100 + dd;
	}
	
	/**
	 * Sets the day.
	 *
	 * @param dd the new day
	 */
	public void setDD( int dd ){
		this.dd = dd;
	}
	
	/**
	 * Gets the day.
	 *
	 * @return the day
	 */
	public int getDD(){
		return dd;
	}
	
	/**
	 * Sets the month.
	 *
	 * @param mm the new month
	 */
	public void setMM( int mm ){
		this.mm = mm;
	}
	
	/**
	 * Gets the month.
	 *
	 * @return the month
	 */
	public int getMM(){
		return mm;
	}
	
	/**
	 * Sets the year.
	 *
	 * @param yyyy the new year
	 */
	public void setYYYY( int yyyy ){
		this.yyyy = yyyy;
	}
	
	/**
	 * Gets the year.
	 *
	 * @return the year
	 */
	public int getYYYY(){
		return yyyy;
	}
	
	/**
	 * Gets the date as a string in the form DD/MM/YYYY
	 * @return the date as a string
	 */
	
	public String toString() {
		return dd.toString() + "/" + mm.toString() + "/" + yyyy.toString();
	}
	
	/**
	 * compares the date of the instance to the date passed int
	 * @param d the Date to be compared with
	 * @return result of comparison as an integer
	 */
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
	
	/**
	 * Checks if the after date occurs after the before date.
	 *
	 * @param before the before
	 * @param after the after
	 * @return true, if after date is after before date
	 */
	public boolean beforeAfterCheck(Date before, Date after){
		return (after.getDate() > before.getDate());
	}
	
	/**
	 * Checks if date is valid 
	 * including if day exists in given month 
	 * and leap year check
	 *
	 * @param d the Date to check
	 * @return true, if valid Date
	 */
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
