package org.jobjects.dbimp.xml;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.jobjects.dbimp.trigger.Field;
import org.jobjects.dbimp.trigger.Line;
import org.testng.annotations.Test;

public class XmlParamsTest {
  private Logger LOGGER = Logger.getLogger(getClass().getName());

  @Test(groups = "MaSuite")
  public void parseFile() {
    try {
      XmlParams xmlParams = new XmlParams();
      URL url = Class.class.getResource("/org/jobjects/dbimp/userfilename.xml");
      XmlDocument xmld = xmlParams.parseFile(new File(url.toURI()));
      StringBuffer sb = new StringBuffer();
      LinkedList<Line> lines = xmld.getLines();
      for (Line xmlLine : lines) {
        sb.append(xmlLine.getName() + System.lineSeparator());
        for (Field field : xmlLine.getFields()) {
          XmlField xmlField = (XmlField) field;
          sb.append("- " + xmlField.getName() + " (" + xmlField.getTypeFormat() + ")" + System.lineSeparator());
        }
      }
      LOGGER.info(sb.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
