package org.jobjects.dbimp;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jobjects.dbimp.report.Report;
import org.jobjects.dbimp.sql.SQLDatatbaseType;
import org.jobjects.dbimp.trigger.Field;
import org.jobjects.dbimp.trigger.Key;
import org.jobjects.dbimp.trigger.Line;
import org.jobjects.dbimp.xml.XmlDocument;
import org.jobjects.dbimp.xml.XmlField;
import org.jobjects.dbimp.xml.XmlParams;
import org.jobjects.dbimp.xml.XmlQueryParam;

/**
 * Class principale avec fonction main. La fonction main trappe les variables de
 * la JVM :<br>
 * <ul>
 * <li>[-u | --url] Url jdbc(jdbc:oracle:thin:@localhost:1521:MYDB).</li>
 * <li>[-U | --user] Login de l'utilisateur.</li>
 * <li>[-P | --password] Password de l'utlisateur.</li>
 * <li>[-f | --file] Nom du fichier source.</li>
 * <li>[-x | --xml] Nom du fichier de paramètrage.</li>
 * <li>[-e | --encode] [ US-ASCII | ISO-8859-1 | UTF-8 | UTF-16 ] encodage du
 * fichier.</li>
 * <li>[-d | --driver] Driver JDBC. Par defaut : oracle.jdbc.driver.OracleDriver
 * </li>
 * <li>[-r | --report] Répertoire du rapport d'importation. Par defaut : $TEMP</li>
 * <li>[-c | --cached] Cache les cursors, attention le nombre de curseur est
 * égal au nombre de type de ligne x3, diminu de faéon importante le temps
 * d'importation (>50% dans certain cas). Par defaut : false</li>
 * </ul>
 * 
 * @author Mickael Patron
 * @version 2.0
 */
public class Importation {

  private static Logger log = Logger.getLogger(Importation.class.getName());

  private static String dirnameReporte = System.getProperty("java.io.tmpdir");

  private static String filenameReporte = "reporting_";

  private static String extnameReporte = ".txt";

  /**
   * Fonction principale de lancement
   * 
   * @param args
   *          Liste des arguments passées en paramétre.
   * @throws Exception
   *           Exception générale.
   */
  public static void main(String[] args) throws Exception {
    System.exit(run(args));
  }
  
