package com.visellico.platty.level.terrain;

import com.visellico.graphics.Screen;
import com.visellico.graphics.Sprite;
import com.visellico.platty.level.Level;
import com.visellico.rainecloud.serialization.RCObject;

public class Wall extends Terrain {

	public static final String WALL_TYPE_NAME = "wall";
	
	private Sprite spriteWall;
	private Sprite spriteWallTop;
	private Sprite spriteWallTrimLeft;
	private Sprite spriteWallTrimRight;
	
	public Wall(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public static Wall load(RCObject objWall) {
		
		int x, y, width, height;
		x = objWall.findField("x").getInt();
		y = objWall.findField("y").getInt();
		width = objWall.findField("width").getInt();
		height = objWall.findField("height").getInt();
		
		Wall f = new Wall(x, y, width, height);
		
		return f;
	}
	
	public void render(Screen screen) {
		screen.renderSpriteTiled(x, y, width, height, spriteWall);
		screen.renderSpriteTiled(x, y, 2, height, spriteWallTrimLeft);
		screen.renderSpriteTiled(x + width - 2, y, 2, height, spriteWallTrimRight);
		screen.renderSpriteTiled(x, y, width, 3, spriteWallTop);
	}
	
	public void update() {
		
	}
	
	public void init(Level l) {
		spriteWall = l.levelType.spriteWall;
		spriteWallTop = l.levelType.spriteWallTop;
		spriteWallTrimLeft = l.levelType.spriteWallTrimLeft;
		spriteWallTrimRight = l.levelType.spriteWallTrimRight;
		
	}
	
}
