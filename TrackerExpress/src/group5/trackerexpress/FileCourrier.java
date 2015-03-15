package group5.trackerexpress;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.google.gson.Gson;

import android.content.Context;

public class FileCourrier<T> {
	private T type;
	
	public FileCourrier(T type) {
		this.type = type;
	}
	
	public void saveFile(Context context, String fileName, T file) throws IOException, NullPointerException {

		FileOutputStream fos = context.openFileOutput(fileName, 0);
		Gson gson = new Gson();
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		gson.toJson(file, osw);
		osw.flush();
		fos.close();
	}
	
	public T loadFile(Context context, String fileName) throws IOException, FileNotFoundException, NullPointerException {		

		//From joshua2ua in lab 3:
		System.out.println ("File Input Start");
		FileInputStream fis = context.openFileInput(fileName);
		Gson gson = new Gson();
		InputStreamReader isr = new InputStreamReader(fis);
		T file = (T) gson.fromJson(isr, type.getClass());
		fis.close();
		
		System.out.println (file.getClass().getName());
		System.out.println ("File Input Done");
		
		return file;
	}
}
