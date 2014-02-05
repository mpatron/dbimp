/*
 * Created on 14 oct. 2004
 */
package org.jobjects.dbimp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Projet : dbimp 
 * Fichier :
 * Creer le : 14 oct. 2004 15:59:49
 * 
 * @author Mickaël Patron
 * 
 */
public class FileAsciiReader {

  private FileInputStream   fileInputStream   = null;

  private InputStreamReader inputStreamReader = null;

  private BufferedReader  bufferedReader  = null;

  public FileAsciiReader(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
    this(fileName, "ISO-8859-1");
  }

  
  public FileAsciiReader(String fileName, String encoding) throws FileNotFoundException, UnsupportedEncodingException {
    fileInputStream = new FileInputStream(fileName);
    inputStreamReader = new InputStreamReader(fileInputStream, encoding);
    bufferedReader = new BufferedReader(inputStreamReader);
  }

  /**
   * @param inputStream
   * @throws FileNotFoundException
   * @throws UnsupportedEncodingException
   */
  public FileAsciiReader(InputStream inputStream) throws FileNotFoundException, UnsupportedEncodingException {
    this(inputStream, "ISO-8859-1");
  }

  /**
   * @param inputStream
   * @param encoding
   * @throws FileNotFoundException
   * @throws UnsupportedEncodingException
   */
  public FileAsciiReader(InputStream inputStream, String encoding) throws FileNotFoundException,
      UnsupportedEncodingException {
    inputStreamReader = new InputStreamReader(inputStream, encoding);
    bufferedReader = new BufferedReader(inputStreamReader);
  }

  
  /**
   * @param file Le fichier
   * @throws FileNotFoundException
   * @throws UnsupportedEncodingException
   */
  public FileAsciiReader(File file) throws FileNotFoundException, UnsupportedEncodingException {
    this(file, "ISO-8859-1");
  }

  public FileAsciiReader(File file, String encoding) throws FileNotFoundException,
      UnsupportedEncodingException {
    fileInputStream = new FileInputStream(file);
    inputStreamReader = new InputStreamReader(fileInputStream, encoding);
    bufferedReader = new BufferedReader(inputStreamReader);
  }
  
  /**
   * @throws java.io.IOException
   */
  public void close() throws IOException {
    bufferedReader.close();
    if (inputStreamReader != null) inputStreamReader.close();
    if (fileInputStream != null) fileInputStream.close();
  }

  /**
   * @param readAheadLimit
   * @throws java.io.IOException
   */
  public void mark(int readAheadLimit) throws IOException {
    bufferedReader.mark(readAheadLimit);
  }

  /**
   * @return boolean
   */
  public boolean markSupported() {
    return bufferedReader.markSupported();
  }

  /**
   * @return @throws
   *         java.io.IOException
   */
  public int read() throws IOException {
    return bufferedReader.read();
  }

  /**
   * @param cbuf
   * @return @throws
   *         java.io.IOException
   */
  public int read(char[] cbuf) throws IOException {
    return bufferedReader.read(cbuf);
  }

  /**
   * @param cbuf
   * @param off
   * @param len
   * @return @throws
   *         java.io.IOException
   */
  public int read(char[] cbuf, int off, int len) throws IOException {
    return bufferedReader.read(cbuf, off, len);
  }

  /**
   * @return @throws
   *         java.io.IOException
   */
  public String readLine() throws IOException {
    return bufferedReader.readLine();
  }

  /**
   * @return @throws
   *         java.io.IOException
   */
  public boolean ready() throws IOException {
    return bufferedReader.ready();
  }

  /**
   * @throws java.io.IOException
   */
  public void reset() throws IOException {
    bufferedReader.reset();
  }

  /**
   * @param n
   * @return @throws
   *         java.io.IOException
   */
  public long skip(long n) throws IOException {
    return bufferedReader.skip(n);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return bufferedReader.toString();
  }
}