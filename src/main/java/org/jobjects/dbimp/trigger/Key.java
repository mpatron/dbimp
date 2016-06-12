package org.jobjects.dbimp.trigger;

public interface Key extends Position {

  /**
   * @return Returns the isBlank.
   */
  public abstract boolean isBlank();

  /**
   * @param isBlank
   *          The isBlank to set.
   */
  public abstract void setBlank(boolean isBlank);

  /**
   * @return Returns the size.
   */
  public abstract int getSize();

  /**
   * @return Returns the startposition.
   */
  public abstract int getStartposition();

  /**
   * @return Returns the value.
   */
  public abstract String getKeyValue();

}