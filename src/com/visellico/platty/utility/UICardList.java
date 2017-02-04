package com.visellico.platty.utility;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.farr.Events.types.MousePressedEvent;
import com.farr.Events.types.MouseReleasedEvent;
import com.farr.Events.types.MouseScrollEvent;
import com.visellico.graphics.ui.UIComponent;
import com.visellico.graphics.ui.UIPanel;
import com.visellico.util.CoordinatePair;
import com.visellico.util.MathUtils;
import com.visellico.util.Vector2i;

public class UICardList extends UIComponent {

//	private UICard[] cards;
	private List<CoordinatePair<UICard>> cards;	//May be over kill- Im not sure how I want this to work yet.
	private int scrollY;
	private int cardWidth, cardHeight;
//	private int width;
//	private int height;
	private int buffer;
	private int cardsPerRow;
	
	private Vector2i spaceTotal, spaceVisible;
	
	private Rectangle rectClickableArea;
	
	public UICardList(int width, int height, int buffer, int cardWidth, int cardHeight) {
		this(new Vector2i(), new Vector2i(width, height));
		setColor(0xAF202020, true);
		
//		this.width = width;
		this.buffer = buffer;
		this.cardWidth = MathUtils.clamp(cardWidth, 20, width - 2 * buffer);
		this.cardHeight = MathUtils.clamp(cardHeight, 20, Integer.MAX_VALUE);
		
		cardsPerRow = (width / (cardWidth + buffer));
		
		spaceVisible = size;
		spaceTotal = new Vector2i(spaceVisible.x, 0);
		
		cards = new ArrayList<>();
	}
	
	public UICardList(Vector2i position, Vector2i size) {
		super(position, size);
		
	}
	
	public void addCard(UICard c) {
		cards.add(new CoordinatePair<>(c, new Vector2i()));
		calculateCoords();
		//Sets the position to the most recently added item- i.e, itself.
		
		c.setPosition(new Vector2i(
				getAbsolutePosition().x + cards.get(cards.size() - 1).coords.x, 
				getAbsolutePosition().y + cards.get(cards.size() - 1).coords.y - scrollY));
//		c.position = new Vector2i(
//				getAbsolutePosition().x + cards.get(cards.size() - 1).coords.x, 
//				getAbsolutePosition().y + cards.get(cards.size() - 1).coords.y - scrollY);
		
	}
	
	public void calculateCoords() {
		
		CoordinatePair<UICard> card;
		int row = 0;
		int column = 0;

		for (int i = 0; i < cards.size(); i++) {
			card = cards.get(i);
			
			card.coords.x = buffer + column * (buffer + cardWidth);
			card.coords.y = buffer + row 	* (buffer + cardHeight);
			
			column++;
			
			if (column >= cardsPerRow) {
				row++;
				column = 0;
			}
			
		}
		
		//Last card
		card = cards.get(cards.size() - 1);
		spaceTotal.y = card.coords.y + card.obj.size.y + 2 * buffer;
		
	}
	
	public void updateAllPos() {
		CoordinatePair<UICard> card;
		
		for (int i = 0;  i < cards.size(); i++) {
			card = cards.get(i);
			card.obj.setPosition(new Vector2i (
					getAbsolutePosition().x + card.coords.x,
					getAbsolutePosition().y + card.coords.y - scrollY));
//			card.obj.position = new Vector2i (
//					getAbsolutePosition().x + card.coords.x,
//					getAbsolutePosition().y + card.coords.y - scrollY);
		}
	}
	
	public boolean isCardVisible(CoordinatePair<UICard> c) {
		return true;	//Temp
//		return (c.coords.y + c.obj.size.y) > scrollY && c.coords.y - scrollY < size.y;
	}
	
	public void init(UIPanel p) {
		super.init(p);
		
		Vector2i absPosition = getAbsolutePosition();
		rectClickableArea = new Rectangle(absPosition.x, absPosition.y, panel.size.x, panel.size.y);
//		Clickable area is the same as the panel
		
	}
	
	public void update() {
		//Forces scrollY to not try and display stuff outside of the total space
		scrollY = MathUtils.clamp(scrollY, 0, spaceTotal.y - spaceVisible.y);
		if (scrollY < 0) scrollY = 0;
		updateAllPos();
		
		for (CoordinatePair<UICard> card : cards) {
			if (!isCardVisible(card)) continue;
			card.obj.update();
		}
	}
	
	public void render(Graphics g) {

		g.setColor(color);
		g.fillRect(rectClickableArea.x, rectClickableArea.y, rectClickableArea.width, rectClickableArea.height);
		g.setClip(rectClickableArea);
//		g.setClip(new Rectangle(rectClickableArea.x, rectClickableArea.y - 10, rectClickableArea.width, rectClickableArea.height + 10));
		
		for (CoordinatePair<UICard> card : cards) {
			if (!isCardVisible(card)) continue;
			card.obj.render(g);
		}
		
		g.setClip(null);
		
	}
	
	public boolean onMouseScroll(MouseScrollEvent e) {
		
		if (rectClickableArea.contains(e.getX(), e.getY())) {
			scrollY += e.getRotation() * 10;
			
			return true;
		}
		return false;
	}

	public boolean onMousePress(MousePressedEvent e) {
		
		Point p = new Point(e.getX(), e.getY());
		
		if (rectClickableArea.contains(p)) {
			
			int xContext = e.getX() - panel.position.x;
			int yContext = e.getY() - panel.position.y + scrollY;
			
			for (CoordinatePair<UICard> cp : cards) {
				
				if (xContext >= cp.coords.x && xContext < (cp.coords.x + cp.obj.size.x)) {
					if (yContext >= cp.coords.y && yContext < (cp.coords.y + cp.obj.size.y)) {
						cp.obj.onMousePress();
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean onMouseRelease(MouseReleasedEvent e) {
		return false;
	}
	
}
