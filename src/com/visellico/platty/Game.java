package com.visellico.platty;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import com.farr.Events.Event;
import com.farr.Events.EventDispatcher;
import com.farr.Events.EventListener;
import com.farr.Events.Layer;
import com.farr.Events.types.KeyTypedEvent;
import com.visellico.graphics.ui.UIActionListener;
import com.visellico.graphics.ui.UIButton;
import com.visellico.graphics.ui.UILabel;
import com.visellico.graphics.ui.UIPanel;
import com.visellico.input.Focus;
import com.visellico.input.Keyboard;
import com.visellico.input.Mouse;
import com.visellico.platty.level.Level;
import com.visellico.platty.menu.Menu;
import com.visellico.platty.utility.UICard;
import com.visellico.platty.utility.UICardList;
import com.visellico.util.Debug;
import com.visellico.util.FileUtils;
import com.visellico.util.Vector2i;

public class Game extends Canvas implements Runnable, EventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JFrame frame;
	private static final String TITLE = "Platty The Platformer";
	private static final String VERSION = "dev 0.2";
	//standard W/H
//	public static int defWidth = 300 * 3;
//	public static int defHeight = 168 * 3;
	public static int defWidth = 600 * 3;
	public static int defHeight = 336 * 3;
	
	public static int width = defWidth;
	public static int height = defHeight;
	public static int newWidth = width;
	public static int newHeight = height;
	
