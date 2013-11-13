package com.example.battlesim;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class TrainingActivity extends Activity {
	public static CountDownTimer counter;
	public TextView refill;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_training);

		
		getTime();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.training, menu);
		return true;
	}
	
	public class MyCount extends CountDownTimer{

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
			
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			getTime();
		}

		@Override
		public void onTick(long untilFinished) {
			// TODO Auto-generated method stub
			
			refill.setText(Long.toString(untilFinished/1000));
			
		}
		
	}

	public void getTime() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Character");
		query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (object != null) {
					final TextView timer = (TextView) findViewById(R.id.refillTime);
					TextView numOfEnergy = (TextView) findViewById(R.id.numOfEnergy);
					int currentE = object.getInt("energy");
					int startE = currentE;
					Date lastDate = object.getDate("lastEnergyUse");
					while (lastDate.getTime() + 20000 < System.currentTimeMillis() && currentE < 3) {
						currentE++;
						lastDate = new Date(lastDate.getTime()+20000);
					}
					
					
					if(currentE != 3){
						CountDownTimer Count = new CountDownTimer(System.currentTimeMillis() + 20000 - lastDate.getTime(), 1000) {
						    public void onTick(long millisUntilFinished) {
						        timer.setText(Long.toString(millisUntilFinished/1000));
						    }

						    public void onFinish() {
						        getTime();
						    }
						};

						Count.start();
						
					//TrainingActivity.MyCount counter = new MyCount(System.currentTimeMillis() - lastDate.getTime(), 1000);
					//counter.start();
					}else{
						timer.setText("Energy full!");
					}
					
					numOfEnergy.setText(String.valueOf(currentE));
					object.put("energy", currentE);
					object.put("lastEnergyUsed", lastDate);
					try {
						object.save();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			}
		});
	}

	public void genericTrain(View view) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Character");
		query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (object != null) {
					int currentE = object.getInt("energy");
					if (currentE < 0) {
						Toast.makeText(getApplicationContext(),
								"Uh oh! You're out of energy!",
								Toast.LENGTH_LONG).show();
					} else {
						object.put("energy", currentE - 1);
						TextView energyDisplay = (TextView) findViewById(R.id.numOfEnergy);
						energyDisplay.setText(Integer.toString(currentE - 1));
						if (currentE == 3) {
							object.put("lastEnergyUse",
									new Date(System.currentTimeMillis()));
						}
						try {
							object.save();
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						getTime();
					}
				}
			}
		});
	}

}
