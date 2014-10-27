/**
 * @author: Ramesh Kuthala
 * 27-Sep-2014
 */
package com.challenge.estore;

import java.util.Calendar;

import com.challenge.utilities.Stores;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ThankYou extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thank_you);
		
		Stores store = CartData.getInstance().getSelectedStore();
		
		TextView storeName = (TextView)findViewById(R.id.storename);
		storeName.setText(store.getStoreName());

		TextView totalPrice = (TextView)findViewById(R.id.totalpricetext);
		totalPrice.setText("$"+CartData.getInstance().getTotalPrice());

		TextView date = (TextView) findViewById(R.id.date);
		date.setText(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
				+ "/" + Calendar.getInstance().get(Calendar.MONTH) + "/"
				+ Calendar.getInstance().get(Calendar.YEAR));
		
		TextView thankyoumsg = (TextView) findViewById(R.id.thankyoumsg);
		String conclusion = "Thank you for shopping with us.\n";
		conclusion = conclusion + "Your order will be ready in 2 hrs.";
		thankyoumsg.setText(conclusion);
		
		TextView address = (TextView) findViewById(R.id.storeinfo);
		
		String addressText = "\n\t\t";
		addressText += store.getAddress() + ",\n\t\t";
		if(store.getPhone() != null)
			addressText += store.getPhone() + ",\n\t\t";
		addressText += store.getCity() + " ";
		addressText += store.getZip() + ",\n\t\t";
		addressText += store.getState() + ", US";
		
		address.setText(addressText);
	}
}
