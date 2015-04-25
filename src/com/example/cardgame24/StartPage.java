package com.example.cardgame24;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class StartPage extends Activity{
	
	ImageButton guide, game;
	Intent intent = new Intent();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_page); 
		
		guide = (ImageButton)findViewById(R.id.guides);	
		guide.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent.setClass(StartPage.this, GuidePage.class);
				startActivity(intent);
				finish();
			}
		});
				
		game = (ImageButton)findViewById(R.id.startGame); 
		game.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent.setClass(StartPage.this, GamePage.class);
				startActivity(intent);
			}
		});
	}
	
	private long exitTime = 0;
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN ) {
        	if ( (System.currentTimeMillis() - exitTime) > 2000 ) {
        		Toast.makeText(getApplicationContext(), "Click once more to quit. ", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
                } else { 
                	finish();
                	System.exit(0);
                	}
        	return true;
        	}
        return super.onKeyDown(keyCode, event);
    }

}