  public static int run(String[] args) throws Exception {
    int returnValue = 0;
    long t_start = System.currentTimeMillis();
    String url = null;
    String user = null;
    String password = null;
    String ascfile = null;
    String xmlfile = null;
    String ascfile_encode = null;
    String schemaName = StringUtils.EMPTY;
    boolean cached = false;
    boolean verbose = false;

    /*
     * Gestion des paramétres
     */
    HelpFormatter formatter = new HelpFormatter();
    String cmdLineSyntax = "$JAVA_HOME/bin/java " + Importation.class.getName();
    String header = "Importation directe version 3.2";
    String footer = "Copyright © 2006 JObjects Corp. All Rights Reserved";

    Options options = new Options();
    Option option = new Option(
        "u",
        "url",
        true,
        "Url jdbc by exemple jdbc:oracle:thin:@<server>:1521:<instance> ou jdbc:microsoft:sqlserver://<server>:1433;DatabaseName=<base> ou jdbc:as400://<server>/<collection>.");
    option.setArgName("jdbc:url");
    option.setRequired(true);
    option.setOptionalArg(false);
    options.addOption(option);

    option = new Option("U", "User", true, "Login de la base de donnée.");
    option.setArgName("username");
    option.setRequired(true);
    option.setOptionalArg(false);
    options.addOption(option);

    option = new Option("P", "Password", true, "Password de la base de donnée.");
    option.setArgName("password");
    option.setRequired(true);
    option.setOptionalArg(false);
    options.addOption(option);

    option = new Option("f", "fileTxt", true, "Nom du fichier source.");
    option.setArgName("fichier");
    option.setRequired(true);
    option.setOptionalArg(false);
    options.addOption(option);

    option = new Option("x", "fileXml", true, "Nom du fchier de parametre.");
    option.setArgName("xml");
    option.setRequired(true);
    option.setOptionalArg(false);
    options.addOption(option);

    option = new Option("e", "encode", true,
        "[ US-ASCII | ISO-8859-1 | UTF-8 | UTF-16 ] encodage par defaut ISO-8859-1.");
    option.setArgName("encodage [ISO-8859-1]");
    option.setRequired(false);
    option.setOptionalArg(false);
    options.addOption(option);

    option = new Option("r", "report", true,
        "Répertoire du rapport d'importation. Par defaut : " + dirnameReporte
            + ".");
    option.setArgName("report [" + dirnameReporte + "]");
    option.setRequired(false);
    option.setOptionalArg(false);
    options.addOption(option);

    option = new Option(
        "c",
        "cached",
        false,
        "Cache les cursors, attention le nombre de curseur est égal au nombre de type de ligne x3, diminu de façon importante le temps d'importation (>50% dans certain cas). Par defaut : false.");
    option.setRequired(false);
    option.setOptionalArg(false);
    options.addOption(option);

    option = new Option(
        "s",
        "schema",
        false,
        "Nom du schema. Par défaut le nom de l'utilisateur pour oracle , le nom de la collection pour DB2AS400.");
    option.setRequired(false);
    options.addOption(option);

    option = new Option("v", "verbose", false,
        "Encheri le rapport des informations de mise à jour.");
    option.setRequired(false);
    options.addOption(option);

    option = new Option("v", "verbose", false,
        "Encheri le rapport des informations de mise à jour.");
    option.setRequired(false);
    options.addOption(option);

    CommandLineParser parser = new PosixParser();
    try {
      CommandLine line = parser.parse(options, args);
      url = line.getOptionValue("u");
      user = line.getOptionValue("U");
      password = line.getOptionValue("P");
      ascfile = line.getOptionValue("f");
      xmlfile = line.getOptionValue("x");
      if (StringUtils.isEmpty(line.getOptionValue("e"))) {
        ascfile_encode = "ISO-8859-1";
      } else {
        if (!("US-ASCII".equals(line.getOptionValue("e"))
            || "ISO-8859-1".equals(line.getOptionValue("e"))
            || "UTF-8".equals(line.getOptionValue("e")) || "UTF-16".equals(line
            .getOptionValue("e")))) {
          System.err.println("Error : encode=" + line.getOptionValue("e"));
          formatter.printHelp(cmdLineSyntax, header, options, footer);
          return 1;
        } else {
          ascfile_encode = line.getOptionValue("e");
        }
      }

      if (!StringUtils.isEmpty(line.getOptionValue("r"))) {
        dirnameReporte = line.getOptionValue("r");
      }

      if (line.hasOption("c")) {
        cached = true;
      }
      if (!(line.hasOption("f") && line.hasOption("x") && line.hasOption("u")
          && line.hasOption("U") && line.hasOption("P"))) {
        formatter.printHelp(cmdLineSyntax, header, options, footer);
        return 1;
      }

      if (!StringUtils.isEmpty(line.getOptionValue("s"))) {
        schemaName = line.getOptionValue("s");
      }

      if (line.hasOption("v")) {
        verbose = true;
      }

    } catch (ParseException pe) {
      formatter.printHelp(cmdLineSyntax, header, options, footer);
      if ((pe instanceof MissingOptionException)
          || (pe instanceof MissingArgumentException)) {
        System.err.println("Parametres manquant : " + pe.getMessage());
      }
      return 1;
    }

    /*
     * Execution du programme
     */
    String driverClassName = SQLDatatbaseType.getType(url).getDriver();
    try {
      Driver driver = (Driver) Class.forName(driverClassName).newInstance();
      System.out.println("JDBC driver version : " + driver.getMajorVersion()
          + "." + driver.getMinorVersion());
      DriverManager.registerDriver(driver);
      Connection connection = DriverManager.getConnection(url, user, password);

      String fileNameReport = File.createTempFile(
          filenameReporte + "-" + getNextNumber() + "-", extnameReporte,
          new File(dirnameReporte)).getAbsolutePath();
      importFile(ascfile, ascfile_encode, xmlfile, connection, schemaName,
          cached, verbose, fileNameReport);
      connection.close();
      DriverManager.deregisterDriver(driver);
    } catch (Exception e) {
      String messageErr = new String();
      messageErr += SystemUtils.LINE_SEPARATOR + "  - driverClassName="
          + driverClassName;
      messageErr += SystemUtils.LINE_SEPARATOR + "  - url=" + url;
      messageErr += SystemUtils.LINE_SEPARATOR + "  - user=" + user;
      messageErr += SystemUtils.LINE_SEPARATOR + "  - password=" + password;
      messageErr += SystemUtils.LINE_SEPARATOR + "  - filenameReporte="+ filenameReporte;
      messageErr += SystemUtils.LINE_SEPARATOR + "  - extnameReporte="+ extnameReporte;
      messageErr += SystemUtils.LINE_SEPARATOR + "  - dirnameReporte="+ dirnameReporte;
      log.log(Level.SEVERE, messageErr, e);
      returnValue = 1;
    }
    long t_end = System.currentTimeMillis();
    System.out.println("Duration : "
        + DurationFormatUtils.formatDuration(t_end - t_start, "HH:mm:ss.SSS")
        + ".");
    return returnValue;
  }

