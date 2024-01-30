package edu.pacific.comp55.starter;

import acm.graphics.*;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.Timer;

public class BottomRoom extends GraphicsPane {
	private MainApplication program;
	public static final int PROGRAM_WIDTH = 800;
	public static final int PROGRAM_HEIGHT = 600;
	public static final String TOP_WALL = "TopWall.gif";
	public static final String LEFT_WALL = "LeftWall.gif";
	public static final String BOTTOM_WALL = "BottomWall.gif";
	public static final String RIGHT_WALL = "RightWall.gif";
	public static final String TOP_LEFT = "TopLeftCorner.png";
	public static final String TOP_RIGHT = "TopRightCorner.png";
	public static final String BOTTOM_LEFT = "BottomLeftCorner.png";
	public static final String BOTTOM_RIGHT = "BottomRightCorner.png";
	public static final String FLOORING = "Flooring.png";
	public static final String TOP_OPEN_DOOR = "TopOpenDoor.png";
	public static final String RIGHT_OPEN_DOOR = "RightOpenDoor.png";
	public static final String BOTTOM_OPEN_DOOR = "BottomOpenDoor.png";
	public static final String LEFT_OPEN_DOOR = "LeftOpenDoor.png";
	public static final String TOP_CLOSED_DOOR = "TopClosedDoor.gif";
	public static final String RIGHT_CLOSED_DOOR = "RightClosedDoor.gif";
	public static final String BOTTOM_CLOSED_DOOR = "BottomClosedDoor.gif";
	public static final String LEFT_CLOSED_DOOR = "LeftClosedDoor.gif";

	public static final String RIGHTROCK = "RightRock.png";
	public static final String LEFTROCK = "LeftRock.png";
	public static final String TOPROCK = "TopRock.png";
	public static final String BOTTOMROCK = "DownRock.png";

	public boolean shotOnce = false;
	private int enemyShotDelay;
	GImage splat = new GImage("splat.png");
	GImage splat2 = new GImage("splat.png");
	GImage splat3 = new GImage("splat.png");

	private Overlay overlay;
	private Board bottomRoom = new Board(11, 11);
	private PlayerCharacter mainCharacter = new PlayerCharacter(5, 1, 5 * cellWidth(), cellHeight(), "Sebastian");;
	private ArcadeMachine arcadeMachine = new ArcadeMachine((bottomRoom.getNumRows() - 2) / 2,
			(bottomRoom.getNumCols() - 2), "flipped");
	int keyCode;
	private BaseRoom base;
	public String prevDirection;
	private Timer gameTimer;
	private ArrayList<GImage> balls = new ArrayList<GImage>();
	private ArrayList<GOval> enemyballs = new ArrayList<GOval>();

	private MonsterCharacter monsterCharacter = new MonsterCharacter("Bug", 100, 500, 400);
	private MonsterCharacter monsterCharacter2 = new MonsterCharacter("Bug", 100, 200, 200);
	private MonsterCharacter monsterCharacter3 = new MonsterCharacter("Bug", 100, 400, 400);

	private int dmgCooldown = 0;
	private final Set<Integer> pressedKeys = new HashSet<>();
	Point offset = new Point();

	public Direction directionResolver(Point dirOffset) {
		System.out.println(dirOffset);
		if (dirOffset.x == 0 && dirOffset.y == -1) {
			return Direction.UP;
		} else if (dirOffset.x == 0 && dirOffset.y == 1) {
			return Direction.DOWN;
		} else if (dirOffset.x == 1 && dirOffset.y == 0) {
			return Direction.RIGHT;
		} else if (dirOffset.x == -1 && dirOffset.y == 0) {
			return Direction.LEFT;
		} else if (dirOffset.x == 1 && dirOffset.y == -1) {
			return Direction.UP_RIGHT;
		} else if (dirOffset.x == -1 && dirOffset.y == -1) {
			return Direction.UP_LEFT;
		} else if (dirOffset.x == 1 && dirOffset.y == 1) {
			return Direction.DOWN_RIGHT;
		} else if (dirOffset.x == -1 && dirOffset.y == 1) {
			return Direction.DOWN_LEFT;
		} else {
			return Direction.STILL;
		}
	}

