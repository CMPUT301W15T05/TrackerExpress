package group5.trackerexpress;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A login screen that offers login via email/password.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 * 
 */
public class LoginActivity extends AccountFormActivity {//implements LoaderCallbacks<Cursor> {
	
	/**
	 * Constants for sign-in result.
	 */
	private static final int NEEDS_ACCOUNT = 0;
	
	/** The Constant SIGN_IN_SUCCESS. */
	private static final int SIGN_IN_SUCCESS = 1;
	
	/** The Constant WRONG_PASSWORD. */
	private static final int WRONG_PASSWORD = 2;
	
	/** The Constant NETWORK_ERROR. */
	private static final int NETWORK_ERROR = 3;
	
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;
	
	// UI references.
	/** The m email view. */
	private AutoCompleteTextView mEmailView;
	
	/** The m password view. */
	private EditText mPasswordView;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		User user = Controller.getUser(this);
		if (user.isSignedIn()) {
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
	    	startActivity(intent);
			finish();
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Set up the login form.
		mEmailView = (AutoCompleteTextView) findViewById(R.id.logInEmailText);
		
		// Autocomplete is not necessary, but might be implemented later
		//populateAutoComplete();

		mPasswordView = (EditText) findViewById(R.id.logInPasswordText);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		Button mEmailSignInButton = (Button) findViewById(R.id.signInButton);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});
		
		Button mCreateAccountButton = (Button) findViewById(R.id.createAccountButton);
		mCreateAccountButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
		    	startActivity(intent);
				finish();
			}
		});
		
		// These are used in showProgress()
		setFormView(findViewById(R.id.login_form));
		setProgressView(findViewById(R.id.login_progress));
	}

	/* TODO: More autocomplete stuff that might get axed
	private void populateAutoComplete() {
		getLoaderManager().initLoader(0, null, this);
	}*/

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);
		
		// Store values at the time of the login attempt.
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();

		if(emailErrors(email, mEmailView) || 
				passwordErrors(password, mPasswordView)) {
			return;
		}

		// Show a progress spinner, and kick off a background task to
		// perform the user login attempt.
		showProgress(true);
		mAuthTask = new UserLoginTask(email, password);
		mAuthTask.execute((Void) null);
	}

	// Auto-complete stuff. Not necessary, but might be added at a later time. 
	// TODO: Can probably be moved into the AccountFormActivity superclass.
	/*@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		return new CursorLoader(this,
				// Retrieve data rows for the device user's 'profile' contact.
				Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
						ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
				ProfileQuery.PROJECTION,

				// Select only email addresses.
				ContactsContract.Contacts.Data.MIMETYPE + " = ?",
				new String[] { ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE },

				// Show primary email addresses first. Note that there won't be
				// a primary email address if the user hasn't specified one.
				ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		List<String> emails = new ArrayList<String>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			emails.add(cursor.getString(ProfileQuery.ADDRESS));
			cursor.moveToNext();
		}

		addEmailsToAutoComplete(emails);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader) {

	}

	private interface ProfileQuery {
		String[] PROJECTION = { ContactsContract.CommonDataKinds.Email.ADDRESS,
				ContactsContract.CommonDataKinds.Email.IS_PRIMARY, };

		int ADDRESS = 0;
		int IS_PRIMARY = 1;
	}

	private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
		// Create adapter to tell the AutoCompleteTextView what to show in its
		// dropdown list.
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				LoginActivity.this,
				android.R.layout.simple_dropdown_item_1line,
				emailAddressCollection);

		mEmailView.setAdapter(adapter);
	}*/
	
	/**
	 * Represents an asynchronous login task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

		/** The m email. */
		private final String mEmail;
		
		/** The m password. */
		private final String mPassword;

		/**
		 * Instantiates a new user login task.
		 *
		 * @param email the email
		 * @param password the password
		 */
		UserLoginTask(String email, String password) {
			mEmail = email;
			mPassword = password;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Integer doInBackground(Void... params) {

			//FIXME: Compare credentials against server
			if (true){
				return SIGN_IN_SUCCESS; 
			}
			
			String[] credentials = new EmailElasticSearchEngine().getCredentials();

			
			for (String credential : credentials) {
				String[] pieces = credential.split(":");
				
				// Account exists
				if (pieces[0].equals(mEmail)) {
					if (pieces[1].equals(mPassword)) {
						// Return here if the password matches.
						return SIGN_IN_SUCCESS;
					}
					else {
						// Return here if the password doesn't match; there 
						// should only be one instance of each email anyways.
						return WRONG_PASSWORD;
					}
				}
			}

			// email wasn't found in database
			return NEEDS_ACCOUNT;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(final Integer success) {
			mAuthTask = null;

			// Had to do if/else block because switch statements wouldn't break
			// properly with finish()
			if (success == SIGN_IN_SUCCESS) {
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		    	startActivity(intent);
				User user = Controller.getUser(LoginActivity.this);
				user.setSignedIn(true);
				finish();
			} else if (success == WRONG_PASSWORD) {
				showProgress(false);
				setError(mPasswordView, R.string.error_incorrect_password);
			} else if (success == NETWORK_ERROR) {
				showProgress(false);
				Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
			} else if (success == NEEDS_ACCOUNT) {
				showPopUp();
				showProgress(false);
			} else {
				// Nothing should bring the user here, but just in case
				showProgress(false);
				Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
			}
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onCancelled()
		 */
		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}

	// http://www.androiddom.com/2011/06/displaying-android-pop-up-dialog_13.html 11/03/15
	/**
	 * Show pop up.
	 */
	private void showPopUp() {

		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		helpBuilder.setTitle("Email not registered");
		helpBuilder.setMessage("Would you like to create an account?");
		helpBuilder.setCancelable(false);
		helpBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
		    public void onClick(DialogInterface dialog, int which) {
				// Go to the create Account activity
				Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
		    	startActivity(intent);
				finish();
		    }
		   });

		 helpBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

		  @Override
		  public void onClick(DialogInterface dialog, int which) {
		   // Do nothing but close the dialog
		  }
		 });
		 
		 // Remember, create doesn't show the dialog
		 AlertDialog helpDialog = helpBuilder.create();
		 helpDialog.show();
	}

}