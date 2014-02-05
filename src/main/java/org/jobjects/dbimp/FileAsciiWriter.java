/*
 * Created on 14 oct. 2004
 */
package org.jobjects.dbimp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.SystemUtils;

/**
 * Projet : dbimp 
 * Fichier : org.jobjects.dbimp.FileAsciiWriter.java 
 * Creer le : 14 oct. 2004 16:22:07
 * 
 * @author Mickaël Patron
 */
public class FileAsciiWriter {

	private String fileName = null;

	private FileOutputStream fileOutputStream = null;

	private OutputStreamWriter outputStreamWriter = null;

	private BufferedWriter bufferedWriter = null;

	public FileAsciiWriter(String fileName) throws FileNotFoundException,
			UnsupportedEncodingException {
		this(fileName, "ISO-8859-1");
	}

	public FileAsciiWriter(String fileName, String encoding)
			throws FileNotFoundException, UnsupportedEncodingException {
		this.fileName = fileName;
		this.fileOutputStream = new FileOutputStream(fileName);
		this.outputStreamWriter = new OutputStreamWriter(fileOutputStream,
				encoding);
		this.bufferedWriter = new BufferedWriter(outputStreamWriter);
	}

	public FileAsciiWriter(File file) throws FileNotFoundException,
			UnsupportedEncodingException {
		this(file, "ISO-8859-1");
	}

	public FileAsciiWriter(File file, String encoding)
			throws FileNotFoundException, UnsupportedEncodingException {
		this.fileName = file.getAbsolutePath();
		this.fileOutputStream = new FileOutputStream(file);
		this.outputStreamWriter = new OutputStreamWriter(fileOutputStream,
				encoding);
		this.bufferedWriter = new BufferedWriter(outputStreamWriter);
	}

	/**
	 * @throws java.io.IOException
	 */
	public void close() throws IOException {
		bufferedWriter.close();
		outputStreamWriter.close();
		fileOutputStream.close();
	}

	/**
	 * @throws java.io.IOException
	 */
	public void flush() throws IOException {
		bufferedWriter.flush();
	}

	/**
	 * @throws java.io.IOException
	 */
	public void newLine() throws IOException {
		bufferedWriter.newLine();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return bufferedWriter.toString();
	}

	/**
	 * @param cbuf
	 * @throws java.io.IOException
	 */
	public void write(char[] cbuf) throws IOException {
		bufferedWriter.write(cbuf);
	}

	/**
	 * @param cbuf
	 * @param off
	 * @param len
	 * @throws java.io.IOException
	 */
	public void write(char[] cbuf, int off, int len) throws IOException {
		bufferedWriter.write(cbuf, off, len);
	}

	/**
	 * @param c
	 * @throws java.io.IOException
	 */
	public void write(int c) throws IOException {
		bufferedWriter.write(c);
	}

	/**
	 * @param str
	 * @throws java.io.IOException
	 */
	public void write(String str) throws IOException {
		bufferedWriter.write(str);
	}

	/**
	 * @param str
	 * @throws java.io.IOException
	 */
	public void writeln(String str) throws IOException {
		bufferedWriter.write(str);
		bufferedWriter.write(SystemUtils.LINE_SEPARATOR);
	}

	/**
	 * @param str
	 * @param off
	 * @param len
	 * @throws java.io.IOException
	 */
	public void write(String str, int off, int len) throws IOException {
		bufferedWriter.write(str, off, len);
	}

	public String getFileName() {
		return fileName;
	}
}
