package edu.umass.cs.ciir.controversy.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SimpleFileWriter {
		FileWriter fw; 
		public SimpleFileWriter(String filename) throws IOException {
			fw = new FileWriter(new File(filename));
		}
		public void writeLine(String line) throws IOException{
			fw.append(line+"\n");
			fw.flush();
		}
		
		public void write(String line) throws IOException {
			fw.append(line); 
		}
		
		public void write(int line) throws IOException {
			fw.append(line + "");
		}
		public void close() throws IOException {
			fw.close();
		}
}


