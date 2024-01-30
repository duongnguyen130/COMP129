package edu.pacific.comp55.starter;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import acm.graphics.*;

public class PacmanGraphic extends GraphicsPane implements ActionListener {
	private MainApplication program;
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	private static final int MAZE_ROWS = 12;
	private static final int MAZE_COLS = 12;
	public static final String TOP_WALL = "TopWall.png";
	public static final String LEFT_WALL = "LeftWall.png";
	public static final String BOTTOM_WALL = "BottomWall.png";
	public static final String RIGHT_WALL = "RightWall.png";
	public static final String TOP_LEFT = "TopRight.png";
	public static final String TOP_RIGHT = "TopLeft.png";
	public static final String BOTTOM_LEFT = "BottomLeft.png";
	public static final String BOTTOM_RIGHT = "BottomRight.png";
	public static final String FLOORING = "Flooring.png";
	public static final String TOP_OPEN_DOOR = "OpenDoor.png";
	public static final String BOTTOM_OPEN_DOOR = "Bottom_Open_Door.png";
	private Overlay overlay;
	private Board maze;
	private PlayerCharacter mainCharacter = new PlayerCharacter(1, 5, 0, 0, "Sebastian");;
    private FriendlyMonster one = new FriendlyMonster("Bug1", 100, 500, 400);
    private FriendlyMonster two = new FriendlyMonster("Bug2", 100, 500, 400);
    private FriendlyMonster three = new FriendlyMonster("Bug3", 100, 500, 400);
    private BaseRoom base;
	private GLine verticalLine;
	private GLine horizontalLine;
	private Timer gameTimer;
	private int totalSaveBugs = 0;
	private final Set<Integer> pressedKeys = new HashSet<>();
	Point offset = new Point();
    private ArrayList<GLine> lines = new ArrayList<>();
    private GRectangle bottomDoorBounds;
    private Timer animationTimer;
    Direction currDir;
	
	public Direction directionResolver(Point dirOffset) {
		// System.out.println(dirOffset);
		if (dirOffset.x == 0 && dirOffset.y == -1) {
			return Direction.UP;
		}
		else if (dirOffset.x == 0 && dirOffset.y == 1) {
			return Direction.DOWN;
		}
		else if (dirOffset.x == 1 && dirOffset.y == 0) {
			return Direction.RIGHT;
		}
		else if (dirOffset.x == -1 && dirOffset.y == 0) {
			return Direction.LEFT;
		}
		else if (dirOffset.x == 1 && dirOffset.y == -1) {
			return Direction.UP_RIGHT;
		}
		else if (dirOffset.x == -1 && dirOffset.y == -1) {
			return Direction.UP_LEFT;
		}
		else if (dirOffset.x == 1 && dirOffset.y == 1) {
			return Direction.DOWN_RIGHT;
		}
		else if (dirOffset.x == -1 && dirOffset.y == 1) {
			return Direction.DOWN_LEFT;
		}
		else {
			return Direction.STILL;
		}
	}

