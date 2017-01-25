package com.visellico.platty.level.entity.collectibles;

import com.visellico.graphics.Screen;
import com.visellico.platty.level.Level;
import com.visellico.platty.level.entity.Hitbox;

public class Coin extends CollectibleItem {

	public Coin(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void update() {
		
	}
	
	public void render(Screen screen) {
		screen.renderSprite(x, y, sprite);
	}
	
	public void init(Level l) {
		sprite = l.levelType.spriteCoin;
		this.hitbox = new Hitbox(sprite.getWidth(), sprite.getHeight());
	}

}
