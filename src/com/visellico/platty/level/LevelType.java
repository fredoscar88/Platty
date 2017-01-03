package com.visellico.platty.level;

import java.io.File;
import java.io.IOException;

import com.visellico.graphics.Sprite;
import com.visellico.platty.Assets;

public class LevelType {

	public static final String PARENT_PATH_DEFAULT = Assets.prgmDir + Assets.dirDefaultLevelTypes;
	public static final String PARENT_PATH_CUSTOM = Assets.prgmDir + Assets.dirCustomLevelTypes;
	
	public Sprite spriteFloor;
	public Sprite spriteFloorTop;
	public Sprite spriteFloorTrimLeft;
	public Sprite spriteFloorTrimRight;
	      
	public Sprite spriteWall;
	public Sprite spriteWallTop;
	public Sprite spriteWallTrimLeft;
	public Sprite spriteWallTrimRight;
	      
	public Sprite spritePlatform;
	      
	public Sprite spriteBackground;
	public Sprite spriteBackgroundObject;
	      
	public Sprite spriteCoin;
	      
	//Path to this Type's assets
	public String levelTypeDirectoryPath;
	
	public LevelType(String name, boolean isDefault) throws IOException {
		
		if (isDefault) {
			levelTypeDirectoryPath = PARENT_PATH_DEFAULT + "/" + name;
		} else {
			levelTypeDirectoryPath = PARENT_PATH_CUSTOM + "/" + name;
		}
		
//		File testFile = new File(System.getProperty("user.dir") + File.separator + "res" + File.separator + levelTypeDirectoryPath);
		System.out.println(levelTypeDirectoryPath);
		File dir = new File(levelTypeDirectoryPath);
//		System.out.println(testFile.getAbsolutePath());
		if (!dir.exists()) {
			throw new IOException("Selected theme was not found");
		}
		
		loadAssets(levelTypeDirectoryPath);
		
	}

	public void loadAssets(String levelTypePath) throws IOException {
//		try {	
			spriteFloor = new Sprite(levelTypePath + "/floor.png");
			spriteFloorTop = new Sprite(levelTypePath + "/floortop.png");
			spriteFloorTrimLeft = new Sprite(levelTypePath + "/floortrimleft.png");
			spriteFloorTrimRight = new Sprite(levelTypePath + "/floortrimright.png");
			
			spriteWall = new Sprite(levelTypePath + "/Wall/wall.png");
			spriteWallTop = new Sprite(levelTypePath + "/Wall/walltop.png");
			spriteWallTrimLeft = new Sprite(levelTypePath + "/Wall/walltrimleft.png");
			spriteWallTrimRight = new Sprite(levelTypePath + "/Wall/walltrimright.png");
			
			spritePlatform = new Sprite(levelTypePath + "/platform.png");
//			spritePlatform = new Sprite(levelTypeDirectoryPath + File.separator + "platform.png");
			
			spriteBackground = new Sprite(levelTypePath + "/background.png");
//			spriteBackgroundObject = new Sprite(levelTypePath + "/backgroundObject.png");
			
			spriteCoin = new Sprite(levelTypePath + "/coin.png");
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.out.println("you crazy?");
//			loadAssets(PARENT_PATH_DEFAULT + "/" + Level.DEFAULT_LEVEL_TYPE);
//			
//		}
		
	}
	
}