  // ---------------------------------------------------------------------------

  /**
   * Fonnction d'importation. Cette fonction est le point d'entré pour les
   * objects CORBA, SOAP,EJB.
   * 
   * @param fileSource
   *          Fichier source à importer.
   * @param fileSourceEncoding
   *          Endodage du fichier source.
   * @param fileNameParameter
   *          Paramètre du fichier source.
   * @param conn
   *          Instance de connection à la base.En effet afin de garantir les
   *          traitements le pooler de connection n'est pas utilisé.
   * @param schemaName
   *          Nom du schema de la base de donnees.
   * @param cached
   *          Mise en cache des cursors, accelere enormement la vitesse
   *          d'importation, mais le nomre de curseur correspond au nombre de
   *          type de ligne du parametrage multiplier par quatre. En
   *          concequence, ce nombre peut etre eleve.
   * @param verbose
   *          Augmente le texte dans les logs.
   * @param fileNameReport
   *          Nom du fichier contenant le rapport.
   */
  public static void importFile(String fileSource, String fileSourceEncoding,
      String fileNameParameter, Connection conn, String schemaName,
      boolean cached, boolean verbose, String fileNameReport) {

    String message="DBImp starting... "+SystemUtils.LINE_SEPARATOR;
    message+="  - fileSource="+fileSource+SystemUtils.LINE_SEPARATOR;
    message+="  - fileSourceEncoding="+fileSourceEncoding+SystemUtils.LINE_SEPARATOR;
    message+="  - fileNameReport="+fileNameReport+SystemUtils.LINE_SEPARATOR;
    message+="  - schemaName="+schemaName+SystemUtils.LINE_SEPARATOR;
    message+="  - cached="+cached+SystemUtils.LINE_SEPARATOR;
    message+="  - verbose="+verbose+SystemUtils.LINE_SEPARATOR;    
    log.config(message);


    
    FileAsciiWriter faw = null;
    long l_start = System.currentTimeMillis();
    long l_end = 0;
    String description = null;
    int rejected = 0;
    int selected = 0;
    int inserted = 0;
    int updated = 0;
    int deleted = 0;
    try {
      Report reporting = null;
      try {
        faw = new FileAsciiWriter(fileNameReport, "UTF-8");
        reporting = new Report(faw);
        reporting.setInputFile(fileSource);
        reporting.setParamFile(fileNameParameter);
        reporting.setVerbose(verbose);
        log.finest("Rapport : " + dirnameReporte);
      } catch (Exception ex) {
        log.log(Level.SEVERE, "", ex);
      }
      /**
       * Chargement du parametrage des lignes et des recordset associé.
       */
      LinkedList<LineAndRecordSet> LineAndRecordSets = new LinkedList<LineAndRecordSet>();
      XmlParams param = new XmlParams();
      XmlDocument document = param.parseFile(new File(fileNameParameter));

      if (log.isLoggable(Level.FINER)) {
        afficheDocument(document);
      }

      description = document.getDescription();
      reporting.setDescription(description);
      for (Iterator<Line> it = document.getLines().iterator(); it.hasNext();) {
        Line xmlline = (Line) it.next();
        LineAndRecordSet lrs = new LineAndRecordSet(conn, schemaName, cached,
            xmlline, reporting.getTypeLine(xmlline));
        LineAndRecordSets.add(lrs);
      }
      /**
       * Chargement du fichier dans la base
       */
      String ligne = null;
      FileAsciiReader flux = null;
      flux = new FileAsciiReader(fileSource, fileSourceEncoding);
      int numberLine = 1;
      while ((ligne = flux.readLine()) != null) {
        log.info("lecture de la ligne = " + numberLine);
        for (Iterator<LineAndRecordSet> it = LineAndRecordSets.iterator(); it
            .hasNext();) {
          LineAndRecordSet lrs = (LineAndRecordSet) it.next();
          if (lrs.isActive(ligne)) {
            lrs.execute(numberLine, ligne);
            reporting.nextLine(numberLine);
          }
        }
        numberLine++;
      }
      flux.close();

      /**
       * Netoyage des connextions
       */
      for (Iterator<LineAndRecordSet> it = LineAndRecordSets.iterator(); it
          .hasNext();) {
        LineAndRecordSet lrs = (LineAndRecordSet) it.next();
        lrs.doAfterAction();
        rejected += lrs.getCountRejected();
        selected += lrs.getCountSelect();
        inserted += lrs.getCountInsert();
        updated += lrs.getCountUpdate();
        deleted += lrs.getCountDelete();
        lrs.release();
      }
      log.finest(reporting.INFO_STATUS("total", selected, inserted, updated,
          deleted, rejected));
      reporting.setDuration(System.currentTimeMillis() - l_start);
      reporting.write();
      faw.flush();
      faw.close();
      faw = null;
    } catch (Throwable t) {
      log.log(Level.SEVERE, "", t);
    }
    l_end = System.currentTimeMillis();
    log.info("Duration : "
        + DurationFormatUtils.formatDuration(l_end - l_start, "HH:mm:ss.SSS")
        + ".");
  }

