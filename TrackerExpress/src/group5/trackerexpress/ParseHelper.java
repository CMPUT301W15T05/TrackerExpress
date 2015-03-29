package group5.trackerexpress;

/**
 * Helper class for checking if numbers are valid.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class ParseHelper {
	
	/**
	 * Checks if is integer parsable.
	 *
	 * @param text the text
	 * @return true, if is integer parsable
	 */
	public static boolean isIntegerParsable( String text ){
		try{
			Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return false;
		} catch ( NullPointerException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if is double parsable.
	 *
	 * @param text the text
	 * @return true, if is double parsable
	 */
	public static boolean isDoubleParsable( String text ){
		try{
			Double.parseDouble(text);
		} catch ( NumberFormatException e ) {
			return false;
		} catch ( NullPointerException e ) {
			return false;
		}
		return true;
	}

}
