package group5.trackerexpress;

import android.app.Activity;

/**
 * Image of a receipt, held by expense objects. Just contains a path to the bitmap. This might be changed. 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class Receipt extends Activity{
	
	/** The path of the image. */
	String path;

	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the path.
	 *
	 * @param path the new path
	 */
	public void setPath(String path) {
		this.path = path;
	}
}
