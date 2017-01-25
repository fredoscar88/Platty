package com.visellico.platty.menu;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.farr.Events.Event;
import com.farr.Events.Layer;
import com.visellico.graphics.ui.UIPanel;
import com.visellico.platty.Assets;

public class Menu implements Layer {

	private List<UIPanel> uiPanels = new ArrayList<UIPanel>();
	private static List<Menu> menuStack = new ArrayList<>();
	
	public BufferedImage imgBackground;
	
	public Menu(String bgPath) throws IOException {
		
		String path = Assets.prgmDir + "/res/Menu/Main Menu bg.png";
		System.out.println(path);
		
		imgBackground = ImageIO.read(new File(path));
		
		menuStack.add(this);
	}
	
	public static Menu getCurrentMenu() {
		
		return menuStack.get(menuStack.size() - 1);
		
	}
	
	public void addPanel(UIPanel p) {
		uiPanels.add(p);
		//Not initing this 
	}
	
	public void render(Graphics g) {
		g.drawImage(imgBackground, 0, 0, null);
		for (int i = 0; i < uiPanels.size(); i++) {
			uiPanels.get(i).render(g);
		}
	}

	//updates go from top to bottom
	public void update() {
		for (int i = uiPanels.size() - 1; i >= 0; i--) {
			uiPanels.get(i).update();
		}
	}

	public void onEvent(Event e) {
		for (int i = uiPanels.size() - 1; i >= 0; i--) {
			uiPanels.get(i).onEvent(e);
		}
	}

	public void init(List<Layer> l) {
		
	}
	
}
