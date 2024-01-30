package edu.pacific.comp55.starter;
import java.util.*;

import javax.sql.RowSetEvent;

public class TrafficJamBoard {
		TrafficJamVehicle[][] grid;
		private int numRows;
		private int numCols;

		public TrafficJamBoard(int rows, int cols) {
			numRows = rows;
			numCols = cols;
			grid = new TrafficJamVehicle[rows][cols];
		}
		
		public int getNumCols() {
			return numCols;
		}

		public int getNumRows() {
			return numRows;
		}
		

		public TrafficJamVehicle getVehicleAt(Location s) {
		if (s == null) {
			return null;
		}
			int row = s.getRow();
			int col = s.getCol();
			if (row >= 0 && row < numRows && col < numCols && col >= 0) {
			return grid[row][col];
			}
			else {
				return null;
			}
		}
		public void addVehicle(VehicleType type, int startRow, int startCol, boolean vert, int length) {
			TrafficJamVehicle v = new TrafficJamVehicle(type, startRow, startCol, vert, length);
		    Location[] t = v.locationsOn();
		    
		    for (int i = 0; i < length; i++) {
		        int row = t[i].getRow();
		        int col = t[i].getCol();
		        
		        if (startRow >= 0 && startRow < getNumRows() && startCol >= 0 && startCol < getNumCols()) {
		            if (vert) {
		                grid[row][startCol] = v;
		            } else {
		                grid[startRow][col] = v;
		            }
		        }
		    }
		}



		public boolean moveVehicleAt(Location start, int numSpaces) {
			if (start == null) {
				return false;
			}
			TrafficJamVehicle v = getVehicleAt(start);	
			if((v != null) && (canMoveAVehicleAt(start,numSpaces))) {
				Location[] current = v.locationsOn();
				 for (int i = 0; i < current.length; i++) {
					 if (current[i] != null) {
			            grid[current[i].getRow()][current[i].getCol()] = null;
			        }
				 }
				v.move(numSpaces);
		        Location[] update = v.locationsOn();
		        for (int i = 0; i < update.length; i++) {
		        	if (current[i] != null) {
		            grid[update[i].getRow()][update[i].getCol()] = v;
		        	}
		        }
				return true;
			}
			return false;
		}
		
		public boolean canMoveAVehicleAt(Location start, int numSpaces) {
			TrafficJamVehicle v = getVehicleAt(start);

			if (v == null) {
			return false;
			}
			Location [] t = v.locationsPath(numSpaces);
			System.out.println(Arrays.toString(t));

			for (Location loc: t) {
				TrafficJamVehicle check = getVehicleAt(loc);			
				if (check != null && check.hashCode() != v.hashCode()) {
					return false;
				}
				if (loc != null && ((loc.getRow() >= getNumRows()) || (loc.getRow() < 0))){
					return false;
				}
				if (loc != null && ((loc.getCol()  >= getNumCols()) || (loc.getCol() < 0))){
					return false;
				} 
			}


			return true;
		}

		public static void main(String[] args) {
			TrafficJamBoard b = new TrafficJamBoard(5, 5);
			b.addVehicle(VehicleType.MYCAR, 1, 0, false, 2);
			b.addVehicle(VehicleType.TRUCK, 0, 2, true, 3);
			b.addVehicle(VehicleType.AUTO, 3, 3, true, 2);
			b.addVehicle(VehicleType.AUTO, 0, 3, true, 2);
			System.out.println(b);
			testCanMove(b);
			testMoving(b);
			System.out.println(b);
		}
		
		public static void testMoving(TrafficJamBoard b) {
			System.out.println("just moving some stuff around");
			b.moveVehicleAt(new Location(1, 2), 1);
			b.moveVehicleAt(new Location(1, 2), 1);
			b.moveVehicleAt(new Location(1, 1), 1);
		}
		
		public static void testCanMove(TrafficJamBoard b) {
			System.out.println("Ok, now testing some moves...");
			System.out.println("These should all be true");
			System.out.println("Moving truck down " + b.canMoveAVehicleAt(new Location(0, 2), 2));
			System.out.println("Moving truck down " + b.canMoveAVehicleAt(new Location(1, 2), 2));
			System.out.println("Moving truck down " + b.canMoveAVehicleAt(new Location(2, 2), 2));
			System.out.println("Moving lower auto up " + b.canMoveAVehicleAt(new Location(3, 3), -1));
			System.out.println("Moving lower auto up " + b.canMoveAVehicleAt(new Location(4, 3), -1));
			
			System.out.println("\nAnd these should all be false");
			System.out.println("Moving truck down " + b.canMoveAVehicleAt(new Location(3, 2), 2));
			System.out.println("Moving the car into truck " + b.canMoveAVehicleAt(new Location(1, 0), 1));
			System.out.println("Moving the car into truck " + b.canMoveAVehicleAt(new Location(1, 0), 2));
			System.out.println("Moving nothing at all " + b.canMoveAVehicleAt(new Location(4, 4), -1));
			System.out.println("Moving lower auto up " + b.canMoveAVehicleAt(new Location(3, 3), -2));
			System.out.println("Moving lower auto up " + b.canMoveAVehicleAt(new Location(4, 3), -2));
			System.out.println("Moving upper auto up " + b.canMoveAVehicleAt(new Location(0, 3), -1));
			System.out.println("Moving upper auto up " + b.canMoveAVehicleAt(new Location(1, 3), -1));
		}
	}


