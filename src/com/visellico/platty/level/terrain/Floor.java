package com.visellico.platty.level.terrain;

import com.visellico.graphics.Screen;
import com.visellico.graphics.Sprite;
import com.visellico.platty.level.Level;

public class Floor extends Terrain {
	
	public static Sprite spriteFloor;
	public static Sprite spriteTop;
	public static Sprite spriteTrimLeft;
	public static Sprite spriteTrimRight;
	
		
	public Floor(int x, int y, int length) {
		
		this(x,y,length,"NONAME");
		
	}
	
	public Floor(int x, int y, int length, String name) {
		this.x = x;
		this.y = y;
		this.width = length;
//		this.height = height;
		this.height = y;
		//Important: Height and length do not have to be sprite units. They are in fact pixel units. I hope this works anyway.
		
		this.name = name;
	}
	
	/**
	 * Renders all of the sprites that make up the floor. They are tiled. Woo
	 */
	public void render(Screen screen) {
		
		//TODO Create sprite rendering method that can cut a sprite off after a certain length
		
		
//		int spriteHeight = spriteFloor.getHeight();
//		int spriteWidth = spriteFloor.getWidth();
		
		screen.renderSpriteTiled(x, y, width, height, spriteFloor);
//		screen.renderSpriteTiled(x, y, width, height, spriteTrimLeft);	//Pretty cool looknig with Debug2
		screen.renderSpriteTiled(x, y, 2, height, spriteTrimLeft);	//Pretty cool looknig with Debug2
		screen.renderSpriteTiled(x + width - 2, y, 2, height, spriteTrimRight);	//Pretty cool looknig with Debug2
		screen.renderSpriteTiled(x, y, width, 3, spriteTop);
		
//		for (int i = 0; i < height / spriteHeight; i++) {
//			for (int j = 0; j < width / spriteWidth; j++) {
//				
//				//Noting that one floor object has multiple, even fractional, sprites.
//				//NOte as well, the y one is minus because it goes down while the x is pos because it goes right.
//				screen.renderSprite(x + (j * spriteWidth), y - (i * spriteHeight), sprite);
//				
//			}
//		}
	}
	
	//Doesn't necessarily have to be used
	public void update() {
		
		
	}
	
	public void init(Level l) {
		spriteFloor = l.levelType.spriteFloor;
		spriteTop = l.levelType.spriteFloorTop;
		spriteTrimLeft = l.levelType.spriteFloorTrimLeft;
		spriteTrimRight = l.levelType.spriteFloorTrimRight;
	}
	
}
