package com.example.trackerexpressreceipttests;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageView;

public class AddReceipt extends
		ActivityInstrumentationTestCase2<TrackerExpressReceipt> {

	public AddReceipt() {
		super(TrackerExpressReceipt.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	// Test Case 10.1
	public void testAddReceipt(){
		takeAPhoto();
		ImageView view = (ImageView) findViewById(R.id.receiptView);
		// From http://stackoverflow.com/questions/4435806/drawable-to-byte accessed 2015-11-02
		Drawable d = view.getDrawable();
		Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] bitmapdata = stream.toByteArray();
		assertNotSame("Testing Existence of Picture", 0, bitmapdata.length);
		assertTrue("Testing Image Size", bitmapdata.length<65536);
	}
	
	// Test Case 11.1
	public void testChangeReceipt(){
		ImageView view = (ImageView) findViewById(R.id.receiptView);
		Drawable original = view.getDrawable();
		takeAPhoto();
		Drawable changed = view.getDrawable();
		assertNotSame("Testing Receipt Change", changed, original);
	}
	
	// Test Case 12.1
	public void testDeleteReceipt(){
		ImageView view = (ImageView) findViewById(R.id.receiptView);
		deletePhoto();
		assertEquals("Testing Delete", null, view.getDrawable());
	}
}
