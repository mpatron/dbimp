package org.jobjects.tools;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Logger. Logger LOGGER =
 * Logger.getLogger(JObjectsLogFormatter.class.getName());
 * LOGGER.info("java.util.logging.config.file="+System.getProperty(
 * "java.util.logging.config.file"));
 * 
 * @author Mickaël Patron 28/02/2015
 */
public class JObjectsLogFormatter extends Formatter {
  private static final DateFormat format = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
  public static final String ANSI_RESET = "\u001B[1;0m";
  public static final String ANSI_BLACK = "\u001B[1;30m";
  public static final String ANSI_RED = "\u001B[1;31m";
  public static final String ANSI_GREEN = "\u001B[1;32m";
  public static final String ANSI_YELLOW = "\u001B[1;33m";
  public static final String ANSI_BLUE = "\u001B[1;34m";
  public static final String ANSI_PURPLE = "\u001B[1;35m";
  public static final String ANSI_CYAN = "\u001B[1;36m";
  public static final String ANSI_WHITE = "\u001B[1;37m";
  public static final int LEVEL_SIZE = 7;

  /**
   * Attention, il faut rendre la methode appelable 1 fois.
   */
  public static void initializeLogging() {
    final String filePathnameLogging = ClassLoader.getSystemResource("logging.properties").getPath();
    try (InputStream is = ClassLoader.getSystemResource("logging.properties").openStream()) {
      if (null == is) {
        Logger.getLogger(JObjectsLogFormatter.class.getName())
            .severe("La ressource " + filePathnameLogging + " est introuvable.");
      }
      LogManager.getLogManager().readConfiguration(is);
      Logger.getLogger(JObjectsLogFormatter.class.getName()).config("Chargement realisé de " + filePathnameLogging);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  String getStringLevel(Level level) {
    StringBuilder output = new StringBuilder();
    output.append("[");
    if (SystemUtils.IS_OS_UNIX | SystemUtils.IS_OS_WINDOWS_10) {
      if (Level.FINEST.equals(level))
        output.append(ANSI_GREEN);
      else if (Level.FINER.equals(level))
        output.append(ANSI_GREEN);
      else if (Level.FINE.equals(level))
        output.append(ANSI_GREEN);
      else if (Level.CONFIG.equals(level))
        output.append(ANSI_PURPLE);
      else if (Level.INFO.equals(level))
        output.append(ANSI_BLUE);
      else if (Level.WARNING.equals(level))
        output.append(ANSI_YELLOW);
      else if (Level.SEVERE.equals(level))
        output.append(ANSI_RED);
    }
    output.append(StringUtils.rightPad(level.getName(), LEVEL_SIZE));
    output.append(ANSI_RESET);
    output.append("]");
    return (StringUtils.rightPad(output.toString(), LEVEL_SIZE));
  }

  @Override
  public String format(LogRecord record) {
    String loggerName = record.getLoggerName();
    if (loggerName == null) {
      loggerName = "root";
    }
    StringBuilder output = new StringBuilder();
    
    String strLevel = getStringLevel(record.getLevel());
    output.append(strLevel);
    output
        // .append(Thread.currentThread().getName()).append('|')
        // .append(" ").append(loggerName).append(" ")
        .append(" " + format.format(new Date(record.getMillis()))).append(" : ")
        .append("..." + StringUtils.substringAfterLast(record.getSourceClassName(), "org.jobjects.") + "."
            + record.getSourceMethodName() + "()")
        .append(" : ");
    if (record.getParameters() != null) {
      output.append(MessageFormat.format(record.getMessage(), record.getParameters()));
    } else {
      output.append(record.getMessage());
    }
    if (record.getThrown() != null) {
      output.append(System.lineSeparator());
      output.append(ANSI_RED + ExceptionUtils.getStackTrace(record.getThrown()) + ANSI_RESET);
    }
    output.append(System.lineSeparator());
    return output.toString();
  }
}
