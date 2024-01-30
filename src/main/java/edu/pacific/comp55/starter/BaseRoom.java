package edu.pacific.comp55.starter;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Timer;
import java.util.Iterator;

import acm.graphics.GImage;
import acm.graphics.GRectangle;

public class BaseRoom extends GraphicsPane {

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

	public String prevDirection;
	private Timer gameTimer;
	int keyCode;

	private final Set<Integer> pressedKeys = new HashSet<>();
	Point offset = new Point();

	public Direction directionResolver(Point dirOffset) {
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

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_UP) {
			offset.y = 0;
		} else if (code == KeyEvent.VK_LEFT) {
			offset.x = 0;
		} else if (code == KeyEvent.VK_DOWN) {
			offset.y = 0;
		} else if (code == KeyEvent.VK_RIGHT) {
			offset.x = 0;
		}
		pressedKeys.remove(e.getKeyCode());
	}

	private Overlay overlay;
	public int currentPhase;
	private Board baseRoom = new Board(11, 11);
	private PlayerCharacter mainCharacter = new PlayerCharacter((baseRoom.getNumCols() - 1) / 2,
			(baseRoom.getNumRows() - 1) / 2, 400, 300, "Sebastian");
	private MonsterCharacter monsterCharacter = new MonsterCharacter("Bug", 100, 500, 400);
	private ArcadeMachine arcadeMachine = new ArcadeMachine((baseRoom.getNumRows() - 2) / 2, 1, "machine");
	public boolean rightLock = true;
	public boolean bottomLock = true;
	public boolean topLock = true;
	private int dmgCooldown = 0;
	private ArrayList<GImage> balls = new ArrayList<GImage>();
	public BaseRoom(MainApplication app) {
    	super();
    	program = app;
    	overlay = program.getOverlay();
    	currentPhase = 0;
    	// This timer is used to move the monsterCharacter towards the playerCharacter on a loop
    	int delay = 16;
    	
        
        gameTimer = new Timer(delay, new ActionListener() {
        	
            @Override
            public void actionPerformed(ActionEvent e) {
                moveAllBallsOnce(prevDirection);
        		checkBorder();
        		if (monsterCharacter.getDX() < program.getWidth()) {
            	monsterCharacter.move(mainCharacter);
        		}
            	for (GImage ball: balls) {
            		if (projectileMonsterCollision(ball, monsterCharacter.monsterIMG)) {
            			program.playBugDieSound();
            			program.remove(monsterCharacter.monsterIMG);
            			GImage splat = new GImage("splat.png");
            			program.add(splat, monsterCharacter.getDX(), monsterCharacter.getDY());
            			splat.setSize(26, 25);
            			monsterCharacter.setDX(1000000);
            			monsterCharacter.setDY(1000000);
            		
            			program.remove(ball);
            		}
            	}
            	boolean overlap = checkCollisions();
            	if (overlap && dmgCooldown == 0) {
            		 System.out.println("Monster hit player!");
            			overlay.damageTaken++;
            			overlay.removeHeartCount();
            			overlay.heartCount();
            		
            		dmgCooldown = 50; // This is basically a tick to make sure the Monster can't hit the player multiple times in one collision
            						  // since the collision check is running so rapidly
            	}
            	else if (dmgCooldown > 0) {
            		dmgCooldown -= 1; // Decrement the tick
            	}
            }
        });
    }

	private boolean projectileMonsterCollision(GImage projectile, GImage monster) {
		GRectangle projectileBounds = projectile.getBounds();
		GRectangle monsterBounds = monster.getBounds();
		return projectileBounds.intersects(monsterBounds);
	}

	private void addABall(String d) {
		
		GImage ball = makeBall( d);

		program.add(ball,mainCharacter.getDX() + cellWidth() / 2, mainCharacter.getDY() + cellHeight() / 2);
		balls.add(ball);
	}

	public GImage makeBall( String d) {
		GImage temp;
		if (d == "Up") {
			temp = new GImage(TOPROCK);		
			temp.setSize(cellWidth() / 2,cellHeight() * 2);

			}
		else if (d == "Down") {
			temp = new GImage(BOTTOMROCK);	
			temp.setSize(cellWidth() / 2, cellHeight() * 2);

		}
		else if (d == "Right") {
			temp = new GImage(RIGHTROCK);	
			temp.setSize(cellWidth(),cellHeight() / 2);

		}
		else  {
			temp = new GImage(LEFTROCK);	
			temp.setSize(cellWidth(),cellHeight() / 2);

		}
		
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
		for (GImage ball : balls) {
			if (ball.getX() <= cellWidth() || ball.getX() >= PROGRAM_WIDTH - cellWidth() || ball.getY() <= cellHeight()
					|| ball.getY() >= PROGRAM_HEIGHT - cellHeight()) {
				System.out.println("detected");
				program.remove(ball);
				balls.remove(ball);
				System.out.print(balls);
				break;
			}
		}
	}

	private boolean checkCollisions() {

		GRectangle boundsPlayer = mainCharacter.player.getBounds();
		GRectangle boundsMonster = monsterCharacter.monsterIMG.getBounds();
		return boundsPlayer.intersects(boundsMonster);
	}

	public int getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(int currentPhase) {
		this.currentPhase = currentPhase;
	}

	public void unlockRight() {
		rightLock = false;
	}

	public void lockRight() {
		rightLock = true;
	}

	public void unlockTop() {
		topLock = false;
	}

	public void lockTop() {
		topLock = true;
	}

	public void unlockBottom() {
		bottomLock = false;
	}

	public void lockBottom() {
		bottomLock = true;
	}

	private void drawBaseRoom(int currentPhase) {

		drawRoom(currentPhase);
		drawMainCharacter();
		drawArcadeMachine();
		drawMonsterCharacters();
		// Add other drawing methods here as needed
	}

	private void drawMainCharacter() {
		mainCharacter.player.setSize(cellWidth(), cellHeight());
		program.add(mainCharacter.player, mainCharacter.getDX(), mainCharacter.getDY());
	}

	private void drawMonsterCharacters() {
		monsterCharacter.monsterIMG.setSize(cellWidth() / 2, cellHeight() / 2);
		program.add(monsterCharacter.monsterIMG, monsterCharacter.getDX(), monsterCharacter.getDY());
	}

	private void drawArcadeMachine() {
		arcadeMachine.machine.setSize(cellWidth(), cellHeight() * 2);
		program.add(arcadeMachine.machine, arcadeMachine.getStartCol() * cellWidth(),
				arcadeMachine.getStartRow() * cellHeight());
	}

	private void drawRoom(int currentPhase) {
		for (int row = 0; row < baseRoom.getNumRows(); row++) {
			for (int col = 0; col < baseRoom.getNumCols(); col++) {
				if (row == 0 && col % 3 == 1) {
					GImage topWall = new GImage(TOP_WALL);
					topWall.setSize(cellWidth() * 3 , cellHeight());
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(topWall, x, y);
				}
				if (row == baseRoom.getNumRows() - 1 && col % 3 == 1) {
					GImage bottomWall = new GImage(BOTTOM_WALL);
					bottomWall.setSize(cellWidth() * 3, cellHeight());
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(bottomWall, x, y);
				}
			}
		}
		for (int col = 0; col < baseRoom.getNumCols(); col++) {
			for (int row = 0; row < baseRoom.getNumRows(); row++) {
				if (col == 0 && row % 3 == 1) {
					GImage leftWall = new GImage(LEFT_WALL);
					leftWall.setSize(cellWidth(), cellHeight() * 3);
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(leftWall, x, y);
				}
				if (col == baseRoom.getNumCols() - 1 && row % 3 == 1) {
					GImage rightWall = new GImage(RIGHT_WALL);
					rightWall.setSize(cellWidth(), cellHeight() * 3);
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(rightWall, x, y);
				}
			}
		}
		for (int row = 0; row < baseRoom.getNumRows(); row++) {
			for (int col = 0; col < baseRoom.getNumCols(); col++) {
				if (row == 0 && col == 0) {
					GImage topLeft = new GImage(TOP_LEFT);
					topLeft.setSize(cellWidth(), cellHeight());
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(topLeft, x, y);
				}
				if (row == 0 && col == baseRoom.getNumCols() - 1) {
					GImage topRight = new GImage(TOP_RIGHT);
					topRight.setSize(cellWidth(), cellHeight());
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(topRight, x, y);
				}
				if (row == baseRoom.getNumRows() - 1 && col == 0) {
					GImage bottomLeft = new GImage(BOTTOM_LEFT);
					bottomLeft.setSize(cellWidth(), cellHeight());
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(bottomLeft, x, y);
				}
				if (row == baseRoom.getNumRows() - 1 && col == baseRoom.getNumCols() - 1) {
					GImage bottomRight = new GImage(BOTTOM_RIGHT);
					bottomRight.setSize(cellWidth(), cellHeight());
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(bottomRight, x, y);
				}
			}
		}

		for (int row = 1; row < baseRoom.getNumRows() - 1; row++) {
			for (int col = 1; col < baseRoom.getNumCols() - 1; col++) {
				GImage floor = new GImage(FLOORING);
				floor.setSize(cellWidth(), cellHeight());
				double x = col * cellWidth();
				double y = row * cellHeight();
				program.add(floor, x, y);
			}
		}

		if (currentPhase == 0 || currentPhase == 2 || currentPhase == 4) {
			for (int row = 0; row < baseRoom.getNumRows(); row++) {
				for (int col = 0; col < baseRoom.getNumCols(); col++) {

					if (col == (baseRoom.getNumCols() - 1) / 2 && row == 0) {
						GImage topDoor = new GImage(TOP_CLOSED_DOOR);
						topDoor.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(topDoor, x, y);
					}
					if (col == baseRoom.getNumCols() - 1 && row == (baseRoom.getNumRows() - 1) / 2) {
						GImage rightDoor = new GImage(RIGHT_CLOSED_DOOR);
						rightDoor.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(rightDoor, x, y);
					}
					if (col == (baseRoom.getNumCols() - 1) / 2 && row == (baseRoom.getNumRows() - 1)) {
						GImage bottomDoor = new GImage(BOTTOM_CLOSED_DOOR);
						bottomDoor.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(bottomDoor, x, y);
					}

				}
			}
		}

		if (currentPhase == 1) {
			for (int row = 0; row < baseRoom.getNumRows(); row++) {
				for (int col = 0; col < baseRoom.getNumCols(); col++) {

					if (col == (baseRoom.getNumCols() - 1) / 2 && row == 0) {
						GImage topDoor = new GImage(TOP_CLOSED_DOOR);
						topDoor.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(topDoor, x, y);
					}
					if (col == baseRoom.getNumCols() - 1 && row == (baseRoom.getNumRows() - 1) / 2) {
						GImage rightDoor = new GImage(RIGHT_OPEN_DOOR);
						rightDoor.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(rightDoor, x, y);
					}
					if (col == (baseRoom.getNumCols() - 1) / 2 && row == (baseRoom.getNumRows() - 1)) {
						GImage bottomDoor = new GImage(BOTTOM_CLOSED_DOOR);
						bottomDoor.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(bottomDoor, x, y);
					}

				}
			}
		}
		if (currentPhase == 3) {
			for (int row = 0; row < baseRoom.getNumRows(); row++) {
				for (int col = 0; col < baseRoom.getNumCols(); col++) {

					if (col == (baseRoom.getNumCols() - 1) / 2 && row == 0) {
						GImage topDoor = new GImage(TOP_CLOSED_DOOR);
						topDoor.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(topDoor, x, y);
					}
					if (col == baseRoom.getNumCols() - 1 && row == (baseRoom.getNumRows() - 1) / 2) {
						GImage rightDoor = new GImage(RIGHT_CLOSED_DOOR);
						rightDoor.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(rightDoor, x, y);
					}
					if (col == (baseRoom.getNumCols() - 1) / 2 && row == (baseRoom.getNumRows() - 1)) {
						GImage bottomDoor = new GImage(BOTTOM_OPEN_DOOR);
						bottomDoor.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(bottomDoor, x, y);
					}

				}
			}
		}
		if (currentPhase == 5) {
			for (int row = 0; row < baseRoom.getNumRows(); row++) {
				for (int col = 0; col < baseRoom.getNumCols(); col++) {

					if (col == (baseRoom.getNumCols() - 1) / 2 && row == 0) {
						GImage topDoor = new GImage(TOP_OPEN_DOOR);
						topDoor.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(topDoor, x, y);
					}
					if (col == baseRoom.getNumCols() - 1 && row == (baseRoom.getNumRows() - 1) / 2) {
						GImage rightDoor = new GImage(RIGHT_CLOSED_DOOR);
						rightDoor.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(rightDoor, x, y);
					}
					if (col == (baseRoom.getNumCols() - 1) / 2 && row == (baseRoom.getNumRows() - 1)) {
						GImage bottomDoor = new GImage(BOTTOM_CLOSED_DOOR);
						bottomDoor.setSize(cellWidth(), cellHeight());
						double x = col * cellWidth();
						double y = row * cellHeight();
						program.add(bottomDoor, x, y);
					}

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
		} else if (e.getKeyCode() == KeyEvent.VK_P) {
			program.switchToTrafficJamPhase4();
		} else if (e.getKeyCode() == KeyEvent.VK_M) {
			setCurrentPhase(5);
			program.switchToPacmanGraphic();
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			System.out.println(mainCharacter.getDX());
			if (mainCharacter.getDX() < (arcadeMachine.getStartCol() + 1) * cellWidth()
					&& (mainCharacter.getDY() > arcadeMachine.getStartRow() * cellHeight()
							&& mainCharacter.getDY() < (arcadeMachine.getStartRow() + 2) * cellHeight())) {
				if (currentPhase == 0) {
					program.playAracadeSound();
					pressedKeys.clear();
					offset = new Point(0, 0);
					program.switchToTrafficJamPhase1();
				}
				if (currentPhase == 2) {
					program.playAracadeSound();
					pressedKeys.clear();
					offset = new Point(0, 0);
					program.switchToTrafficJamPhase2();
				}
				if (currentPhase == 4) {
					program.playAracadeSound();
					pressedKeys.clear();
					offset = new Point(0, 0);
					program.switchToTrafficJamPhase3();
				}
				if (currentPhase == 6) {
					program.playAracadeSound();
					pressedKeys.clear();
					offset = new Point(0, 0);
					program.switchToTrafficJamPhase4();
				}

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
							&& mainCharacter.getDX() < cellWidth() * 6.5)) {

						if (currentPhase == 5) {
							if (!topLock) {

								pressedKeys.clear();
								offset = new Point(0, 0);
								program.switchToTopRoom();
								program.playOpenSound();
								break;
							}
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
					if (mainCharacter.getDY() == 600 - cellHeight() * 2 && (mainCharacter.getDX() > cellWidth() * 4.5
							&& mainCharacter.getDX() < cellWidth() * 6.5)) {
						if (currentPhase == 3) {
							if (!bottomLock) {

								pressedKeys.clear();
								offset = new Point(0, 0);
								program.playOpenSound();
								program.switchToBottomRoom();
								break;
							}
						} else {
							System.out.println("Locked");
							program.playLockedSound();

						}
					}
					offset.y = 1;
					break;
				case KeyEvent.VK_RIGHT:
					if (balls.isEmpty()) {
						prevDirection = "Right";
					}
					if (mainCharacter.getDX() == 654.5454545454545
							&& (mainCharacter.getDY() > 218.1818181818 && mainCharacter.getDY() < 327.2727272727)) {
						if (currentPhase == 1) {
							if (!rightLock) {
								pressedKeys.clear();
								offset = new Point(0, 0);
								program.playOpenSound();
								program.switchToRightRoom();
								break;
							}
						} else {
							System.out.println("Locked");
							program.playLockedSound();
						}
					}

					offset.x = 1;
					System.out.println(mainCharacter.getDX());
					break;
				}
			}
			mainCharacter.move(directionResolver(offset), 11, 11);
		}

	}

	private double cellWidth() {
		double cellWidth = (double) PROGRAM_WIDTH / baseRoom.getNumRows();
		return cellWidth;
	}

	private double cellHeight() {
		double cellHeight = (double) PROGRAM_HEIGHT / baseRoom.getNumCols();
		return cellHeight;
	}

	@Override
	public void showContents() {

		drawBaseRoom(currentPhase);
		overlay.showContents();
		overlay.startTimer();
		overlay.addTask(currentPhase);

		gameTimer.start();

	}

	@Override
	public void hideContents() {
		program.removeAll();
		gameTimer.stop();
	}
}
