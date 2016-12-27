package com.visellico.platty.menu;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.farr.Events.Event;
import com.farr.Events.Layer;
import com.visellico.graphics.ui.UIPanel;

public class Menu implements Layer {

	private List<UIPanel> uiPanels = new ArrayList<UIPanel>();
	
	
	
	public void render(Graphics g) {
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
