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
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Performs elastic search operations. 
 * NOTE: DOES NOT USE THREADS, SO WILL NOT WORK IF CALLED DIRECTLY. Instead,
 * use ElasticSearchEngine, which will use this class, but on a seperate thread.
 * 
 * Based on original by Chenlei Zhang:
 * https://github.com/rayzhangcl/ESDemo
 * March 31 2015
 * 
 *  @See ElasticSearchEngine
 */
public class ElasticSearchEngineUnthreaded {

	// Http Connector
	private HttpClient httpclient = new DefaultHttpClient();

	// JSON Utilities
	private Gson gson = new Gson();
	/**
	 * 
	 */
	private static final String HTTP_PATH = "http://cmput301.softwareprocess.es:8080/testing/group5claimsBitmaps/";

	/**
	 * @return
	 */
	public Claim[] getClaims() {

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

			ArrayList<Claim> credentials = new ArrayList<Claim>();
			for (ElasticSearchResponse claimResponse : esResponse.getHits()) {
				Claim claim = claimResponse.getSource();
				credentials.add(claim);
			}
			//searchRequest.getEntity().consumeContent();

			//return in reverse sorted order:
			Collections.sort(credentials, new Comparator<Claim>(){
				@Override
				public int compare(Claim arg0, Claim arg1) {
					return -1 * arg0.compareTo(arg1);
				}
			});
			
			return credentials.toArray(new Claim[credentials.size()]);
			
		} catch(IOException e){
			throw new RuntimeException();
		}
	}
	
	/**
	 * @return
	 */
	public Claim getClaim(UUID id) {

		try{
			HttpGet getRequest = new HttpGet(HTTP_PATH + id);

			getRequest.addHeader("Accept","application/json");

			HttpResponse response = httpclient.execute(getRequest);

			String status = response.getStatusLine().toString();
			System.out.println(status);

			String json = getEntityContent(response);

			Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse>(){}.getType();
			ElasticSearchResponse esResponse = gson.fromJson(json, elasticSearchResponseType);

			return esResponse.getSource();

		} catch (ClientProtocolException e) {
			throw new RuntimeException();
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}
	
	
	

	/**
	 * 
	 * 
	 * @param claim
	 */
	public void submitClaim(Claim claim) {
		try {
			HttpPost httpPost = new HttpPost(HTTP_PATH + claim.getUuid());
			StringEntity stringentity = null;

			stringentity = new StringEntity(gson.toJson(claim));

			httpPost.setHeader("Accept","application/json");

			httpPost.setEntity(stringentity);
			HttpResponse response = null;

			response = httpclient.execute(httpPost);

			//Response code:
			String status = response.getStatusLine().toString();
			System.out.println(status);
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
			String output;
			System.err.println("Output from Server -> ");
			while ((output = br.readLine()) != null) {
				System.err.println(output);
			}

			//EntityUtils.consume(entity);

			//httpPost.releaseConnection();
			
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
	
	
	
	
	public void deleteClaim(UUID id){
		try {
			HttpDelete httpDelete = new HttpDelete(HTTP_PATH + id);
			httpDelete.addHeader("Accept","application/json");

			HttpResponse response = httpclient.execute(httpDelete);

			String status = response.getStatusLine().toString();
			System.out.println(status);

			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
			String output;
			System.err.println("Output from Server -> ");
			while ((output = br.readLine()) != null) {
				System.err.println(output);
			}
			entity.consumeContent();

			//		httpDelete.releaseConnection();

		}
		catch (IOException e){
			throw new RuntimeException();
		}
	}	
	
	public void approveClaim(UUID id, String comments, String approverName, String approverEmail){
		try {
			HttpPost updateRequest = new HttpPost(HTTP_PATH + id + "/_update");
			String query = "{\"doc\": {" +
					" \"status\"        :   " + Claim.APPROVED + ", "   +
					" \"comments\"      : \"" + comments       + "\", " +
					" \"approverName\"  : \"" + approverName   + "\", " +		
					" \"approverEmail\" : \"" + approverEmail  + "\" " +							
					" }}";
			StringEntity stringentity = new StringEntity(query);
			
			updateRequest.setHeader("Accept","application/json");
			updateRequest.setEntity(stringentity);
			
			HttpResponse response = httpclient.execute(updateRequest);
			String status = response.getStatusLine().toString();
			System.out.println(status);

			String json = getEntityContent(response);
			//		updateRequest.releaseConnection();
		}
		catch(IOException E){
			throw new RuntimeException();
		}
	}	
	
	
	public void returnClaim(UUID id, String comments, String approverName, String approverEmail){
		try {
			HttpPost updateRequest = new HttpPost(HTTP_PATH + id + "/_update");
			String query = "{\"doc\": {" +
					" \"status\"        :   " + Claim.RETURNED + ","   +
					" \"comments\"      : \"" + comments       + "\"," +
					" \"approverName\"  : \"" + approverName   + "\"," +		
					" \"approverEmail\" : \"" + approverEmail  + "\" " +							
					" }}";
			StringEntity stringentity = new StringEntity(query);

			updateRequest.setHeader("Accept","application/json");
			updateRequest.setEntity(stringentity);

			HttpResponse response = httpclient.execute(updateRequest);
			String status = response.getStatusLine().toString();
			System.out.println(status);

			String json = getEntityContent(response);
			//		updateRequest.releaseConnection();
		}
		catch(IOException E){
			throw new RuntimeException();
		}
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
	    public Collection<Claim> getSources() {
	        Collection<Claim> out = new ArrayList<Claim>();
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
	    Claim _source;
	    double max_score;
	    public Claim getSource() {
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