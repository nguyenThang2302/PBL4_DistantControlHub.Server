package config;

import java.time.LocalDateTime;

public class Logger {
	public static void logger(String message) {
		LocalDateTime now = LocalDateTime.now();
		System.out.println("[" + now + "]  " + "[INFO]  " + message);
	}
}
