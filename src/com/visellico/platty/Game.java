package com.visellico.platty;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.farr.Events.Event;
import com.farr.Events.EventDispatcher;
import com.farr.Events.EventListener;
import com.farr.Events.Layer;
import com.farr.Events.types.KeyTypedEvent;
import com.visellico.input.Keyboard;
import com.visellico.platty.level.Level;
import com.visellico.util.Debug;

public class Game extends Canvas implements Runnable, EventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JFrame frame;
	private static final String TITLE = "Platty The Platformer";
	private static final String VERSION = "dev 0.1";
	//standard W/H
	public static int defWidth = 300 * 3;
	public static int defHeight = 168 * 3;
	public static int width = defWidth;
	public static int height = defHeight;
	public static int newWidth = width;
	public static int newHeight = height;
	
//	public static int width = 1777;
//	public static int height = 1000;
	
	public static Game game;
	public Keyboard key;
	
	private Thread thread;
	private boolean running = false;
	
	private List<Layer> layerStack = new ArrayList<>();
	
	public Level level;
	
	
	
	
	public Game() {
		
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		
		frame = new JFrame();
		
//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		frame.setUndecorated(true);
		
		level = new Level("Testing");
		addLayer(level);
		
		key = new Keyboard(this);
		this.addKeyListener(key);
		
	}
	
	public void start() {
		running = true;
		
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
				if (level != null) 
					frame.setTitle(TITLE + " | " + VERSION + " | " + level.name + " | DEBUG: FPS: " + frames + " UPS: " + updates);
				else 
					frame.setTitle(TITLE + " | DEBUG: FPS: " + frames + " UPS: " + updates);
				updates = 0;
				frames = 0;
			}
			
		}
		stop();
	}

	public void update() {
		
		//changing size
//		frame.setSize(1777, 1000);
//		frame.setLocationRelativeTo(null);
		
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
		
		
		for (int i = 0; i < layerStack.size(); i++) {
			layerStack.get(i).render(g);
			
		}
		
		g.drawString("-----DIAGNOSTICS-----", 0, 10);
		g.drawString("OBJ RENDER : " + Debug.objectsRendered, 0, 43);
		
		g.drawString("DEBUG KEYS", 0, 150);
		g.drawString("CTRL Z : Zoom in", 0, 161);
		g.drawString("CTRL X : Zoom out", 0, 172);
		g.drawString("CTRL R : Zoom reset", 0, 183);
		g.drawString("SHFT R : Reload assets", 0, 194);
		
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
		if (e.getKeyChar() == '\u001b') System.exit(0);
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
	
	public static void main(String[] args) {
		
		game = new Game();
		
		game.frame.setResizable(false);
		
		//For full screen, get rid of setTitle and setLocation, and uncomment the stuff in Game constructor
		
		game.frame.setTitle(TITLE + " | " + VERSION);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
		
	}

}
