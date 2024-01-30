package edu.pacific.comp55.starter;

import acm.graphics.GImage;

public class FriendlyMonster implements Character {
	public String monsterName;
	private double dx;
	private double dy;
	public GImage monsterIMG;
	public static final String BUG = "Friendly_Bug.png";
	
	FriendlyMonster(String name, int health, double startingDx, double startingDy) {
		monsterName = name;
		dx = startingDx;
		dy = startingDy;
		monsterIMG = new GImage(BUG);
	}
	
	public void setTargetPlayer(PlayerCharacter player) {
	}
	
	public double getDX() {
		return dx;
	}
	
	public double getDY() {
		return dy;
	}
	
	public void setDX(double dx) {
		this.dx = dx;
	}
	public void setDY(double dy) {
		this.dy = dy;
	}
	@Override
	public void Character(String name) {
		// TODO Auto-generated method stub
		monsterName = name;
	}

	@Override
	public void move(Direction d, double rows, double cols) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}
	
}
