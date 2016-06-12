package org.jobjects.dbimp.report;

import java.io.IOException;

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
public interface Reporting {

  public boolean isUsed();

  public void write() throws IOException;
}
