package group5.trackerexpress;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore.Files;

/**
 * Image of a receipt, held by expense objects. Just contains a path to the bitmap. This might be changed. 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class Receipt extends TModel {
	
	private static final long serialVersionUID = 1L;

	/** The path of the image. May or may not be null*/
	protected String path;

	/**
	 * Path to the file, if stored locally.
	 * @return
	 */
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/** The image itself. May or may not be null*/
	protected byte[] byteArray;
	
	public Receipt(String path){
		this.path = path;
		this.byteArray = null;
	}

	/**
	 * Gets bitmap from byte array is not null. Otherwise, gets from file system
	 * using uri path.
	 * 
	 * @return the bitmap
	 */
	public Bitmap getBitmap(){
		if (byteArray == null) {
			return getBitmapFromPath();
		}
		else {
			return getBitmapFromByteArray();
		}
	}
	

	/**
	 * Call this before saving to elastic search. Otherwise, images won't
	 * be saved.
	 */
	public void switchToStoringActualBitmap(){
		this.byteArray = getByteArrayFromPath();
	}
	
	

	/**
	 * Call this before file is saved. Needed because saving an actual
	 * bitmap is too slow.
	 */
	public void stopStoringActualBitmap(){
		this.byteArray = null;
	}
	

	
	/**
	 * @return byte array stored in path
	 * 
	 * http://howtodoinjava.com/2014/11/04/how-to-read-file-content-into-byte-array-in-java/
	 * April 04 2015
	 */
	private byte[] getByteArrayFromPath() {
	      FileInputStream fileInputStream = null;
	      File file = new File(path);
	      byte[] bFile = new byte[(int) file.length()];
	      try
	      {
	         //convert file into array of bytes
	         fileInputStream = new FileInputStream(file);
	         fileInputStream.read(bFile);
	         fileInputStream.close();
	         for (int i = 0; i < bFile.length; i++)
	         {
	            System.out.print((char) bFile[i]);
	         }
	      }
	      catch (Exception e)
	      {
	         throw new RuntimeException("Problem with converting image to byte array");
	      }
	      return bFile;

	}

	private Bitmap getBitmapFromPath(){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap tempBitmap = BitmapFactory.decodeFile(path, new BitmapFactory.Options());
		if (tempBitmap == null)
			throw new RuntimeException("Invalid uri path for receipt.");
		else
			return tempBitmap;
	}
	
	private Bitmap getBitmapFromByteArray() {
		return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
	}

	/**
	 * Gets a drawable. Will NOT work for global claims.
	 * 
	 * @deprecated
	 * @return
	 */
	public Drawable getDrawable() {
		return Drawable.createFromPath(path);
	}

}
