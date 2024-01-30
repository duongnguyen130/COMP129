package edu.pacific.comp55.starter;

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
import acm.graphics.*;

public class MazeGraphic extends GraphicsPane {
	private MainApplication program;
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	private static final int MAZE_ROWS = 10;
	private static final int MAZE_COLS = 10;
	private Direction currDir;

	public static final String EXIT_SIGN = "EXIT";
	public static final String lABEL_FONT = "Arial-Bold-22";
	private Board maze;
	private PlayerCharacter mainCharacter = new PlayerCharacter(1, 5, 0, 0, "Sebastian");
	private ArrayList<GLine> lines = new ArrayList<>();
	private GLine verticalLine;
	private GLine horizontalLine;
	private GLabel exitLabel;
	private BaseRoom base;
	private Overlay overlay;
	int keyCode;
	private final Set<Integer> pressedKeys = new HashSet<>();
	Point offset = new Point();
	private Timer animationTimer;

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

	public MazeGraphic(MainApplication app) {
		super();
		program = app;
		maze = new Board(12, 12);
		base = program.getBase();
		overlay = program.getOverlay();

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

	public void actionPerformed(ActionEvent e) {
	}

	private double cellWidth() {
		double cellWidth = (double) WINDOW_WIDTH / maze.getNumCols();
		return cellWidth;
	}

	private double cellHeight() {

		double cellHeight = (double) WINDOW_HEIGHT / maze.getNumCols();
		return cellHeight;
	}

	private void drawMainCharacter() {
		mainCharacter.setDX(mainCharacter.getStartCol() * cellWidth());
		mainCharacter.setDY(mainCharacter.getStartRow() * cellWidth());
		mainCharacter.player.setSize(30.0, 30.0);
		program.add(mainCharacter.player, mainCharacter.getStartCol() * cellWidth(),
				mainCharacter.getStartRow() * cellHeight());
	}

	private void drawWinningTile() {
		exitLabel = new GLabel(EXIT_SIGN, cellWidth() * 6.1, cellHeight() * 10.8);
		exitLabel.setFont(lABEL_FONT);
		exitLabel.setColor(Color.red);
		program.add(exitLabel);
	}

	private void buildMaze() {
		GImage mazeArt = new GImage("Maze_Art2.png");
		mazeArt.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		program.add(mazeArt);

		GLine topWall = new GLine(1 * cellWidth(), 1 * cellHeight(), 5 * cellWidth(), 1 * cellHeight());
		topWall.setColor(Color.BLACK);
		program.add(topWall);
		lines.add(topWall);

		topWall = new GLine(6 * cellWidth(), 1 * cellHeight(), (maze.getNumCols() - 1) * cellWidth(), 1 * cellHeight());
		topWall.setColor(Color.BLACK);
		program.add(topWall);
		lines.add(topWall);

		GLine rightWall = new GLine((maze.getNumCols() - 1) * cellWidth(), 1 * cellHeight(),
				(maze.getNumCols() - 1) * cellWidth(), (maze.getNumRows() - 1) * cellHeight());
		rightWall.setColor(Color.BLACK);
		program.add(rightWall);
		lines.add(rightWall);

		GLine bottomWall = new GLine(1 * cellWidth(), (maze.getNumRows() - 1) * cellHeight(), 6 * cellWidth(),
				(maze.getNumRows() - 1) * cellHeight());
		bottomWall.setColor(Color.BLACK);
		program.add(bottomWall);
		lines.add(bottomWall);

		bottomWall = new GLine(7 * cellWidth(), (maze.getNumRows() - 1) * cellHeight(),
				(maze.getNumCols() - 1) * cellWidth(), (maze.getNumRows() - 1) * cellHeight());
		bottomWall.setColor(Color.BLACK);
		program.add(bottomWall);
		lines.add(bottomWall);

		GLine leftWall = new GLine(1 * cellWidth(), 1 * cellHeight(), 1 * cellWidth(),
				(maze.getNumRows() - 1) * cellHeight());
		leftWall.setColor(Color.BLACK);
		program.add(leftWall);
		lines.add(leftWall);

		verticalLine = new GLine(3 * cellWidth(), 1 * cellHeight(), 3 * cellWidth(), 2 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(10 * cellWidth(), 1 * cellHeight(), 10 * cellWidth(), 3 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(6 * cellWidth(), 1 * cellHeight(), 6 * cellWidth(), 2 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(2 * cellWidth(), 2 * cellHeight(), 2 * cellWidth(), 5 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(4 * cellWidth(), 2 * cellHeight(), 4 * cellWidth(), 5 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(7 * cellWidth(), 2 * cellHeight(), 7 * cellWidth(), 5 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(9 * cellWidth(), 2 * cellHeight(), 9 * cellWidth(), 8 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(5 * cellWidth(), 3 * cellHeight(), 5 * cellWidth(), 6 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(5 * cellWidth(), 3 * cellHeight(), 5 * cellWidth(), 4 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		horizontalLine = new GLine(4 * cellWidth(), 2 * cellHeight(), 5 * cellWidth(), 2 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(8 * cellWidth(), 2 * cellHeight(), 9 * cellWidth(), 2 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(2 * cellWidth(), 3 * cellHeight(), 4 * cellWidth(), 3 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(6 * cellWidth(), 3 * cellHeight(), 8 * cellWidth(), 3 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(2 * cellWidth(), 4 * cellHeight(), 3 * cellWidth(), 4 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(4 * cellWidth(), 5 * cellHeight(), 8 * cellWidth(), 5 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(8 * cellWidth(), 4 * cellHeight(), 9 * cellWidth(), 4 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(10 * cellWidth(), 4 * cellHeight(), 11 * cellWidth(), 4 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);

		verticalLine = new GLine(10 * cellWidth(), 4 * cellHeight(), 10 * cellWidth(), 5 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(6 * cellWidth(), 3 * cellHeight(), 6 * cellWidth(), 4 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		horizontalLine = new GLine(1 * cellWidth(), 6 * cellHeight(), 4 * cellWidth(), 6 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(7 * cellWidth(), 6 * cellHeight(), 9 * cellWidth(), 6 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(10 * cellWidth(), 6 * cellHeight(), 11 * cellWidth(), 6 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		verticalLine = new GLine(3 * cellWidth(), 5 * cellHeight(), 3 * cellWidth(), 6 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(6 * cellWidth(), 5 * cellHeight(), 6 * cellWidth(), 7 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(4 * cellWidth(), 6 * cellHeight(), 4 * cellWidth(), 7 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(2 * cellWidth(), 6 * cellHeight(), 2 * cellWidth(), 7 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		horizontalLine = new GLine(5 * cellWidth(), 7 * cellHeight(), 8 * cellWidth(), 7 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(9 * cellWidth(), 7 * cellHeight(), 10 * cellWidth(), 7 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		verticalLine = new GLine(3 * cellWidth(), 7 * cellHeight(), 3 * cellWidth(), 9 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(5 * cellWidth(), 7 * cellHeight(), 5 * cellWidth(), 10 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(6 * cellWidth(), 8 * cellHeight(), 6 * cellWidth(), 9 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		horizontalLine = new GLine(1 * cellWidth(), 8 * cellHeight(), 2 * cellWidth(), 8 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(3 * cellWidth(), 8 * cellHeight(), 5 * cellWidth(), 8 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(6 * cellWidth(), 8 * cellHeight(), 9 * cellWidth(), 8 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(10 * cellWidth(), 8 * cellHeight(), 11 * cellWidth(), 8 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(2 * cellWidth(), 9 * cellHeight(), 3 * cellWidth(), 9 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(6 * cellWidth(), 9 * cellHeight(), 8 * cellWidth(), 9 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(9 * cellWidth(), 9 * cellHeight(), 10 * cellWidth(), 9 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(1 * cellWidth(), 10 * cellHeight(), 2 * cellWidth(), 10 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(3 * cellWidth(), 10 * cellHeight(), 4 * cellWidth(), 10 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		horizontalLine = new GLine(5 * cellWidth(), 10 * cellHeight(), 7 * cellWidth(), 10 * cellHeight());
		horizontalLine.setColor(Color.BLACK);
		program.add(horizontalLine);
		lines.add(horizontalLine);

		verticalLine = new GLine(10 * cellWidth(), 8 * cellHeight(), 10 * cellWidth(), 10 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(9 * cellWidth(), 9 * cellHeight(), 9 * cellWidth(), 10 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(8 * cellWidth(), 9 * cellHeight(), 8 * cellWidth(), 11 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(3 * cellWidth(), 10 * cellHeight(), 3 * cellWidth(), 11 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(6 * cellWidth(), 10 * cellHeight(), 6 * cellWidth(), 11 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

		verticalLine = new GLine(4 * cellWidth(), 9 * cellHeight(), 4 * cellWidth(), 10 * cellHeight());
		verticalLine.setColor(Color.BLACK);
		program.add(verticalLine);
		lines.add(verticalLine);

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
				switch (it.next()) {
				case KeyEvent.VK_UP:
					temp.moveMaze(Direction.UP, 12, 12);
					System.out.println(lines.size());

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
					GRectangle playerBounds = mainCharacter.player.getBounds();
					GRectangle exitBounds = exitLabel.getBounds();

					if (playerBounds.intersects(exitBounds)) {
						pressedKeys.clear();
						program.setMazeState(false);
						offset = new Point(0, 0);
						base.setCurrentPhase(2);

						program.switchToRightRoom();
						overlay.removeTask(1);
						overlay.addTask(10);
					}
					// Check for collisions with GLine objects
					collisionDetected = checkCollision(temp.player, lines);

					if (collisionDetected) {
						temp.moveMaze(Direction.UP, 12, 12);
						temp.updateSprite(Direction.DOWN);
					}

					mainCharacter = temp;
					break;

				case KeyEvent.VK_RIGHT:
					temp.moveMaze(Direction.RIGHT, 12, 12);
					System.out.println(lines.size());

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
	public void showContents() {

		buildMaze();
		drawMainCharacter();
		drawWinningTile();

	}

	@Override
	public void hideContents() {
		offset.x = 0;
		offset.y = 0;
		program.removeAll();
	}
}