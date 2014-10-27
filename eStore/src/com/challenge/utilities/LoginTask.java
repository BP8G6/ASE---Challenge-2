package com.challenge.utilities;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.challenge.estore.SearchAStore;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class LoginTask extends AsyncTask<String, Void, String> {

	private String BASE_URL = "http://kc-sce-cs551.kc.umkc.edu/aspnet_client/Group4/Hackathon2/UserDetailsImpl.svc";
	private Context contxt;
	public LoginTask(Context c) {
		contxt = c;
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		String LOGIN_URL= BASE_URL + "/validate/" +params[0] + "/" + params[1]; 
		return getJSONfromURL(LOGIN_URL);
		}
	
	private String getJSONfromURL(String serviceurl) {
		// TODO Auto-generated method stub
		
		StringBuilder result = new StringBuilder();
		try {
			URL url = new URL(serviceurl);
		HttpURLConnection  httpUrlConnection = (HttpURLConnection)url.openConnection();
		//httpUrlConnection.setDoOutput(true);
		//httpUrlConnection.setRequestProperty("Content-Type", "application/json");
		httpUrlConnection.setRequestMethod("GET");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(
				httpUrlConnection.getInputStream()));
		System.out.println(httpUrlConnection.getResponseCode());
		
		if (httpUrlConnection.getResponseCode() == 200) {
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				result.append(inputLine);
				System.out.println(inputLine);
			}
			in.close();
		}
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
		return result.toString();
	}

	protected void onPostExecute(String result) {
          
		JSONObject obj;
		try {
			obj = new JSONObject(result);
			if(obj.getBoolean("success")){
				Toast.makeText(contxt, "Login Successfull !!! \n Redirecting...", 
	      		Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClass(contxt, SearchAStore.class);
				contxt.startActivity(intent);   
		}
		else{
				Toast.makeText(contxt, "Wrong Credentials.... Please Re-Enter",
	      		Toast.LENGTH_SHORT).show();
				/*
				username.setText("");
				username.setFocusable(true);
				password.setFocusable(false);
				*/
		}
    
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}			
}