//	public static int width = 1777;
//	public static int height = 1000;
	
	public static Game game;
	public Keyboard key;
	public Mouse mouse;
	public Focus focus;
	
	private Thread thread;
	private boolean running = false;
	
	private List<Layer> layerStack = new ArrayList<>();
	
	public Level currentLevel;
	public Menu currentMenu;
	public Menu menuLevelSelect;	//This menu gets an express reference because we'd like to be able to update this, particularly upon navigating to it.
									//Perhaps I should consider adding an action to all menus, onOpen, that lets me do those actions so I dont need a reference here...
	public static Font fontDefault = new Font("Times New Roman", Font.PLAIN, 48);
	public static final int LEVEL_SELECT_CARD_WIDTH = 150;
	
	//-------
	public UIPanel panelMainMenu = new UIPanel(new Vector2i(0,0), new Vector2i(Game.defWidth, Game.defHeight), false);
	public UILabel labelAnim = (UILabel) new UILabel(new Vector2i(430,240), "Tons of fun!", fontDefault).setColor(0x485C5A);
	//-------
	
	//BIG ASS TODO - SUPPORT MULTIPLE RESOLUTIONS, CHOOSING A NATIVE ONE AS DEFAULT PER USER SCREEN WITH A DEFINITE MINIMUM! POSITION UI RELATIVELY!
	public Game() throws IOException {
		
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		
		frame = new JFrame();
		
//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); <FULL SCREEN>
//		frame.setUndecorated(true);
		
		key = new Keyboard(this);
		mouse = new Mouse(this);
		focus = new Focus(this);
		this.addKeyListener(key);
		this.addMouseListener(mouse);
		this.addMouseMotionListener(mouse);
		this.addMouseWheelListener(mouse);
		this.addFocusListener(focus);
		
		
	}
	
	public void start() {
		running = true;
		
		Menu.navigateTo(Assets.menuNameMain);
		currentMenu = Menu.getCurrentMenu();
		addLayer(currentMenu);
		
		thread = new Thread(this, "Game");
		thread.start();	//automatically executes run(), given we implement runnable and have provided a run method.
	}
	
	public void stop() {
		running = false;
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("GAME, main class; main thread has stopped! (debug)");
	}
	
	public void startLevel(String levelName) {
		
//		level = new Level("Testing");
//		level = Level.loadLevelFromFile("res/Levels/Default/" + "New Features Testing" + ".lvl");
//		currentLevel = Level.loadLevelFromFile("res/Levels/Default/" + "New Features Testing" + ".lvl");
		currentLevel = Level.loadLevelFromFile("res/Levels/Default/" + levelName + ".lvl");
		if (currentLevel == null) {
			//TODO Return to menu
			System.err.println("Level not found or otherwise not loaded.");
			System.exit(0);
		}
		
		Menu.clear();
	}
	
	public void exitLevel() {
		
		Menu.navigateTo(Assets.menuNameMain);
		//TODO add progress saving. Or not because this is a platformer. But any extra stuff that should go here.
		currentLevel = null;
		
	}
	
	/**
	 * Sorts out layerStack, keeping it tidy.
	 */
	public void layerSort() {
		
		layerStack.clear();
		
		if (currentLevel != null) {
			addLayer(currentLevel);
		} else if (currentMenu != null) {
			addLayer(currentMenu);
		} else {
			Menu.navigateTo(Assets.menuNameMain);
			currentMenu = Menu.getCurrentMenu();
			addLayer(currentMenu);
		}
		
	}
	
	
	public static void exit() {
		//TODO other things that need must be done on exit, such as saving the game. We might even override the window close event.
		System.exit(0);
	}

	private int updates;
	private int frames;
	
	public void run() {
		
		final double updatesPerSec = 60.0;
		final double updatesPerSecRatio = 1_000_000_000/updatesPerSec;	// Every one billion nano seconds has 60 updates per second
		
//		final double framesPerSec = 60.0;	// Frame Rate cap
//		final double framesPerSecRatio = (framesPerSec == 0.0) ? 0 : 1_000_000_000/framesPerSec;

		long lastTime = System.nanoTime();
		long currentTime;
		double updateDelta = 0;
		
		long timer = System.currentTimeMillis();
		updates = 0; 
		frames = 0;
		
		while (running) {
			currentTime = System.nanoTime();
			updateDelta += (currentTime - lastTime) / updatesPerSecRatio;
			lastTime = currentTime;
			
			while(updateDelta >= 1) {
				update();
				updates++;
				updateDelta--;
			}
			
			//Frames are uncapped at the moment
			render();
			frames++;
			
			//Display the framerate and update rate every second
			while(System.currentTimeMillis() - timer >= 0) {
				timer += 1000;
				if (currentLevel != null) 
					frame.setTitle(TITLE + " | " + VERSION + " | " + currentLevel.name + " | DEBUG: FPS: " + frames + " UPS: " + updates);
				else 
					frame.setTitle(TITLE + " | DEBUG: FPS: " + frames + " UPS: " + updates);
				updates = 0;
				frames = 0;
			}
			
		}
		stop();
	}

	public int increment = 0;
	public int labelAnimDefX;
	public int labelAnimDefY;
	public void update() {
		
		double incModifier = increment / 1;
		labelAnim.position.x = (int) (labelAnimDefX + 80 * Math.sin(.0005 * 180/Math.PI * incModifier));
		labelAnim.position.y = (int) (labelAnimDefY + 100 * Math.sin(.001 * 180/Math.PI * incModifier));
		
		increment++;
		if (increment == Integer.MAX_VALUE) increment = 0;	//This would just rollover into Integer.MIN_VALUE and probably not be that big of a deal.
		
		currentMenu = Menu.getCurrentMenu();

		layerSort();
		
		//frame.getWidth being, essentially, our "newWidth"
		//Basically, lets us resize the frame and region its rendered to. Screen has to take care of itself.
		if (frame.getWidth() != width) {
			width = frame.getWidth();
		}
		if (frame.getHeight() != height) {
			height = frame.getHeight();
		}
		
		//Updates from top layer to bottom. actually doesn't matter, and Im going to make it from bottom up.
		for (int i = layerStack.size() - 1; i >= 0; i--) {
			layerStack.get(i).update();
		}
		
	}
	
	public void render() {
		
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;	//quit out for this iteration of rendering
		}
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(new Color(0xFF00FF));
		g.fillRect(0, 0, width, height);		
		
		Debug.clearStr();
		Debug.addStr("----------DIAGNOSTICS/INFO----------");
		
		for (int i = 0; i < layerStack.size(); i++) {
			layerStack.get(i).render(g);
			
		}
		
