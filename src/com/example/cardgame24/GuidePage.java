package com.example.cardgame24;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class GuidePage extends Activity {

	Intent intent = new Intent();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_page);
		
		ImageButton back = (ImageButton)findViewById(R.id.back_start);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent.setClass(GuidePage.this, StartPage.class);
				startActivity(intent);
				finish();
			}
		});
		
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){ 
        	System.out.println("Back Avaliable"); 
        	}
        return false;
	}

}
