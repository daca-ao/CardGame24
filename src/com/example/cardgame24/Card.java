package com.example.cardgame24;

public class Card {
	/*
	 * 
	 * We can regard every card as an object which is the instant of the class Card, 
	 * therefore we can get its index, actual value(which ranges from 1 to 13), 
	 * position and name according to the image in resources. This is a more 
	 * convenient way to operate. 
	 * 
	 */
	private 
		int index, value, position;
		String imageName = "";
		boolean selected;
	
	public Card( int idx, int pos ){
		index = idx; 
		value = (index % 13) + 1; 
		position = pos;
		imageName = creatName();
		selected = false;
	}
		
	public int getIndex() {return index;}
	public void setIndex(int index) {this.index = index;}

	public int getValue() {return value;}
	public void setValue(int value) {this.value = value;}

	public int getPosition() {return position;}
	public void setPosition(int position) {this.position = position;}

	public String getImageName() {return imageName;}
	public void setImageName(String imageName) {this.imageName = imageName;}

	public boolean isSelected() {return selected;}
	public void setSelected(boolean selected) {this.selected = selected;}

	public String creatName(){
		String prefix = "bordered_";
		String fig = ""; 
		String num = "";
		
		switch( (index/13) ){
			case 0: fig = "s"; break;
			case 1: fig = "h"; break;
			case 2: fig = "c"; break;
			case 3: fig = "d"; break;
		}
		
		if( value >1 && value <11){
			num = String.valueOf(value);
		}else switch( value ){
			case 1: num = "a"; break;
			case 11: num = "j"; break; 
			case 12: num = "q"; break; 
			case 13: num = "k"; break; 
		}
		imageName = prefix + fig + '_' + num;
		return imageName;
	}

}