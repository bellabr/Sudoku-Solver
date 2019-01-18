package code;

public class node 
{
	int digit;
	int boxNumber, columnNumber, rowNumber;
	boolean[] potentialNumbers = new boolean[10];
	
	// constructor
	public node()
	{
		digit = 0;
		boxNumber = 0;
		columnNumber = 0;
		rowNumber = 0;
		
		for(int x = 0; x < 10; x++)
		{
			potentialNumbers[x] = true;
		}
	}

	// getters & setters
	public int getDigit() {
		return digit;
	}
	public void setDigit(int digit) {
		this.digit = digit;
	}
	public int getBoxNumber() {
		return boxNumber;
	}
	public void setBoxNumber(int boxNumber) {
		this.boxNumber = boxNumber;
	}
	public int getColumnNumber() {
		return columnNumber;
	}
	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}
	public int getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
	public boolean[] getPotentialNumbers() {
		return potentialNumbers;
	}
	public void setPotentialNumbers(boolean[] potentialNumbers) {
		this.potentialNumbers = potentialNumbers;
	}

}
