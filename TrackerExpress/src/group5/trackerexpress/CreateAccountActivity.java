package group5.trackerexpress;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccountActivity extends AccountFormActivity {
	
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };

	
	/**
	 * Constants for create account result.
	 */
	public static final int ACCOUNT_SUCCESS = 0;
	public static final int EMAIL_TAKEN = 1;
	public static final int NETWORK_ERROR = 2;
	
	private UserAccountTask mAuthTask = null;
	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private EditText mConfirmPasswordView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		
		
		mEmailView = (EditText) findViewById(R.id.createAccountEmailText);
		mPasswordView = (EditText) findViewById(R.id.createAccountPasswordText);
		
		// http://stackoverflow.com/questions/5203477/android-edittext-hint 11/03/15
		// Makes parts of the hint text different sizes
		String[] hintText = getString(R.string.prompt_create_account_password).split(" ", 2);
		mPasswordView.setHint(Html.fromHtml("<font size=\"16\">" + hintText[0] 
				+ " " + "</font>" + "<small>" + hintText[1] + "</small>" ));
		
		mConfirmPasswordView = (EditText) findViewById(R.id.createAccountConfirmPasswordText);
		
		Button mConfirmCreateAccountButton = (Button) findViewById(R.id.confirmCreateAccountButton);
		mConfirmCreateAccountButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptCreateAccount();
			}
		});
		
		Button mCancelCreateAccountButton = (Button) findViewById(R.id.cancelCreateAccountButton);
		mCancelCreateAccountButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
		    	startActivity(intent);
		    	finish();
			}
		});

		// These are used in showProgress()
		setFormView(findViewById(R.id.create_account_form));
		setProgressView(findViewById(R.id.create_account_progress));
	}
	
	/**
	 * Attempts to create the account specified by the form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptCreateAccount() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);
		mConfirmPasswordView.setError(null);

		// Store values at the time of the create account attempt.
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();
		String confirmPassword = mConfirmPasswordView.getText().toString();
		String name = mConfirmPasswordView.getText().toString();
		
		if (emailErrors(email, mEmailView) || 
				passwordErrors(password, mPasswordView)) {
			return;
		}
				
		// Check that the password fields match.
		if (TextUtils.isEmpty(confirmPassword) || 
				!TextUtils.equals(password, confirmPassword)) {
			setError(mConfirmPasswordView, R.string.error_mismatched_passwords);
			return;
		}

		// Show a progress spinner, and kick off a background task to
		// perform the user create account attempt.
		showProgress(true);
		mAuthTask = new UserAccountTask(email, password, name);
		mAuthTask.execute((Void) null);
	}
	
	/**
	 * Represents an asynchronous login task used to authenticate
	 * the user.
	 */
	public class UserAccountTask extends AsyncTask<Void, Void, Integer> {

		private final String mEmail;
		private final String mPassword;
		private final String mName;

		UserAccountTask(String email, String password, String name) {
			mEmail = email;
			mPassword = password;
			mName = name;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				Thread.sleep(0); //Thread.sleep(2000);
			} catch (InterruptedException e) {
				return NETWORK_ERROR;
			}

			for (String credential : DUMMY_CREDENTIALS) {
				String[] pieces = credential.split(":");
				
				// Account already exists
				if (pieces[0].equals(mEmail)) {
					return EMAIL_TAKEN;
				}
			}

			// Email wasn't found in database, so add it right away

			// Update User object
			User user = UserController.getInstance(CreateAccountActivity.this).getUser();
			user.setEmail(CreateAccountActivity.this, mEmail);
			user.setPassword(CreateAccountActivity.this, mPassword);
			user.setName(CreateAccountActivity.this, mName);
			
			// TODO: add user to the proper database
			
			return ACCOUNT_SUCCESS;
		}

		@Override
		protected void onPostExecute(final Integer success) {
			mAuthTask = null;

			// Had to do if/else block because switch statements wouldn't break
			// properly with finish()
			if (success == ACCOUNT_SUCCESS) {
				Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
		    	startActivity(intent);
				finish();
			} else if (success == EMAIL_TAKEN) {
				showProgress(false);
				setError(mEmailView, R.string.error_email_taken);
			} else if (success == NETWORK_ERROR) {
				showProgress(false);
				Toast.makeText(CreateAccountActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
			} else {
				// Nothing should bring the user here, but just in case
				showProgress(false);
				Toast.makeText(CreateAccountActivity.this, "Error", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}


}
