package org.jobjects.dbexp;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.DebugGraphics;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;

//import com.borland.jbcl.layout.*;

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

public class MainFrame extends JFrame {
  private static final long serialVersionUID = 7273459552812917920L;
  JPanel contentPane;
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu jMenuFile = new JMenu();
  JMenuItem jMenuFileExit = new JMenuItem();
  JMenu jMenuHelp = new JMenu();
  JMenuItem jMenuHelpAbout = new JMenuItem();
  ImageIcon image1;
  ImageIcon image2;
  ImageIcon image3;
  JLabel statusBar = new JLabel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JButton jButtonAction = new JButton();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jLabelUsername = new JLabel();
  JLabel jLabelPassword = new JLabel();
  JLabel jLabelUrl = new JLabel();
  JTextField jTextUsername = new JTextField();
  JTextField jTextPassword = new JTextField();
  JTextField jTextUrl = new JTextField();
  JLabel jLabelLocationFilter = new JLabel();
  JTextField jTextLnFilter = new JTextField();
  JTextField jTextNcLnCodTypReplace = new JTextField();
  JLabel jLabelLocationReplace = new JLabel();
  JTextField jTextLnReplace = new JTextField();
  JTextField jTextNcLnCodTypFilter = new JTextField();
  JLabel jLabelAscFile = new JLabel();
  JLabel jLabelXmlFile = new JLabel();
  JTextField jTextChooseAscFile = new JTextField();
  JTextField jTextChooseXmlFile = new JTextField();
  JButton jButtonChooseAscFile = new JButton();
  JButton jButtonChooseXmlFile = new JButton();
  JProgressBar progressBar = new JProgressBar();

  // Construct the frame
  public MainFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Component initialization
  private void jbInit() throws Exception {
    image1 = new ImageIcon(MainFrame.class.getResource("openFile.png"));
    image2 = new ImageIcon(MainFrame.class.getResource("closeFile.png"));
    image3 = new ImageIcon(MainFrame.class.getResource("help.png"));
    contentPane = (JPanel) this.getContentPane();
    contentPane.setLayout(borderLayout1);
    this.setResizable(false);
    this.setSize(new Dimension(481, 305));
    this.setTitle("Frame Title");
    statusBar.setDebugGraphicsOptions(DebugGraphics.FLASH_OPTION);
    statusBar.setText(" ");
    jMenuFile.setText("File");
    jMenuFileExit.setText("Exit");
    jMenuFileExit
        .addActionListener(new Frame1_jMenuFileExit_ActionAdapter(this));
    jMenuHelp.setText("Help");
    jMenuHelpAbout.setText("About");
    jMenuHelpAbout.addActionListener(new Frame1_jMenuHelpAbout_ActionAdapter(
        this));
    jPanel1.setLayout(xYLayout1);
    jButtonAction.setText("Run");
    jButtonAction
        .addActionListener(new Frame1_jButtonAction_actionAdapter(this));
    jLabelUsername.setText("User :");
    jLabelPassword.setText("Password :");
    jTextUsername.setText("dbExp");
    jTextPassword.setText("dbExp");
    // jTextField1.setText("jdbc:oracle:thin:@<server>:1521:<INSTANCE>");
    jTextUrl.setText("jdbc:oracle:thin:@localhost:1521:XE");
    jLabelUrl.setText("Url :");
    jLabelLocationFilter.setText("Location filter :");
    jTextLnFilter.setText("");
    jTextNcLnCodTypFilter.setText("");
    jLabelLocationReplace.setToolTipText("");
    jLabelLocationReplace.setText("Location Replace :");
    jTextLnReplace.setText("");
    jTextNcLnCodTypReplace.setText("");
    jButtonChooseAscFile.setText("...");
    jButtonChooseAscFile
        .addActionListener(new Frame1_jButtonChooseAscFile_actionAdapter(this));
    jButtonChooseXmlFile.setText("...");
    jButtonChooseXmlFile
        .addActionListener(new Frame1_jButtonChooseXmlFile_actionAdapter(this));
    jTextChooseAscFile.setText("c:\\temp\\toto.asc");
    jTextChooseXmlFile.setText("c:\\temp\\toto.xml");
    jLabelAscFile.setText("Ascii File :");
    jLabelXmlFile.setText("Xml File :");
    progressBar.setOpaque(false);
    // jProgressBar1.setString("50%");
    progressBar.setValue(0);
    jPanel1.setBorder(BorderFactory.createEtchedBorder());
    jMenuFile.add(jMenuFileExit);
    jMenuHelp.add(jMenuHelpAbout);
    jMenuBar1.add(jMenuFile);
    jMenuBar1.add(jMenuHelp);
    this.setJMenuBar(jMenuBar1);
    contentPane.add(statusBar, BorderLayout.SOUTH);
    contentPane.add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(jTextUsername, new XYConstraints(117, 13, 138, -1));
    jPanel1.add(jTextUrl, new XYConstraints(117, 68, 319, -1));
    jPanel1.add(jTextLnReplace, new XYConstraints(117, 126, 146, -1));
    jPanel1.add(jTextNcLnCodTypReplace, new XYConstraints(281, 127, 44, -1));
    jPanel1.add(jTextLnFilter, new XYConstraints(117, 97, 147, -1));
    jPanel1.add(jTextPassword, new XYConstraints(117, 41, 138, -1));
    jPanel1.add(jTextNcLnCodTypFilter, new XYConstraints(281, 97, 43, -1));
    jPanel1.add(jTextChooseXmlFile, new XYConstraints(117, 180, 288, -1));
    jPanel1.add(jTextChooseAscFile, new XYConstraints(117, 154, 287, -1));
    jPanel1.add(jButtonChooseAscFile, new XYConstraints(409, 147, 25, -1));
    jPanel1.add(jButtonChooseXmlFile, new XYConstraints(409, 176, 25, -1));
    jPanel1.add(jButtonAction, new XYConstraints(354, 206, 80, 26));
    jPanel1.add(progressBar, new XYConstraints(46, 209, 302, 20));
    jPanel1.add(jLabelPassword, new XYConstraints(6, 45, -1, 14));
    jPanel1.add(jLabelUsername, new XYConstraints(7, 16, 37, 14));
    jPanel1.add(jLabelLocationReplace, new XYConstraints(7, 129, 104, 14));
    jPanel1.add(jLabelLocationFilter, new XYConstraints(7, 102, 94, 14));
    jPanel1.add(jLabelUrl, new XYConstraints(7, 75, 31, 14));
    jPanel1.add(jLabelXmlFile, new XYConstraints(4, 181, 86, 14));
    jPanel1.add(jLabelAscFile, new XYConstraints(6, 152, 66, 14));
  }

