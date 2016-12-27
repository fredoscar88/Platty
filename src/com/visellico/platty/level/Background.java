package com.visellico.platty.level;

import com.visellico.graphics.Screen;
import com.visellico.graphics.Sprite;

public class Background implements Addable {

	public static Sprite spriteBackground;
	
	public int x, y;
	public int width, height;
	
	public Background() {
	}
	
	public void update() {
		
	}
	
	/**
	 * Renders background, drawing from the upper left (level x + 1, level height - 1)
	 * To the right of the screen (width - 1)
	 * @param screen
	 */
	public void render(Screen screen) {
		screen.renderSpriteTiled(x + 1, height - 1, width - 1, height - 1, spriteBackground);
	}

	public void init(Level l) {
		spriteBackground = l.levelType.spriteBackground;
		x = 0;
		y = 0;
		width = l.width;
		height = l.height;
	}
	
}
