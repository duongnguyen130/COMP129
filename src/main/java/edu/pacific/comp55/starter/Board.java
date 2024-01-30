package edu.pacific.comp55.starter;

import java.util.ArrayList;

public class Board {
	
	private int numRows;
	private int numCols;
	private ArrayList<Character> addChar = new ArrayList<Character>();
	
	public Board(int rows, int cols) {
		this.numRows = rows;
		this.numCols = cols;
	}
	
	 public ArrayList<Character> getArrList() {
		 return addChar;
	 }
	

	public int getNumCols() {	
		return numCols;
	}
	
	public void setNumCols(int cols) {
		this.numCols = cols;
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public void setNumRows(int rows) {
		this.numRows = rows;
	}
	
	
	public static void main(String[] args) {
		new Board(20, 15);
	}
}