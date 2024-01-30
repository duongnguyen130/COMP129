package edu.pacific.comp55.starter;

public class TrafficJamVehicle {

	private VehicleType vehicleType;
	private int startRow;
	private int startCol;
	private int length;
	private boolean isVertical;
		
	public TrafficJamVehicle (VehicleType vehicleType, int startRow, int startCol, boolean isVertical, int length) {
		this.vehicleType = vehicleType;
		this.startRow = startRow;
		this.startCol = startCol;
		this.isVertical = isVertical;
		this.length = length;
	}

		public VehicleType getVehicleType() {
			return this.vehicleType;
		}
		public void setVehicleType(VehicleType type) {
			this.vehicleType = type;
		}
		public Location[] locationsOn() {
			
			Location loc[] = new Location[this.getLength() + 1];
			
			for(int i = 0; i < this.getLength(); i++) {
				if(this.isVertical) {
					loc[i] = new Location((this.startRow + i), (this.startCol));
				}
				else {
					loc[i] = new Location((this.startRow),(this.startCol + i)); 
				}
			}
			return loc;
		}
		public void move(int numSpaces) {
			if (this.isVertical) {
				this.startRow += numSpaces;
			}
			else {
				this.startCol += numSpaces;
			}
		}
		public Location potentialMove(int numSpaces) {
			int newRow;
			int newCol;
			
			if (this.isVertical) {
				newRow = this.startRow + numSpaces;
				newCol = this.startCol;
			}
			else {
				newRow = this.startRow;
				newCol = this.startCol + numSpaces;
			}
			
			Location loc = new Location(newRow, newCol); {
				return loc;
			}
			
		}

		public Location[] locationsPath(int numSpaces) {
		    Location[] trail = new Location[Math.abs(numSpaces)];
		    int newRow = this.startRow;
		    int newCol = this.startCol;

		    for (int i = 0; i < Math.abs(numSpaces); i++) {
		        if (this.isVertical) {
		            newRow += (numSpaces > 0) ? 1 : -1;
		        } else {
		            newCol += (numSpaces > 0) ? 1 : -1;
		        }
		        trail[i] = new Location(newRow, newCol);
		    }

		    return trail;
		}

		public int getStartRow() {
			return startRow;
		}
		public void setStartRow(int startRow) {
			this.startRow = startRow;
		}
		public int getStartCol() {
			return startCol;
		}
		public void setStartCol(int startCol) {
			this.startCol = startCol;
		}
		public int getLength() {
			return length;
		}
		public void setLength(int length) {
			this.length = length;
		}
		public boolean isVertical() {
			return isVertical;
		}
		public void setVertical(boolean isVertical) {
			this.isVertical = isVertical;
		}

		public String toString(){
	        if(isVertical()){
	            return "Vehicle Type: " + this.getVehicleType().toString() + "\nStart Row: " + this.getStartRow() + "\nStart Column: " + this.getStartCol() + "\nLength: " + this.getLength() + "\nIs " + this.getVehicleType().toString() + " vertical?: Yes";
	        }
	        else{
	            return "Vehicle Type: " + this.getVehicleType().toString() + "\nStart Row: " + this.getStartRow() + "\nStart Column: " + this.getStartCol() + "\nLength: " + this.getLength() + "\nIs " + this.getVehicleType().toString() + " vertical?: No";
	        }   
	    }
	public static void main(String arr[]) {
		TrafficJamVehicle someTruck = new TrafficJamVehicle(VehicleType.TRUCK, 1, 1, true, 3);
		TrafficJamVehicle someAuto = new TrafficJamVehicle(VehicleType.AUTO, 2, 2, false, 2);
		System.out.println("This next test is for locationsOn: ");
		System.out.println("vert truck at r1c1 should give you r1c1; r2c1; r3c1 as the locations its on top of...does it?");
		printLocations(someTruck.locationsOn());
		System.out.println("horiz auto at r2c2 should give you r2c2; r2c3 as the locations its on top of...does it?");
		printLocations(someAuto.locationsOn());
		System.out.println("if we were to move horiz auto -2 it should give you at least r2c0; r2c1; it may also add r2c2; r2c3 to its answer...does it?");
		printLocations(someAuto.locationsPath(2));
	}
	public static void printLocations(Location[] arr) {
		for(int i = 0; i < arr.length; i++) {
			if(arr[i] != null) {
			System.out.print("r" + arr[i].getRow() + "c" + arr[i].getCol() + "; ");
		}
		}
			System.out.println();
		}

	

}
