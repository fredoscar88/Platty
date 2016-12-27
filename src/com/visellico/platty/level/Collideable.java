package com.visellico.platty.level;

public interface Collideable {

	public boolean isColliding(Collideable c);
	
	public void onCollision(Collideable c);
	
}
