package com.visellico.platty.level;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.farr.Events.Event;
import com.farr.Events.EventDispatcher;
import com.farr.Events.Layer;
import com.farr.Events.types.KeyPressedEvent;
import com.farr.Events.types.KeyReleasedEvent;
import com.farr.Events.types.KeyTypedEvent;
import com.visellico.graphics.Screen;
import com.visellico.platty.Game;
import com.visellico.platty.level.entity.collectibles.Coin;
import com.visellico.platty.level.entity.collectibles.CollectibleItem;
import com.visellico.platty.level.terrain.Floor;
import com.visellico.platty.level.terrain.Platform;
import com.visellico.platty.level.terrain.Terrain;
import com.visellico.platty.level.terrain.Wall;
import com.visellico.rainecloud.serialization.RCDatabase;
import com.visellico.rainecloud.serialization.RCObject;
import com.visellico.util.Debug;
import com.visellico.util.FileUtils;

public class Level implements Layer {

	//TODO in WAY FUTURE 
	//	When loading a level, CACHE AN IMAGE OF THE STATIC OBJECTS OF THE LEVEL (LOOKING AT YOU, ANY SUBCLASS OF TERRAIN AS IT STANDS ATM)
	//	Moving "terrain" can get its own category, and stuff that isn't terrain isn't really 'static' in the same way and can be rendered as is now (ala coins)
	//	Essentially, for any static pieces (basically background and terrain) we just have to cache the pixel array, perhaps as a seperate screen, just once. Then,
	//		we'll draw that screen first, then the dynamic objects screen on top. Alternatively, layer the pixels of the static screen into the dynamic screen,
	//		then make all of the render calls for the dynamic objects to cover over the pixels. This would make for a more efficient experience das ist gut yes.
	
	//List of Entities
	//List of Items (which might be entities..)
	//List of Objects
	
	public String name;
	
	public Screen screen;
	//Default scale
	private static final int SCREEN_SCALE = 4;
	private int newScale = SCREEN_SCALE;
	public static final String DEFAULT_LEVEL_TYPE = "Fields";	
	
	public int width;
	public int height;
	
	private int xPos = 0;
	private int yPos = 0;
	
	public LevelType levelType;	//Refers to the assets this level uses
	public boolean isDefault;
	public boolean usesDefaultAssets;
	
	private List<Addable> addables = new ArrayList<>();
	private List<Terrain> terrainObjs = new ArrayList<>();
	private List<CollectibleItem> collectibles = new ArrayList<>();
	private Background backgroundMain;
//	private BackgroundObject backgroundObject;
	
