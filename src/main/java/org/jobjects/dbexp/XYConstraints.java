package org.jobjects.dbexp;

import java.io.Serializable;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
 * 
 * @author Mickael Patron
 * @version 1.0
 */
public class XYConstraints implements Cloneable, Serializable, Comparable<XYConstraints> {
  private static final long serialVersionUID = -1;

  private int x;

  private int y;

  private int width;

  private int height;

  public XYConstraints() {
    this(0, 0, 0, 0);
  }

  public XYConstraints(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public Object clone() {
    return new XYConstraints(x, y, width, height);
  }

  public String toString() {
    return String.valueOf(String.valueOf((new StringBuffer("PositionConstraints[")).append(x).append(",").append(y).append(",")
        .append(width).append(",").append(height).append("]")));
  }

  /**
   * @see java.lang.Object#equals(Object)
   */
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof XYConstraints)) {
      return false;
    }
    XYConstraints rhs = (XYConstraints) object;
    return new EqualsBuilder().append(this.width, rhs.width).append(this.height, rhs.height).append(this.y, rhs.y).append(this.x, rhs.x)
        .isEquals();
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return new HashCodeBuilder(1552374383, 1779360547).append(this.width).append(this.height).append(this.y).append(this.x).toHashCode();
  }

  /**
   * @see java.lang.Comparable#compareTo(Object)
   */
  public int compareTo(XYConstraints object) {
    return new CompareToBuilder().append(this.width, object.width).append(this.height, object.height).append(this.y, object.y)
        .append(this.x, object.x).toComparison();
  }
}