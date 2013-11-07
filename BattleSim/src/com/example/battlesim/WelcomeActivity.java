package com.example.battlesim;

import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		if(!ParseUser.getCurrentUser().has("char")){
			setContentView(R.layout.activity_welcome);
		}
		else{
			setContentView(R.layout.activity_welcomechar);
			//((TextView) findViewById(R.id.textView2)).setText(ParseUser.getCurrentUser().getUsername().toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
        case R.id.logout:
            logout();
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
	}
	
	public void logout(){
		ParseUser.logOut();
		finish();
	}
}
