package com.visellico.platty.entity.collectibles;

import com.visellico.graphics.Screen;
import com.visellico.graphics.Sprite;
import com.visellico.platty.entity.Entity;
import com.visellico.platty.entity.Hitbox;
import com.visellico.platty.level.Collideable;
import com.visellico.platty.level.Level;

public class CollectibleItem extends Entity {

	protected Sprite sprite;
	protected Hitbox hitbox;
	
	int x, y;
	
	public void render(Screen screen) {
		
	}
	
	
	//Override section
	public boolean isColliding(Collideable c) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onCollision(Collideable c) {
		// TODO Auto-generated method stub
		
	}

	public void init(Level l) {
		// TODO Auto-generated method stub
		
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

}
