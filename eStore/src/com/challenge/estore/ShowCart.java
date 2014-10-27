/**
 * @author: Ramesh Kuthala
 * 27-Sep-2014
 */
package com.challenge.estore;


import java.util.ArrayList;

import com.challenge.utilities.ItemDetails;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ShowCart extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_cart);

		final ListView list = (ListView) findViewById(R.id.selecteditemlist);

		final ItemListArrayAdapter itemListArrayAdapter = new ItemListArrayAdapter(this);
		list.setAdapter(itemListArrayAdapter);
			
		Button checkout = (Button) findViewById(R.id.checkout);
		final TextView totalPrice = (TextView) findViewById(R.id.totalbill);

		if (CartData.getInstance().getSelectedItemList().size() == 0) {
			totalPrice.setVisibility(View.GONE);
			checkout.setVisibility(View.GONE);
		}
		
		checkout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(getApplicationContext(), ThankYou.class);
				startActivity(i);
			}
		});
		
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				final int selectedPos = arg2;
				final ItemDetails itemDetails = (ItemDetails)list.getAdapter().getItem(selectedPos);
				System.out.println("+++ $$ grocery on Item long click invoked +++");
				AlertDialog.Builder builder = new AlertDialog.Builder(ShowCart.this);
				builder.setMessage("Do you want delete the Item?")
						.setCancelable(true)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										CartData.getInstance().removeSelectedItem(itemDetails);
										itemListArrayAdapter.notifyDataSetChanged();
										totalPrice.setText("Total Price: $"
												+ CartData.getInstance().getTotalPrice());
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
									}
								});
		        builder.create().show();
		        
				return false;
			}
		});

		totalPrice.setText("Total Price: $"
				+ CartData.getInstance().getTotalPrice());
	}

	public class ItemListArrayAdapter extends ArrayAdapter<ItemDetails> {
		private final Context context;
		private final ArrayList<ItemDetails> values;

		public ItemListArrayAdapter(Context context) {
			super(context, R.layout.inflate_item_list_, CartData.getInstance().getSelectedItemList());
			this.values = CartData.getInstance().getSelectedItemList();
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.inflate_item_list_,
					parent, false);
			TextView titleView = (TextView) rowView
					.findViewById(R.id.itemtitle);
			TextView priceView = (TextView) rowView.findViewById(R.id.price);
			ImageView imageView = (ImageView) rowView
					.findViewById(R.id.itemimage);
			imageView.setVisibility(View.GONE);
			titleView.setText(values.get(position).getName());
			priceView.setText("$" + values.get(position).getPricing());
			return rowView;
		}
	}
}
