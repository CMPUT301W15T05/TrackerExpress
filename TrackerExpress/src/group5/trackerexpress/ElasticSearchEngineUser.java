/**
 * 
 */
package group5.trackerexpress;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

/**
 * Performs elastic search operations needed by this app.
 * 
 * 
 * @author crinklaw
 *
 */
public class ElasticSearchEngineUser {

	
	private final ElasticSearchEngineUnthreadedUser elasicSearchEngineUnthreaded = new ElasticSearchEngineUnthreadedUser();

	/*
	private UncaughtExceptionHandler uncaughtExceptionHandler = new UncaughtExceptionHandler() {
		
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			threadException = ex;
		}
	};
	
	
	volatile Throwable threadException;
	 */	
	public User[] getUsers(Context context) throws IOException {
		
		if (!Controller.isInternetConnected(context))
			throw new IOException();
		
		final User[][] users = new User[1][];

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				users[0] = elasicSearchEngineUnthreaded.getUsers();
			}
		});

		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			throw new IOException();
		}
		
		return users[0];
	}

	public void insertUser(Context context, User user) throws IOException {
		
		if (!Controller.isInternetConnected(context))
			throw new IOException();
		
		final User userFinal = user;
		
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					elasicSearchEngineUnthreaded.submitUser(userFinal);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}
	}
}
