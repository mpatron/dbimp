package org.jobjects.dbimp.xml;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.jobjects.dbimp.trigger.Field;
import org.jobjects.dbimp.trigger.Line;
import org.junit.jupiter.api.Test;

public class XmlParamsTest {
  private Logger LOGGER = Logger.getLogger(getClass().getName());

  @Test
  public void parseFile() {
    try {
      XmlParams xmlParams = new XmlParams();
      URL url = ClassLoader.getSystemResource("org/jobjects/dbimp/userfilename-asc.xml");
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
