package com.example.cardgame24;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.singularsys.jep.Jep;
import com.singularsys.jep.JepException;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class GamePage extends Activity {	
	
	private TextView exportFormula, exportResult;
	private int[] operatorList = { R.id.plus, R.id.minus, R.id.multiply, R.id.divide, 
								R.id.leftBracket, R.id.rightBracket };
	private ImageButton[] operator = new ImageButton[ operatorList.length ];
	private int[] cardList = { R.id.card1, R.id.card2, R.id.card3, R.id.card4 };
	private ImageButton[] cards2Select = new ImageButton[ cardList.length ];
	private ImageButton clear, backspace, calculate;
	
	Card newCard[] = new Card[4];
	ArrayList<Integer> allCards = new ArrayList<Integer>();			// A card sheet without jokers. 
	ArrayList<String> expression = new ArrayList<String>(); 
	ArrayList<Integer> iden = new ArrayList<Integer>();				// Storing IDs of operands: -2 represents "(", -1 represents ")", 
																	// 0 represents "+", "-", "*" and "/". 
																	// 1, 2, 3, 4 represent 4 value of cards respectively. 
	
	ActionBar actionBar; 
	int rbNum = 0, lbNum = 0, flag = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_page);
		setButtons();  									// Activate the views of all text-viewer, calculators and cards. 
		setCards();    									// Generate a sheet of cards without 2 jokers and shuffle the cards. 
		
		calculate.setOnClickListener(new OnClickListener() {
			// Press " = " button to get the finalized result. 
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Jep jep = new Jep();
				String str = "";
				for( int i=0; i<expression.size(); i++ ){
					str += expression.get(i);
				}
				
				if( str == "" ){
					Toast.makeText(getApplicationContext(), "Calculating sequence empty. ", Toast.LENGTH_SHORT).show();
				}else if ( lbNum != rbNum ) {
					Toast.makeText(getApplicationContext(), "Bracket lost. ", Toast.LENGTH_SHORT).show();
				}
				else if ( newCard[0].selected == false || newCard[1].selected == false || newCard[2].selected == false ||newCard[3].selected == false ) {
					Toast.makeText(getApplicationContext(), "Card lost. ", Toast.LENGTH_SHORT).show();
				}
				else if ( flag == 1 ) {
					setCards();
					exportFormula.setText("");
					exportResult.setText("");
					expression.clear();
					lbNum = rbNum = 0;
					flag = 0;
				}
				else if ( flag == 2 ) {
					for (int i = 0; i < 4; i++) {
						newCard[i].selected = false;
						cards2Select[i].setAlpha(255);
					}
					expression.clear();
					iden.clear();
					exportResult.setText("");
					printing();
					lbNum = rbNum = 0;
					flag = 0;
				}
				else {
					try {
						jep.parse(str);
						Object result = jep.evaluate();
						String tempResult = String.valueOf(result);
						DecimalFormat optFormat = new DecimalFormat("0.00");
						Double doubleResult = Double.parseDouble(tempResult);
						int intResult = (int)Math.floor(doubleResult);
						
						if ( intResult == 24 && intResult == doubleResult ) {
							exportFormula.setTextColor(Color.parseColor("#ea0000"));
							exportResult.setTextColor(Color.parseColor("#ea0000"));
							exportResult.setText(" √       = 24 ");
							flag = 1;
						}else {
							exportResult.setTextColor(Color.parseColor("#000000"));
							exportResult.setText( "×       " + " = " + String.valueOf(optFormat.format(doubleResult)) );
							flag = 2;
						}
						}catch (JepException e) {
							// TODO: handle exception
							System.out.println("An error occured: " + e.getMessage());
						}
					}
			}
		});
		
		backspace.setOnClickListener(new OnClickListener() {
			// Delete one operand if the expression is wrong. 
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (flag == 0) {
					if( expression.isEmpty() == true ){
						Toast.makeText(getApplicationContext(), "Calculating sequence empty. ", Toast.LENGTH_SHORT).show();
					}
					else if( iden.get( iden.size() - 1 ) == -2 ){
						expression.remove( expression.size() - 1 );
						iden.remove( iden.size() - 1 );
						lbNum--;
						printing();
					}
					else if( iden.get( iden.size() - 1 ) == -1 ){
						expression.remove( expression.size() - 1 );
						iden.remove( iden.size() - 1 );
						rbNum--;
						printing();
					}
					else if( iden.get( iden.size() - 1 ) == 0 ){
						expression.remove( expression.size() - 1 );
						iden.remove( iden.size() - 1 );
						printing();
					}
					else if( iden.get( iden.size() - 1 ) > 0 ){
						newCard[iden.get(iden.size() - 1) - 1].selected = false; 
						cards2Select[iden.get(iden.size() - 1) - 1].setAlpha(255);
						if( newCard[iden.get(iden.size() - 1) - 1].selected == false ){
							expression.remove( expression.size() - 1 );
							iden.remove( iden.size() - 1 );
						}
						printing();
						exportResult.setText("");
					}
				}
			}
		});
		
		clear.setOnClickListener(new OnClickListener() {
			// Clear the expression shown in the EditText. 
			
			@SuppressWarnings("deprecation")
			@Override
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (flag == 0) {
					if( expression.isEmpty() == true ){
						Toast.makeText(getApplicationContext(), "Calculating sequence empty. ", Toast.LENGTH_SHORT).show();
					}else {
						for (int i = 0; i < 4; i++) {
							newCard[i].selected = false;
							cards2Select[i].setAlpha(255);
						}
						expression.clear();
						iden.clear();
						lbNum = rbNum = 0;
						exportResult.setText("");
						printing();
					}
				}
				
			}
		});

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();  
	    inflater.inflate(R.menu.main_menu, menu); 
		return true;
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    
		switch (item.getItemId()) {
	    case R.id.refresh:
	    	setCards();
			exportFormula.setText("");
			exportResult.setText("");
			expression.clear();
			lbNum = rbNum = 0;
			flag = 0;
	        return true;
	    case R.id.test:
	    	setTest();
	    	exportFormula.setText("");
			exportResult.setText("");
			expression.clear();
			lbNum = rbNum = 0;
			flag = 0;
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
		
	}
		
	public void setButtons(){
		// Implementation of activating the views of all text-viewer, calculators and cards. 
		
		clear = (ImageButton)findViewById(R.id.clr);
		backspace = (ImageButton)findViewById(R.id.bsc);
		calculate = (ImageButton)findViewById(R.id.equal);
		exportFormula = (TextView)findViewById(R.id.formula);
		exportResult = (TextView)findViewById(R.id.result);
		exportFormula.setEnabled(false);
		exportResult.setEnabled(false);

		for( int i = 0; i <= 51; i++ ){ allCards.add(i); }
		
		GetOpt getOpt = new GetOpt();
		for( int i = 0; i < operatorList.length; i++ ){
			operator[i] = (ImageButton)findViewById( operatorList[i] );
			operator[i].setOnClickListener( getOpt );
		}
		
		GetCard getCard = new GetCard();
		for( int i = 0; i < cardList.length; i++ ){
			cards2Select[i] = (ImageButton)findViewById( cardList[i] );
			cards2Select[i].setOnClickListener( getCard );
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public void setCards(){
		// Implementation of generating a sheet of cards without 2 jokers and shuffle the cards
		
		Collections.shuffle( allCards );
		for( int i = 0; i < 4; i++ ){
			newCard[i] = new Card( allCards.get(i), i+1 );
			cards2Select[i].setAlpha(255);
		}
		
		String fileName[] = new String[4];
		for ( int i = 0; i < 4; i++ ){
			fileName[i] = newCard[i].imageName;
			cards2Select[i].setImageResource( getResources().getIdentifier(fileName[i], "drawable", "com.example.cardgame24") );
		}

	}
	
	@SuppressWarnings("deprecation")
	public void setTest(){
		// Implementation of generating a sheet of cards without 2 jokers and shuffle the cards
		
		int[] testValueList = { 4, 5, 9, 10 };
		Random random = new Random();
		ArrayList<Integer> testList = new ArrayList<Integer>();
		
		for( int i = 0; i < 4; i++ ){
			testList.add( testValueList[i] + 13*(random.nextInt(4)) - 1 );
		}
		Collections.shuffle( testList );
		for (int i = 0; i < 4; i++) {
			newCard[i] = new Card( testList.get(i), i+1 );
			cards2Select[i].setAlpha(255);
		}
		
		String fileName[] = new String[4];
		for ( int i = 0; i < 4; i++ ){
			fileName[i] = newCard[i].imageName;
			cards2Select[i].setImageResource( getResources().getIdentifier(fileName[i], "drawable", "com.example.cardgame24") );
		}

	}
	
	public void printing(){
		// Showing expression on the text-viewer. 
		
		String temp = "";
		for( int i=0; i<expression.size(); i++ ){
			temp += expression.get(i);
		}
		exportFormula.setTextColor(Color.parseColor("#000000"));
		exportResult.setTextColor(Color.parseColor("#ea0000"));
		exportFormula.setText(temp);
		
	}
	
	class GetOpt implements OnClickListener{
		// Listeners of the operators. 
		
		String opt;
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (flag == 0) {
				switch(v.getId()){
				case R.id.plus: 
					opt = "+"; break;
				case R.id.minus: opt = "-"; break;
				case R.id.multiply: opt = "*"; break;
				case R.id.divide: opt = "/"; break;
				case R.id.leftBracket: opt = "("; break;
				case R.id.rightBracket: opt = ")"; break;
			}
			
			if( rbNum == lbNum && opt == ")" ){
				// While the number of open brackets is not equal to that of close brackets. 
				Toast.makeText(getApplicationContext(), "Unexpected close bracket. ", Toast.LENGTH_SHORT).show();
			}
			else if( lbNum > 3 && opt == "(" ){
				// While the number of open brackets is exceeded to 4. 
				Toast.makeText(getApplicationContext(), "Open bracket number exceed. ", Toast.LENGTH_SHORT).show();
			}
			else if( expression.isEmpty() == true && opt != "(" ){
				// While the initial input is not one of card values or open brackets. 
				Toast.makeText(getApplicationContext(), "Unexpected Input", Toast.LENGTH_SHORT).show();
			}
			else if( expression.isEmpty() == false && iden.get(iden.size() - 1) == -2 && opt != "(" ){
				// While the potential input is one of the other operators next to an open bracket. 
				Toast.makeText(getApplicationContext(), "Unexpected input except for cards. ", Toast.LENGTH_SHORT).show();
			}
			else if( expression.isEmpty() == false && iden.get(iden.size() - 1) == 0 && opt != "(" ){
				// Operators like  "+", "-", "*" and "/" should only be input before "(" and values. 
				Toast.makeText(getApplicationContext(), "Unexpected input except for cards / open bracket. ", Toast.LENGTH_SHORT).show();
			}
			else if( expression.isEmpty() == false && iden.get(iden.size() - 1) == -1 && opt == "(" ){
				// Close bracket should not be input next to an open bracket. 
				Toast.makeText(getApplicationContext(), "Unexpected open bracket. ", Toast.LENGTH_SHORT).show();
			}
			else if( expression.isEmpty() == false && iden.get(iden.size() - 1) > 0 && opt == "(" ){
				// Open bracket should not be input next to values of cards. 
				Toast.makeText(getApplicationContext(), "Unexpected open bracket. ", Toast.LENGTH_SHORT).show();
			}
			else{
				expression.add(opt);
				if( opt == "+" || opt == "-" || opt == "*" || opt == "/" ){ iden.add(0); }
				else if( opt == "(" ){ iden.add(-2); lbNum++; }
				else if( opt == ")" ){ iden.add(-1); rbNum++; }
			}
			printing();
			}
		}
		
	}
	
	class GetCard implements OnClickListener{
		// Listeners of those four cards. 
		
		String cardValue;
		int index; 
		
		@SuppressWarnings( "deprecation" )
		@Override
		public void onClick(View v) {
			
			// TODO Auto-generated method stub
			if (flag == 0) {
				switch(v.getId()){
				case R.id.card1:
					cardValue = String.valueOf(newCard[0].getValue());
					index = 0;
					break;
				case R.id.card2:
					cardValue = String.valueOf(newCard[1].getValue());
					index = 1;
					break;
				case R.id.card3:
					cardValue = String.valueOf(newCard[2].getValue());
					index = 2;
					break;
				case R.id.card4:
					cardValue = String.valueOf(newCard[3].getValue());
					index = 3;
					break;
			}
			if( newCard[index].selected == true ){
				// One card should not be selected anymore in the same calculation. 
				Toast.makeText(getApplicationContext(), "Card selected before. ", Toast.LENGTH_SHORT).show();
			}
			else if( expression.isEmpty() == false && iden.get( iden.size() - 1) > 0 ){
				// A value should not be followed by another card's value. 
				Toast.makeText(getApplicationContext(), "Unexpected input except for operators. ", Toast.LENGTH_SHORT).show();
			}
			else if( expression.isEmpty() == false && iden.get( iden.size() - 1) == -1 ){
				// A value should not follow a close bracket directly. 
				Toast.makeText(getApplicationContext(), "Unexpected card input. ", Toast.LENGTH_SHORT).show();
			}
			else{
				newCard[index].selected = true;
				expression.add(cardValue);
				iden.add(newCard[index].getPosition());
				cards2Select[index].setAlpha(90);
				printing();
			}
			}
		}
		
	}
	
}