  // ---------------------------------------------------------------------------
  private static void afficheDocument(XmlDocument document) {
    try {
      Iterator<Line> e = document.getLines().iterator();
      while (e.hasNext()) {
        Line xmlline = (Line) e.next();
        System.out.println("<line name='" + xmlline.getName() + "' tablename='"
            + xmlline.getTableName() + "'>");
        Iterator<Key> i_keys = xmlline.getKeys().iterator();
        while (i_keys.hasNext()) {
          Key key = (Key) i_keys.next();
          System.out.println("  " + "<key value='" + key.getValue()
              + "' startposition='" + key.getStartposition() + "' size='"
              + key.getSize() + "'>");
        }
        Iterator<Field> i_fields = xmlline.getFields().iterator();
        while (i_fields.hasNext()) {
          XmlField field = (XmlField) i_fields.next();
          System.out.println("  " + "<field fieldname='" + field.getName()
              + "' type='" + field.getType().getTypeString() + "' dateformat='"
              + field.getDateFormat() + "'>");
          switch (field.getDiscriminator()) {
          case XmlField.CONSTANTE:
            System.out.println("  " + "  " + "<constante value='"
                + field.getConstante().getValue() + "'/>");
            break;
          case XmlField.POSITION:
            System.out.println("  " + "  " + "<position startposition='"
                + field.getPosition().getStartposition() + "' size='"
                + field.getPosition().getSize() + "'/>");
            break;
          case XmlField.QUERY:
            System.out.println("  " + "  " + "<query sql='"
                + field.getQuery().getSql() + "'/>");
            Iterator<XmlQueryParam> e_query_params = field.getQuery()
                .getQueryParams().iterator();
            while (e_query_params.hasNext()) {
              XmlQueryParam queryparam = (XmlQueryParam) e_query_params.next();
              System.out.println("  " + "  " + "  " + "<query-param><"
                  + queryparam.getType().getTypeString() + "/>"
                  + "</query-param>");
            }
            break;
          default:
          }
          System.out.println("  " + "</field>");
        }
        System.out.println("</line>");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  // ---------------------------------------------------------------------------

  private static int getNextNumber() {
    int returnValue = 0;
    String filePath = SystemUtils.USER_HOME + SystemUtils.FILE_SEPARATOR
        + ".reportnumber.asc";
    File file = new File(filePath);
    if (file.exists()) {
      try {
        FileAsciiReader fileAsciiReader = new FileAsciiReader(filePath);
        String chaine = fileAsciiReader.readLine();
        fileAsciiReader.close();
        fileAsciiReader = null;
        if (StringUtils.isNumeric(chaine)) {
          returnValue = Integer.parseInt(chaine);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    try {
      FileAsciiWriter fileAsciiWriter = new FileAsciiWriter(filePath);
      fileAsciiWriter.write("" + (++returnValue));
      fileAsciiWriter.close();
      fileAsciiWriter = null;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return returnValue;
  }
}