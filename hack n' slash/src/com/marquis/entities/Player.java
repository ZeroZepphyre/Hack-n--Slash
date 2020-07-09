package com.marquis.entities;

import java.awt.image.BufferedImage;

public class Player extends Entity{
	
	
	public boolean right,left,up,down;
	public int speed = 4;
	public String debug = "debug";
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
	}
	@Override
	public void tick() {
		if(right == true) {
			x+=speed;
		}else if(left == true) {
			x-=speed;
		}
		if (up == true) {
			y-=speed;
		}else if(down == true) {
			y+=speed;
			
		}
		
	}public void setRight(boolean newRight){
		this.right = newRight;
	}
	

}
