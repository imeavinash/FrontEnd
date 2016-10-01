/***
	Copyright (c) 2009 
	Author: Stefan Klumpp <stefan.klumpp@gmail.com>
	Web: http://stefanklumpp.com

	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */

package com.avinashbehera.sabera.network;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

public class HttpClient {
	private static final String TAG = "HttpClient";

	public static JSONObject SendHttpPostUsingHttpClient(String URL, JSONObject jsonObjSend) {

		try {
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 60*1000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 60*1000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpPost httpPostRequest = new HttpPost(URL);

			StringEntity se;
			se = new StringEntity(jsonObjSend.toString());

			// Set HTTP parameters
			httpPostRequest.setEntity(se);
			//httpPostRequest.setHeader("Accept", "application/json");
			httpPostRequest.setHeader("Content-type", "application/json");
			//httpPostRequest.setHeader("Accept-Encoding", "gzip"); // only set this parameter if you would like to use gzip compression

			long t = System.currentTimeMillis();
			Log.d(TAG,"httpPostReuest = "+httpPostRequest.toString());
			HttpResponse response = (HttpResponse) httpclient.execute(httpPostRequest);
			Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis()-t) + "ms]");

			// Get hold of the response entity (-> the data):
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// Read the content stream
				InputStream instream = entity.getContent();
				Header contentEncoding = response.getFirstHeader("Content-Encoding");
				if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					instream = new GZIPInputStream(instream);
				}

				

				// convert content stream to a String
				String resultString= convertStreamToString(instream);
				instream.close();
				resultString = resultString.substring(1,resultString.length()-1); // remove wrapping "[" and "]"

				// Transform the String into a JSONObject
				JSONParser parser = new JSONParser();
				JSONObject jsonObjRecv = (JSONObject)parser.parse(resultString);
				// Raw DEBUG output of our received JSON object:
				Log.i(TAG,"<JSONObject>\n"+jsonObjRecv.toString()+"\n</JSONObject>");

				return jsonObjRecv;
			} 

		}
		catch (Exception e)
		{
			Log.d(TAG,"catch block");
			// More about HTTP exception handling in another tutorial.
			// For now we just print the stack trace.
			e.printStackTrace();
		}
		Log.d(TAG,"SendHttpPostUsingHttpClient returning null");
		return null;
	}

    public static JSONObject SendHttpPostUsingUrlConnection(String url,JSONObject jsonObjSend){

		URL sendUrl;
		try{
			sendUrl = new URL(url);
		}catch(MalformedURLException e){
			Log.d(TAG,"SendHttpPostUsingUrlConnection malformed URL");
			return null;
		}

		HttpURLConnection conn = null;


        try {
			conn = (HttpURLConnection)sendUrl.openConnection();
			conn.setReadTimeout(15000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("POST");
			conn.setChunkedStreamingMode(1024);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Connection", "Keep-Alive");
			conn.addRequestProperty("Content-length", jsonObjSend.toJSONString().length()+"");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(os, "UTF-8"));
			//writer.write(getPostDataStringfromJsonObject(jsonObjSend));
			Log.d(TAG,"jsonobjectSend = "+jsonObjSend.toString());
			//writer.write(jsonObjSend.toString());
			writer.write(String.valueOf(jsonObjSend));

			writer.flush();
			writer.close();
			os.close();

			int responseCode=conn.getResponseCode();
            Log.d(TAG,"responseCode = "+responseCode);

			if (responseCode == HttpsURLConnection.HTTP_OK){

				Log.d(TAG,"responseCode = HTTP OK");

				InputStream instream = conn.getInputStream();

				String resultString= convertStreamToString(instream);
				instream.close();
				Log.d(TAG,"resultString = " + resultString);
				//resultString = resultString.substring(1,resultString.length()-1); // remove wrapping "[" and "]"

				// Transform the String into a JSONObject
				JSONParser parser = new JSONParser();
				JSONObject jsonObjRecv = (JSONObject)parser.parse(resultString);
				// Raw DEBUG output of our received JSON object:
				Log.i(TAG,"<JSONObject>\n"+jsonObjRecv.toString()+"\n</JSONObject>");

				return jsonObjRecv;

			}


        }
        catch (Exception e)
        {
            // More about HTTP exception handling in another tutorial.
            // For now we just print the stack trace.
            e.printStackTrace();
        } finally{
			if(conn != null){
				conn.disconnect();
			}
		}
        return null;

    }

	/*public static String getPostDataStringfromJsonObject(JSONObject params) throws Exception {

		StringBuilder result = new StringBuilder();
		boolean first = true;

		Iterator<String> itr = params.keys().;

		while(itr.hasNext()){

			String key= itr.next();
			Object value = params.get(key);

			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(key, "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(value.toString(), "UTF-8"));

		}
		return result.toString();
	}*/

	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 * 
		 * (c) public domain: http://senior.ceng.metu.edu.tr/2009/praeda/2009/01/11/a-simple-restful-client-at-android/
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
