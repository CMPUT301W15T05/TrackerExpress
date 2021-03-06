/*
 * Copyright (C) 2015 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

// https://github.com/googlesamples/android-play-places/blob/master/PlaceComplete/Application/src/main/java/com/example/google/playservices/placecomplete/MainActivity.java
// accessed 01/04/2015

// As is the case with the PlaceAutocompleteAdapter class, some of this code was taken
// from google's own sample code for testing Place autocomplete functionality since it
// properly and efficiently implements it.

package group5.trackerexpress;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Map for our app. Lets user search for locations and set locations.
 */
public class InteractiveMapActivity extends BasicMapActivity 
			implements  OnMapClickListener, OnMapLongClickListener, 
			GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    protected GoogleApiClient mGoogleApiClient;

    private PlaceAutocompleteAdapter mAdapter;

    private AutoCompleteTextView mAutocompleteView;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    
    private ImageButton mConfirmButton;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set up the Google API Client if it has not been initialized yet.
        if (mGoogleApiClient == null) {
            rebuildGoogleApiClient();
        }

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);
        mAutocompleteView.setVisibility(View.VISIBLE);

        setAutoCompleteListeners();

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_GREATER_SYDNEY, null);
        mAutocompleteView.setAdapter(mAdapter);
        
        mConfirmButton = (ImageButton) findViewById(R.id.confirm_geolocation_button);
        mConfirmButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				
				if (lastMarker != null) {
					returnIntent.putExtra("resultLatLng", lastMarker.getPosition());
					returnIntent.putExtra("resultTitle", lastMarker.getTitle());

					setResult(RESULT_OK, returnIntent);
				} else {
					setResult(RESULT_CANCELED, returnIntent);
				}
				
				finish();
			}
		});
    }
    
    /**
     * Autocompletion of search field
     */
    private void setAutoCompleteListeners() {
    	
        // Register a listener that receives callbacks when a suggestion has been selected
    	mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
        
        mAutocompleteView.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {
			
        	@Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            		String vText = v.getText().toString();
            		
                	if (vText.length() <= 1) {
                		v.setError("Not enough characters");
                		return false;
                	}
                	
                	if (mAdapter.getCount() < 1) {
                		v.setError("No results");
                		return false;
                	}
                	
                	getAutocompleteResult(0);
                    return true;
                }
                return false;
            }
        });
        
        // http://stackoverflow.com/a/12867844/4269270 03/04/2015
        // & http://stackoverflow.com/a/8171014/4269270 03/04/2015
        // Adding the clearable listeners requires the layout be loaded
        final ViewTreeObserver observer = mAutocompleteView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                setClearable(mAutocompleteView);
                mAutocompleteView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        // Cursor will sometimes start as visible
		mAutocompleteView.setCursorVisible(false);
        mAutocompleteView.setOnFocusChangeListener(new AutoCompleteTextView.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mAutocompleteView.setCursorVisible(true);
				} else {
					mAutocompleteView.setCursorVisible(false);
				}
			}
		});
    }
    
    public void setClearable(final EditText et) {

		final Drawable x = getResources().getDrawable(R.drawable.ic_clear_text);
		final int dimens = Math.round((et.getHeight() - (et.getPaddingTop() + et.getPaddingBottom())) * 0.7f);
		
		x.setBounds(0, 0, dimens, dimens);

		et.setOnTouchListener(new AutoCompleteTextView.OnTouchListener() {
		    @SuppressLint("ClickableViewAccessibility")
			@Override
		    public boolean onTouch(View v, MotionEvent event) {
		        if (et.getCompoundDrawables()[2] == null) {
		            return false;
		        }
		        
		        if (event.getAction() != MotionEvent.ACTION_UP) {
		            return false;
		        }
		        
		        if (event.getX() > et.getWidth() - et.getPaddingRight() - dimens) {
		            et.setText("");
		            et.setCompoundDrawables(null, null, null, null);
		        }
		        return false;
		    }
		});
		
		et.addTextChangedListener(new TextWatcher() {
		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
		        et.setCompoundDrawables(null, null, et.getText().toString().equals("") ? null : x, null);
		    }
		
		    @Override
		    public void afterTextChanged(Editable s) {}
		
		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		});
    }
    
    /**
     * http://stackoverflow.com/questions/15412943/hide-soft-keyboard-on-losing-focus 03/04/2015
     * Hides the keypad
     */
    private void hideKeypad() {

    	// With the dummy Linear Layout in the xml, clear focus selects that 
    	// instead of reselecting the autocomplete box again
		mAutocompleteView.clearFocus();
		
    	InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mAutocompleteView.getWindowToken(), 0);
    }
    
    private PlaceAutocompleteAdapter.PlaceAutocomplete getAutocompleteResult(int position) {
		hideKeypad();
		
    	/*
        Retrieve the place ID of the selected item from the Adapter.
        The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
        read the place ID.
         */
    	PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
    	String placeId = String.valueOf(item.placeId);

    	/*
       	Issue a request to the Places Geo Data API to retrieve a Place object with additional
       	details about the place.
         */
    	PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
    			.getPlaceById(mGoogleApiClient, placeId);
    	placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
    	
    	return item;
    }
    
    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	mAutocompleteView.setSelection(0);
        	mAutocompleteView.clearFocus();
            getAutocompleteResult(position);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                return;
            }
            
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            LatLng placeLatLng = place.getLatLng();
            
            String snippet = place.getAddress() + System.getProperty("line.separator") + 
            				latLngFormat(placeLatLng);
            
    		makeMarker(placeLatLng, place.getName().toString(), snippet);
    		goToMarker(lastMarker, MAX_AUTO_ZOOM);
        }
    };

    /**
     * Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
     * functionality.
     * This automatically sets up the API client to handle Activity lifecycle events.
     */
    protected synchronized void rebuildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and connection failed
        // callbacks should be returned, which Google APIs our app uses and which OAuth 2.0
        // scopes our app requests.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addConnectionCallbacks(this)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Toast.makeText(this, "Could not connect to Google Services",
                Toast.LENGTH_SHORT).show();

        // Disable API access in the adapter because the client was not initialised correctly.
        mAdapter.setGoogleApiClient(null);

		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
        finish();

    }

    @Override
    public void onConnected(Bundle bundle) {
        // Successfully connected to the API client. Pass it to the adapter to enable API access.
        mAdapter.setGoogleApiClient(mGoogleApiClient);

        // User already selected a location; abort
		if (lastMarker != null) {
			return;
		}
		
		if (intentLatLng != null) {
			makeLatLngMarker(intentLatLng, intentDestination, MAX_AUTO_ZOOM);
		} else if (intentDestination != null) {
			mAutocompleteView.setText(intentDestination);
			
			if (intentDestination.length() > 1) {
				mAdapter.getFilter().filter(intentDestination, new Filter.FilterListener() {
	                public void onFilterComplete(int count) {
	                	if (count > 0) {
	                		getAutocompleteResult(0);
	                	}
	                 }
	             });
			}
		} else {
			LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				LatLng locLatLng = new LatLng(location.getLatitude(), location.getLongitude());
				goToLocation(locLatLng, MAX_AUTO_ZOOM);
			}
		}
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Connection to the API client has been suspended. Disable API access in the client.
        mAdapter.setGoogleApiClient(null);
    }
    
	@Override
	public void onMapClick(LatLng latLng) {
		hideKeypad();
		
		makeLatLngMarker(latLng, "", null);
	}
	
	@Override
	public void makeMarker(LatLng latLng, String title, String snippet) {
		super.makeMarker(latLng, title, snippet);
		
		if (mConfirmButton.getVisibility() != View.VISIBLE) {
			mConfirmButton.setVisibility(View.VISIBLE);
		}
		
	}
	
	/**
	 * Moves the map to a specified location.
	 */
	@Override
	public void goToLocation(LatLng latLng, Integer newZoom) {
		super.goToLocation(latLng, newZoom);
		
		// Set the bounds for search proximity to be around the newly selected location
        LatLng neLatLng = new LatLng(latLng.latitude + 0.3, latLng.longitude + 0.3);
        LatLng swLatLng = new LatLng(latLng.latitude - 0.3, latLng.longitude - 0.3);
        
        mAdapter.setBounds(new LatLngBounds(swLatLng, neLatLng));
	}

	@Override
	public void onMapReady(GoogleMap map) {
		
        map.setOnMapClickListener(this);
        map.setOnMapLongClickListener(this);
        

        final ViewTreeObserver observer = mAutocompleteView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mMap.setPadding(0, mAutocompleteView.getHeight(), 0, 0);
                mAutocompleteView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        
		super.onMapReady(map);
	}

	
	@Override
	public void onCameraChange(CameraPosition camPos) {
		super.onCameraChange(camPos);
		hideKeypad();
	}

	@Override
	public void onMapLongClick(LatLng latLng) {
		onMapClick(latLng);
	}
}