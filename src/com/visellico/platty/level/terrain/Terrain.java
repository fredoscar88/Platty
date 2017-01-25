package com.visellico.platty.level.terrain;

import com.visellico.graphics.Screen;
import com.visellico.platty.level.Addable;
import com.visellico.platty.level.Collideable;
import com.visellico.platty.level.Level;
import com.visellico.rainecloud.serialization.RCObject;

public class Terrain implements Addable, Collideable {

	public static final String TERRAIN_NAME = "terrain";
	
	public int x;
	public int y;
	public int width;
	public int height;
	public String name;
	
	public static Terrain load(RCObject objTerrain, String type) {
		
		switch (type) {
		case (Floor.FLOOR_TYPE_NAME): return Floor.load(objTerrain);
		case (Wall.WALL_TYPE_NAME): return Wall.load(objTerrain);
		default: return null;
		}
		
	}
	
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
