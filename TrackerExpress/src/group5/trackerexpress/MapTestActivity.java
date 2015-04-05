package group5.trackerexpress;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MapTestActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_map_test);
		
		final EditText destination = (EditText) findViewById(R.id.maptestdest);
		final EditText latlng = (EditText) findViewById(R.id.maptestlatlng);
		
		Button bMap = (Button) findViewById(R.id.gotomap);
		bMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MapTestActivity.this, MapActivity.class);
				
				if (!destination.getText().toString().isEmpty()) {
					intent.putExtra("destination", destination.getText().toString());
				}
				
				String latlngtext = latlng.getText().toString();
				if (!latlngtext.isEmpty()) {
					double lat = Double.parseDouble(latlngtext.split(" ")[0]);
					double lng = Double.parseDouble(latlngtext.split(" ")[1]);
					LatLng newlatlng = new LatLng(lat, lng);
					intent.putExtra("latlng", newlatlng);
				}
				
		    	startActivityForResult(intent, 1);
			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	    if (requestCode == 1) {
	        if(resultCode == RESULT_OK){
	            LatLng latLng = data.getParcelableExtra("resultLatLng");
	            String title = data.getStringExtra("resultTitle");
	            
	            TextView latLngText = (TextView) findViewById(R.id.maptestlatlngtext);
	            
	            latLngText.setText(latLng.toString());

	            TextView titleText = (TextView) findViewById(R.id.maptestdesttext);
	            
	            titleText.setText(title);
	        } else if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result
	        }
	    }
	}
}
