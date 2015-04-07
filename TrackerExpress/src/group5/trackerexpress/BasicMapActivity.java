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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class BasicMapActivity extends FragmentActivity implements OnMapReadyCallback, OnCameraChangeListener {
    
    protected Geocoder mGeocoder;

    protected Marker lastMarker = null;
    protected GoogleMap mMap;
    
    protected String intentDestination;
    protected LatLng intentLatLng;
    
    protected Rect windowRect;
    
    // A comfortable zoom level to go to
    protected static final int MAX_AUTO_ZOOM = 13;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = this.getIntent();
	    intentDestination = intent.getStringExtra("destination");
	    intentLatLng = intent.getParcelableExtra("latlng");
	    System.out.println("Recieving destination: " + intentDestination + " " + intentLatLng);
         
        mGeocoder = new Geocoder(this);
        
        setContentView(R.layout.activity_map);
        
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
	
    public String latLngFormat(LatLng latLng) {
    	return String.format(Locale.US, "Lat: %.4f Lng: %.4f", latLng.latitude, latLng.longitude);
    }
	
	protected void makeLatLngMarker(LatLng latLng, String locName, Integer zoom) {
		String snippet = "";
		
		if (locName == null) {
			locName = "";
		}
		
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
	        	if (locName.isEmpty()) {
	        		System.out.println("Location name is empty");
	        		locName = address.getAddressLine(0);
	        	}
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
		System.out.println("MAKING MARKER");
		goToMarker(lastMarker, zoom);
	}
	
	public void makeMarker(LatLng latLng, String title, String snippet) {
		
		if (lastMarker != null) {
			lastMarker.remove();
		}
		
		lastMarker = mMap.addMarker(new MarkerOptions()
										.position(latLng)
										.title(title)
										.snippet(snippet));
		
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
	}
	
	@Override
	public void onMapReady(GoogleMap map) {
		
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
                    RowTableLayout rtl = new RowTableLayout(v, BasicMapActivity.this);
                    Spanned title = Html.fromHtml("<b>" + marker.getTitle() + "</b>");
                	rtl.insertRow(R.id.marker_title, title, false);
                }
                
                TextView markerSnippet = (TextView) v.findViewById(R.id.marker_snippet);
                markerSnippet.setText(marker.getSnippet());

                return v;
            }
        });

		UiSettings uis = map.getUiSettings();
		uis.setZoomControlsEnabled(true);
		uis.setMapToolbarEnabled(false);
		uis.setMyLocationButtonEnabled(true);
		
		mMap = map;
		
        if (lastMarker == null && intentLatLng != null) {
        	System.out.println("NOT NULL");
			makeLatLngMarker(intentLatLng, intentDestination, MAX_AUTO_ZOOM);
		}
	}

	
	@Override
	public void onCameraChange(CameraPosition camPos) {
		
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
	
}
