package group5.trackerexpress;

// TODO: Auto-generated Javadoc
// Simple date object that consists of an integer
// in the form of DDMMYYYY
/**
 * The Class Date.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class Date implements Comparable<Date>{
	
	/** The dd. */
	private Integer dd;
	
	/** The mm. */
	private Integer mm;
	
	/** The yyyy. */
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
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Integer getDate(){
		return yyyy * 10000 + mm * 100 + dd;
	}
	
	/**
	 * Sets the dd.
	 *
	 * @param dd the new dd
	 */
	public void setDD( int dd ){
		this.dd = dd;
	}
	
	/**
	 * Gets the dd.
	 *
	 * @return the dd
	 */
	public int getDD(){
		return dd;
	}
	
	/**
	 * Sets the mm.
	 *
	 * @param mm the new mm
	 */
	public void setMM( int mm ){
		this.mm = mm;
	}
	
	/**
	 * Gets the mm.
	 *
	 * @return the mm
	 */
	public int getMM(){
		return mm;
	}
	
	/**
	 * Sets the yyyy.
	 *
	 * @param yyyy the new yyyy
	 */
	public void setYYYY( int yyyy ){
		this.yyyy = yyyy;
	}
	
	/**
	 * Gets the yyyy.
	 *
	 * @return the yyyy
	 */
	public int getYYYY(){
		return yyyy;
	}
	
	public String getString() {
		return dd.toString() + "/" + mm.toString() + "/" + yyyy.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
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
	 * Before after check.
	 *
	 * @param before the before
	 * @param after the after
	 * @return true, if successful
	 */
	public boolean beforeAfterCheck(Date before, Date after){
		return (after.getDate() > before.getDate());
	}
	
	/**
	 * Valid date.
	 *
	 * @param d the d
	 * @return true, if successful
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
