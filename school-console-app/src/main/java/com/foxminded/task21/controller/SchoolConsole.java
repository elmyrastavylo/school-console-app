package com.foxminded.task21.controller;

import java.io.Closeable;

public interface SchoolConsole extends Closeable {
	
	void println(CharSequence str);
	void print(CharSequence str);
	void printlnErr(CharSequence str);
	void printf(String format, Object... args);
	String nextLine();
	int nextInt();
}