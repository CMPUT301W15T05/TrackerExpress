package group5.trackerexpress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Class to rotate and scale down the bitmap images captured by the user.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */

public class EditBitmap {

	/** Rotates the receipt bitmap. */
	public Bitmap rotateBitmap(Bitmap sourceBitmap) {
		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		Bitmap rotatedBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight(), matrix, true);
		
		return rotatedBitmap;
	}

	/** resizes the receipt bitmap. */
	public Bitmap resizeBitmap(Bitmap sourceBitmap, int maxSize) {
		int width = sourceBitmap.getWidth();
		int height = sourceBitmap.getHeight();

		float bitmapRatio = (float) width / (float) height;
		if (bitmapRatio > 1) {
			width = maxSize;
			height = (int) (width / bitmapRatio);
		} else {
			height = maxSize;
			width = (int) (height * bitmapRatio);
		}
		return Bitmap.createScaledBitmap(sourceBitmap, width, height, true);
	}

}