	public PacmanGraphic(MainApplication app) {
		super();
		program = app;
		base = program.getBase();
		maze = new Board(12, 12);
		overlay = program.getOverlay();
		gameTimer = new Timer(50, (this));
		
		animationTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	currDir = directionResolver(offset);
                mainCharacter.updateSprite(currDir);
                mainCharacter.setPlayerImageAndSize(mainCharacter.getSprite(), MAZE_ROWS, MAZE_COLS);
            }
        });

        animationTimer.start();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	private double cellWidth() {
		double cellWidth = 800 / maze.getNumCols();
		return cellWidth;
	}

	private double cellHeight() {
		double cellHeight = 600 / maze.getNumCols();
		return cellHeight;
	}

	private void drawMainCharacter() {
		mainCharacter.player.setSize(30.0,30.0);

    	mainCharacter.setDX(mainCharacter.getStartCol() * cellWidth());
    	mainCharacter.setDY(mainCharacter.getStartRow() * cellWidth()); 
		program.add(mainCharacter.player, (mainCharacter.getStartCol()) * cellWidth(),
				mainCharacter.getStartRow() * cellHeight());
	}
	
	private void drawMonsterCharacters() {
		one.setDX(cellWidth() + cellWidth() / 20);
		one.setDY(program.getHeight() - cellHeight() - cellHeight() - cellHeight()/1.2);
		one.monsterIMG.setSize(cellWidth() , cellHeight());
		program.add(one.monsterIMG, one.getDX(), one.getDY());
		
		two.setDX(program.getWidth() - (2* cellWidth() + cellWidth() / 20));
		two.setDY(program.getHeight() - cellHeight() - cellHeight() - cellHeight()/1.2);
		two.monsterIMG.setSize(cellWidth(), cellHeight());
		program.add(two.monsterIMG, two.getDX(), two.getDY());

		three.setDX(program.getWidth() - (2* cellWidth() + cellWidth() / 20));
		three.setDY(cellHeight());
		three.monsterIMG.setSize(cellWidth(), cellHeight());
		program.add(three.monsterIMG, three.getDX(), three.getDY());
	}
	 private boolean checkCollision(GImage image, ArrayList<GLine> lines) {
	        GRectangle imageBounds = image.getBounds();

	        for (GLine line : lines) {
	            // Convert the GLine into a bounding rectangle
	            double x1 = line.getStartPoint().getX();
	            double y1 = line.getStartPoint().getY();
	            double x2 = line.getEndPoint().getX();
	            double y2 = line.getEndPoint().getY();
	            
	            double minX = Math.min(x1, x2);
	            double maxX = Math.max(x1, x2);
	            double minY = Math.min(y1, y2);
	            double maxY = Math.max(y1, y2);
	            
	            GRectangle lineBounds = new GRectangle(minX, minY, maxX - minX, maxY - minY);

	            // Check if the image bounds intersect with the line bounds
	            if (imageBounds.intersects(lineBounds)) {
	                return true; // Collision detected
	            }
	        }

	        return false; // No collision detected
	    }

	private void buildMaze() {
		for (int row = 1; row < 12 - 1; row++) {
			for (int col = 1; col < 12 - 1; col++) {
				GImage floor = new GImage(FLOORING);
				floor.setSize(cellWidth(), cellHeight());
				double x = col * cellWidth();
				double y = row * cellHeight();
				program.add(floor, x, y);
			}
		}

		for (int row = 0; row < 12; row++) {
			for (int col = 0; col < 12; col++) {
				if (row == 0) {
					GImage topWallI = new GImage(TOP_WALL);
					topWallI.setSize(cellWidth(), cellHeight());
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(topWallI, x, y);
				}
				if (row == 12 - 1) {
					GImage bottomWallI = new GImage(BOTTOM_WALL);
					bottomWallI.setSize(cellWidth(), cellHeight());
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(bottomWallI, x, y);
				}
			}
		}
		for (int col = 0; col < 12; col++) {
			for (int row = 0; row < 12; row++) {
				if (col == 0) {
					GImage leftWallI = new GImage(LEFT_WALL);
					leftWallI.setSize(cellWidth(), cellHeight());
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(leftWallI, x, y);
				}
				if (col == 12 - 1) {
					GImage rightWallI = new GImage(RIGHT_WALL);
					rightWallI.setSize(cellWidth(), cellHeight());
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(rightWallI, x, y);
				}
			}
		}
		for (int row = 0; row < 12; row++) {
			for (int col = 0; col < 12; col++) {
				if (row == 0 && col == 0) {
					GImage topLeft = new GImage(TOP_LEFT);
					topLeft.setSize(cellWidth(), cellHeight());
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(topLeft, x, y);
				}
				if (row == 0 && col == 12 - 1) {
					GImage topRight = new GImage(TOP_RIGHT);
					topRight.setSize(cellWidth(), cellHeight());
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(topRight, x, y);
				}
				if (row == 12 - 1 && col == 0) {
					GImage bottomLeft = new GImage(BOTTOM_LEFT);
					bottomLeft.setSize(cellWidth(), cellHeight());
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(bottomLeft, x, y);
				}
				if (row == 12 - 1 && col == 12 - 1) {
					GImage bottomRight = new GImage(BOTTOM_RIGHT);
					bottomRight.setSize(cellWidth(), cellHeight());
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(bottomRight, x, y);
				}
			}
		}

		GImage bottomDoor = new GImage(BOTTOM_OPEN_DOOR);
		bottomDoor.setSize(cellWidth(), cellHeight());
		program.add(bottomDoor, 6 * cellWidth(), 11 * cellHeight());
		bottomDoorBounds = bottomDoor.getBounds();

		GImage topDoor = new GImage(TOP_OPEN_DOOR);
		topDoor.setSize(cellWidth(), cellHeight());
		program.add(topDoor, 5 * cellWidth(), 0 * cellHeight());

		GLine topWall = new GLine(1 * cellWidth(), 1 * cellHeight(), 5 * cellWidth(), 1 * cellHeight());
		topWall.setColor(Color.YELLOW);
		topWall.setLineWidth(3);
		program.add(topWall);
		lines.add(topWall);
		topWall = new GLine(6 * cellWidth(), 1 * cellHeight(), 11 * cellWidth(), 1 * cellHeight());
		topWall.setColor(Color.YELLOW);
		topWall.setLineWidth(3);
		program.add(topWall);
		lines.add(topWall);
		GLine bottomWall = new GLine(1 * cellWidth(), 11 * cellHeight(), 6 * cellWidth(),
				(maze.getNumRows() - 1) * cellHeight());
		bottomWall.setColor(Color.YELLOW);
		bottomWall.setLineWidth(3);
		program.add(bottomWall);
		lines.add(bottomWall);
		bottomWall = new GLine(7 * cellWidth(), 11 * cellHeight(), 11 * cellWidth(), 11 * cellHeight());
		bottomWall.setColor(Color.YELLOW);
		bottomWall.setLineWidth(3);
		program.add(bottomWall);
		lines.add(bottomWall);
		GLine rightWall = new GLine((maze.getNumCols() - 1) * cellWidth(), 1 * cellHeight(),
				(maze.getNumCols() - 1) * cellWidth(), (maze.getNumRows() - 1) * cellHeight());
		rightWall.setColor(Color.YELLOW);
		rightWall.setLineWidth(3);
		program.add(rightWall);
		lines.add(rightWall);
		GLine leftWall = new GLine(1 * cellWidth(), 1 * cellHeight(), 1 * cellWidth(),
				(maze.getNumRows() - 1) * cellHeight());
		leftWall.setColor(Color.YELLOW);
		leftWall.setLineWidth(3);
		program.add(leftWall);
		lines.add(leftWall);
		verticalLine = new GLine(3 * cellWidth(), 1 * cellHeight(), 3 * cellWidth(), 2 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		verticalLine = new GLine(4 * cellWidth(), 2 * cellHeight(), 4 * cellWidth(), 3 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		horizontalLine = new GLine(3 * cellWidth(), 3 * cellHeight(), 4 * cellWidth(), 3 * cellHeight());
		horizontalLine.setColor(Color.YELLOW);
		horizontalLine.setLineWidth(3);
		program.add(horizontalLine);
		lines.add(horizontalLine);
		
		verticalLine = new GLine(2 * cellWidth(), 2 * cellHeight(), 2 * cellWidth(), 5 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		verticalLine = new GLine(3 * cellWidth(), 5 * cellHeight(), 3 * cellWidth(), 7 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		horizontalLine = new GLine(2 * cellWidth(), 6 * cellHeight(), 3 * cellWidth(), 6 * cellHeight());
		horizontalLine.setColor(Color.YELLOW);
		horizontalLine.setLineWidth(3);
		program.add(horizontalLine);
		lines.add(horizontalLine);
		
		verticalLine = new GLine(4 * cellWidth(), 4 * cellHeight(), 4 * cellWidth(), 5 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		verticalLine = new GLine(5 * cellWidth(), 5 * cellHeight(), 5 * cellWidth(), 7 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		horizontalLine = new GLine(4 * cellWidth(), 6 * cellHeight(), 5 * cellWidth(), 6 * cellHeight());
		horizontalLine.setColor(Color.YELLOW);
		horizontalLine.setLineWidth(3);
		program.add(horizontalLine);
		lines.add(horizontalLine);
		
		verticalLine = new GLine(4 * cellWidth(), 7 * cellHeight(), 4 * cellWidth(), 8 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		horizontalLine = new GLine(2 * cellWidth(), 10 * cellHeight(), 3 * cellWidth(), 10 * cellHeight());
		horizontalLine.setColor(Color.YELLOW);
		horizontalLine.setLineWidth(3);
		program.add(horizontalLine);
		lines.add(horizontalLine);
		
		horizontalLine = new GLine(2 * cellWidth(), 8 * cellHeight(), 3 * cellWidth(), 8 * cellHeight());
		horizontalLine.setColor(Color.YELLOW);
		horizontalLine.setLineWidth(3);
		program.add(horizontalLine);
		lines.add(horizontalLine);
		
		verticalLine = new GLine(2 * cellWidth(), 8 * cellHeight(), 2 * cellWidth(), 10 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		verticalLine = new GLine(3 * cellWidth(), 8 * cellHeight(), 3 * cellWidth(), 10 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		verticalLine = new GLine(5 * cellWidth(), 8 * cellHeight(), 5 * cellWidth(), 10 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		horizontalLine = new GLine(4 * cellWidth(), 9 * cellHeight(), 5 * cellWidth(), 9 * cellHeight());
		horizontalLine.setColor(Color.YELLOW);
		horizontalLine.setLineWidth(3);
		program.add(horizontalLine);
		lines.add(horizontalLine);
		
		verticalLine = new GLine(6 * cellWidth(), 9 * cellHeight(), 6 * cellWidth(), 11 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		verticalLine = new GLine(5 * cellWidth(), 1 * cellHeight(), 5 * cellWidth(), 3 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		verticalLine = new GLine(9 * cellWidth(), 11 * cellHeight(), 9 * cellWidth(), 10 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		verticalLine = new GLine(8 * cellWidth(), 10 * cellHeight(), 8 * cellWidth(), 9 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		horizontalLine = new GLine(9 * cellWidth(), 9 * cellHeight(), 8 * cellWidth(), 9 * cellHeight());
		horizontalLine.setColor(Color.YELLOW);
		horizontalLine.setLineWidth(3);
		program.add(horizontalLine);
		lines.add(horizontalLine);
		
		verticalLine = new GLine(10 * cellWidth(), 10 * cellHeight(), 10 * cellWidth(), 7 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		verticalLine = new GLine(9 * cellWidth(), 7 * cellHeight(), 9 * cellWidth(), 5 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		horizontalLine = new GLine(10 * cellWidth(), 6 * cellHeight(), 9 * cellWidth(), 6 * cellHeight());
		horizontalLine.setColor(Color.YELLOW);
		horizontalLine.setLineWidth(3);
		program.add(horizontalLine);
		lines.add(horizontalLine);
		
		verticalLine = new GLine(8 * cellWidth(), 8 * cellHeight(), 8 * cellWidth(), 7 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		verticalLine = new GLine(7 * cellWidth(), 7 * cellHeight(), 7 * cellWidth(), 5 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		horizontalLine = new GLine(8 * cellWidth(), 6 * cellHeight(), 7 * cellWidth(), 6 * cellHeight());
		horizontalLine.setColor(Color.YELLOW);
		horizontalLine.setLineWidth(3);
		program.add(horizontalLine);
		lines.add(horizontalLine);
		
		verticalLine = new GLine(8 * cellWidth(), 5 * cellHeight(), 8 * cellWidth(), 4 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		horizontalLine = new GLine(10 * cellWidth(), 2 * cellHeight(), 9 * cellWidth(), 2 * cellHeight());
		horizontalLine.setColor(Color.YELLOW);
		horizontalLine.setLineWidth(3);
		program.add(horizontalLine);
		lines.add(horizontalLine);
		
		horizontalLine = new GLine(10 * cellWidth(), 4 * cellHeight(), 9 * cellWidth(), 4 * cellHeight());
		horizontalLine.setColor(Color.YELLOW);
		horizontalLine.setLineWidth(3);
		program.add(horizontalLine);
		lines.add(horizontalLine);
		
		verticalLine = new GLine(10 * cellWidth(), 4 * cellHeight(), 10 * cellWidth(), 2 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		verticalLine = new GLine(9 * cellWidth(), 4 * cellHeight(), 9 * cellWidth(), 2 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		verticalLine = new GLine(7 * cellWidth(), 4 * cellHeight(), 7 * cellWidth(), 2 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		horizontalLine = new GLine(8 * cellWidth(), 3 * cellHeight(), 7 * cellWidth(), 3 * cellHeight());
		horizontalLine.setColor(Color.YELLOW);
		horizontalLine.setLineWidth(3);
		program.add(horizontalLine);
		lines.add(horizontalLine);
		
		verticalLine = new GLine(6 * cellWidth(), 3 * cellHeight(), 6 * cellWidth(), 1 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		verticalLine = new GLine(7 * cellWidth(), 11 * cellHeight(), 7 * cellWidth(), 9 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		horizontalLine = new GLine(5 * cellWidth(), 4 * cellHeight(), 6 * cellWidth(), 4 * cellHeight());
		horizontalLine.setColor(Color.YELLOW);
		horizontalLine.setLineWidth(3);
		program.add(horizontalLine);
		lines.add(horizontalLine);
		
		horizontalLine = new GLine(7 * cellWidth(), 8 * cellHeight(), 6 * cellWidth(), 8 * cellHeight());
		horizontalLine.setColor(Color.YELLOW);
		horizontalLine.setLineWidth(3);
		program.add(horizontalLine);
		lines.add(horizontalLine);
		
		verticalLine = new GLine(6 * cellWidth(), 4 * cellHeight(), 6 * cellWidth(), 8 * cellHeight());
		verticalLine.setColor(Color.YELLOW);
		verticalLine.setLineWidth(3);
		program.add(verticalLine);
		lines.add(verticalLine);
		
		GLine topGateLine = new GLine(5 * cellWidth(), 1 * cellHeight() - 3, 6 * cellWidth(), 1 * cellHeight() - 3);
		topGateLine.setVisible(false);
		topGateLine.setLineWidth(3);
		program.add(topGateLine);
		lines.add(topGateLine);
		
		GLine bottomGateLine = new GLine(6 * cellWidth(), 11 * cellHeight() + 30, 7 * cellWidth(), 11 * cellHeight() + 30);
		bottomGateLine.setVisible(false);
		bottomGateLine.setLineWidth(3);
		program.add(bottomGateLine);
		lines.add(bottomGateLine);
		
	}

	@Override
    public synchronized void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            program.switchToPause();
        }

        if (!pressedKeys.isEmpty()) {
            PlayerCharacter temp = mainCharacter;
            temp.player.setSize(30.0, 30.0);

            for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext();) {
                GRectangle playerBounds = mainCharacter.player.getBounds();
                GRectangle bug1 = one.monsterIMG.getBounds();
                GRectangle bug2 = two.monsterIMG.getBounds();
                GRectangle bug3 = three.monsterIMG.getBounds();

            	switch (it.next()) {
                    case KeyEvent.VK_UP:
                        temp.moveMaze(Direction.UP, 12, 12);
                        System.out.println(lines.size());
                        if (playerBounds.intersects(bug1) ) {
                        	program.remove(one.monsterIMG);
                        	one.setDX(10000);
                        	program.add(one.monsterIMG, one.getDX(), one.getDY());
                        	bug1 = one.monsterIMG.getBounds();
                        	program.playAracadeSound();
                        	totalSaveBugs++;
                        }
                        if (playerBounds.intersects(bug2) ) {
                        	program.remove(two.monsterIMG);
                        	two.setDX(10000);
                        	program.add(two.monsterIMG, two.getDX(), two.getDY());
                        	two.monsterIMG.getBounds();
                        	program.playAracadeSound();
                        	totalSaveBugs++;
                        }
                        if (playerBounds.intersects(bug3) ) {
                        	program.remove(three.monsterIMG);
                        	three.setDX(10000);
                        	program.add(three.monsterIMG, three.getDX(), three.getDY());
                        	 three.monsterIMG.getBounds();
                        	program.playAracadeSound();
                        	totalSaveBugs++;
                        }
                        // Check for collisions with GLine objects
                        boolean collisionDetected = checkCollision(temp.player, lines);

                        if (collisionDetected) {
                            temp.moveMaze(Direction.DOWN, 12, 12);
                            temp.updateSprite(Direction.UP);
                        }

                        mainCharacter = temp;
                        break;

                    case KeyEvent.VK_LEFT:
                        temp.moveMaze(Direction.LEFT, 12, 12);
                        System.out.println(lines.size());
                        if (playerBounds.intersects(bug1) ) {
                        	program.remove(one.monsterIMG);
                        	one.setDX(10000);
                        	program.add(one.monsterIMG, one.getDX(), one.getDY());
                        	bug1 = one.monsterIMG.getBounds();
                        	program.playAracadeSound();
                        	totalSaveBugs++;
                        }
                        if (playerBounds.intersects(bug2) ) {
                        	program.remove(two.monsterIMG);
                        	two.setDX(10000);
                        	program.add(two.monsterIMG, two.getDX(), two.getDY());
                        	two.monsterIMG.getBounds();
                        	program.playAracadeSound();
                        	totalSaveBugs++;
                        }
                        if (playerBounds.intersects(bug3) ) {
                        	program.remove(three.monsterIMG);
                        	three.setDX(10000);
                        	program.add(three.monsterIMG, three.getDX(), three.getDY());
                        	 three.monsterIMG.getBounds();
                        	program.playAracadeSound();
                        	totalSaveBugs++;
                        }
                        // Check for collisions with GLine objects
                        collisionDetected = checkCollision(temp.player, lines);

                        if (collisionDetected) {
                            temp.moveMaze(Direction.RIGHT, 12, 12);
                            temp.updateSprite(Direction.LEFT);
                        } 

                        mainCharacter = temp;
                        break;

                    case KeyEvent.VK_DOWN:
                        temp.moveMaze(Direction.DOWN, 12, 12);
                        System.out.println(lines.size());
                        if (playerBounds.intersects(bug1) ) {
                        	program.remove(one.monsterIMG);
                        	one.setDX(10000);
                        	program.add(one.monsterIMG, one.getDX(), one.getDY());
                        	bug1 = one.monsterIMG.getBounds();
                        	program.playAracadeSound();
                        	totalSaveBugs++;
                        }
                        if (playerBounds.intersects(bug2) ) {
                        	program.remove(two.monsterIMG);
                        	two.setDX(10000);
                        	program.add(two.monsterIMG, two.getDX(), two.getDY());
                        	two.monsterIMG.getBounds();
                        	program.playAracadeSound();
                        	totalSaveBugs++;
                        }
                        if (playerBounds.intersects(bug3) ) {
                        	program.remove(three.monsterIMG);
                        	three.setDX(10000);
                        	program.add(three.monsterIMG, three.getDX(), three.getDY());
                        	 three.monsterIMG.getBounds();
                        	program.playAracadeSound();
                        	totalSaveBugs++;
                        }
                        // Check for collisions with GLine objects
                        collisionDetected = checkCollision(temp.player, lines);

                        if (collisionDetected) {
                            temp.moveMaze(Direction.UP, 12, 12);
                            temp.updateSprite(Direction.DOWN);
                        }
                        
                        
                        
                        if(playerBounds.intersects(bottomDoorBounds)) {
                        	if(totalSaveBugs >= 3) {
                        		base.setCurrentPhase(4);
                        		program.setPacmanState(false);
                        		program.switchToBottomRoom();

                        		overlay.removeTask(3);
                            	overlay.addTask(10);
                        	}
                        }

                        mainCharacter = temp;
                        break;

                    case KeyEvent.VK_RIGHT:
                        temp.moveMaze(Direction.RIGHT, 12, 12);
                        System.out.println(lines.size());
                        if (playerBounds.intersects(bug1) ) {
                        	program.remove(one.monsterIMG);
                        	one.setDX(10000);
                        	program.add(one.monsterIMG, one.getDX(), one.getDY());
                        	bug1 = one.monsterIMG.getBounds();
                        	program.playAracadeSound();
                        	totalSaveBugs++;
                        }
                        if (playerBounds.intersects(bug2) ) {
                        	program.remove(two.monsterIMG);
                        	two.setDX(10000);
                        	program.add(two.monsterIMG, two.getDX(), two.getDY());
                        	two.monsterIMG.getBounds();
                        	program.playAracadeSound();
                        	totalSaveBugs++;
                        }
                        if (playerBounds.intersects(bug3) ) {
                        	program.remove(three.monsterIMG);
                        	three.setDX(10000);
                        	program.add(three.monsterIMG, three.getDX(), three.getDY());
                        	 three.monsterIMG.getBounds();
                        	program.playAracadeSound();
                        	totalSaveBugs++;
                        }
                        // Check for collisions with GLine objects
                        collisionDetected = checkCollision(temp.player, lines);

                        if (collisionDetected) {
                            temp.moveMaze(Direction.LEFT, 12, 12);
                            temp.updateSprite(Direction.RIGHT);
                        }

                        mainCharacter = temp;
                        break;
                }
            }

            mainCharacter.move(directionResolver(offset), 12, 12);
        } else {
            // Handle other key presses
            char keyChar = e.getKeyChar();
            System.out.println("Key pressed: " + keyChar);
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
            }
            else if (code == KeyEvent.VK_LEFT) {
                    offset.x = 0;
                    System.out.println("Left offset reset");
            }
            else if (code == KeyEvent.VK_DOWN) {
                    offset.y = 0;
                    System.out.println("Down offset reset.");
            }
            else if (code == KeyEvent.VK_RIGHT) {
                    offset.x = 0;
                    System.out.println("Right offset reset.");
            }
		pressedKeys.remove(e.getKeyCode());
	}
	@Override
	public void showContents() {
		buildMaze();
		drawMainCharacter();
		drawMonsterCharacters();
	}

	@Override
	public void hideContents() {
		program.removeAll();
	}
	
}