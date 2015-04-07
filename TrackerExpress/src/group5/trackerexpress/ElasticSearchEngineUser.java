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
 * Performs elastic search operations needed for user 
 * account management
 * 
 * @author crinklaw
 */
public class ElasticSearchEngineUser {

	
	private final ElasticSearchEngineUnthreadedUser elasicSearchEngineUnthreaded = new ElasticSearchEngineUnthreadedUser();
	
	/**
	 * Gets all users from server
	 * @param context
	 * @return
	 * @throws IOException
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

	/**
	 * Adds user to server
	 * @param context
	 * @param user
	 * @throws IOException
	 */
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
