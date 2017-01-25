package com.visellico.platty.level.entity.collectibles;

import com.visellico.graphics.Sprite;
import com.visellico.platty.level.Collideable;

public class Key implements Collectible {

	private Sprite spriteKey;
	
	public boolean isColliding(Collideable c) {
		return false;
	}

	public void onCollision(Collideable c) {
		//if c is a player, have "c" collect the object.
		//	There may be a mob that can also collect content.
		//	This may not be how I want to handle collectibles- it seems a little strange. Im not sure how I want to do this yet.
		//	For instance- we can make the Collectible interface a Marker interface and just get rid of the collect portion.
		//	This way we can perhaps have a collect method in the mob that collects only collectibles.
		collect();
	}

	public void collect() {
		
	}

	
	
}
