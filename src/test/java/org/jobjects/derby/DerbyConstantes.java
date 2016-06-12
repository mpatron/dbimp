package org.jobjects.derby;

public interface DerbyConstantes {
  public final static String USER = "user";
  public final static String PASSWORD = "password";
  public final static String SCHEMA_NAME = "MYDERBYDB";

  // public final static String DRIVER_CLASSNAME =
  // "org.apache.derby.jdbc.EmbeddedDriver";
  // public final static String USER_VALUE = "sa";
  // public final static String PASSWORD_VALUE = "manager";
  // public final static String URL = "jdbc:derby:memory:MyDerbyDB";
  // public final static String DATABASE_NAME = "MyDerbyDB";

  public final static String DRIVER_CLASSNAME = "org.apache.derby.jdbc.ClientDriver";
  public final static String DRIVER_EMBEDDED_CLASSNAME = "org.apache.derby.jdbc.EmbeddedDriver";
  public final static String USER_VALUE = "derby";
  public final static String PASSWORD_VALUE = "derby";
  // public final static String URL =
  // "jdbc:derby://localhost:1527/db;user=derby;password=derby";
  // public final static String URL = "jdbc:derby://localhost:1527/db";
  public final static String URL = "jdbc:derby:db";
  public final static String DATABASE_NAME = "db";

  // jdbc:derby://localhost:1527/db;user=derby;password=derby

}
