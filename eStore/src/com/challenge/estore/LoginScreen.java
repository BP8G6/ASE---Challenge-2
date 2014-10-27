package com.challenge.estore;


import com.challenge.utilities.LoginTask;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginScreen extends Activity {
	
	private EditText  username=null;
	private EditText  password=null;
	private Button login=null;
	private Button signup=null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		username = (EditText)findViewById(R.id.username_et);
		password = (EditText)findViewById(R.id.password_et);

	    signup = (Button)findViewById(R.id.signup_button);
	    login = (Button)findViewById(R.id.login_button);
		    
	    signup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						RegistrationScreen.class);
				startActivity(i);
			}
		});
	    login.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	
	        	if(username.getText().toString().equals("") || password.getText().toString().equals("")) {
	        		Toast.makeText(getApplicationContext(), "Please Enter your Credentials !!", 
				      		Toast.LENGTH_SHORT).show();
	        	}else 	
	        		new LoginTask(getApplicationContext()).execute(username.getText().toString(),password.getText().toString());
	        }
	    });
	    
	}
}
