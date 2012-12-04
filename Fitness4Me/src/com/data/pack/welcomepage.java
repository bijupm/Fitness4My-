package com.data.pack;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.data.fitness4me.R;

public class welcomepage extends Activity {
	private static float GESTURE_THRESHOLD_DIP = 13.0f;
	private VOUserDetails obUser = new VOUserDetails(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcomepage);
		TextView txtwelcome = (TextView) findViewById(R.id.txtwelcome);
		TextView lblWelcomeHead = (TextView) findViewById(R.id.lblWelcomeHead);
		txtwelcome.setText(Html.fromHtml(getString(R.string.welcomemessage)));
		Button imgButton = (Button) findViewById(R.id.imgButton);

		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/ARIAL.TTF");
		txtwelcome.setTypeface(tf);
		lblWelcomeHead.setTypeface(tf);
		imgButton.setTypeface(tf);
		// Convert the dips to pixels
		final float scale = getResources().getDisplayMetrics().density;
		float mGestureThreshold = (int) (GESTURE_THRESHOLD_DIP * scale + 0.5f);

		lblWelcomeHead.setTextSize(mGestureThreshold);
		txtwelcome.setLineSpacing(mGestureThreshold, .5f);
		txtwelcome.setTextSize(TypedValue.COMPLEX_UNIT_PX, mGestureThreshold);
		if (obUser.getSelectedLanguage().equals("1")) {
			lblWelcomeHead.setText("Welcome to fitness4.me");
			// txtwelcome.setText("Welcome to fitness4.me");
			txtwelcome
					.setText("This is your own personal gym!\n"
							+ "With this app or our web access you can exercise ANYWHERE and ANYTIME.\n"
							+ "Here you will find workouts designed to give you the best results in the shortest time.\n"
							+ "You will be amazed how fast you will see and feel results by just working out 10 minutes a day.\n"
							+ "Have fun and don’t forget: YOU ALWAYS HAVE TEN MINUTES!");
		} else {
			imgButton.setText("Fortfahren");
			lblWelcomeHead.setText("Willkommen bei fitness4.me");
			txtwelcome
					.setText("Hier ist dein persönliches Fitnessstudio!\n"
							+ "Mit diesen app oder unsere Webzugang kannst du IMMER und ÜBERALL trainieren.\n"
							+ "Wir haben hart daran gearbeitet die bestmöglichen und effizientesten Workouts zusammenzustellen damit du schnellst möglichst Ergebnisse siehst und spürst.\n"
							+ "Viel Spass!");
		}
		imgButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					Intent intent = new Intent(welcomepage.this, userinfo.class);
					startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

}
