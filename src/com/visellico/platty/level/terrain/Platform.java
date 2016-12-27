package com.visellico.platty.level.terrain;

import com.visellico.graphics.Screen;
import com.visellico.graphics.Sprite;
import com.visellico.platty.level.Level;

public class Platform extends Terrain {

	private Sprite spritePlatform;
	
	public Platform(int x, int y, int length) {
		this.x = x;
		this.y = y;
		this.height = 3;
		this.width = length;
	}
	
	public void update() {
		
	}
	
	public void render(Screen screen) {
		screen.renderSpriteTiled(x, y, width, height, spritePlatform);
	}
	
	public void init(Level l) {
		spritePlatform = l.levelType.spritePlatform;
	}
	
}
