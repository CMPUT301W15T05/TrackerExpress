package group5.trackerexpress;
/**
 * 
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.fluent.Content;
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
public class ElasticSearchClient<T extends Serializable & Identifiable> {
	
	// Http Connector
	private HttpClient httpclient = new DefaultHttpClient();

	// JSON Utilities
	private Gson gson = new Gson();

	private String httpPath;

	private Type elasticSearchSearchResponseType;
	private Type elasticSearchResponseType;

	public ElasticSearchClient(String httpPath2, Type elasticSearchResponseType, Type elasticSearchSearchResponseType) {
		this.httpPath = httpPath;
		this.elasticSearchResponseType = elasticSearchResponseType;
		this.elasticSearchSearchResponseType = elasticSearchSearchResponseType;
	}





	/**
	 * @return
	 */
	public ArrayList<T> getAll() {

		try {
			HttpPost searchRequest = new HttpPost(httpPath + "_search?pretty=1");
			//String query = 	"{\"query\" : {\"query_string\" : {\"default_field\" : \"ingredients\",\"query\" : \"" + str + "\"}}}";
			//StringEntity stringentity = new StringEntity(query);

			searchRequest.setHeader("Accept","application/json");
			//searchRequest.setEntity(stringentity);

			HttpResponse response = httpclient.execute(searchRequest);

			String status = response.getStatusLine().toString();
			System.out.println(status);

			String json = getEntityContent(response);

			//Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse>(){}.getType();
			ElasticSearchSearchResponse<T> esResponse = gson.fromJson(json, elasticSearchSearchResponseType);
			//System.err.println(esResponse);

			ArrayList<T> objects = new ArrayList<T>();
			for (ElasticSearchResponse<T> objectResponse : esResponse.getHits()) {
				T object = objectResponse.getSource();
				objects.add(object);
			}
			//searchRequest.getEntity().consumeContent();
			
			return objects;
			
		} catch(IOException e){
			throw new RuntimeException();
		}
	}
	
	/**
	 * @return
	 */
	public T get(UUID id) {

		try{
			HttpGet getRequest = new HttpGet(httpPath + id);

			getRequest.addHeader("Accept","application/json");

			HttpResponse response = httpclient.execute(getRequest);

			String status = response.getStatusLine().toString();
			System.out.println(status);

			String json = getEntityContent(response);

			//Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<T>>(){}.getType();
			ElasticSearchResponse<T> esResponse = gson.fromJson(json, elasticSearchResponseType);

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
	 * @param object
	 */
	public void insert(T object) {
		try {
			HttpPost httpPost = new HttpPost(httpPath + object.getUuid());
			StringEntity stringentity = null;

			stringentity = new StringEntity(gson.toJson(object));

			httpPost.setHeader("Accept","application/json");

			httpPost.setEntity(stringentity);
			HttpResponse response = httpclient.execute(httpPost);

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
	
	
	
	
	public void delete(UUID id){
		try {
			HttpDelete httpDelete = new HttpDelete(httpPath + id);
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
	
	public void update(UUID id, String updateString){
		try {
			HttpPost updateRequest = new HttpPost(httpPath + id + "/_update");

			StringEntity stringentity = new StringEntity(updateString);
			
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
	
	
	
	public class ElasticSearchResponse<T1> {
	    String _index;
	    String _type;
	    String _id;
	    int _version;
	    boolean exists;
	    T1 _source;
	    double max_score;
	    public T1 getSource() {
	        return _source;
	    }
	}
	
	
	public class ElasticSearchSearchResponse<T2> {
	    int took;
	    boolean timed_out;
	    transient Object _shards;
	    Hits<T2> hits;
	    boolean exists;    
	    public Collection<ElasticSearchResponse<T2>> getHits() {
	        return hits.getHits();        
	    }
	    public Collection<T2> getSources() {
	        Collection<T2> out = new ArrayList<T2>();
	        for (ElasticSearchResponse<T2> essrt : getHits()) {
	            out.add( essrt.getSource() );
	        }
	        return out;
	    }
	    public String toString() {
	        return (super.toString() + ":" + took + "," + _shards + "," + exists + ","  + hits);     
	    }
	}
	
	
	public class Hits<T3> {
	    int total;
	    double max_score;
	    Collection<ElasticSearchResponse<T3>> hits;
	    public Collection<ElasticSearchResponse<T3>> getHits() {
	        return hits;
	    }
	    public String toString() {
	        return (super.toString()+","+total+","+max_score+","+hits);
	    }
	}

}
