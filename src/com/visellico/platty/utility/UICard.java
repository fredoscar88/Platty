package com.visellico.platty.utility;

import java.awt.Color;
import java.util.Random;

import com.farr.Events.Event;
import com.visellico.graphics.ui.UIActionListener;
import com.visellico.graphics.ui.UIPanel;
import com.visellico.util.Vector2i;

public class UICard extends UIPanel {
	
	private UIActionListener actionListener;
	
	public UICard(Vector2i position, Vector2i size) {
		this(position, size, new Color(0x22AF22, false));
	
	}
	
	public UICard(Vector2i position, Vector2i size, Color color) {
		super(position, size, true);
		Random random = new Random();
		setColor(color);	
		
	}

	public void setActionListener(UIActionListener actionListener) {
		this.actionListener = actionListener;
	}

	public void update() {
//		System.out.println(components.size());
	}
	
//	public void render(Graphics g) {
////		cardDetails.render(g);
//		
////		g.setColor(color);
////		g.fillRect(getAbsolutePosition().x, getAbsolutePosition().y, size.x, size.y);
//		super.render(g);
//		
//		
////		g.fillRect(getAbsolutePosition().x + size.x / 4, getAbsolutePosition().y + size.y / 4, size.x/2, size.y/2);
//		
//	}
	
	
	public boolean pressedHere = false;
	public boolean entered = false;
	
	public void onMousePress() {
		actionListener.perform();
	}
	
	public void onEnter() {
		
		entered = true;
//		cardDetails.setColor(0xAFAFAF, false);
		
	}
	
	public void onExit() {
		entered = false;
//		cardDetails.setColor(0x7F7F7F, false);
		
	}
	
	public void onEvent(Event event) {
		//Events, like onEnter, onExit, whatever are going to be called from mistah UICardList so we're just overriding it here :)
	}
	
	
}
