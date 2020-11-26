package test.clyde;

import java.io.FileWriter;
import java.io.PrintWriter;

public class LoggingOutput {
	private static final String fileName = "c:\\temp\\trace.txt";
	private static PrintWriter writer;

	static {
		try {
			writer = new PrintWriter(new FileWriter(fileName), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private LoggingOutput() {

	}

	public static void addStart(Thread thread, String methodName) {
		synchronized (writer) {
			writer.println(String.format("%s\t[%s] >> %s", System.nanoTime(), thread.getId(), methodName));
		}
	}

	public static void addEnd(Thread thread, String methodName) {
		synchronized (writer) {
			writer.println(String.format("%s\t[%s] << %s", System.nanoTime(), thread.getId(), methodName));
		}
	}

	public static void addLine(long startTime, long endTime, Thread thread) {
		synchronized (writer) {
			writer.print(startTime + "\t" + endTime + "\t[");
			StackTraceElement[] traces = thread.getStackTrace();
			for (int i = 2; i < traces.length; i++) {
				StackTraceElement trace = traces[i];
				writer.print((i > 2 ? ", " : "") + trace.getClassName() + '.' + trace.getMethodName());
			}
			writer.println("]");
		}
	}

	public static void close() {
		writer.close();
	}
}
