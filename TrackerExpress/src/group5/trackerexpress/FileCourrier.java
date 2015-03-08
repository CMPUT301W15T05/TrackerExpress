package group5.trackerexpress;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Context;

public class FileCourrier<T> {

	
	public void saveFile(Context context, String fileName, T file) throws IOException {
		
		FileOutputStream fos = context.openFileOutput(fileName, 0);
		Gson gson = new Gson();
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		gson.toJson(file, osw);
		osw.flush();
		fos.close();
	}
	
	public T loadFile(Context context, String fileName) throws IOException {		

		//From joshua2ua in lab 3:
		FileInputStream fis = context.openFileInput(fileName);
		Gson gson = new Gson();
		Type dataType = new TypeToken<T>() {}.getType();
		InputStreamReader isr = new InputStreamReader(fis);
		T file = gson.fromJson(isr, dataType);
		fis.close();
		
		return file;
	}
}
