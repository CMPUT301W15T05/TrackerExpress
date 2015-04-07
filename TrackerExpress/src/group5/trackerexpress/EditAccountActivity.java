package group5.trackerexpress;

import com.google.android.gms.maps.model.LatLng;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditAccountActivity extends AccountFormActivity {

	private User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_account);
		
		user = Controller.getUser(this);
		
		TextView mUserName = (TextView) findViewById(R.id.edit_account_user_name);
		TextView mUserEmail = (TextView) findViewById(R.id.edit_account_user_email);

		
		Button mChangePassword = (Button) findViewById(R.id.edit_account_change_password);
		Button mChangeGeolocation = (Button) findViewById(R.id.edit_account_add_geolocation);
		
		mUserName.setText(user.getName());
		mUserEmail.setText(user.getEmail());
		
		displayGeolocation();
		
		mChangePassword.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showPasswordPopUp();
			}
		});
		
		mChangeGeolocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(EditAccountActivity.this, InteractiveMapActivity.class);
				Location location = user.getLocation();
				
				if (location != null) {
					intent.putExtra("latlng", new LatLng(location.getLatitude(), location.getLongitude()));
					intent.putExtra("destination", location.getProvider());
					
				}
				startActivityForResult(intent, 1);
				
			}
		});
		
	}
	
	private void displayGeolocation() {

		final Location location = user.getLocation();
		
		TextView mGeolocationText = (TextView) findViewById(R.id.edit_account_geolocation_text);
		TextView mUserGeolocation = (TextView) findViewById(R.id.edit_account_geolocation);
		TextView mUserGeolocationLatLng = (TextView) findViewById(R.id.edit_account_geolocation_latlng);
		

		if (location != null) {
			mGeolocationText.setText("Home Location:");
			mUserGeolocation.setText(location.getProvider());
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			mUserGeolocationLatLng.setText(BasicMapActivity.latLngFormat(latLng));
		}
	}
	
	private void showPasswordPopUp() {

		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		helpBuilder.setTitle("Change Password");
		helpBuilder.setCancelable(false);
		
		LayoutInflater inflater = getLayoutInflater();
        View popupview = inflater.inflate(R.layout.activity_popup_password, null);
        helpBuilder.setView(popupview);
		
		final EditText prevPass = (EditText) popupview.findViewById(R.id.inputCurPass);
	    final EditText newPass = (EditText) popupview.findViewById(R.id.inputNewPass);
	    final EditText confirmPass = (EditText) popupview.findViewById(R.id.inputConfrimPass);
		
		helpBuilder.setPositiveButton("Change Password", EditableActivity.doNothingClicker);
		helpBuilder.setNegativeButton("Cancel", EditableActivity.doNothingClicker);
		 
		final AlertDialog helpDialog = helpBuilder.create();
		
		helpDialog.setOnShowListener(new DialogInterface.OnShowListener() {

	        @Override
	        public void onShow(DialogInterface dialog) {

	            Button b = helpDialog.getButton(AlertDialog.BUTTON_POSITIVE);
	            b.setOnClickListener(new View.OnClickListener() {

	                @Override
	                public void onClick(View view) {

	                	String s = prevPass.getText().toString();
	                	if (s == null || s.isEmpty()) {
	                		prevPass.setError("Please enter your current password");
	                		return;
	                	} else if (!s.equals(user.getPassword())) {
	                		prevPass.setError("Does not match your current password");
	                		return;
	                	}
	                	
	                	String s1 = newPass.getText().toString();
	                	String s2 = confirmPass.getText().toString();
	                	
	                	if (passwordErrors(s1, confirmPass) || passwordErrors(s2, confirmPass)) {
	                		return;
	                	}
	                	
	                	user.setPassword(EditAccountActivity.this, s1);
	                	user.notifyViews(EditAccountActivity.this);
	                	
	                    helpDialog.dismiss();
	                }
	            });
	        }
	    });
		
		helpDialog.show();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	    if (requestCode == 1) {
	        if(resultCode == RESULT_OK) {
	            LatLng latLng = data.getParcelableExtra("resultLatLng");
	            String title = data.getStringExtra("resultTitle");
	            
	            Location location = user.getLocation();
	            
	            if (location == null) {
	            	location = new Location("");
	            }
	            
	            location.setProvider(title);
	            location.setLongitude(latLng.longitude);
	            location.setLatitude(latLng.latitude);
	            
	            user.setLocation(location);
	            
	            displayGeolocation();
	            
	        } else if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result
	        }

        	user.notifyViews(EditAccountActivity.this);
	        
	    }
	    
	}
	
}
