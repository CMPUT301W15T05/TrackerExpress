package group5.trackerexpress;

public class ParseHelper {
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