	public BottomRoom(MainApplication app) {
		super();
		program = app;
		overlay = program.getOverlay();
		base = program.getBase();
		int delay = 16;

		gameTimer = new Timer(delay, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// This code is executed on each timer tick

				enemyShotDelay -= 1;

				if (enemyballs.isEmpty() && shotOnce == false) {
					shotOnce = true;
					if (enemyShotDelay <= 0) {
						enemyShoot(monsterCharacter2);
						enemyShotDelay = 50;
					}

				}
				updateEnemyBalls();
				moveAllBallsOnce(prevDirection);
				checkBorder();
				if (monsterCharacter != null) {
					monsterCharacter.move(mainCharacter);
				}
				for (GImage ball : balls) {
					if (monsterCharacter != null) {
						if (projectileMonsterCollision(ball, monsterCharacter.monsterIMG)) {
							program.playBugDieSound();
							program.remove(monsterCharacter.monsterIMG);
							program.add(splat, monsterCharacter.getDX(), monsterCharacter.getDY());
							splat.setSize(26, 25);
							monsterCharacter.setDX(100000);
							monsterCharacter.setDY(100000);
							program.remove(ball);
						}
					}
					if (projectileMonsterCollision(ball, monsterCharacter2.monsterIMG)) {
						program.playBugDieSound();
						program.remove(monsterCharacter2.monsterIMG);
						program.add(splat2, monsterCharacter2.getDX(), monsterCharacter2.getDY());
						splat2.setSize(50, 50);
						monsterCharacter2.setDX(100000);
						monsterCharacter2.setDY(100000);
						program.remove(ball);
					}
					if (projectileMonsterCollision(ball, monsterCharacter3.monsterIMG)) {
						program.playBugDieSound();
						program.remove(monsterCharacter3.monsterIMG);
						program.add(splat3, monsterCharacter3.getDX(), monsterCharacter3.getDY());
						splat3.setSize(50, 50);
						monsterCharacter3.setDX(100000);
						monsterCharacter3.setDY(100000);
						program.remove(ball);
					}
				}

				for (GOval enemy : enemyballs) {
					if (enemyballs.size() > 0) {
						GRectangle monsterBounds = enemy.getBounds();

						GRectangle player = mainCharacter.player.getBounds();

						if (player.intersects(monsterBounds) && dmgCooldown == 0) {
							program.remove(enemy);

							enemyballs.remove(enemy);
							overlay.damageTaken++;
							overlay.removeHeartCount();
							overlay.heartCount();
							dmgCooldown = 50;
						}

					}
					break;
				}
				boolean overlap = checkCollisions();
				if (overlap && dmgCooldown == 0) {
					System.out.println("Monster hit player!");
					overlay.damageTaken++;
					overlay.removeHeartCount();
					overlay.heartCount();

					dmgCooldown = 50; // This is basically a tick to make sure the Monster can't hit the player
										// multiple times in one collision
										// since the collision check is running so rapidly
				} else if (dmgCooldown > 0) {
					dmgCooldown -= 1; // Decrement the tick
				}
			}
		});
	}

	private boolean checkCollisions() {

		GRectangle boundsPlayer = mainCharacter.player.getBounds();
		GRectangle boundsMonster1;
		GRectangle boundsMonster2;
		GRectangle boundsMonster3;
		if (monsterCharacter != null) {
			boundsMonster1 = monsterCharacter.monsterIMG.getBounds();
			return boundsPlayer.intersects(boundsMonster1);
		}
		if (monsterCharacter2 != null) {
			boundsMonster2 = monsterCharacter2.monsterIMG.getBounds();
			return boundsPlayer.intersects(boundsMonster2);
		}
		if (monsterCharacter3 != null) {
			boundsMonster3 = monsterCharacter3.monsterIMG.getBounds();
			return boundsPlayer.intersects(boundsMonster3);
		}
		return false;
	}

	private void enemyShoot(MonsterCharacter m) {
		addAnEnemyBall(m);
	}

	private void updateEnemyBalls() {
		double playerX = mainCharacter.getDX();
		double playerY = mainCharacter.getDY();

		for (GOval enemyBall : enemyballs) {
			double enemyX = enemyBall.getX();
			double enemyY = enemyBall.getY();

			double dxToPlayer = playerX - enemyX;
			double dyToPlayer = playerY - enemyY;

			// Normalize the direction vector and scale with speed
			double distanceToPlayer = Math.sqrt(dxToPlayer * dxToPlayer + dyToPlayer * dyToPlayer);
			double speed = 2.0; // Adjust speed as needed
			double moveX = (dxToPlayer / distanceToPlayer) * speed;
			double moveY = (dyToPlayer / distanceToPlayer) * speed;

			enemyBall.move(moveX, moveY);
		}
	}

	private boolean projectileMonsterCollision(GImage projectile, GImage monster) {
		GRectangle projectileBounds = projectile.getBounds();
		GRectangle monsterBounds = monster.getBounds();

		return projectileBounds.intersects(monsterBounds);
	}

	private void addABall(String d) {

		GImage ball = makeBall(d);

		program.add(ball, mainCharacter.getDX() + cellWidth() / 2, mainCharacter.getDY() + cellHeight() / 2);
		balls.add(ball);
	}

	private void addAnEnemyBall(MonsterCharacter m) {
		GOval ball = makeEnemyBall(m.getDX() + cellWidth() / 2, m.getDY() + cellHeight() / 2);

		program.add(ball);
		enemyballs.add(ball);
	}

	public GImage makeBall(String d) {
		GImage temp;
		if (d == "Up") {
			temp = new GImage(TOPROCK);
			temp.setSize(cellWidth() / 2, cellHeight() * 2);

		} else if (d == "Down") {
			temp = new GImage(BOTTOMROCK);
			temp.setSize(cellWidth() / 2, cellHeight() * 2);

		} else if (d == "Right") {
			temp = new GImage(RIGHTROCK);
			temp.setSize(cellWidth(), cellHeight() / 2);

		} else {
			temp = new GImage(LEFTROCK);
			temp.setSize(cellWidth(), cellHeight() / 2);

		}

		return temp;
	}

	public GOval makeEnemyBall(double x, double y) {
		GOval temp = new GOval(x - 25 / 2, y - 25 / 2, 25, 25);
		temp.setColor(Color.GREEN);
		temp.setFilled(true);
		return temp;
	}

	private void moveAllBallsOnce(String d) {
		for (GImage ball : balls) {
			if (d == "Up") {
				ball.move(0, -20);
			}
			if (d == "Down") {
				ball.move(0, 20);
			}
			if (d == "Right") {
				ball.move(20, 0);
			}
			if (d == "Left") {
				ball.move(-20, 0);
			}
			if (d == "Down_Right") {
				ball.move(20, 20);
			}
			if (d == "Down_Left") {
				ball.move(20, -20);
			}
			if (d == "Up_Left") {
				ball.move(-20, 20);
			}
			if (d == "Up_Right") {
				ball.move(-20, -20);
			}

		}
	}

	public void setPrevDirection(String d) {
		prevDirection = d;
	}

	private void checkBorder() {
		ArrayList<GImage> ballsToRemove = new ArrayList<>();
		ArrayList<GOval> enemyBallsToRemove = new ArrayList<>();

		for (GImage ball : balls) {
			if (ball.getX() <= cellWidth() || ball.getX() >= PROGRAM_WIDTH - cellWidth() || ball.getY() <= cellHeight()
					|| ball.getY() >= PROGRAM_HEIGHT - cellHeight()) {
				ballsToRemove.add(ball);
			}

			for (GOval enemy : enemyballs) {
				GRectangle mainBallBounds = ball.getBounds();
				GRectangle monsterBounds = enemy.getBounds();

				if (mainBallBounds.intersects(monsterBounds)) {
					enemyBallsToRemove.add(enemy);
					ballsToRemove.add(ball);
				}
			}
		}

		// Remove the balls after iteration
		for (GImage ball : ballsToRemove) {
			program.remove(ball);
			balls.remove(ball);
		}

		for (GOval enemy : enemyBallsToRemove) {
			program.remove(enemy);
			enemyballs.remove(enemy);
			shotOnce = false;
		}

		enemyBallsToRemove.clear();
	}

	private void drawbottomRoom() {
		drawRoom();
		drawMainCharacter();
		drawArcadeMachine();
		drawMonsterCharacters();
		// Add other drawing methods here as needed
	}

	public void unlockTop() {
	}

	public void lockTop() {
	}

	private void drawMainCharacter() {
		mainCharacter.player.setSize(cellWidth(), cellHeight());
		program.add(mainCharacter.player, mainCharacter.getDX(), mainCharacter.getDY());
	}

	private void drawArcadeMachine() {
		arcadeMachine.machine.setSize(cellWidth(), cellHeight() * 2);
		program.add(arcadeMachine.machine, arcadeMachine.getStartCol() * cellWidth(),
				arcadeMachine.getStartRow() * cellHeight());
	}

	private void drawMonsterCharacters() {
		if (monsterCharacter != null) {
			monsterCharacter.monsterIMG.setSize(cellWidth() / 2, cellHeight() / 2);
			program.add(monsterCharacter.monsterIMG, monsterCharacter.getDX(), monsterCharacter.getDY());
		}
		if (monsterCharacter2 != null) {
			monsterCharacter2.monsterIMG.setSize(cellWidth(), cellHeight());
			program.add(monsterCharacter2.monsterIMG, monsterCharacter2.getDX(), monsterCharacter2.getDY());
		}
		if (monsterCharacter3 != null) {
			monsterCharacter3.monsterIMG.setSize(cellWidth(), cellHeight());
			program.add(monsterCharacter3.monsterIMG, monsterCharacter3.getDX(), monsterCharacter3.getDY());
		}

	}

	private void drawRoom() {
		if (base.getCurrentPhase() == 3) {
			for (int row = 0; row < bottomRoom.getNumRows(); row++) {
				for (int col = 0; col < bottomRoom.getNumCols(); col++) {
					if (row == 0 && col % 3 == 1) {
						GImage topWall = new GImage(TOP_WALL);
						topWall.setSize(cellWidth() * 3, cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(topWall, x, y);
					}
					if (row == bottomRoom.getNumRows() - 1 && col % 3 == 1) {
						GImage bottomWall = new GImage(BOTTOM_WALL);
						bottomWall.setSize(cellWidth() * 3, cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(bottomWall, x, y);
					}
				}
			}
			for (int col = 0; col < bottomRoom.getNumCols(); col++) {
				for (int row = 0; row < bottomRoom.getNumRows(); row++) {
					if (col == 0 && row % 3 == 1) {
						GImage leftWall = new GImage(LEFT_WALL);
						leftWall.setSize(cellWidth(), cellHeight() * 3);
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(leftWall, x, y);
					}
					if (col == bottomRoom.getNumCols() - 1 && row % 3 == 1) {
						GImage rightWall = new GImage(RIGHT_WALL);
						rightWall.setSize(cellWidth(), cellHeight() * 3);
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(rightWall, x, y);
					}
				}
			}
			for (int row = 0; row < bottomRoom.getNumRows(); row++) {
				for (int col = 0; col < bottomRoom.getNumCols(); col++) {
					if (row == 0 && col == 0) {
						GImage topLeft = new GImage(TOP_LEFT);
						topLeft.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(topLeft, x, y);
					}
					if (row == 0 && col == bottomRoom.getNumCols() - 1) {
						GImage topRight = new GImage(TOP_RIGHT);
						topRight.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(topRight, x, y);
					}
					if (row == bottomRoom.getNumRows() - 1 && col == 0) {
						GImage bottomLeft = new GImage(BOTTOM_LEFT);
						bottomLeft.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(bottomLeft, x, y);
					}
					if (row == bottomRoom.getNumRows() - 1 && col == bottomRoom.getNumCols() - 1) {
						GImage bottomRight = new GImage(BOTTOM_RIGHT);
						bottomRight.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(bottomRight, x, y);
					}
					if (col == (bottomRoom.getNumCols() - 1) / 2 && row == 0) {
						GImage topDoor = new GImage(TOP_CLOSED_DOOR);
						topDoor.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(topDoor, x, y);
					}
				}
			}

			for (int row = 1; row < bottomRoom.getNumRows() - 1; row++) {
				for (int col = 1; col < bottomRoom.getNumCols() - 1; col++) {
					GImage floor = new GImage(FLOORING);
					floor.setSize(cellWidth(), cellHeight());
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(floor, x, y);
				}
			}
		}
		if (base.getCurrentPhase() == 4) {
			for (int row = 0; row < bottomRoom.getNumRows(); row++) {
				for (int col = 0; col < bottomRoom.getNumCols(); col++) {
					if (row == 0 && col % 3 == 1) {
						GImage topWall = new GImage(TOP_WALL);
						topWall.setSize(cellWidth() * 3, cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(topWall, x, y);
					}
					if (row == bottomRoom.getNumRows() - 1 && col % 3 == 1) {
						GImage bottomWall = new GImage(BOTTOM_WALL);
						bottomWall.setSize(cellWidth() * 3, cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(bottomWall, x, y);
					}
				}
			}
			for (int col = 0; col < bottomRoom.getNumCols(); col++) {
				for (int row = 0; row < bottomRoom.getNumRows(); row++) {
					if (col == 0 && row % 3 == 1) {
						GImage leftWall = new GImage(LEFT_WALL);
						leftWall.setSize(cellWidth(), cellHeight() * 3);
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(leftWall, x, y);
					}
					if (col == bottomRoom.getNumCols() - 1 && row % 3 == 1) {
						GImage rightWall = new GImage(RIGHT_WALL);
						rightWall.setSize(cellWidth(), cellHeight() * 3);
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(rightWall, x, y);
					}
				}
			}
			for (int row = 0; row < bottomRoom.getNumRows(); row++) {
				for (int col = 0; col < bottomRoom.getNumCols(); col++) {
					if (row == 0 && col == 0) {
						GImage topLeft = new GImage(TOP_LEFT);
						topLeft.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(topLeft, x, y);
					}
					if (row == 0 && col == bottomRoom.getNumCols() - 1) {
						GImage topRight = new GImage(TOP_RIGHT);
						topRight.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(topRight, x, y);
					}
					if (row == bottomRoom.getNumRows() - 1 && col == 0) {
						GImage bottomLeft = new GImage(BOTTOM_LEFT);
						bottomLeft.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(bottomLeft, x, y);
					}
					if (row == bottomRoom.getNumRows() - 1 && col == bottomRoom.getNumCols() - 1) {
						GImage bottomRight = new GImage(BOTTOM_RIGHT);
						bottomRight.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(bottomRight, x, y);
					}
					if (col == (bottomRoom.getNumCols() - 1) / 2 && row == 0) {
						GImage topDoor = new GImage(TOP_OPEN_DOOR);
						topDoor.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(topDoor, x, y);
					}
				}
			}

			for (int row = 1; row < bottomRoom.getNumRows() - 1; row++) {
				for (int col = 1; col < bottomRoom.getNumCols() - 1; col++) {
					GImage floor = new GImage(FLOORING);
					floor.setSize(cellWidth(), cellHeight());
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(floor, x, y);
				}
			}
		}
	}

	@Override
	public synchronized void keyPressed(KeyEvent e) {
		pressedKeys.add(e.getKeyCode());

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			overlay.stopTimer();
			program.switchToPause();
		}

		else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			System.out.println(mainCharacter.getDX());
			if (mainCharacter.getDX() > (arcadeMachine.getStartCol() - 1) * cellWidth()
					&& (mainCharacter.getDY() > arcadeMachine.getStartRow() * cellHeight()
							&& mainCharacter.getDY() < (arcadeMachine.getStartRow() + 2) * cellHeight())) {
				program.playAracadeSound();
				pressedKeys.clear();
				offset = new Point(0, 0);
				program.setBottomRoomState(false);
				program.switchToPacmanGraphic();

			} else {
				for (GImage b : balls) {
					if (b.getX() < 1000) {
						return;
					}
				}
				program.playShootingSound();
				addABall(prevDirection);
			}
		}
		if (!pressedKeys.isEmpty()) {
			for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext();) {
				switch (it.next()) {
				case KeyEvent.VK_UP:
					if (balls.isEmpty()) {
						prevDirection = "Up";
					}
					if (mainCharacter.getDY() == cellHeight() && (mainCharacter.getDX() > cellWidth() * 4.5
							&& mainCharacter.getDY() < cellWidth() * 5.5)) {
						if (base.getCurrentPhase() == 4) {
							pressedKeys.clear();
							offset = new Point(0, 0);
							base.lockRight();
							program.playOpenSound();
							program.setBottomRoomState(false);
							program.switchToBaseRoom();
						} else {
							System.out.println("Locked");
							program.playLockedSound();
						}
					}
					offset.y = -1;
					break;
				case KeyEvent.VK_LEFT:
					if (balls.isEmpty()) {
						prevDirection = "Left";
					}
					offset.x = -1;

					break;
				case KeyEvent.VK_DOWN:
					if (balls.isEmpty()) {
						prevDirection = "Down";
					}
					offset.y = 1;
					break;
				case KeyEvent.VK_RIGHT:
					if (balls.isEmpty()) {
						prevDirection = "Right";
					}
					offset.x = 1;
					System.out.println(mainCharacter.getDX());
					break;
				}
			}
			mainCharacter.move(directionResolver(offset), 11, 11);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		System.out.println("keyReleased called");
		if (code == KeyEvent.VK_UP) {
			offset.y = 0;
			System.out.println("Up offset reset.");
		} else if (code == KeyEvent.VK_LEFT) {
			offset.x = 0;
			System.out.println("Left offset reset");
		} else if (code == KeyEvent.VK_DOWN) {
			offset.y = 0;
			System.out.println("Down offset reset.");
		} else if (code == KeyEvent.VK_RIGHT) {
			offset.x = 0;
			System.out.println("Right offset reset.");
		}
		pressedKeys.remove(e.getKeyCode());
	}

	private double cellWidth() {
		double cellWidth = (double) PROGRAM_WIDTH / bottomRoom.getNumRows();
		return cellWidth;
	}

	private double cellHeight() {
		double cellHeight = (double) PROGRAM_HEIGHT / bottomRoom.getNumCols();
		return cellHeight;
	}

	@Override
	public void showContents() {

		drawbottomRoom();
		overlay.showContents();
		overlay.startTimer();
		overlay.addTask(3);
		gameTimer.start();

	}

	@Override
	public void hideContents() {

		program.removeAll();
		gameTimer.stop();
	}

}
