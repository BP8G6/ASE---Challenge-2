package com.challenge.estore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

public class SplashScreen extends Activity {

	protected boolean splashActive = true;
	protected int splashTime = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();

		setContentView(R.layout.activity_splash_screen);

		// thread for displaying the SplashScreen
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waitTime = 0;
					while (splashActive && (waitTime < splashTime)) {
						sleep(100);
						if (splashActive) {
							waitTime += 100;
						}
					}
				} catch (InterruptedException e) {
					// do nothing
					Toast.makeText(getApplicationContext(), " text ",
							Toast.LENGTH_SHORT).show();
				} finally {
					Intent i = new Intent(getApplicationContext(),
							LoginScreen.class);
					startActivity(i);
					finish();
				}// end of finally
			}// end of run()
		};// end of thread
		splashTread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			splashActive = false;
		}
		return true;
	}
}
