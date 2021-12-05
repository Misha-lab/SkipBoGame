package game.io;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private FileWriter fileWriter = null;
	private SimpleDateFormat formatForDateNow = null;
	private Date date = null;
	
	public Logger() {
		formatForDateNow = new SimpleDateFormat("yyyy.MM.dd ' time ' hh:mm:ss a zzz");
	}
	public synchronized boolean toLog(String mess) {
		try {
			fileWriter = new FileWriter("log.txt", true);
			date = new Date();
			fileWriter.write(formatForDateNow.format(date) + ": " + mess + "\n\r\n");
			fileWriter.close();
			return true;
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
}