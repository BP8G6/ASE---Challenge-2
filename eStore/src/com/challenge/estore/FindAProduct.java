/**
 * @author: Ramesh Kuthala
 */
package com.challenge.estore;

import java.util.ArrayList;
import java.util.Locale;

import com.challenge.utilities.DownloadAndSetItemImage;
import com.challenge.utilities.ItemDetails;
import com.challenge.utilities.XMLParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FindAProduct extends Activity implements OnInitListener {

	static final String TAG = "SearchAndSelectItem";
	TextToSpeech tts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_and_select_item);

		tts = new TextToSpeech(this, this);
		
		final EditText selectitemtext = (EditText)findViewById(R.id.selectitemtext);
		final ListView list = (ListView) findViewById(R.id.itemlist);
		Button searchButton = (Button)findViewById(R.id.search);
		
		selectitemtext.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					list.setVisibility(View.INVISIBLE);
			}
		});
		
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String itemToSearch = selectitemtext.getText().toString();
				
				if(itemToSearch.isEmpty())
				{
					Toast.makeText(getApplicationContext(), "Enter something to search", Toast.LENGTH_SHORT).show();
					return;
				}
				itemToSearch = itemToSearch.replaceAll(" ", "+");
				new DownLoadItemList(list, FindAProduct.this).execute(itemToSearch);
				list.setVisibility(View.VISIBLE);
				
				InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
				inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
				
				list.requestFocus();
			}
		});
		
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i = new Intent(getApplicationContext(),
						ProductDetails.class);
				// pass ItemDetails obj -- serialize
				ItemDetails itemDetails = (ItemDetails)list.getAdapter().getItem(arg2);
				Bundle bundle = new Bundle();
				bundle.putSerializable("ItemDetails", itemDetails);
				i.putExtras(bundle);
				startActivity(i);
			}
		});
	}

	@Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
        	Log.e(TAG, "Shutting down tts");
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
	
	private class DownLoadItemList extends
			AsyncTask<String, Void, ArrayList<ItemDetails>> {

		ProgressDialog pDialog;
		ListView listItems;
		public DownLoadItemList(ListView list, FindAProduct instance) {
			this.listItems = list;
		}

		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            tts.speak("Please wait", TextToSpeech.QUEUE_FLUSH, null);
            pDialog = new ProgressDialog(FindAProduct.this);
            pDialog.setMessage(Html.fromHtml("Please wait..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
		
		@Override
		protected ArrayList<ItemDetails> doInBackground(String... params) {
			return XMLParser.getInstance().iItemList(params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<ItemDetails> listOfSearchedItems) {
			pDialog.cancel();
			listItems.setAdapter(new ItemListArrayAdapter(FindAProduct.this, listOfSearchedItems));
		}
	}

	public class ItemListArrayAdapter extends ArrayAdapter<ItemDetails> {
		private final Context context;
		private final ArrayList<ItemDetails> values;

		public ItemListArrayAdapter(Context context, ArrayList<ItemDetails> values) {
			super(context, R.layout.inflate_item_list_, values);
			this.context = context;
			this.values = values;
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
			ImageView itemImage = (ImageView) rowView.findViewById(R.id.itemimage);
			if (values.get(position).getName().equalsIgnoreCase("NOITEM")) {
				itemImage.setVisibility(View.GONE);
				priceView.setVisibility(View.GONE);
				titleView.setText("Item Not Found");
				titleView.setTextColor(Color.RED);
			} else {
				titleView.setText(values.get(position).getName());
				titleView.setTextColor(Color.BLACK);
				priceView.setText("$" + values.get(position).getPricing());
				new DownloadAndSetItemImage().execute(itemImage,
						values.get(position).getImageLocation());
			}
			return rowView;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.cart_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_show_cart) {
			Intent i = new Intent(getApplicationContext(), ShowCart.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.US);
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
	}
}
