package group5.trackerexpress;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * Super class for CreateAccount and Login Activities
 * that contains their necessary string checking functions
 * 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 * @see CreateAccountActivity, LoginActivity
 */
public class AccountFormActivity extends Activity {

	/** The progress view for setting spinners of each activity. */
	private View mProgressView;
	
	/** The form view for setting spinners of each activity. */
	private View mFormView;
	
	/**
	 * Gets the progress view, used to show progress
	 *
	 * @return the progress view
	 */
	protected View getProgressView() {
		return mProgressView;
	}

	/**
	 * Sets the progress view.
	 *
	 * @param mProgressView the new progress view
	 */
	protected void setProgressView(View mProgressView) {
		this.mProgressView = mProgressView;
	}

	/**
	 * Gets the form view.
	 *
	 * @return the form view
	 */
	protected View getFormView() {
		return mFormView;
	}

	/**
	 * Sets the form view.
	 *
	 * @param mFormView the new form view
	 */
	protected void setFormView(View mFormView) {
		this.mFormView = mFormView;
	}

	/**
	 * Checks for a valid email address.
	 *
	 * @param email
	 * @param mEmailView email view
	 * @return true if valid
	 */
	protected boolean emailErrors(String email, TextView mEmailView) {
		
		if (TextUtils.isEmpty(email)) {
			setError(mEmailView, R.string.error_field_required);
			return true;
		} else if (!isEmailValid(email)) {
			setError(mEmailView, R.string.error_invalid_email);
			return true;
		}
			
		return false;
	}
	

	/**
	 * Check for a valid password.
	 *
	 * @param password the password
	 * @param mPasswordView the m password view
	 * @return true, if valid
	 */
	protected boolean passwordErrors(String password, TextView mPasswordView) {
		
		if (TextUtils.isEmpty(password)) {
			setError(mPasswordView, R.string.error_field_required);
			return true;
		} else if (!isPasswordValid(password)) {
			setError(mPasswordView, R.string.error_invalid_password);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if is email valid.
	 *
	 * @param email the email
	 * @return true, if email is valid
	 */
	protected boolean isEmailValid(String email) {
		boolean valid = true;
		if (!email.contains("."))
			valid = false;
		else if (email.indexOf("@") == 0) // Check if there are chars before @ symbol
			valid = false;
		else if (email.indexOf("@") == email.length()-1) // Check if there are chars after @ symbol
			valid = false;
		else if (!email.contains("@"))
			valid = false;
		return valid;
	}

	/**
	 * Checks if is password valid.
	 *
	 * @param password the password
	 * @return true, if is password valid
	 */
	protected boolean isPasswordValid(String password) {
		return password.length() > 4;
	}
	
	/**
	 * Sets the error.
	 *
	 * @param view the view
	 * @param errorID the error id
	 */
	protected void setError(TextView view, int errorID) {
		view.setError(getString(errorID));
		view.requestFocus();
	}
	
	/**
	 * Shows the progress UI and hides the form.
	 *
	 * @param show true if ui should be shown
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	protected void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
}
