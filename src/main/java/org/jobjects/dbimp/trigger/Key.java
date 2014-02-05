package org.jobjects.dbimp.trigger;

public interface Key {

  /**
   * @return Returns the isBlank.
   */
  public abstract Boolean getIsBlank();

  /**
   * @param isBlank The isBlank to set.
   */
  public abstract void setIsBlank(Boolean isBlank);

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
  public abstract String getValue();

}