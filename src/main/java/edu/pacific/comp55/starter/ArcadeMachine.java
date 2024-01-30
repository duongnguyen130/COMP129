package edu.pacific.comp55.starter;

import acm.graphics.GImage;


public class ArcadeMachine implements Character {
	private int startRow;
	private int startCol;
	Location currLocation;
	public GImage machine;
	
	public static final String ARCADE_MACHINE = "ArcadeMachine.png";
	public static final String FLIPPED_ARCADE_MACHINE = "FlippedArcadeMachine.png";

//	cell width 72.72727272727273
//	cell height 51.8181818181818
	
	public void move(Direction d) {
		
	}
	
	
	public ArcadeMachine(int startRow, int startCol, String name) {
		this.startRow = startRow;
		this.startCol = startCol;
		if (name == "flipped") {
		machine = new GImage(FLIPPED_ARCADE_MACHINE);

		}
		else {
		machine = new GImage(ARCADE_MACHINE);
		}
		currLocation = new Location (startRow, startCol);
	}
	
	public int getStartRow() {
		return startRow;
	}
	public int getStartCol() {
		return startCol;
	}
	@Override
	public void Character(String name) {
	}


	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void move(Direction d, double row, double cols) {
		// TODO Auto-generated method stub
		
	}
	

}

