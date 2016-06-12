package org.jobjects.dbimp;

import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Importation2 {
  private static Logger LOGGER = Logger.getLogger(Importation2.class.getName());

  public static void main(String[] args) {
    try {
      Charset.availableCharsets().keySet().stream().forEach(s -> {
        LOGGER.info(Charset.forName(s).displayName());
      });

      // URL url =
      // ClassLoader.getSystemResource("org/jobjects/logging.properties");
      // Path path = Paths.get(url.toURI());
      // LOGGER.info("org/jobjects/logging.properties=" +
      // Files.isReadable(path));
      // System.setProperty("java.util.logging.config.file",
      // path.toAbsolutePath().toString());
      // LOGGER.info(System.getProperty("java.util.logging.config.file"));
      // // TODO Auto-generated method stub
      //
      // // Path pathAsc =
      // //
      // Paths.get(ClassLoader.getSystemResource("org/jobjects/dbimp/api.randomuser.me.txt").toURI());
      // //
      // LOGGER.info("org/jobjects/dbimp/api.randomuser.me.txt="+Files.isReadable(pathAsc));
      // // String fileSource = new
      // //
      // File(ClassLoader.getSystemResource("org/jobjects/dbimp/api.randomuser.me.txt").toURI()).getAbsolutePath();
      // String fileSource = "D:/Utilisateurs/Mickael
      // Patron/Documents/java/github/tools/dbimp/target/test-classes/org/jobjects/dbimp/api.randomuser.me.txt";
      // // File file= new File("D:/Utilisateurs/Mickael
      // //
      // Patron/Documents/java/github/tools/dbimp/target/test-classes/org/jobjects/dbimp/api.randomuser.me.txt");
      // // LOGGER.log(Level.INFO, ""+file.exists());
      // try (Stream<String> stream = Files.lines(Paths.get(fileSource),
      // StandardCharsets.UTF_8)) {
      //
      // stream.forEach(System.out::println);
      //
      // } catch (IOException e) {
      // e.printStackTrace();
      // }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }

}
