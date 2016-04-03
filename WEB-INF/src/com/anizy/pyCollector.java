package com.anizy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.ScriptException;

/**
 * 获取python的输出内容,仅windows平台下使用
 * @author kcetry
 *
 */

public class pyCollector extends Thread {
	
	public static class StreamGobbler extends Thread {

		private InputStream in;
		private String result;

		public StreamGobbler(InputStream in) {
			this.in = in;
		}

		public void run() {
			BufferedReader inline = new BufferedReader(new InputStreamReader(in));
			try {
				result = inline.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public String getResult() {
			return result;
		}
	}

	public String execIt(String command) {
		Runtime rn = Runtime.getRuntime();
		String s[] = { "cmd", "/c", command };
		Process p = null;
		try {
			p = rn.exec(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		StreamGobbler tp1 = new StreamGobbler(p.getInputStream());
		StreamGobbler tp2 = new StreamGobbler(p.getErrorStream());
		tp1.start();
		tp2.start();
		
		try {
			tp1.join();
			tp2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("F"+command);
		return tp1.getResult();
	}
}