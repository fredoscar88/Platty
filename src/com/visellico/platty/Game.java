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
import com.farr.Events.EventListener;
import com.farr.Events.Layer;

public class Game extends Canvas implements Runnable, EventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JFrame frame;
	private static final String title = "Platty The Platformer";
	private int width = 300 * 3;
	private int height = 168 * 3;
	
	public static Game game;
	
	private Thread thread;
	private boolean running = false;
	
	private List<Layer> layerStack = new ArrayList<>();
	
	public Game() {
		
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		
		frame = new JFrame();
		
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

	public void run() {
		
		final double updatesPerSec = 60.0;
		final double updatesPerSecRatio = 1_000_000_000/updatesPerSec;	// Every one billion nano seconds has 60 updates per second
		
//		final double framesPerSec = 60.0;	// Frame Rate cap
//		final double framesPerSecRatio = (framesPerSec == 0.0) ? 0 : 1_000_000_000/framesPerSec;

		long lastTime = System.nanoTime();
		long currentTime;
		double updateDelta = 0;
		
		long timer = System.currentTimeMillis();
		int updates = 0; 
		int frames = 0;
		
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
				frame.setTitle(title + " DEBUG: FPS: " + frames + " UPS: " + updates);
				updates = 0;
				frames = 0;
			}
			
		}
		stop();
	}

	public void update() {
		
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
		
		bs.show();
		g.dispose();
		
	}
	
	public void onEvent(Event event) {
		//events go down the layer stack in reverse order
		for (int i = layerStack.size() - 1; i >= 0; i--) {
			layerStack.get(i).onEvent(event);
		}
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
		game.frame.setTitle(title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
		
	}

}
