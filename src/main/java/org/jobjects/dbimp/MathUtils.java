/*
 * Created on 12 mai 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.jobjects.dbimp;

/**
 * @author MP
 *
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class MathUtils {

  public static boolean isInteger(String buffer) {
    boolean returnValue = false;
    try {
      double d = Double.parseDouble(buffer);
      returnValue = (Math.rint(d) == d);
    } catch (NumberFormatException nfe) {
      returnValue &= false;
    }
    return returnValue;
  }

  public static boolean isInteger(double d) {
    boolean returnValue = false;
    try {
      returnValue = (Math.rint(d) == d);
    } catch (NumberFormatException nfe) {
      returnValue &= false;
    }
    return returnValue;
  }

  public static String toStringInteger(String buffer) {
    String returnValue = buffer;
    // if (isInteger(returnValue)) {
    int i = returnValue.length() - 1;
    while ((i >= 0) && (returnValue.charAt(i) != '.')) {
      i--;
    }
    // if ((i >= 0) && (returnValue.charAt(i) == '.')) i--;
    if (i > 0)
      returnValue = returnValue.substring(0, i);
    // }
    return returnValue;
  }

  public static String toStringInteger(double d) {
    String returnValue = "" + ((long) d);
    return returnValue;
  }
}
