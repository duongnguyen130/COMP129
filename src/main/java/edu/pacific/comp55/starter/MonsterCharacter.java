package edu.pacific.comp55.starter;

import acm.graphics.GImage;

public class MonsterCharacter implements Character {
	public String monsterName;
	private double dx;
	private double dy;
	public GImage monsterIMG;
	public static final String BUG = "bug.png";

	MonsterCharacter(String name, int health, double startingDx, double startingDy) {
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
		monsterIMG.setLocation(dx, dy);
	}

	public void setDY(double dy) {
		this.dy = dy;
		monsterIMG.setLocation(dx, dy);
	}

	@Override
	public void Character(String name) {
		// TODO Auto-generated method stub
		monsterName = name;
	}

	public void vertMove() {
		monsterIMG.move(0, 1);
		setDY(dy + 1);
	}

	public void move(PlayerCharacter targetPlayer) {
		// TODO Auto-generated method stub
		if (targetPlayer != null) {
			double monsterX = dx;
			double monsterY = dy;
			double playerX = targetPlayer.getDX();
			double playerY = targetPlayer.getDY();

			// Calculate the direction from the monster to the player
			double dxToPlayer = playerX - monsterX;
			double dyToPlayer = playerY - monsterY;

			// Adjust monster position to move it closer to player
			double distanceToPlayer = Math.sqrt(dxToPlayer * dxToPlayer + dyToPlayer * dyToPlayer);
			if (distanceToPlayer > 0) {
				double speed = 1.0; // We can adjust this as needed

				double moveX = (dxToPlayer / distanceToPlayer) * speed;
				double moveY = (dyToPlayer / distanceToPlayer) * speed;

				dx += moveX;
				dy += moveY;
				monsterIMG.setLocation(dx, dy);
			}
		}
	}

	public void decrementHealth(int dmg) {
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
