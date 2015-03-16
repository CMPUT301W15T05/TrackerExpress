package group5.trackerexpress;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * The Class FileCourrier.
 *
 * @param <T> the generic type
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class FileCourrier<T> {
	
	/** The type. */
	private T type;
	
	/**
	 * Instantiates a new file courrier.
	 *
	 * @param type the type
	 */
	public FileCourrier(T type) {
		this.type = type;
	}
	
	/**
	 * Save file.
	 *
	 * @param context the context
	 * @param fileName the file name
	 * @param file the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void saveFile(Context context, String fileName, T file) throws IOException {

		FileOutputStream fos = context.openFileOutput(fileName, 0);
		Gson gson = new Gson();
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		gson.toJson(file, osw);
		osw.flush();
		fos.close();
	}
	
	/**
	 * Load file.
	 *
	 * @param context the context
	 * @param fileName the file name
	 * @return the t
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws FileNotFoundException the file not found exception
	 */
	public T loadFile(Context context, String fileName) throws IOException, FileNotFoundException {		

		//From joshua2ua in lab 3:
		FileInputStream fis = context.openFileInput(fileName);
		Gson gson = new Gson();
		InputStreamReader isr = new InputStreamReader(fis);
		
		Object fileUncasted = gson.fromJson(isr, type.getClass());
		fis.close();
		
		if (fileUncasted == null || !fileUncasted.getClass().equals(type.getClass()))
			throw new FileNotFoundException();
				
		return (T) fileUncasted;
	}
}
