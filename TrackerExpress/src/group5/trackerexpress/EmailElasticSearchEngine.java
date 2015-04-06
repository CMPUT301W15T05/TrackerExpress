/**
 * 
 */
package group5.trackerexpress;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author crinklaw
 *
 */
public class EmailElasticSearchEngine {

	// Http Connector
	private HttpClient httpclient = new DefaultHttpClient();

	// JSON Utilities
	private Gson gson = new Gson();
	
	protected static final String HTTP_PATH = "http://cmput301.softwareprocess.es:8080/testing/group5credentials/";
	
	
	/**
	 * Adds an email to server. Does not check for uniqueness.
	 * 
	 * @param claim
	 */
	public void addCredential(String credential) {

		try {
			HttpPost httpPost = new HttpPost(HTTP_PATH + UUID.randomUUID());
			StringEntity stringentity = null;
//			stringentity = new StringEntity(gson.toJson(credential));
			stringentity = new StringEntity(credential);
			
			httpPost.setHeader("Accept","application/json");
			httpPost.setEntity(stringentity);
			HttpResponse response = httpclient.execute(httpPost);
			
			String status = response.getStatusLine().toString();
			System.out.println(status);
			
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			throw new RuntimeException();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}		
	




	public String[] getCredentials() {

		final String[][] credentialsFinal = new String[1][];

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					HttpPost searchRequest = new HttpPost(HTTP_PATH + "_search?pretty=1");
					//String query = 	"{\"query\" : {\"query_string\" : {\"default_field\" : \"ingredients\",\"query\" : \"" + str + "\"}}}";
					//StringEntity stringentity = new StringEntity(query);

					searchRequest.setHeader("Accept","application/json");
					//searchRequest.setEntity(stringentity);

					HttpResponse response = httpclient.execute(searchRequest);

					String status = response.getStatusLine().toString();
					System.out.println(status);

					String json = getEntityContent(response);

					Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse>(){}.getType();
					ElasticSearchSearchResponse esResponse = gson.fromJson(json, elasticSearchSearchResponseType);
					System.err.println(esResponse);

					ArrayList<String> credentials = new ArrayList<String>();
					for (ElasticSearchResponse stringResponse : esResponse.getHits()) {
						credentials.add(stringResponse.getSource());
					}

					credentialsFinal[0] = credentials.toArray(new String[credentials.size()]);

				} catch (Exception e) {
					throw new RuntimeException();
				}
			}
		});

		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}
		return credentialsFinal[0];
	}
	
	
	
	
	
	
	
	/**
	 * get the http response and return json string
	 */
	private String getEntityContent(HttpResponse response) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader((response.getEntity().getContent())));
		String output;
		System.err.println("Output from Server -> ");
		String json = "";
		while ((output = br.readLine()) != null) {
			System.err.println(output);
			json += output;
		}
		System.err.println("JSON:"+json);
		return json;
	}
	
	
	private class ElasticSearchSearchResponse {
	    int took;
	    boolean timed_out;
	    transient Object _shards;
	    Hits hits;
	    boolean exists;    
	    public Collection<ElasticSearchResponse> getHits() {
	        return hits.getHits();        
	    }
	    public Collection<String> getSources() {
	        Collection<String> out = new ArrayList<String>();
	        for (ElasticSearchResponse essrt : getHits()) {
	            out.add( essrt.getSource() );
	        }
	        return out;
	    }
	    public String toString() {
	        return (super.toString() + ":" + took + "," + _shards + "," + exists + ","  + hits);     
	    }
	}
	
	
	private class ElasticSearchResponse {
	    String _index;
	    String _type;
	    String _id;
	    int _version;
	    boolean exists;
	    String _source;
	    double max_score;
	    public String getSource() {
	        return _source;
	    }
	}
	
	public class Hits {
	    int total;
	    double max_score;
	    Collection<ElasticSearchResponse> hits;
	    public Collection<ElasticSearchResponse> getHits() {
	        return hits;
	    }
	    public String toString() {
	        return (super.toString()+","+total+","+max_score+","+hits);
	    }
	}


	
}
