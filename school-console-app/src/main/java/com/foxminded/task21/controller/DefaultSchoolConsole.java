package com.foxminded.task21.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;
import org.springframework.stereotype.Component;

@Component
public class DefaultSchoolConsole implements SchoolConsole{

	private final Scanner scanner;
	private final PrintWriter out;
	
	public DefaultSchoolConsole() {
		this(System.in, System.out);
	}

	public DefaultSchoolConsole(InputStream in, PrintStream out) {		
		this.scanner = new Scanner(in);
		this.out = new PrintWriter(out, true);
	}	

	@Override
	public void println(CharSequence str) {
		out.println(str);		
	}
	
	@Override
	public void print(CharSequence str) {
		out.print(str);
		out.flush();
	}
	
	@Override
	public void printlnErr(CharSequence str) {
		System.err.println(str);
	}
	
	@Override
	public void printf(String format, Object... args) {
		out.printf(format, args);
		out.flush();
	}

	@Override
	public String nextLine() {
		return scanner.nextLine();
	}
	
	@Override
	public int nextInt() {
		return scanner.nextInt();
	}
	
	@Override
	public void close() throws IOException {
		scanner.close();
		out.close();
	}
}