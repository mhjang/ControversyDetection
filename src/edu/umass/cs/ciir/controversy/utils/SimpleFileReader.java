package edu.umass.cs.ciir.controversy.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * I am just sick and tired of writing unncessarily long-winded codes for simple I/O task
 * so here comes my simple file reader!
 */
public class SimpleFileReader {
	String line;
	BufferedReader br; 
	public SimpleFileReader(String filename) throws IOException {
		br = new BufferedReader(new FileReader(new File(filename)));
	}
	public boolean hasMoreLines() throws IOException {
		line = br.readLine(); 
		if(line == null) {
			br.close();
			return false; 
		}
		else return true; 
	}
	public String readLine() {
		return line; 
	}
	public void close() throws IOException {
		br.close();
	}

}
