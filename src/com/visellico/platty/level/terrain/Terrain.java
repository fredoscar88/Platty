package com.visellico.platty.level.terrain;

import com.visellico.graphics.Screen;
import com.visellico.platty.level.Addable;
import com.visellico.platty.level.Collideable;
import com.visellico.platty.level.Level;

public class Terrain implements Addable, Collideable {

	public int x;
	public int y;
	public int width;
	public int height;
	public String name;
	
	public void update() {
		
	}
	
	public void render(Screen screen) {
		
	}
	
	public void init(Level l) {
		
	}

	public boolean isColliding(Collideable c) {
		return false;
	}

	public void onCollision(Collideable c) {
		
	}
	
}