  // File | Exit action performed
  public void jMenuFileExit_actionPerformed(ActionEvent e) {
    System.exit(0);
  }

  // Help | About action performed
  public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
    AboutBox dlg = new AboutBox(this);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
        (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.pack();
    dlg.setVisible(true);
    //dlg.show();
  }

  // Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      jMenuFileExit_actionPerformed(null);
    }
  }

  ThreadExportData threadExpData = null;
  Timer timer = null;

  void jButtonAction_actionPerformed(ActionEvent ae) {
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    progressBar.setStringPainted(true);
    jButtonAction.setEnabled(false);
    statusBar.setText(null);
    try {
      threadExpData = new ThreadExportData(jTextUrl.getText(),
          jTextUsername.getText(), jTextPassword.getText(),
          jTextChooseAscFile.getText(), jTextChooseXmlFile.getText());
      threadExpData.start();

      timer = new Timer(250, new ActionListener() {

        public void actionPerformed(ActionEvent evt) {
          int i = threadExpData.getProgress();
          progressBar.setValue(i);
          progressBar.setString("" + i + "%");
          if (threadExpData.getErrorDescription() != null) {
            progressBar.setValue(0);
            progressBar.setString(null);
            progressBar.setStringPainted(false);
            statusBar.setText(threadExpData.getErrorDescription());
            timer.stop();
          }
          if (i > 100 || threadExpData.isEnd()) {
            progressBar.setValue(0);
            progressBar.setString(null);
            progressBar.setStringPainted(false);
            statusBar.setText(null);
            timer.stop();
          }
        }
      });
      timer.start();
    } finally {
      jButtonAction.setEnabled(true);
      setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    //
  }

  void jButtonChooseAscFile_actionPerformed(ActionEvent e) {
    JFileChooser chooser = new JFileChooser();
    int returnVal = chooser.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      jTextChooseAscFile.setText(chooser.getSelectedFile().getAbsolutePath());
    }
  }

  void jButtonChooseXmlFile_actionPerformed(ActionEvent e) {
    JFileChooser chooser = new JFileChooser();
    int returnVal = chooser.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      jTextChooseXmlFile.setText(chooser.getSelectedFile().getAbsolutePath());
    }
  }
}

class Frame1_jMenuFileExit_ActionAdapter implements ActionListener {
  MainFrame adaptee;

  Frame1_jMenuFileExit_ActionAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuFileExit_actionPerformed(e);
  }
}

class Frame1_jMenuHelpAbout_ActionAdapter implements ActionListener {
  MainFrame adaptee;

  Frame1_jMenuHelpAbout_ActionAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuHelpAbout_actionPerformed(e);
  }
}

class Frame1_jButtonAction_actionAdapter implements
    java.awt.event.ActionListener {
  MainFrame adaptee;

  Frame1_jButtonAction_actionAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonAction_actionPerformed(e);
  }
}

class Frame1_jButtonChooseAscFile_actionAdapter implements
    java.awt.event.ActionListener {
  MainFrame adaptee;

  Frame1_jButtonChooseAscFile_actionAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonChooseAscFile_actionPerformed(e);
  }
}

class Frame1_jButtonChooseXmlFile_actionAdapter implements
    java.awt.event.ActionListener {
  MainFrame adaptee;

  Frame1_jButtonChooseXmlFile_actionAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonChooseXmlFile_actionPerformed(e);
  }
}
