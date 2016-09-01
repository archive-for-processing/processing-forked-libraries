package org.raissi.spark;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSplitter {
	
	public static Logger LOGGER = LoggerFactory.getLogger(FileSplitter.class);

	public static String[] split(String filePath){
		try {
			return Files.lines(Paths.get(filePath)).toArray(String[]::new);
		} catch (IOException e) {
			LOGGER.error("Error reading file at path : {} null will be returned", filePath, e);
			return null;
		}
		
	}
}
