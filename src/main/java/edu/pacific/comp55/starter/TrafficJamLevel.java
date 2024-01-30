package edu.pacific.comp55.starter;

import java.util.ArrayList;

public class TrafficJamLevel {


		private TrafficJamBoard board;
		private Location winLocation;
		private int numMoves;
		private int nCols;
		private int nRows;
		private ArrayList<TrafficJamVehicle> list = new ArrayList<TrafficJamVehicle>();
		public TrafficJamLevel(int nRows, int nCols) {
			this.nCols = nCols;
			this.nRows  =nRows;
			this.board = new TrafficJamBoard(nRows, nCols);
			this.numMoves = 0;
			setUpLevel(nRows, nCols);
		}
		public int getColumns() {
			return this.nCols;
		}
		public int getRows() {
			return this.nRows;
		}
		public int getNumMoves() {
			return numMoves;
		}
		public int incrementMoves() {
			return ++numMoves;
		}
		public void setUpLevel(int maxRows, int maxCols) {
			this.board = new TrafficJamBoard(maxRows, maxCols);
			board.addVehicle(VehicleType.MYCAR, 2, 0, false, 2);
			board.addVehicle(VehicleType.TRUCK, 0, 2, true, 3);
			board.addVehicle(VehicleType.AUTO, 3, 3, true, 2);
			board.addVehicle(VehicleType.AUTO, 4, 4, false, 2);
			board.addVehicle(VehicleType.AUTO, 0, 4, false, 2);
			board.addVehicle(VehicleType.AUTO, 1, 4, true, 2);
			board.addVehicle(VehicleType.AUTO, 1, 5, true, 2);
					
			this.winLocation = getWinLocation();
			this.numMoves = 0;
			    
		}
		
		public Location getWinLocation() {

			this.winLocation = new Location(2, getColumns()-1);
			

			return winLocation;
		}
		public TrafficJamVehicle getVehicle(Location s) {
			return board.getVehicleAt(s);
			}
		public boolean moveVehicleOn(Location loc, int numSpaces) {
			incrementMoves();
			return board.moveVehicleAt(loc, numSpaces);
		}
		public boolean passedLevel() {
			if (getVehicle(winLocation) != null) {
				if(getVehicle(winLocation).getVehicleType() == VehicleType.MYCAR) {
					return true;
				}
			}
			else {
				return false;
			}
			return false;
		}

		public ArrayList<TrafficJamVehicle> allVehicles(){
			for (int row = 0; row < nRows; row++) {
				for (int col = 0; col < nCols; col++) {
					Location holder = new Location(row,col);
					if (holder != null) {
						list.add(getVehicle(holder));
					}
				}
			} 
			for(int i = 0; i < list.size(); i++) {
				for (int z = 0; z < list.size(); z++) {
					if(list.get(i) == list.get(z)) {
						list.remove(z);
					}
				}
			}
			return list;
		}

			public ArrayList<TrafficJamVehicle> getAllVehicles(){
				return list;
			}
		public String toString() {
			String result = generateColHeader(getColumns());
			result+=addRowHeader(board.toString());
			return result;
		}
		private String addRowHeader(String origBoard) {
			String result = "";
			String[] elems = origBoard.split("\n");
			for(int i = 0; i < elems.length; i++) {
				result += (char)('A' + i) + "|" + elems[i] + "\n"; 
			}
			return result;
		}
		private String generateColHeader(int cols) {
			String result = "  ";
			for(int i = 1; i <= cols; i++) {
				result+=i;
			}
			result+="\n  ";
			for(int i = 0; i < cols; i++) {
				result+="-";
			}
			result+="\n";
			return result;
		}
	}


