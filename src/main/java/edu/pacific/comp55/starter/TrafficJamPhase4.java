package edu.pacific.comp55.starter;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GLine;
import acm.graphics.GObject;

public class TrafficJamPhase4 extends GraphicsPane {

	public static final String lABEL_FONT = "Arial-Bold-22";
	public static final String EXIT_SIGN = "EXIT";
	public static final String IMG_FILENAME_PATH = "images/";
	public static final String IMG_EXTENSION = ".png";
	public static final String VERTICAL_IMG_FILENAME = "_vert";
	public static final String WINNINGTILENOTDETECTED = "WinningTileNotDetected.png";
	public static final String BACKBUTTON = "BackButton.png";
	private TrafficJamLevel level;
	public int startX;
	public int startY;
	public int lastX;
	public int lastY;
	public int moveX;
	public int moveY;
	GImage backgroundImage;
	BaseRoom base;
	Overlay overlay;
	private GObject obj;
	private MainApplication program;

	public TrafficJamPhase4(MainApplication app) {
		super();
		program = app;
		this.overlay = program.getOverlay();
		this.base = app.getBase();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		startX = e.getX();
		startY = e.getY();
		moveX = e.getX();
		moveY = e.getY();
		obj = program.getElementAt(startX, startY);
		System.out.println(level.getVehicle(convertXYToLocation(startX, startY)));
		System.out.println(convertXYToLocation(startX, startY));
		if (!(obj instanceof GImage)) {
			obj = null;
			return;
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ESCAPE) {
			program.switchToBaseRoom();

		}

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (obj != null) {
			int deltaY = e.getY() - moveY;
			int deltaX = e.getX() - moveX;

			obj.move(deltaX, deltaY);
			moveX = e.getX();
			moveY = e.getY();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		lastX = e.getX();
		lastY = e.getY();
		System.out.println(calculateNumSpacesMoved());
		Location end = convertXYToLocation(startX, startY);

		int resetX = end.getCol();
		int resetY = end.getRow();

		if (level.getVehicle(end) != null) {
			resetX = getVehicleFromXY(startX, startY).getStartCol() * (int) cellWidth();
			resetY = getVehicleFromXY(startX, startY).getStartRow() * (int) cellHeight();

		}

		if (level.moveVehicleOn(end, calculateNumSpacesMoved())) {
			System.out.println();
			program.removeAll();
			drawLevel();
		}

		else {
			if (obj != null) {
				obj.setLocation(resetX, resetY);
			}
		}

		level.getWinLocation();
		if (level.passedLevel()) {
			System.out.println("Congrats");
			overlay.stopTimer();
			overlay.addtoLeaders();
			program.switchToCredit();
		}

	}

	private void drawLevel() {
		drawGridLines();
		drawCars();
		drawWinningTile();

	}

	private void drawWinningTile() {
		level.getWinLocation();
		GLabel exitLabel = new GLabel(EXIT_SIGN, level.getColumns() * cellWidth() - cellWidth(),
				cellHeight() * 3 - (cellHeight() / 2));
		exitLabel.setFont(lABEL_FONT);
		exitLabel.setColor(Color.red);
		program.add(exitLabel);
	}

	private void drawGridLines() {
		for (int row = 0; row <= level.getRows(); row++) {
			double y = row * cellHeight();
			GLine horizontalLine = new GLine(0, y, 500, y); // Create a horizontal line
			program.add(horizontalLine); // Add the line to the canvas
		}

		for (int col = 0; col <= level.getColumns(); col++) {
			double x = col * cellWidth();
			GLine verticalLine = new GLine(x, 0, x, 500); // Create a vertical line
			program.add(verticalLine); // Add the line to the canvas
		}
	}

	private void drawCars() {
		ArrayList<TrafficJamVehicle> vehicles = new ArrayList<TrafficJamVehicle>();
		for (int row = 0; row < level.getRows(); row++) {
			for (int col = 0; col < level.getColumns(); col++) {
				Location location = new Location(row, col);
				TrafficJamVehicle vehicle = level.getVehicle(location);

				if (vehicle != null && !vehicles.contains(vehicle)) {
					vehicles.add(vehicle);
				}
			}
		}
		for (TrafficJamVehicle vehicle : vehicles) {
			drawCar(vehicle);
		}
	}

	private void drawCar(TrafficJamVehicle v) {
		if (v.isVertical()) {
			GImage vertCar = new GImage(IMG_FILENAME_PATH + v.getVehicleType() + VERTICAL_IMG_FILENAME + IMG_EXTENSION);
			vertCar.setSize(cellWidth(), cellHeight() * v.getLength());
			double x = v.getStartCol() * cellWidth();
			double y = v.getStartRow() * cellHeight();
			program.add(vertCar, x, y);
		} else {
			GImage car = new GImage(IMG_FILENAME_PATH + v.getVehicleType() + IMG_EXTENSION);
			car.setSize(cellWidth() * v.getLength(), cellHeight());
			double x = v.getStartCol() * cellWidth();
			double y = v.getStartRow() * cellHeight();
			program.add(car, x, y);
		}
	}

	private TrafficJamVehicle getVehicleFromXY(double x, double y) {
		convertXYToLocation(x, y);
		return level.getVehicle(convertXYToLocation(x, y));
	}

	private int calculateNumSpacesMoved() {
		int numSpacesMoved = 0;
		Location initialPosition = convertXYToLocation(startX, startY);
		Location finalPosition = convertXYToLocation(lastX, lastY);
		level.getVehicle(initialPosition);

		if (initialPosition.getCol() == finalPosition.getCol()) {
			numSpacesMoved = finalPosition.getRow() - initialPosition.getRow();
		} else if (initialPosition.getRow() == finalPosition.getRow()) {
			numSpacesMoved = finalPosition.getCol() - initialPosition.getCol();
		} else {
			numSpacesMoved = 0;
		}
		return numSpacesMoved;
	}

	private Location convertXYToLocation(double x, double y) {
		int numRows = level.getRows();
		int numCols = level.getColumns();
		double width = cellWidth();
		double height = cellHeight();
		int i;
		int j;

		for (i = 0; i < numRows; i++) {
			if (x < height) {
				x = i;
				break;
			} else {
				height += cellHeight();
			}
		}

		for (j = 0; j < numCols; j++) {
			if (y < width) {
				y = j;
				break;
			} else {
				width += cellWidth();
			}
		}

		Location pressed = new Location((int) y, (int) x);
		System.out.println(y + "," + x);
		return pressed;
	}

	private double cellWidth() {
		double cellWidth = (double) 500 / 6;
		return cellWidth;
	}

	private double cellHeight() {
		double cellHeight = (double) 500 / 6;
		return cellHeight;
	}

	@Override
	public void showContents() {
		// TODO Auto-generated method stub
		level = new TrafficJamLevel(6, 6);
		drawLevel();
	}

	@Override
	public void hideContents() {
		program.removeAll();
		// TODO Auto-generated method stub

	}
}
