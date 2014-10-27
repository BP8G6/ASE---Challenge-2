/**
 * @author: Ramesh Kuthala
 */
package com.challenge.estore;

import java.util.Locale;

import com.challenge.utilities.DownloadAndSetItemImage;
import com.challenge.utilities.ItemDetails;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDetails extends Activity implements OnInitListener {

	private TextToSpeech tts;
	Button addToCart;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_show_item_details);

		tts = new TextToSpeech(this, this);
        
		final ItemDetails itemDetails = (ItemDetails)getIntent().getSerializableExtra("ItemDetails");
		System.out.println("item name from bundle: "+ itemDetails.getName());
		TextView itemtitle = (TextView) findViewById(R.id.itemtitle);
		TextView itemPrice = (TextView) findViewById(R.id.itemprice);
		ImageView itemImage = (ImageView) findViewById(R.id.itemimage);
		itemtitle.setText(itemDetails.getName());
		
		TextView itemdisc = (TextView) findViewById(R.id.itemdisc);
		itemdisc.setText(itemDetails.getDescription());
		itemPrice.setText("$"+itemDetails.getPricing());
		new DownloadAndSetItemImage().execute(itemImage, itemDetails.getImageLocation());
		
		addToCart = (Button)findViewById(R.id.addtocart);
		addToCart.setEnabled(false);
		addToCart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CartData.getInstance().addSelectedItem(itemDetails);
				tts.speak(itemDetails.getName() + " is added to the cart", TextToSpeech.QUEUE_FLUSH, null);
				Toast.makeText(getApplicationContext(), "Your Item is added to list", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}

	@Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
	
	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			 
            int result = tts.setLanguage(Locale.US);
            
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }else
            {
            	addToCart.setEnabled(true);
            }
 
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
		
	}
}
