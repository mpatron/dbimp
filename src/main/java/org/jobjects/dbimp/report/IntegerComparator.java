package org.jobjects.dbimp.report;

import java.util.Comparator;

/**
 * <p>
 * Title: IHM
 * </p>
 * <p>
 * Description: Exportation dbExp
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: JObjects
 * </p>
 * <p>
 * Date : 5 sept. 2003
 * </p>
 * 
 * @author Mickael Patron
 * @version 1.0
 */
public class IntegerComparator implements Comparator<Integer> {

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  public int compare(Integer o1, Integer o2) {
    if ((o1 == null) | !(o1 instanceof Integer)) {
      return 1;
    }
    if ((o2 == null) | !(o2 instanceof Integer)) {
      return -1;
    }
    Integer i1 = (Integer) o1;
    Integer i2 = (Integer) o2;
    return i1.compareTo(i2);
  }

}
