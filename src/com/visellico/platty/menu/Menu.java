package com.visellico.platty.menu;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.farr.Events.Event;
import com.farr.Events.EventDispatcher;
import com.farr.Events.Layer;
import com.farr.Events.types.KeyTypedEvent;
import com.farr.Events.types.MousePressedEvent;
import com.visellico.graphics.ui.UIPanel;
import com.visellico.platty.Assets;
import com.visellico.util.Debug;

//TODO Subclass this so we can use this as more of a framework class. I'd like to have menus that, for instance, have transparent or no background (i.e render over other things,
//	like a pause menu!) I just want it to be more versatile. Sub-menus that pop out (like lists?) would also be small enough, but they may be panels.
//	Also, I sort of treat panels like menus, that is probably not ideal.
public class Menu implements Layer {

	private List<UIPanel> uiPanels = new ArrayList<UIPanel>();
	private static Map<String, Menu> menuMap = new HashMap<>();
	private static List<Menu> menuStack = new ArrayList<>();
	
	
	public BufferedImage imgBackground;
	public String name;
	
//	public static Menu errorMenu;
	
	public Menu(String name, String bgPath) throws IOException {
		
		String path = Assets.prgmDir + bgPath;
//		System.out.println(path);
		
		imgBackground = ImageIO.read(new File(path));
		
		this.name = name;
		
		menuMap.put(name, this);
	}
	
	public static Menu getCurrentMenu() {
		
		if (menuStack.size() == 0) return null;
		return menuStack.get(menuStack.size() - 1);
		
	}
	
	public void addPanel(UIPanel p) {
		uiPanels.add(p);
		//Not initing this 
	}
	
	private void onOpen() {
		//Do a thing
	}
	
	/*
	 * Places the menu associated with the menuName on top of the Menu Stack (highest visible layer) (Menu's may or may not be able to overlap each other)
	 */
	public static void navigateTo(String menuName) {
		
		
		if (menuMap.containsKey(menuName))
			menuStack.add((Menu) menuMap.get(menuName));
		else {			
			menuStack.add(menuMap.get(Assets.menuNameError));
		}
		menuStack.get(menuStack.size() - 1).onOpen();
	}
	
	/*
	 * Removes the top item from the menuStack, taking us back one level down
	 */
	public static void back() {
		if (menuStack.size() > 0)	//This should not ever be called when size() == 0, as my intent is to only call it from this class.
			menuStack.remove(menuStack.size() - 1);
	}
	
	/*
	 * Resets menuStack to an empty list. For when no menu should be shown.
	 */
	public static void clear() {
		menuStack = new ArrayList<>();
	}
	
	public void render(Graphics g) {
		g.drawImage(imgBackground, 0, 0, null);
		for (int i = 0; i < uiPanels.size(); i++) {
			uiPanels.get(i).render(g);
		}
		
		Debug.addStr("DEBUG KEYS");
		Debug.addStr("ESC    : Go back to previous menu");
	}

	//updates go from top to bottom
	public void update() {
		for (int i = uiPanels.size() - 1; i >= 0; i--) {
			uiPanels.get(i).update();
		}
	}

	public void onEvent(Event event) {
		
		EventDispatcher dispatcher = new EventDispatcher(event);
		dispatcher.dispatch(Event.Type.KEY_TYPED, (Event e) -> onKeyType((KeyTypedEvent) e));
		
		for (int i = uiPanels.size() - 1; i >= 0; i--) {
			uiPanels.get(i).onEvent(event);
		}
		
		dispatcher.dispatch(Event.Type.MOUSE_PRESSED, (Event e) -> onMousePress((MousePressedEvent) e));
	}

	public boolean onKeyType(KeyTypedEvent e) {
		
//		System.out.printf("%04X\n", (short) e.getKeyChar());
		//Changing this to esc
//		if (e.getKeyChar() == 'b' || e.getKeyChar() == 'B') {
//			back();
//			return true;
//		}
		if (e.getKeyChar() == '\u001B') {// || e.getKeyChar() == 'B') {
			back();
			return true;
		}
		
		return false;
	}
	
	public boolean onMousePress(MousePressedEvent e) {
		return true;
	}
	
	public void init(List<Layer> l) {
		
	}
	
}
