package com.challenge.estore;


import com.challenge.utilities.Registration;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationScreen extends Activity {

	private EditText  firstname=null;
	private EditText  lastname=null;
	private EditText  address=null;
	private EditText  city=null;
	private EditText  state=null;
	private EditText  mobile=null;
	private EditText  username=null;
	private EditText  password=null;
	private EditText  confirmpassword=null;
	private Button register=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_registration);

		firstname = (EditText)findViewById(R.id.firstname_et);
		lastname =  (EditText)findViewById(R.id.lastname_et);
		address = (EditText)findViewById(R.id.address_et);
		city = (EditText)findViewById(R.id.city_et);
		state = (EditText)findViewById(R.id.state_et);
		mobile = (EditText)findViewById(R.id.mobilenumber_et);
		username = (EditText)findViewById(R.id.username_reg_et);
		password = (EditText)findViewById(R.id.password_reg_et);
		confirmpassword = (EditText)findViewById(R.id.confirmpassword_reg_et);
		register = (Button)findViewById(R.id.register_button);
		
		register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(validateRegistration()){
					new Registration(getApplicationContext()).execute(firstname.getText().toString(),
							lastname.getText().toString(),mobile.getText().toString(),address.getText().toString(),
							city.getText().toString(),state.getText().toString(),
							username.getText().toString(),password.getText().toString());
					
					//Toast.makeText(getApplicationContext(), "Registered Successfully...", 
			      		//      Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "Enter details properly...", 
			      		      Toast.LENGTH_SHORT).show();
			      		    
				}
			}
		});

	}
	private boolean validateRegistration(){
		
		boolean validator = true;
		
		if(firstname.getText().toString() == null || firstname.getText().toString().equals("")) {
			firstname.setHint("enter firstname");
			validator = false;
		}
		else if(lastname.getText().toString() == null || lastname.getText().toString().equals("")) {
			lastname.setHint("enter lastname");
			validator = false;
		}
		else if(address.getText().toString() == null || address.getText().toString().equals("")) {
			address.setHint("enter address");
			validator = false;
		}
		else if(city.getText().toString() == null || city.getText().toString().equals("")) {
			city.setHint("enter city");
			validator = false;
		}
		else if(state.getText().toString() == null || state.getText().toString().equals("")) {
			state.setHint("enter state");
			validator = false;
		}
		else if(mobile.getText().toString() == null || mobile.getText().toString().equals("")) {
			mobile.setHint("enter mobilenumber");
			validator = false;
		}
		else if(username.getText().toString() == null || username.getText().toString().equals("")) {
			username.setHint("enter password");
			validator = false;
		}
		else if(password.getText().toString() == null || password.getText().toString().equals("")) {
			password.setHint("enter password");
			validator = false;
		}
		else if(confirmpassword.getText().toString() == null || confirmpassword.getText().toString().equals("")) {
			confirmpassword.setHint("enter confirm password");
			validator = false;
		}
		else if(! (confirmpassword.getText().toString().equalsIgnoreCase(password.getText().toString()))) {
			System.out.println("passwords didn't match");
			Toast.makeText(getApplicationContext(), "Password Mismatch", 
	      		      Toast.LENGTH_SHORT).show();
			validator = false;
		}
		return validator;
	}

}