package config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
	public static void logger(String message) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(new Date());
        
        FileWriter fileWriter = new FileWriter("log.txt", true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        
        printWriter.println("[" + now + "]  [INFO]  " + message);
        printWriter.close();
        
		System.out.println("[" + now + "]  " + "[INFO]  " + message);
	}
}
