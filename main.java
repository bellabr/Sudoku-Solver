package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class main {
	
	public static node[][] grid = new node[9][9];
	public static node start = null;
	
	public static void main(String[] args) throws FileNotFoundException
	{
		File infile = new File("hard.txt");
		Scanner input = new Scanner(infile);
		
		// instantiate array and nodes within
		for(int x = 0; x < 9; x++) // which row (moves vertically)
		{
			for(int y = 0; y < 9; y++) // which column (moves horizontally)
			{
				grid[x][y] = new node();
				
				grid[x][y].setColumnNumber(y);
				grid[x][y].setRowNumber(x);
				
				if(y < 3 && x < 3)
					grid[x][y].setBoxNumber(1);
				else if(y < 6 && x < 3)
					grid[x][y].setBoxNumber(2);
				else if(x < 3)
					grid[x][y].setBoxNumber(3);
				else if(y < 3 && x < 6)
					grid[x][y].setBoxNumber(4);
				else if(y < 6 && x < 6)
					grid[x][y].setBoxNumber(5);
				else if(x < 6)
					grid[x][y].setBoxNumber(6);
				else if(y < 3)
					grid[x][y].setBoxNumber(7);
				else if(y < 6)
					grid[x][y].setBoxNumber(8);
				else
					grid[x][y].setBoxNumber(9);
			}
		}
		
		// set nodes to given digits from easy.txt
		for(int x = 0; x < 9; x++) // row
		{
			for(int y = 0; y < 9; y++) // column
			{
				if(input.hasNext())
				{
					grid[x][y].setDigit(Integer.parseInt(input.next()));
				}
			}
		}
		
		updatePotentialNumbers();
		
		display();
		System.out.println("");
		
		/*
		for(int x = 0; x < 9; x++)
		{
			for(int y = 0; y < 9; y++)
			{
				for(int i = 1; i < 10; i++)
				{
					System.out.print(grid[x][y].getPotentialNumbers()[i] + " ");
				}
				System.out.println("");
			}
			System.out.println("");
		}
		*/
		
		int counter = 0;
				
		while(!solved())
		{
			//one();
			two();
			//three();
			four();
			
			counter++;
			
			if(counter == 10)
				break;
		}
		
		guess(0, 0);
		
		display();
		input.close();
	}
	
	// if cell in r/c/b can only be a certain number, it is that number
	public static void one()
	{
		for(int x = 0; x < 9; x++)
		{
			for(int y = 0; y < 9; y++)
			{
				if(grid[x][y].getDigit() == 0)
				{ // if digit is zero
					for(int i = 0; i < 10; i++)
					{ // potential numbers
						if(grid[x][y].getPotentialNumbers()[i])
						{ // if i is a potential number
							if(isOnlyPotentialInRow(x, y, i) || isOnlyPotentialInColumn(x, y, i) || isOnlyPotentialInBox(grid[x][y].getBoxNumber(), x, y, i))
							{
								grid[x][y].setDigit(i);
								
								/*
								for(int ig = 1; ig < 10; ig++)
								{
									System.out.print(grid[x][y].getPotentialNumbers()[ig] + " ");
								}
								System.out.println("");
								
								System.out.println("x:" + x);
								System.out.println("y:" + y);
								
								System.out.println("row:" + isOnlyPotentialInRow(x, y, i));
								System.out.println("column:" + isOnlyPotentialInColumn(x, y, i));
								System.out.println("box:" + isOnlyPotentialInBox(grid[x][y].getBoxNumber(), x, y, i));
								*/
								
								updatePotentialNumbers();
							}
						}
					}
				}
			}
		}
	}
	
	// if cell has only one potential number, it must be that number
	public static void two()
	{
		for(int x = 0; x < 9; x++)
		{
			for(int y = 0; y < 9; y++)
			{
				if(grid[x][y].getDigit() == 0 && numberOfPotentialNumbers(grid[x][y]) == 1)
				{
					int temp = 0;
					
					for(int z = 1; z < 10; z++)
					{ // go through potential numbers for that cell and find the one
						if(grid[x][y].getPotentialNumbers()[z])
						{ // can do this because only one potential number
							temp = z;
						}
					}
					
					grid[x][y].setDigit(temp);
					updatePotentialNumbers();
				}
			}
		}
	}
	
	// if r/c has 2 cells that share the same potential number and both are in the same box and they are the only cells in this box with that potential number, rest of cells in r/c cannot be this number
	public static void three()
	{
		//int counter = 0;
		node[] temp = new node[9];
		
		int[] xx = new int[3];
		int[] yy = new int[3];
		
		int a;
		int b;
		
		int boxNumber = 1;
		
		while(boxNumber < 10)
		{
			int counter = 0;
			
			if(boxNumber == 1)
			{
				a = 0;
				b = 0;
			}
			else if(boxNumber == 2)
			{
				a = 0;
				b = 3;
			}
			else if(boxNumber == 3)
			{
				a = 0;
				b = 6;
			}
			else if(boxNumber == 4)
			{
				a = 3;
				b = 0;
			}
			else if(boxNumber == 5)
			{
				a = 3;
				b = 3;
			}
			else if(boxNumber == 6)
			{
				a = 3;
				b= 6;
			}
			else if(boxNumber == 7)
			{
				a = 6;
				b = 0;
			}
			else if(boxNumber == 8)
			{
				a = 6;
				b = 3;
			}
			else
			{
				a = 6;
				b = 6;
			}
			
			int z = 0;
			int q = 0;
			
// if r/c has 2 cells that share the same potential number and both are in the same box and they are 
//the only cells in this box with that potential number, rest of cells in r/c cannot be this number
			
			// row
			for(int x = a; x < 3 + a; x++) // which row in box
			{
				for(int i = 1; i < 10; i++) // potential numbers
				{
					for(int y = b; y < 3 + b; y++) // which column in box
					{
						// go through one 3 node row at a time
						if(grid[x][y].getDigit() == 0 && grid[x][y].getPotentialNumbers()[i])
						{ // if i is a potential number
							counter++;
							
							temp[z] = grid[x][y];
							z++;
							
							xx[q] = x;
							yy[q] = y;
							q++;
						}
					}
					// finished row of 3 within a box
					
					// if potential number works, do here before switches potential numbers
					if(!isPotentialNumberInBox(a, b, xx, yy, i) && counter == 2)
					{
						// rest of cells in row cannot have this as a potential number
						for(int c = a; c < 3 + a; c++) // which row
						{
							for(int y = b; y < 3 + b; y++) // which column
							{
								if((c != xx[0] && y != yy[0]) || (c != xx[1] && y != yy[1]))
								{ 
									removePotentialNumber(grid[c][y], i);
								}
							}
						}
					}
					counter = 0;
					z = 0;
					q = 0;
				}
			} // end row
			
			// column
			for(int x = a; x < 3 + a; x++) // which column
			{
				for(int i = 1; i < 10; i++) // potential numbers
				{
					for(int y = b; y < 3 + b; y++) // which row
					{
						// go through one three node column at a time here
						if(grid[y][x].getDigit() == 0 && grid[y][x].getPotentialNumbers()[i])
						{
							counter++;
							
							temp[z] = grid[y][x];
							z++;
							
							xx[q] = y;
							yy[q] = x;
							q++;
						}
					}
					
					// if potential number works, do here before switches potential numbers
					if(!isPotentialNumberInBox(a, b, xx, yy, i) && counter == 2)
					{
						// rest of cells in row cannot have this as a potential number
						for(int c = a; c < 3 + a; c++) // which column
						{
							for(int y = b; y < 3 + b; y++) // which row
							{
								if((y != xx[0] && c != yy[0]) || (y != xx[1] && c != yy[1])) 
								{
									removePotentialNumber(grid[y][c], i);
								}
							}
						}
					}
					counter = 0;
					z = 0;
					q = 0;
				}
			} // end column
			
			boxNumber++;
		}
	}
	
	// if 2 cells in the same r/c/b share the same 2 potential numbers only, other cells in r/c/b cannot be those 2 numbers
	public static void four()
	{
		int counter = 0;
		int z = 0;
		int q = 0;
		
		node[] temp = new node[9];
		
		int[] xx = new int[3];
		int[] yy = new int[3];
		
		// row
		for(int i = 1; i < 10; i++) // potential number 1
		{
			for(int j = 1; j < 10; j++) // potential number 2
			{
				// choose two potential numbers, i & j
				if(j != i) // make sure different numbers
				{
					for(int x = 0; x < 9; x++)
					{
						for(int y = 0; y < 9; y++)
						{
							if(grid[x][y].getDigit() == 0 && grid[x][y].getPotentialNumbers()[i] && grid[x][y].getPotentialNumbers()[j] && numberOfPotentialNumbers(grid[x][y]) == 2)
							{ // if a node has only 2 potential numbers and both are the given i & j
								counter++;
								
								temp[z] = grid[x][y];
								z++;
								
								xx[q] = x;
								yy[q] = y;
								q++;
							}
						}
						
						if(counter == 2) // if 2 nodes have this condition,
						{
							// other cells in row cannot be those 2 numbers
							for(int c = 0; c < 9; c++)
							{
								if(grid[x][c] != temp[0] && grid[x][c] != temp[1] && ((xx[0] != x && yy[0] != c) && (xx[1] != x && yy[1] != c)))
								{
									removePotentialNumber(grid[x][c], i);
									removePotentialNumber(grid[x][c], j);
								}
							}
						}
							
						counter = 0;
						z = 0; 
						q = 0;
						// change row, reset all counters
					}
				}
			}
		}
		
		// column
		for(int i = 1; i < 10; i++)
		{
			for(int j = 1; j < 10; j++)
			{
				// choose two potential numbers, i & j
				if(j != i)
				{
					for(int x = 0; x < 9; x++)
					{
						for(int y = 0; y < 9; y++)
						{
							if(grid[y][x].getDigit() == 0 && grid[y][x].getPotentialNumbers()[i] && grid[y][x].getPotentialNumbers()[j] && numberOfPotentialNumbers(grid[y][x]) == 2)
							{
								counter++;
								
								temp[z] = grid[y][x];
								z++;
								
								xx[q] = x;
								yy[q] = y;
								q++;
							}
						}
						
						if(counter == 2)
						{
							// other cells in row cannot be those 2 numbers
							for(int c = 0; c < 9; c++)
							{
								if(grid[c][x] != temp[0] && grid[c][x] != temp[1] && ((xx[0] != c && yy[0] != x) && (xx[1] != c && yy[1] != x)))
								{
									removePotentialNumber(grid[c][x], i);
									removePotentialNumber(grid[c][x], j);
								}
							}
						}
							
						counter = 0;
						z = 0;
						q = 0; 
					}
				}
			}
		}
		
		// box
		int a;
		int b;
		
		int boxNumber = 1;
		
		while(boxNumber < 10)
		{
			if(boxNumber == 1)
			{
				a = 0;
				b = 0;
			}
			else if(boxNumber == 2)
			{
				a = 0;
				b = 3;
			}
			else if(boxNumber == 3)
			{
				a = 0;
				b = 6;
			}
			else if(boxNumber == 4)
			{
				a = 3;
				b = 0;
			}
			else if(boxNumber == 5)
			{
				a = 3;
				b = 3;
			}
			else if(boxNumber == 6)
			{
				a = 3;
				b= 6;
			}
			else if(boxNumber == 7)
			{
				a = 6;
				b = 0;
			}
			else if(boxNumber == 8)
			{
				a = 6;
				b = 3;
			}
			else
			{
				a = 6;
				b = 6;
			}
			
			// it will do fourBox for each box in the grid
			fourBox(a, b);
			
			boxNumber++;
		}
	}
	
	public static void fourBox(int a, int b)
	{
		int counter = 0;
		int z = 0;
		int q = 0;
		
		int[] xx = new int[9];
		int[] yy = new int[9];
		
		node[] temp = new node[9];
		
		for(int i = 1; i < 10; i++)
		{ // first potential number
			for(int j = 1; j < 10; j++)
			{ // second potential number
				if(i != j) 
				{ // different potential numbers
					for(int x = a; x < 3 + a; x++)
					{
						for(int y = b; y < 3 + b; y++)
						{ // go through 3 x 3 box
							if(grid[x][y].getDigit() == 0 && grid[x][y].getPotentialNumbers()[i] && grid[x][y].getPotentialNumbers()[j] && numberOfPotentialNumbers(grid[x][y]) == 2)
							{ // if i and j are both potential numbers and the only potential numbers
								counter++;
								
								temp[z] = grid[x][y];
								z++;

								xx[q] = x;
								yy[q] = y;
								q++;
							}
						}
					}
						
					if(counter == 2)
					{
						// other cells in box cannot be those 2 numbers
						for(int c = a; c < 3 + a; c++)
						{
							for(int y = b; y < 3 + b; y++)
							{
								if(grid[c][y] != temp[0] && grid[c][y] != temp[1] && ((xx[0] != c && yy[0] != y) && (xx[1] != c && yy[1] != y)))
								{
									removePotentialNumber(grid[c][y], i);
									removePotentialNumber(grid[c][y], j);
								}
							}
						}
					}
						
					counter = 0;
					z = 0;
					q = 0;
				}
			}
		}
	}
	
	public static void guess(int x, int y)
	{
		boolean found = false;
		
		if(grid[x][y].getDigit() != 0)
		{
			while(!found)
			{
				if(y < 8)
				{ // if is not in last column
					y++;
				}
				else if(y == 8 && x < 8)
				{ // if is in last column, but not in last row
					x++;
					y = 0;
				}
				else
				{ // is at end of list
					found = true;
				}
				
				if(grid[x][y].getDigit() == 0)
				{
					found = true;
				}
			}
		}
		
		for(int i = 1; i < 10; i++)
		{ // potential numbers
			if(grid[x][y].getPotentialNumbers()[i])
			{ // if i is a potential number
				grid[x][y].setDigit(i);
				
				updatePotentialNumbersGuess();
				
				if(y < 8)
				{ // if is not in last column
					guess(x, y + 1);
				}
				else if(y == 8 && x < 8)
				{ // if is in last column, but not in last row
					guess(x + 1, 0);
				}
				
				updatePotentialNumbersGuess();
			}
		}
		
		if(!solved())
		{
			grid[x][y].setDigit(0);
			updatePotentialNumbersGuess();
		}
	}
	
	// // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
	
	public static void removePotentialNumber(node node, int potentialNumber)
	{
		boolean[] temp = node.getPotentialNumbers();
		
		temp[potentialNumber] = false;
		
		node.setPotentialNumbers(temp);
	}
	
	public static boolean isPotentialInRow(int rowNumber, int columnNumber, int potentialNumber) // columns and rows are less code thus easier to put in main code
	{
		// now that we have a potential number in that column, we can see if it's in any of the other columns of that row
		for(int j = 0; j < 9; j++)
		{
			// go through columns in the specific row
			if(j != columnNumber)
			{
				// to see if any other nodes (other than og) in the row have that potential number
				if(grid[rowNumber][j].getDigit() == potentialNumber)
				{
					return false;
				}
			}
		} // end row	
		
		return true;
	}
	
	public static boolean isPotentialInColumn(int rowNumber, int columnNumber, int potentialNumber) // columns and rows are less code thus easier to put in main code
	{
		// now that we have a potential number in that column, we can see if it's in any of the other column of that row
		for(int j = 0; j < 9; j++)
		{
			// go through rows in the specific column
			if(j != rowNumber)
			{
				// to see if any other nodes (other than og) in the row have that potential number
				if(grid[j][columnNumber].getDigit() == potentialNumber)
				{
					return false;
				}
			}
		} // end column	
		
		return true;
	}
	
	public static boolean isPotentialInBox(int boxNumber, int rowNumber, int columnNumber, int potentialNumber) // columns and rows are less code thus easier to put in main code
	{
		int x;
		int y;
		
		if(boxNumber == 1)
		{
			x = 0;
			y = 0;
		}
		else if(boxNumber == 2)
		{
			x = 0;
			y = 3;
		}
		else if(boxNumber == 3)
		{
			x = 0;
			y = 6;
		}
		else if(boxNumber == 4)
		{
			x = 3;
			y = 0;
		}
		else if(boxNumber == 5)
		{
			x = 3;
			y = 3;
		}
		else if(boxNumber == 6)
		{
			x = 3;
			y = 6;
		}
		else if(boxNumber == 7)
		{
			x = 6;
			y = 0;
		}
		else if(boxNumber == 8)
		{
			x = 6;
			y = 3;
		}
		else
		{
			x = 6;
			y = 6;
		}
		
		for(int i = x; i < 3 + x; i++)
		{
			for(int j = y; j < 3 + y; j++)
			{ // go through box 3 by 3 L to R, T to B
				if(i != rowNumber && j != columnNumber && grid[i][j].getDigit() == 0)
				{ // if is not og and digit is zero
					if(grid[i][j].getPotentialNumbers()[potentialNumber])
					{
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	public static boolean isPotentialNumberInBox(int a, int b, int[] xx, int[] yy, int potentialNumber)
	{ // if two nodes given are the only ones in the box with this potential number
		/*
		System.out.println(grid[xx[0]][yy[0]].getBoxNumber());
		System.out.println("x: " + xx[0]);
		System.out.println("y: " + yy[0]);
		System.out.println(grid[xx[1]][yy[1]].getBoxNumber());
		System.out.println("x: " + xx[1]);
		System.out.println("y: " + yy[1]);
		*/
		
		for(int i = a; i < 3 + a; i++)
		{
			for(int j = b; j < 3 + b; j++)
			{ // go through box
				if((i != xx[0] && j != yy[0]))
				{ // if not position of two nodes given, if another one has that potential number, false
					if((i != xx[1] && j != yy[1]) && grid[i][j].getPotentialNumbers()[potentialNumber])
					{
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	public static boolean isOnlyPotentialInRow(int rowNumber, int columnNumber, int potentialNumber)
	{
		for(int j = 0; j < 9; j++)
		{ // go through row changing column numbers
			if(j != columnNumber && grid[rowNumber][j].getDigit() == 0)
			{ // if is not og and digit is zero
				if(grid[rowNumber][j].getPotentialNumbers()[potentialNumber])
				{ // if potential number is possible for that row
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static boolean isOnlyPotentialInColumn(int rowNumber, int columnNumber, int potentialNumber)
	{
		for(int j = 0; j < 9; j++)
		{ // go through rows in column
			if(j != rowNumber && grid[j][columnNumber].getDigit() == 0)
			{ // if not og and digit is zero
				if(grid[j][columnNumber].getPotentialNumbers()[potentialNumber])
				{ // if ha potential number
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static boolean isOnlyPotentialInBox(int boxNumber, int rowNumber, int columnNumber, int potentialNumber)
	{
		int x;
		int y;
		
		if(boxNumber == 1)
		{
			x = 0;
			y = 0;
		}
		else if(boxNumber == 2)
		{
			x = 0;
			y = 3;
		}
		else if(boxNumber == 3)
		{
			x = 0;
			y = 6;
		}
		else if(boxNumber == 4)
		{
			x = 3;
			y = 0;
		}
		else if(boxNumber == 5)
		{
			x = 3;
			y = 3;
		}
		else if(boxNumber == 6)
		{
			x = 3;
			y = 6;
		}
		else if(boxNumber == 7)
		{
			x = 6;
			y = 0;
		}
		else if(boxNumber == 8)
		{
			x = 6;
			y = 3;
		}
		else
		{
			x = 6;
			y = 6;
		}
		
		for(int i = x; i < 3 + x; i++)
		{
			for(int j = y; j < 3 + y; j++)
			{
				if((i != rowNumber && j != columnNumber) && grid[i][j].getDigit() == 0)
				{ // if digit is zero and not og
					if(grid[i][j].getPotentialNumbers()[potentialNumber])
					{ // if potential number is a potential number
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	public static int numberOfPotentialNumbers(node node)
	{
		int numberOfNumbers = 0;
		
		for(int x = 1; x < 10; x++)
		{
			if(node.getPotentialNumbers()[x] == true)
			{
				numberOfNumbers++;
			}
		}
		
		return numberOfNumbers;
	}
	
	public static boolean solved()
	{
		for(int x = 0; x < 9; x++)
		{
			for(int y = 0; y < 9; y++)
			{
				if(grid[x][y].getDigit() == 0) // never make digits 10 or more
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static void updatePotentialNumbers() // for all but guess
	{
		// update potential numbers for whole list
		for(int x = 0; x < 9; x++) // which row
		{
			for(int y = 0; y < 9; y++) // which column
			{
				boolean[] temp = grid[x][y].getPotentialNumbers();
				
				// default is true, but can be set false by methods, so must not be set back to true until guess
				
				for(int z = 1; z < 10; z++) // go through potential number array
				{
					if(isInColumn(grid[x][y], z) || isInRow(grid[x][y], z) || isInBox(grid[x][y], z))
					{
						temp[z] = false;
					}
				}
				
				grid[x][y].setPotentialNumbers(temp);
			} // end y
		} // end x
	}
	
	public static void updatePotentialNumbersGuess()
	{
		// update potential numbers for whole list
		for(int x = 0; x < 9; x++) // which row
		{
			for(int y = 0; y < 9; y++) // which column
			{
				//if(grid[x][y].getDigit() == 0) // if does not already have a digit
				//{
					boolean[] temp = grid[x][y].getPotentialNumbers();
					
					for(int z = 1; z < 10; z++)
					{
						temp[z] = true;
					}
					
					for(int z = 1; z < 10; z++) // go through potential number array
					{
						if(isInColumn(grid[x][y], z) || isInRow(grid[x][y], z) || isInBox(grid[x][y], z))
						{
							temp[z] = false;
						}
					}
					
					grid[x][y].setPotentialNumbers(temp);
			} // end y
		} // end x
	} // end update potential numbers
	
	public static boolean isInRow(node node, int number)
	{
		for(int y = 0; y < 9; y++)
		{
			if(y != node.getColumnNumber() && grid[node.getRowNumber()][y].getDigit() == number)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isInColumn(node node, int number)
	{
		for(int x = 0; x < 9; x++)
		{
			if(x != node.getRowNumber() && grid[x][node.getColumnNumber()].getDigit() == number)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isInBox(node node, int number)
	{
		int x;
		int y;
		
		if(node.getBoxNumber() == 1)
		{
			x = 0;
			y = 0;
		}
		else if(node.getBoxNumber() == 2)
		{
			x = 0;
			y = 3;
		}
		else if(node.getBoxNumber() == 3)
		{
			x = 0;
			y = 6;
		}
		else if(node.getBoxNumber() == 4)
		{
			x = 3;
			y = 0;
		}
		else if(node.getBoxNumber() == 5)
		{
			x = 3;
			y = 3;
		}
		else if(node.getBoxNumber() == 6)
		{
			x = 3;
			y = 6;
		}
		else if(node.getBoxNumber() == 7)
		{
			x = 6;
			y = 0;
		}
		else if(node.getBoxNumber() == 8)
		{
			x = 6;
			y = 3;
		}
		else
		{
			x = 6;
			y = 6;
		}
		
		for(int i = x; i < 3 + x; i++)
		{
			for(int j = y; j < 3 + y; j++)
			{
				if(grid[i][j].getDigit() == number)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static void display()
	{
		for(int x = 0; x < 9; x++) // which row
		{
			for(int y = 0; y < 9; y++) // which column
			{
				System.out.print(grid[x][y].getDigit() + " ");
				
				if(grid[x][y].getDigit()/10 < 1)
				{
					System.out.print(" ");
				}
			}
			System.out.println("");
		}
	} // end display
}