//		if (currentMenu != null) {
//			currentMenu.render(g);
//		}
		
//		Menu.getCurrentMenu().render(g);
		
		
		
		Debug.drawStrings(g);
		
//		g.setFont(new Font("Consolas", Font.PLAIN, 12));
//		g.setColor(new Color(0xFFFFFF));
//		
//		g.drawString("-----DIAGNOSTICS-----", 0, 10);
		
//		g.drawString("DEBUG KEYS", 0, 150);
//		g.drawString("CTRL Z : Zoom in", 0, 161);
//		g.drawString("CTRL X : Zoom out", 0, 172);
//		g.drawString("CTRL R : Zoom reset", 0, 183);
//		g.drawString("SHFT R : Reload assets", 0, 194);
		
//		g.drawString("FPS : " + frames, 0, 35);
//		g.drawString("UPS : " + updates, 0, 46);
		
		bs.show();
		g.dispose();
		
	}
	
	public void onEvent(Event event) {
		
		EventDispatcher dispatcher = new EventDispatcher(event);
		dispatcher.dispatch(Event.Type.KEY_TYPED, (Event e) -> onKeyType((KeyTypedEvent) e));
		
		//events go down the layer stack in reverse order
		for (int i = layerStack.size() - 1; i >= 0; i--) {
			layerStack.get(i).onEvent(event);
		}
	}
	
	public void onKeyType(KeyTypedEvent e) {
		//ESC key
//		if (e.getKeyChar() == '\u001b') System.exit(0);
		if (e.getKeyChar() == 'm') exitLevel();
		
	}
	
	public void addLayer(Layer l) {
		layerStack.add(l);
		l.init(layerStack);
		
	}
	public void removeLayer(Layer l) {
		layerStack.remove(l);
	}
	public void removeLayer(int index) {
		if (index < layerStack.size() - 1)
			layerStack.remove(index);
	}
	
	public void init() throws IOException {
		Menu menuMain = new Menu(Assets.menuNameMain, Assets.filePathMenuBGMain);
		Menu menuError = new Menu(Assets.menuNameError, Assets.filePathMenuBGMain);
		Menu menuLevelSelect = new Menu(Assets.menuNameLevelSelect, Assets.filePathMenuBGDefault);
				
		labelAnim.backgrndVisible = false;
		labelAnimDefX = labelAnim.position.x;
		labelAnimDefY = labelAnim.position.y;
		labelAnim.setActionListener(() -> exit());
		
		panelMainMenu.add(labelAnim);
//		panelMainMenu.add(new UIButton(new Vector2i(750,350), new Vector2i(310,40), () -> Menu.navigateTo(Assets.menuNamePlay), "Play"));		//Navigate to level select
//		panelMainMenu.add(new UIButton(new Vector2i(750,350), new Vector2i(310,40), () -> startLevel(), "Play"));		//Navigate to level select
		panelMainMenu.add(new UIButton(new Vector2i(750,350), new Vector2i(310,40), () -> Menu.navigateTo(Assets.menuNameLevelSelect), "Play"));		//Navigate to level select
		panelMainMenu.add(new UIButton(new Vector2i(750,392), new Vector2i(310,40), () -> Menu.navigateTo(Assets.menuNameOption), "Options"));
		panelMainMenu.add(new UIButton(new Vector2i(750,434), new Vector2i(310,40), () -> Menu.navigateTo(Assets.menuNameHelp), "Help"));
		panelMainMenu.add(new UIButton(new Vector2i(750,476), new Vector2i(310,40), () -> Menu.navigateTo(Assets.menuNameAbout), "About"));
		panelMainMenu.add(new UIButton(new Vector2i(750,518), new Vector2i(310,40), () -> exit(), "Exit"));
		
		menuMain.addPanel(panelMainMenu);
		
		UIPanel errorPanel = new UIPanel(new Vector2i(0,0), new Vector2i(Game.defWidth, Game.defHeight), false);
		errorPanel.add(new UILabel(new Vector2i(550, 350), "Something went wrong, hit Esc to go back!", fontDefault, false).setColor(0));
		errorPanel.add(new UILabel(new Vector2i(550, 400), "In fact, I probably just haven't implemented this menu yet!", fontDefault, false).setColor(0));
		menuError.addPanel(errorPanel);
		
		UIPanel panelLevels = new UIPanel(new Vector2i(0,0), new Vector2i(Game.defWidth, Game.defHeight), false);
		panelLevels.add(new UILabel(new Vector2i((int) (Game.defWidth * .05), (int) (Game.defHeight * .1) - 24), "Select a level below", fontDefault.deriveFont(24f), false).setColor(0));
		
		UIPanel panelLevelSelect = new UIPanel(new Vector2i((int) (Game.defWidth * .05), (int) (Game.defHeight * .1)), new Vector2i((int) (Game.defWidth * .9), (int) (Game.defHeight * .75)), false);
		panelLevels.add(panelLevelSelect);

		UICardList clistLevels = new UICardList(panelLevelSelect.size.x, panelLevelSelect.size.y, 10, LEVEL_SELECT_CARD_WIDTH, LEVEL_SELECT_CARD_WIDTH);
		panelLevelSelect.add(clistLevels);
		
		
		List<String> lvlsDefault = loadLevelNames(Assets.prgmDir + Assets.dirDefaultLevels + "/");
		List<String> lvlsCustom = loadLevelNames(Assets.prgmDir + Assets.dirCustomLevels + "/");
		
		for (int i = 0; i < lvlsDefault.size(); i++) {
			
			final int temp = i;
			UICard c = new UICard(new Vector2i(), new Vector2i(LEVEL_SELECT_CARD_WIDTH, LEVEL_SELECT_CARD_WIDTH));
			clistLevels.addCard(c);
			c.add(new UILabel(new Vector2i(2,2), lvlsDefault.get(temp), fontDefault.deriveFont(16f), false).setColor(Color.black));
			c.setActionListener((UIActionListener) () -> startLevel(lvlsDefault.get(temp)));
		}
		for (int i = 0; i < lvlsCustom.size(); i++) {
			
			final int temp = i;
			UICard c = new UICard(new Vector2i(), new Vector2i(LEVEL_SELECT_CARD_WIDTH, LEVEL_SELECT_CARD_WIDTH));
			clistLevels.addCard(c);
			c.add(new UILabel(new Vector2i(2,2), lvlsCustom.get(temp), fontDefault.deriveFont(16f), false).setColor(Color.black));
			c.setActionListener((UIActionListener) () -> startLevel(lvlsCustom.get(temp)));
		}
		
		menuLevelSelect.addPanel(panelLevels);
	}
	
	public List<UICard> loadLevelsAsCards() {
		
		return null;
		
	}
	
	/**
	 * Loads all of the file names, and saves a path to them, inside a hashmap.
	 * @param directory Directory to load file names from. File names are the Key, file paths are the value.
	 * @return
	 */
	public List<String> loadLevelNames(String directory) {
		
		File levelDir = new File(directory);
		List<String> result = new ArrayList<>();
		
		for (String str : levelDir.list()) {
			result.add(FileUtils.stripExtension(str));
		}
		
		
		return result;
	}

	public static void main(String[] args) throws IOException {
		
		game = new Game();
		
		game.frame.setResizable(false);
		
		//For full screen, get rid of setTitle and setLocation, and uncomment the stuff in Game constructor
		
		game.frame.setTitle(TITLE + " | " + VERSION);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.init();
		game.start();
		
	}

}
