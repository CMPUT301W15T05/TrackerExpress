package group5.trackerexpress;

/**
 * TrackerExpress View. Interface that all views (aka activity) classes implement.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public interface TView {
	
	/**
	 * Called by models to let the view know its data has changed and it needs to update views, such as
	 * list adapters.
	 *
	 * @param model The model that called the function. May or may not be used, depending on how
	 * the method was implemented.
	 */
	public void update(TModel model);
}
