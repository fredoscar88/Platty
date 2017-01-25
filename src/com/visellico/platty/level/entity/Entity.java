package com.visellico.platty.level.entity;

import com.visellico.platty.level.Addable;

public abstract class Entity implements Addable {

	public double x, y;
	
	public abstract void update();
	
	public abstract void init();
	
}