	private Level(String name, String type, int width, int height, boolean isDefault, boolean usesDefaultAssets) {
		
		this.name = name;
		this.isDefault = isDefault;
		this.usesDefaultAssets = usesDefaultAssets;
		
		try {
			levelType = new LevelType(type, usesDefaultAssets);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		screen = new Screen(Game.width/SCREEN_SCALE, Game.height/SCREEN_SCALE, SCREEN_SCALE);
		
		this.width = width;
		this.height = height;
		
		add(new Background());
		
	}
	
	/**
	 * Loads a default testing level- not meant for production, but only debug and testing
	 */
	public Level(String name) {
		
		this.name = name;
		
		try {
			levelType = new LevelType(DEFAULT_LEVEL_TYPE, true);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
//		screen = new Screen(300*3/SCREEN_SCALE, 168*3/SCREEN_SCALE, SCREEN_SCALE);
		screen = new Screen(Game.width/SCREEN_SCALE, Game.height/SCREEN_SCALE, SCREEN_SCALE);
		
		width = 800;
//		height = 500;
		height = 200;
		
		Wall wall0 = new Wall(100,80,30,40);
		Wall wall1 = new Wall(166,80,16,40);
		
		//Construct a default level
		Floor defaultFloor0 = new Floor(1,44,62, "Floor 0");
		Floor defaultFloor1 = new Floor(53,60,96, "Floor 1");
		Floor defaultFloor2 = new Floor(70,50,96, "Floor 2");
		Floor defaultFloor3 = new Floor(90,40,96, "Floor 3");
		
		Platform pf0 = new Platform(186,40,100);
		Platform pf1 = new Platform(130,80,36);
		
		Background bg = new Background();
		add(bg);
		
		//Background scrolling slower is probably going to have to wait a bit
//		BackgroundObject bgo = new BackgroundObject(100,25, 2);
//		add(bgo);
		
		Coin c0 = new Coin(10, 44);
		
		
		//Note well- floors should be added in decreasing order of height, starting with the tallest.
		//	Way to sort objects by a variable?
		add(defaultFloor1);
		add(defaultFloor2);
		add(defaultFloor3);
		add(defaultFloor0);
		
		add(wall0);
		add(wall1);
		add(pf0);
		add(pf1);
		
		add(c0);
		
	}
	
//	public void sort() {
//		//Sort terrain based on height, in turn based on "layers", then just re-add entities or w/e. I don't know if entities need to be sorted. I imagine players will be
//		//	on top of the rendering pile (last to be rendered in) and generally won't need to collide with other entities.
//	}
	
	/**
	 * Main way to load levels
	 * Loads a level from a platty .lvl file in the resources. Maybe I should add some sort of header to these files...
	 * @param path
	 * @return
	 */
	public static Level loadLevelFromFile(String path) {
		
		RCDatabase db = RCDatabase.deserializeFromFile(path);
		String name = FileUtils.stripPath(path);
		
		if (db == null) return null;
		
		RCObject meta = db.objects.get(0);
		Level l;
		
		try {
			l = new Level(
				name,	//db.getName(), 
				meta.findString("type").getString(), 
				meta.findField("width").getInt(), 
				meta.findField("height").getInt(), 
				meta.findField("isDefault").getBoolean(),
				meta.findField("usesDefaultAssets").getBoolean());
		} catch (Exception e) {	//I believe these will throw NullPointerException
			e.printStackTrace();
			return null;
			
		}
		
		//THIS STARTS AT 1
		RCObject obj;
		for (int i = 1; i < db.objects.size(); i++) {
			Addable a = null;
			obj = db.objects.get(i);
			
			switch (obj.getName()) {
			case (Terrain.TERRAIN_NAME): 
				a = Terrain.load(obj, obj.findString("type").getString()); break;
				
			}
			l.add(a);
		}
		
//		l.sort();
		
		return l;
	}
	
	public void add(Addable a) {
		
		addables.add(a);
		
		//Adds object to Entity list, Object list, Collectible list, etc. (or w/e these lists end up being)
		if (a instanceof Terrain) terrainObjs.add((Terrain) a);
		if (a instanceof CollectibleItem) collectibles.add((CollectibleItem) a);
		//Other addable things that we need categories for
		
		//Stuff that isnt kept in a list is still added like this
		if (a instanceof Background) backgroundMain = (Background) a;
//		if (a instanceof BackgroundObject) backgroundObject = (BackgroundObject) a;		
		
		//TODO OTHER ADDING THINGS
		a.init(this);
	}
	
	public void reloadAssets() {
		try {
			levelType.loadAssets(levelType.levelTypeDirectoryPath);
		} catch (IOException e) {
			try {
				levelType = new LevelType(DEFAULT_LEVEL_TYPE, true);
			} catch (IOException e1) {
				System.err.println("Listen you really dun' messed up");
				e1.printStackTrace();
			}
		}
		for (Addable a : addables)
			a.init(this);
	}
	
	public void render(Graphics g) {
		
		screen.setOffset(xPos - screen.width/2, getScreenY(yPos) - screen.height/2);
//		screen.setOffset(0, 0);
//		screen.setOffset(-screen.width/2, -screen.height/2);
		screen.clear(0x0);
		
		//Render level borders- for debug purposes
		//	If we want to have actual lines and the like, we ought to wrap them in a level object that gives some more minute control.
		screen.renderFilledRec(0, 0, width, height,0x202020);
		screen.renderLine(0, 0, width, 0, 0xFF0000);
		screen.renderLine(0, 0, 0, height, 0xFF0000);
		screen.renderLine(width, 0, width, height, 0xFF0000);
		screen.renderLine(0, height, width, height, 0xFF0000);
		screen.renderPoint(width, height, 0xFF0000);
		
		if (backgroundMain != null) backgroundMain.render(screen);
//		if (backgroundObject != null) backgroundObject.render(screen);
		for (Terrain t : terrainObjs) {
			t.render(screen);
		}
		for (CollectibleItem ci : collectibles) {
			ci.render(screen);
		}
		
		//"Player"
//		screen.renderLine(0, 0, xPos, yPos, 0x101010);
		screen.renderInvertedPoint(xPos, yPos);
		
		//Debug and testing
//		screen.renderLine(10, 10, 100, 130, 0x087C1A);
		
//		screen.renderLine(30, 10, 120, 100, 0x087C1A);
				
		screen.pack();
		
		g.drawImage(screen.image, 0, 0, screen.width * screen.scale, screen.height * screen.scale, null);
		
		Debug.addStr("LEVEL  POS : " + xPos + " " + yPos);
		Debug.addStr("SCREEN POS : " + xPos + " " + getScreenY(yPos));
		Debug.addStr("ZOOM LVL   : " + newScale + ((newScale == SCREEN_SCALE) ? " (Default)" : ""));
		Debug.addStr("PX IN FRAME: " + screen.height * screen.width);
		Debug.addStr("SCREEN W   : " + screen.width);
		Debug.addStr("SCREEN H   : " + screen.height);
		
//		g.setColor(Color.white);
//		g.setFont(new Font("Consolas", 0, 12));	
		//TBH REALLY NEED A DEBUG STRING DRAWING CLASS
//		g.drawImage(screen.image, 0, 0, screen.width * screen.scale, screen.height * screen.scale, null);
//		
//		g.drawString("LEVEL  POS : " + xPos + " " + yPos, 0, 21);
//		g.drawString("SCREEN POS : " + xPos + " " + getScreenY(yPos), 0, 32);
//		g.drawString("ZOOM LVL   : " + newScale + ((newScale == SCREEN_SCALE) ? " (Default)" : ""), 0, 54);
//		g.drawString("PX IN FRAME: " + screen.height * screen.width, 0, 65);
//		g.drawString("SCREEN W   : " + screen.width, 0, 76);
//		g.drawString("SCREEN H   : " + screen.height, 0, 87);
		
	}
	
	public static int getScreenY(int levelY) {
		
		int screenY = -levelY;
		return screenY;
		
	}
	
	int xMoving = 0;
	int yMoving = 0;
	
	public void update() {
		
		//TODO create a static background screen, for all of the static, nonmoving object like floors and walls so they don't have to be constantly rendered over one another
		//	Which would be rendered first as a static image, then the other screen rendered on top (the other screen has to clear to a pink background and skip rendering them)
		
		//If new scale has been changed, creates new screen
		if (screen.scale != newScale) 
			screen = new Screen(Game.width/newScale, Game.height/newScale, newScale);
		
		if (screen.width != Game.width || screen.height != Game.height) {
			
			screen = new Screen(Game.width/newScale, Game.height/newScale, newScale);
		}
		
		for (Terrain t : terrainObjs) {
			t.update();
		}
		
		if (backgroundMain != null) {
			backgroundMain.update();
		}
		
		xPos += xMoving;
		yPos += yMoving;
	}
	
	public void onEvent(Event event) {
		EventDispatcher dispatcher = new EventDispatcher(event);
		
		dispatcher.dispatch(Event.Type.KEY_TYPED, (Event e) -> onKeyType((KeyTypedEvent) e));
		dispatcher.dispatch(Event.Type.KEY_PRESSED, (Event e) -> onKeyPress((KeyPressedEvent) e));
		dispatcher.dispatch(Event.Type.KEY_RELEASED, (Event e) -> onKeyRelease((KeyReleasedEvent) e));
		
	}
	
	public boolean onKeyPress(KeyPressedEvent e) {
		
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_S) yMoving = -1;
		if (keyCode == KeyEvent.VK_W) yMoving = +1;
		if (keyCode == KeyEvent.VK_D) xMoving = +1;
		if (keyCode == KeyEvent.VK_A) xMoving = -1;
		
		return true;
		
	}
	
	public boolean onKeyRelease(KeyReleasedEvent e) {
		
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_S) yMoving = 0;
		if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_A) xMoving = 0;
		
		return true;
	}
	
	public boolean onKeyType(KeyTypedEvent e) {
		
		final char charCtrlR = '\u0012';	//Could case to short (implicitly!) if we wanted this numerically but that is unnecessary
		final char charCtrlZ = '\u001A';
		final char charCtrlX = '\u0018';
		final char charShftR = '\u0052';
		
//		System.out.printf("%x\n", (short) e.getKeyChar());
		
//		char charUnicode = e.getKeyChar();
		
		switch (e.getKeyChar()) {
		case (charCtrlR): 
			newScale = SCREEN_SCALE;
			return true;
		case (charCtrlZ): 
			if (screen.scale < 100) newScale = screen.scale + 1;
			return true;
		case (charCtrlX): 
			if (screen.scale > 1) newScale = screen.scale - 1;
			return true;
		case (charShftR): 
			reloadAssets();
			return true;
		default: return false;
		}
		
	}
	
	public void init(List<Layer> l) {
	}

}
