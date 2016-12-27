package com.visellico.platty.level;

import com.visellico.graphics.Screen;
import com.visellico.graphics.Sprite;
import com.visellico.util.MathUtils;

public class BackgroundObject implements Addable {

	private Sprite spriteBackgroundObject;
	
	public int x, y;
	public int dist;
	public int width, height;
	
	public BackgroundObject(int x, int y, int distFromFore) {
		this.x = x;
		this.y = y;
		dist = distFromFore + 1;
		System.out.println(x + " " + y);
	}
	
	public void update() {
		
	}
	
	/**
	 * Renders background, drawing from the upper left (level x + 1, level height - 1)
	 * To the right of the screen (width - 1)
	 * @param screen
	 */
	public void render(Screen screen) {
//		screen.renderSpriteTiled(x + 1, height - 1, width - 1, height - 1, spriteBackgroundObject);
		screen.renderSpriteFar(x , y, spriteBackgroundObject, dist);
	}

	public void init(Level l) {
		spriteBackgroundObject = l.levelType.spriteBackgroundObject;
		width = spriteBackgroundObject.getWidth();
		height = spriteBackgroundObject.getHeight();
		
		int lXHalf = l.width/2;
		int lYHalf = l.height/2;
		
		System.out.println(x + " " + lXHalf);
		System.out.println(y + " " + lYHalf);
//		x += Math.abs(lXHalf - x) / 2;
		x += MathUtils.distance(x, y, lXHalf, y) / dist;
//		y += MathUtils.distance(x, y, x, lYHalf) / dist;
		System.out.println(x + " " + y);
		
	}

}
