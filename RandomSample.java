// Cliff Jarrell
// Dec. 6, 2016, UPDATED: Mar. 9, 2017
// PURPOSE: Produces a text file that displays a chosen random number of trees given a vineyard with any number of rows and columns. 

// Designations: S = South tree, N = North tree. 
// An odd number of rows will produce an extra N.
// Output is formatted for ease of use. 
// USAGE: java RandomSample out.txt

import java.util.*;
import java.io.*;
import java.lang.*;

public class RandomSample {
	public static void main(String[] args) {
		if (args.length < 1) {
			usage();
			System.exit(0);
		}
			
		try {
			if (!args[0].contains(".txt")) {
				throw new IOException();
			}
			
			PrintWriter writer = new PrintWriter(new File(args[0]));
			Scanner scan = new Scanner(System.in);
			
			int sampleNum;
			int row;
			int col;
			int randSampNum;
			int increment;
			
			// Get input data
			while (true) {
				System.out.println("How many samples should be generated?");
				sampleNum = Integer.parseInt(scan.next());
				if (sampleNum<=0) {
					System.out.println("Enter a number greater than 0");
					continue;
				}
				scan = new Scanner(System.in);
				
				System.out.println("How many rows and columns?");
				String line = scan.nextLine();
				String[] tokens = line.split(" ");
				if (tokens.length != 2) {
					System.out.println("You must enter row and column numbers separated by a space");
					continue;
				}
				row = Integer.parseInt(tokens[0]);
				col = Integer.parseInt(tokens[1]);
				
				System.out.println("How many random samples from each column?");
				randSampNum = Integer.parseInt(scan.next());
				if (randSampNum >= col) {
					System.out.println("ERROR: number of random samples cannot be greater than or equal to the number of columns.");
					continue;
				}
			
				System.out.println("What number should the samples begin at?");
				increment = Integer.parseInt(scan.next());
				break;
			}
			
			// Instantiate String representations
			ArrayList<String> values = new ArrayList<String>();
			for (int i = 0; i < (row / 2); i++) {
				values.add((i + 1) + "S");
			}
			int count = (row / 2); 
			for(int i = (row / 2); i < row; i++) {
				if (row % 2 == 0) {
					values.add(count + "N");
				}
				else if (row % 2 != 0) {
					values.add((count + 1) + "N");
				}
				count--;
			}
			
			ArrayList<String> randValues = new ArrayList<String>();
			String[][] vSamples = new String[col][randSampNum];
				
				
			// Begin generating sample
			for (int z = 0; z < sampleNum; z++) {
				writer.println("Sample: " + increment);
				increment++;
				
				// Select which trees in column should be random
				for (int i = 0; i < col; i++) {
					Collections.shuffle(values); // Shuffles all items in a list randomly. Faster and doesn't produce duplicate randoms.
					produceRandValues(values, randValues, randSampNum);
					for (int x = 0; x < randSampNum; x++) {
						vSamples[i][x] = randValues.get(x);
					}
					randValues.clear();
				}
				
				// Write file
				vRow = 0; // these static variables are initialized and can be manipulated from anywhere in the class.
				vCol = 0;
				colsLeft = col;
	            
				int remainder = (col % 5);
				int section = (col / 5);
				if (remainder > 0) { 
					section += 1;
				}
				
				for (int i = 0; i < section; i++) { // The loop that prints the column headers as well as the random samples
					printCols(remainder, writer);
					printSection(vSamples, remainder, randSampNum, writer);
				}
			}
			
			writer.close();
			System.out.println("Done");
		}
		catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch (IOException e){
			System.out.println("File not writable");
			usage();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> produceRandValues(ArrayList<String> values, ArrayList<String> randValues, int randSampNum) {
		for (int i = 0; i < randSampNum; i++) {
			randValues.add(values.get(i));
		}
		
		for (int i = 0; i < randValues.size(); i++) { // Sorts from South to North
			if (randValues.get(i).contains("N")) {
				for (int x = (i + 1); x < randValues.size(); x++) {
					if (randValues.get(x).contains("S")) {
						Collections.swap(randValues, i, x);
					}
				}
			}
		}
		
		for (int i = 0; i < randValues.size(); i++) { //Remove the S or N, parse the ints, then compare to sort numerically.
			if (randValues.get(i).contains("S")) {
				for (int x = (i + 1); x < randValues.size(); x++) {
					if (randValues.get(x).contains("S")) { 
						String temp = randValues.get(i).substring(0, (randValues.get(i).length() - 1));
						String temp2 = randValues.get(x).substring(0, (randValues.get(x).length() - 1));
							if (Integer.parseInt(temp) > Integer.parseInt(temp2)) {
								Collections.swap(randValues, i, x);
							}
					}
				}
			}
			else if (randValues.get(i).contains("N")) {
				for (int x = (i + 1); x < randValues.size(); x++) {
					if (randValues.get(x).contains("N")) { 
						String temp = randValues.get(i).substring(0, (randValues.get(i).length() - 1));
						String temp2 = randValues.get(x).substring(0, (randValues.get(x).length() - 1));
							if (Integer.parseInt(temp) < Integer.parseInt(temp2)) {
								Collections.swap(randValues, i, x);
							}
					}
				}
			}
		}
		return randValues;
	}
	
	static int colsLeft;
	static int vRow;
	static int vCol;
	
	public static void printCols(int remainder, PrintWriter writer) {
		String column = "Col:";
		if (colsLeft >= 5) {
			writer.printf("%10s%10s%10s%10s%10s%n", column + (vRow + 1), column + (vRow + 2),
				column + (vRow + 3), column + (vRow + 4), column + (vRow + 5));
		}
		else if (colsLeft < 5) {
			for (int i = 1; i < remainder + 1; i++) {
				writer.printf("%10s", column + (vRow + i));
			}
			writer.println();
		}
	}
	
	public static void printSection(String[][] vSamples, int remainder, int randSampNum, PrintWriter writer) {
		if (colsLeft >= 5) {
			for (int x = 0; x < randSampNum; x++) {
				for (int z = 0; z < 5; z++) {
					writer.printf("%10s", vSamples[vRow][vCol]);
					vRow++;
				}
				vRow -= 5;
				vCol++;
				writer.println();
			}
			vRow += 5;
			vCol = 0;
			colsLeft -= 5; 
		}
		else if (colsLeft < 5) {
			for (int x = 0; x < randSampNum; x++) {
				for (int z = 0; z < remainder; z++) {
					writer.printf("%10s", vSamples[vRow][vCol]);
					vRow++;
				}
				writer.println();
				vRow -= remainder;
				vCol++;
			}
		}
		writer.println();
	}
	
	public static void usage() {
		System.out.println("USAGE: java RandomSample outFile.txt");
	}
}