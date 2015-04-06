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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
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

public class MapActivity extends FragmentActivity 
			implements OnMapReadyCallback, OnMapClickListener, OnMapLongClickListener,
			OnCameraChangeListener, GoogleApiClient.OnConnectionFailedListener, 
			GoogleApiClient.ConnectionCallbacks {

    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    protected GoogleApiClient mGoogleApiClient;
    
    private Geocoder mGeocoder;

    private PlaceAutocompleteAdapter mAdapter;

    private AutoCompleteTextView mAutocompleteView;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    private Marker lastMarker = null;
    private GoogleMap mMap;
    
    private String intentDestination;
    private LatLng intentLatLng;
    
    private Rect windowRect;
    
    private ImageButton mConfirmButton;
    
    // A comfortable zoom level to go to
    private static final int MAX_AUTO_ZOOM = 13;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
	    intentDestination = intent.getStringExtra("destination");
	    System.out.println("Recieving destination: " + intentDestination);
	    intentLatLng = intent.getParcelableExtra("latlng");
        
        // Set up the Google API Client if it has not been initialized yet.
        if (mGoogleApiClient == null) {
            rebuildGoogleApiClient();
        }
        
        mGeocoder = new Geocoder(this);
        
        setContentView(R.layout.activity_map);

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);

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
        
        // Get a rectangle covering 60% of the width and 60% of the height of the window, 
        // from the center, that is used in checking when to deselect the marker
        windowRect = new Rect();
        this.findViewById(android.R.id.content).getWindowVisibleDisplayFrame(windowRect);
        
        int widthOffset = Math.round(0.20f * windowRect.width());
        int heightOffset = Math.round(0.20f * windowRect.height());
        
        windowRect.set(windowRect.left + widthOffset, windowRect.top + heightOffset, 
        				windowRect.right - widthOffset, windowRect.bottom - heightOffset);
        
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    
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

    // http://stackoverflow.com/questions/15412943/hide-soft-keyboard-on-losing-focus 03/04/2015
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
            	// TODO: Some sort of message
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

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();

        // Disable API access in the adapter because the client was not initialised correctly.
        mAdapter.setGoogleApiClient(null);

    }

    @Override
    public void onConnected(Bundle bundle) {
        // Successfully connected to the API client. Pass it to the adapter to enable API access.
        mAdapter.setGoogleApiClient(mGoogleApiClient);

		System.out.println("CONNECTED " + intentDestination);
        // User already selected a location; abort
		if (lastMarker != null) {
			System.out.println("LAST MARKER IS NULL");
			return;
		}
		
        if (intentLatLng != null) {
			makeLatLngMarker(intentLatLng);
		} else if (intentDestination != null) {
			System.out.println("ATTEMPTING SEARCH");
			mAutocompleteView.setText(intentDestination);
			System.out.println("TEXT SET");
			
			if (intentDestination.length() > 1) {
				System.out.println("GREATER THAN ONE, STARTING FILTER");
				mAdapter.getFilter().filter(intentDestination, new Filter.FilterListener() {
	                public void onFilterComplete(int count) {
	        			System.out.println("FILTER COUNT: " + count);
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

    public String latLngFormat(LatLng latLng) {
    	return String.format(Locale.US, "Lat: %.4f Lng: %.4f", latLng.latitude, latLng.longitude);
    }
    
	@Override
	public void onMapClick(LatLng latLng) {
		hideKeypad();
		
		makeLatLngMarker(latLng);
	}
	
	private void makeLatLngMarker(LatLng latLng) {
		String locName = "";
		String snippet = "";
		
		// https://developer.android.com/training/location/display-address.html 03/04/2015
		List<Address> addresses = null;
		
		try {
			addresses = mGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
		} catch (IOException ioException) {
	        // Catch network or other I/O problems.
			System.err.println("Geocoder IO EXCEPTION");
	    } catch (IllegalArgumentException illegalArgumentException) {
	        // Catch invalid latitude or longitude values.
			System.err.println("Geocoder IllegalArgument at " + latLng.toString());
	    }

		if (addresses != null && addresses.size() > 0) {
	        Address address = addresses.get(0);
	        ArrayList<String> addressFragments = new ArrayList<String>();
	        
	        // Fetch the address lines using getAddressLine,
	        // join them, and send them to the thread.
	        if (address.getMaxAddressLineIndex() > 0) {
	        	locName = address.getAddressLine(0);
	        	for(int i = 1; i <= address.getMaxAddressLineIndex(); i++) {
	        		addressFragments.add(address.getAddressLine(i));
	        	}
	        }
	        
	        snippet = TextUtils.join(" ", addressFragments);
	    }
		
		if (!snippet.isEmpty()) {
			snippet += System.getProperty("line.separator");
		}
		
        snippet += latLngFormat(latLng);
		
		makeMarker(latLng, locName, snippet);
		goToMarker(lastMarker);
	}
	
	
	
	public void makeMarker(LatLng latLng, String title, String snippet) {
		
		if (lastMarker != null) {
			lastMarker.remove();
		}

		if (mConfirmButton.getVisibility() != View.VISIBLE) {
			mConfirmButton.setVisibility(View.VISIBLE);
		}
		
		lastMarker = mMap.addMarker(new MarkerOptions()
										.position(latLng)
										.title(title)
										.snippet(snippet));
		
	}

	public void goToMarker(final Marker marker) {
		goToMarker(marker, null);
	}
	
	public void goToMarker(final Marker marker, Integer newZoom) {

		marker.showInfoWindow();
		
		mMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
            	if (!marker.isInfoWindowShown() && markerOnMap(marker)) {
            		marker.showInfoWindow();
            	}
            }
        });
		
		goToLocation(marker.getPosition(), newZoom);
	}
	
	public void goToLocation(LatLng latLng, Integer newZoom) {
		float curZoom = mMap.getCameraPosition().zoom;
		
		if (newZoom != null) {
			curZoom = newZoom;
		} else if (curZoom < MAX_AUTO_ZOOM) {
			curZoom += 2;
		}
		
    	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, curZoom));
		
		// Set the bounds for search proximity to be around the newly selected location
        LatLng neLatLng = new LatLng(latLng.latitude + 0.3, latLng.longitude + 0.3);
        LatLng swLatLng = new LatLng(latLng.latitude - 0.3, latLng.longitude - 0.3);
        
        mAdapter.setBounds(new LatLngBounds(swLatLng, neLatLng));
	}

	@Override
	public void onMapReady(GoogleMap map) {
		
        map.setOnMapClickListener(this);
        map.setOnMapLongClickListener(this);
        map.setOnCameraChangeListener(this);
        map.setMyLocationEnabled(true);
        
        // http://stackoverflow.com/a/15786363/4269270 04/04/2015
        map.setInfoWindowAdapter(new InfoWindowAdapter() {
        	
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @SuppressLint("InflateParams")
			@Override
            public View getInfoContents(Marker marker) {

                View v = getLayoutInflater().inflate(R.layout.custom_map_marker, null);
                
                if (!marker.getTitle().isEmpty()) {
                    RowTableLayout rtl = new RowTableLayout(v, MapActivity.this);
                    Spanned title = Html.fromHtml("<b>" + marker.getTitle() + "</b>");
                	rtl.insertRow(R.id.marker_title, title, false);
                }
                
                TextView markerSnippet = (TextView) v.findViewById(R.id.marker_snippet);
                markerSnippet.setText(marker.getSnippet());

                return v;
            }
        });

        final ViewTreeObserver observer = mAutocompleteView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mMap.setPadding(0, mAutocompleteView.getHeight(), 0, 0);
                mAutocompleteView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

		UiSettings uis = map.getUiSettings();
		uis.setZoomControlsEnabled(true);
		uis.setMapToolbarEnabled(false);
		uis.setMyLocationButtonEnabled(true);
		
		mMap = map;
	}

	
	@Override
	public void onCameraChange(CameraPosition camPos) {
		hideKeypad();
		
		if (lastMarker != null && lastMarker.isInfoWindowShown() && !markerOnMap(lastMarker)) { 
			lastMarker.hideInfoWindow();
		} 
	}
	
	private Boolean markerOnMap(Marker marker) {
		if (marker == null) {
			return false;
		}
		
		Point markerCenter = mMap.getProjection().toScreenLocation(marker.getPosition());
		
		if (windowRect.contains(markerCenter.x, markerCenter.y)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onMapLongClick(LatLng latLng) {
		onMapClick(latLng);
	}
}